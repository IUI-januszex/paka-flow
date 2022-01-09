package pl.com.januszex.paka.flow.parcel.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.januszex.paka.flow.address.api.request.AddressRequest;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.address.model.Address;
import pl.com.januszex.paka.flow.base.DateTimeServicePort;
import pl.com.januszex.paka.flow.delivery.model.DeliveryAttempt;
import pl.com.januszex.paka.flow.parcel.api.exception.*;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelRepositoryPort;
import pl.com.januszex.paka.flow.parcel.api.request.DeliverToWarehouseRequest;
import pl.com.januszex.paka.flow.parcel.api.request.MoveCourierArrivalDateRequest;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelRequest;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelCourierArrivalServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelTypeServicePort;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.parcel.model.ParcelType;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.AtWarehouse;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.security.AuthorizationException;
import pl.com.januszex.paka.users.api.dao.UserDao;
import pl.com.januszex.paka.users.api.service.CurrentUserServicePort;
import pl.com.januszex.paka.users.domain.UserDto;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackRequestDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ParcelServiceAdapter implements ParcelServicePort {

    private final ParcelRepositoryPort parcelRepository;
    private final ParcelStateServicePort parcelStateService;
    private final SecureRandom secureRandom;
    private final ParcelTypeServicePort parcelTypeService;
    private final WarehouseDao warehouseDao;
    private final UserDao userDao;
    private final ParcelCourierArrivalServicePort parcelCourierArrivalService;
    private final DateTimeServicePort dateTimeService;
    private final CurrentUserServicePort currentUserService;

    @Override
    public Parcel getById(long id) {
        return parcelRepository.getById(id).orElseThrow(() -> new ParcelNotFound(id));
    }

    @Override
    @Transactional
    public Parcel registerParcel(String senderId, ParcelRequest request) {
        if (Objects.nonNull(request.getPrice()) && request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        Parcel parcel = new Parcel();
        ParcelType parcelType = parcelTypeService.getById(request.getParcelType());

        parcel.setDeliveryAddress(mapAddressRequest(request.getDeliveryAddress()));
        parcel.setSenderAddress(mapAddressRequest(request.getSenderAddress()));
        parcel.setSendingUserId(senderId);
        parcel.setPin(generatePin());
        parcel.setParcelType(parcelType);
        parcel.setParcelFee(parcelType.getPrice());
        parcel.setParcelPrice(request.getPrice());
        parcel.setPaid(Objects.nonNull(request.getPrice()) && !request.getPrice().equals(BigDecimal.ZERO));
        parcel.setReceiverDetails(request.getReceiverDetails());
        parcel.setReceiverPhoneNumber(request.getReceiverPhoneNumber());
        parcel.setReceiverEmailAddress(request.getReceiverEmailAddress());
        parcel.setSenderDetails(request.getSenderDetails());
        parcel.setSenderPhoneNumber(request.getSenderPhoneNumber());
        parcel.setSenderEmailAddress(request.getSenderEmailAddress());
        parcel.setObserverIds(prepareObservingUserIds(senderId, request.getReceiverEmailAddress()));
        parcel.setStates(prepareParcelStates(parcel));

        parcel = parcelRepository.add(parcel);
        parcelStateService.doPostChangeOperations(parcel.getStates().iterator().next());
        return parcel;
    }

    @Override
    @Transactional
    public void deleteParcel(long id) {
        Parcel parcel = getById(id);
        verifyIfCurrentStateIsAtSenderAndCurrentUserIsSender(id, parcel);
        parcelRepository.delete(parcel);
    }

    @Override
    public AddressDto getSourceAddress(long parcelId) {
        return parcelStateService.getSourceAddress(parcelStateService.getCurrentParcelState(parcelId));
    }

    @Override
    public AddressDto getDestinationAddress(long parcelId) {
        return parcelStateService.getDestinationAddress(parcelStateService.getCurrentParcelState(parcelId));
    }

    @Override
    public Collection<Parcel> getParcelFormWarehouse(long warehouseId, WarehouseType warehouseType) {
        return parcelRepository.findParcelFormWarehouse(warehouseId, warehouseType);
    }

    @Override
    public Collection<Parcel> getParcelFormWarehouseToReturn(long warehouseId, WarehouseType warehouseType) {
        return getParcelFormWarehouse(warehouseId, warehouseType).stream()
                .filter(parcel -> parcel.getDeliveryAttempts().size() == 3)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Parcel> getCouriersParcels(String courierId) {
        return parcelRepository.findParcelOfCourier(courierId);
    }

    @Override
    public Collection<Parcel> getObservedParcelByUser(String userId) {
        return parcelRepository.findObservedParcel(userId);
    }

    @Override
    public Collection<Parcel> getParcelSendByUser(String userId) {
        return parcelRepository.findSentParcel(userId);
    }

    @Override
    @Transactional
    public void pickupParcel(long parcelId, String courierId) {
        ChangeParcelStateRequest request = ChangeParcelStateRequest.builder()
                .nextState(ParcelStateType.AT_COURIER)
                .parcelId(parcelId)
                .courierId(courierId)
                .build();
        parcelStateService.changeParcelState(request);
    }

    @Override
    @Transactional
    public void returnToWarehouse(long parcelId, String courierId) {
        AtWarehouse atWarehouse = (AtWarehouse) parcelStateService.getCurrentParcelState(parcelId)
                .getPreviousState() //ASSIGNED
                .getPreviousState(); //AT_WAREHOUSE
        ChangeParcelStateRequest request = ChangeParcelStateRequest.builder()
                .nextState(ParcelStateType.AT_WAREHOUSE)
                .parcelId(parcelId)
                .warehouseId(atWarehouse.getWarehouseId())
                .warehouseType(atWarehouse.getWarehouseType())
                .build();
        parcelStateService.changeParcelState(request);
    }

    @Override
    @Transactional
    public void deliverParcelAtWarehouse(long parcelId, DeliverToWarehouseRequest deliverToWarehouseRequest) {
        ChangeParcelStateRequest request = ChangeParcelStateRequest.builder()
                .nextState(ParcelStateType.AT_WAREHOUSE)
                .parcelId(parcelId)
                .warehouseId(deliverToWarehouseRequest.getWarehouseId())
                .warehouseType(deliverToWarehouseRequest.getWarehouseType())
                .build();
        parcelStateService.changeParcelState(request);

    }

    @Override
    public void deliverParcelToClient(long parcelId, String courierId) {
        ChangeParcelStateRequest request = ChangeParcelStateRequest.builder()
                .nextState(ParcelStateType.DELIVERED)
                .parcelId(parcelId)
                .courierId(courierId)
                .build();
        parcelStateService.changeParcelState(request);
    }

    @Override
    @Transactional
    public void assignParcelToCourier(long parcelId, String courierId, String logisticianId) {
        //TODO verify if courier and logistician is working at the same warehouse and parcel is assigned to it
        ChangeParcelStateRequest request = ChangeParcelStateRequest.builder()
                .courierId(courierId)
                .parcelId(parcelId)
                .nextState(ParcelStateType.ASSIGNED_TO_COURIER)
                .build();
        parcelStateService.changeParcelState(request);
    }

    @Override
    @Transactional
    public void moveCourierArrivalDate(long parcelId, MoveCourierArrivalDateRequest request) {
        Parcel parcel = getById(parcelId);
        if (!isMoveable(parcel)) {
            throw new IllegalParcelStateException(parcelId);
        }
        if (!Arrays.equals(request.getPin(), parcel.getPin())) {
            throw new InvalidPinException();
        }
        if (!parcelCourierArrivalService.isMoveDateValid(request.getNewDate(), parcel.getExpectedCourierArrivalDate())) {
            throw new InvalidMoveDate();
        }
        parcel.setExpectedCourierArrivalDate(request.getNewDate());
        parcel.setDateMoved(true);
    }

    @Override
    @Transactional
    public void setParcelPaid(long parcelId, boolean paid) {
        Parcel parcel = getById(parcelId);
        parcel.setPaid(paid);
    }

    @Override
    @Transactional
    public void setParcelFeePaid(long parcelId, boolean paid) {
        Parcel parcel = getById(parcelId);
        parcel.setFeePaid(paid);
    }

    @Override
    public boolean isMoveable(Parcel parcel) {
        if (parcel.isDateMoved()) {
            return false;
        }
        AddressDto deliveryAddress = AddressDto.of(parcel.getDeliveryAddress());
        ParcelState currentState = parcelStateService.getCurrentParcelState(parcel.getId());
        return !((currentState.getType().equals(ParcelStateType.AT_COURIER) ||
                currentState.getType().equals(ParcelStateType.ASSIGNED_TO_COURIER)) &&
                deliveryAddress.equals(parcelStateService.getDestinationAddress(currentState)));
    }

    @Override
    @Transactional
    public void addDeliveryAttempt(long parcelId, String courierId) {
        Parcel parcel = getById(parcelId);
        if (!parcelStateService.getCurrentParcelState(parcelId).getType().equals(ParcelStateType.AT_COURIER)) {
            throw new IllegalParcelStateException(parcelId);
        }
        DeliveryAttempt deliveryAttempt = new DeliveryAttempt();
        deliveryAttempt.setParcel(parcel);
        deliveryAttempt.setCourierId(courierId);
        deliveryAttempt.setDateTime(dateTimeService.getNow());
        parcel.getDeliveryAttempts().add(deliveryAttempt);
    }

    @Override
    public Collection<DeliveryAttempt> getParcelDeliveryAttempts(long id) {
        return getById(id).getDeliveryAttempts();
    }

    @Override
    public void addParcelToObserved(long parcelId, String userId) {
        Parcel parcel = getById(parcelId);
        if (parcel.getObserverIds().contains(userId)) {
            throw new ParcelAlreadyObserved(parcelId, userId);
        }
        parcel.getObserverIds().add(userId);
        parcelRepository.update(parcel);
    }

    @Override
    public void removeParcelFromObserved(long parcelId, String userId) {
        Parcel parcel = getById(parcelId);
        parcel.getObserverIds().remove(userId);
        parcelRepository.update(parcel);
    }

    private Address mapAddressRequest(AddressRequest request) {
        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setBuildingNumber(request.getBuildingNumber());
        address.setFlatNumber(request.getFlatNumber());
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        return address;
    }

    private char[] generatePin() {
        char[] pin = new char[4];
        for (int i = 0; i < 4; i++) {
            pin[i] = (char) ('0' + secureRandom.nextInt(10));
        }
        return pin;
    }

    private Set<String> prepareObservingUserIds(String senderId, String receiverEmailAddress) {
        Set<String> observingUsers = new HashSet<>();
        observingUsers.add(senderId);
        userDao.getUserByEmail(receiverEmailAddress)
                .map(UserDto::getId)
                .ifPresent(observingUsers::add);
        return observingUsers;
    }

    private List<ParcelState> prepareParcelStates(Parcel parcel) {
        List<ParcelState> parcelStates = new ArrayList<>();
        WarehouseTrackRequestDto warehouseTrackRequestDto = WarehouseTrackRequestDto
                .builder()
                .sourcePostalCode(parcel.getSenderAddress().getPostalCode())
                .destinationPostalCode(parcel.getDeliveryAddress().getPostalCode())
                .build();
        parcelStates.add(parcelStateService.getInitState(warehouseDao.getTrack(warehouseTrackRequestDto)
                        .getSourceWarehouseId(),
                parcel));
        return parcelStates;
    }


    private void verifyIfCurrentStateIsAtSenderAndCurrentUserIsSender(long id, Parcel parcel) {
        if (!parcelStateService.getCurrentParcelState(id).getType().equals(ParcelStateType.AT_SENDER)) {
            throw new IllegalParcelStateException(id);
        }
        if (!currentUserService.hasId(parcel.getSendingUserId())) {
            throw new AuthorizationException("Cannot delete parcel sent by anotherUser");
        }
    }

}
