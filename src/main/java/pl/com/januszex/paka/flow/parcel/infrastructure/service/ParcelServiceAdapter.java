package pl.com.januszex.paka.flow.parcel.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.januszex.paka.flow.address.api.request.AddressRequest;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.address.model.Address;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelNotFound;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelRepositoryPort;
import pl.com.januszex.paka.flow.parcel.api.request.RegisterParcelRequest;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelTypeServicePort;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.parcel.model.ParcelType;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcelServiceAdapter implements ParcelServicePort {

    private final ParcelRepositoryPort parcelRepository;
    private final ParcelStateServicePort parcelStateService;
    private final SecureRandom secureRandom;
    private final ParcelTypeServicePort parcelTypeService;

    @Override
    public Parcel getById(long id) {
        return parcelRepository.getById(id).orElseThrow(() -> new ParcelNotFound(id));
    }

    @Override
    @Transactional
    public Parcel registerParcel(String senderId, RegisterParcelRequest request, LocalDateTime now) {
        if (Objects.nonNull(request.getPrice()) && request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        Parcel parcel = new Parcel();
        ParcelType parcelType = parcelTypeService.getById(request.getParcelType());

        parcel.setDeliveryAddress(mapAddressRequest(request.getDeliveryAddress()));
        parcel.setSenderAddress(mapAddressRequest(request.getSenderAddress()));
        parcel.setSenderId(senderId);
        parcel.setPin(generatePin());
        parcel.setParcelType(parcelType);
        parcel.setParcelFee(parcelType.getPrice());
        parcel.setParcelPrice(request.getPrice());
        parcel.setPaid(Objects.nonNull(request.getPrice()) && !request.getPrice().equals(BigDecimal.ZERO));
        parcel.setReceiverDetails(request.getReceiverDetails());
        parcel.setReceiverEmailAddress(request.getReceiverEmailAddress());
        parcel.setObserverIds(prepareObservingUserIds(senderId, request.getReceiverEmailAddress()));
        parcel.setStates(prepareParcelStates(parcel, request, now));

        return parcelRepository.add(parcel);
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
    public Collection<Parcel> getObservedParcelByUser(String userId) {
        return parcelRepository.findObservedParcel(userId);
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
        //TODO find user by email
        return observingUsers;
    }

    private List<ParcelState> prepareParcelStates(Parcel parcel, RegisterParcelRequest request, LocalDateTime now) {
        List<ParcelState> parcelStates = new ArrayList<>();
        parcelStates.add(parcelStateService.getInitState(request.getWarehouseId(), parcel, now));
        return parcelStates;
    }

}
