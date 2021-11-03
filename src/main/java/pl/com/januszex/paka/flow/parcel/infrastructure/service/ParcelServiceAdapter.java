package pl.com.januszex.paka.flow.parcel.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelNotFound;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelRepositoryPort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcelServiceAdapter implements ParcelServicePort {

    private final ParcelRepositoryPort parcelRepository;
    private final ParcelStateServicePort parcelStateService;

    @Override
    public Parcel getById(long id) {
        return parcelRepository.getById(id).orElseThrow(() -> new ParcelNotFound(id));
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
    public Collection<Parcel> getObservedParcelByUser(long userId) {
        return parcelRepository.findObservedParcel(userId);
    }

}
