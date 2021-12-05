package pl.com.januszex.paka.flow.parcel.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.januszex.paka.flow.address.api.request.AddressRequest;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.address.model.Address;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelNotFound;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelRepositoryPort;
import pl.com.januszex.paka.flow.parcel.api.request.DeliverToWarehouseRequest;
import pl.com.januszex.paka.flow.parcel.api.request.MoveCourierArrivalDateRequest;
import pl.com.januszex.paka.flow.parcel.api.request.RegisterParcelRequest;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelTypeServicePort;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.parcel.model.ParcelType;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
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
public class ParcelServiceAdapter implements ParcelServicePort {

    private final ParcelRepositoryPort parcelRepository;
    private final ParcelStateServicePort parcelStateService;
    private final SecureRandom secureRandom;
    private final ParcelTypeServicePort parcelTypeService;
    private final WarehouseDao warehouseDao;
    private final UserDao userDao;
    private final CurrentUserServicePort currentUserService;

    @Override
    public Parcel getById(long id) {
        return parcelRepository.getById(id).orElseThrow(() -> new ParcelNotFound(id));
    }

    @Override
    @Transactional
    public Parcel registerParcel(String senderId, RegisterParcelRequest request) {
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
    public void deliverParcelsAtWarehouse(String courierId, DeliverToWarehouseRequest deliverToWarehouseRequest) {

    }

    @Override
    public void deliverParcelToClient(long parcelId, String courierId) {

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
    public void markParcelToReturn(long parcelId) {

    }

    @Override
    public void moveCourierArrivalDate(long parcelId, MoveCourierArrivalDateRequest request) {

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

}
