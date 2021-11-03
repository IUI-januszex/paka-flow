package pl.com.januszex.paka.flow.parcel.api.service;

import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.util.Collection;

public interface ParcelServicePort {

    Parcel getById(long id);

    AddressDto getSourceAddress(long parcelId);

    AddressDto getDestinationAddress(long parcelId);

    Collection<Parcel> getParcelFormWarehouse(long warehouseId, WarehouseType warehouseType);

    Collection<Parcel> getParcelFormWarehouseToReturn(long warehouseId, WarehouseType warehouseType);

    Collection<Parcel> getObservedParcelByUser(long userId);
}
