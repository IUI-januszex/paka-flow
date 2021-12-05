package pl.com.januszex.paka.flow.parcel.api.service;

import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.api.request.DeliverToWarehouseRequest;
import pl.com.januszex.paka.flow.parcel.api.request.MoveCourierArrivalDateRequest;
import pl.com.januszex.paka.flow.parcel.api.request.RegisterParcelRequest;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.util.Collection;

public interface ParcelServicePort {

    Parcel getById(long id);

    Parcel registerParcel(String senderId, RegisterParcelRequest request);

    AddressDto getSourceAddress(long parcelId);

    AddressDto getDestinationAddress(long parcelId);

    Collection<Parcel> getParcelFormWarehouse(long warehouseId, WarehouseType warehouseType);

    Collection<Parcel> getParcelFormWarehouseToReturn(long warehouseId, WarehouseType warehouseType);

    Collection<Parcel> getCouriersParcels(String courierId);

    Collection<Parcel> getObservedParcelByUser(String userId);

    void pickupParcel(long parcelId, String courierId);

    void deliverParcelsAtWarehouse(String courierId, DeliverToWarehouseRequest deliverToWarehouseRequest);

    void deliverParcelToClient(long parcelId, String courierId);

    void assignParcelToCourier(long parcelId, String courierId, String logisticianId);

    void markParcelToReturn(long parcelId);

    void moveCourierArrivalDate(long parcelId, MoveCourierArrivalDateRequest request);

    boolean isMoveable(Parcel parcel);
}
