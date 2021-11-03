package pl.com.januszex.paka.flow.parcel.api.service;

import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.api.request.RegisterParcelRequest;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ParcelServicePort {

    Parcel getById(long id);

    Parcel registerParcel(String senderId, RegisterParcelRequest request, LocalDateTime now);

    AddressDto getSourceAddress(long parcelId);

    AddressDto getDestinationAddress(long parcelId);

    Collection<Parcel> getParcelFormWarehouse(long warehouseId, WarehouseType warehouseType);

    Collection<Parcel> getParcelFormWarehouseToReturn(long warehouseId, WarehouseType warehouseType);

    Collection<Parcel> getObservedParcelByUser(String userId);
}
