package pl.com.januszex.paka.flow.parcel.api.service;

import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.delivery.model.DeliveryAttempt;
import pl.com.januszex.paka.flow.parcel.api.request.DeliverToWarehouseRequest;
import pl.com.januszex.paka.flow.parcel.api.request.MoveCourierArrivalDateRequest;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelRequest;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.util.Collection;

public interface ParcelServicePort {

    Parcel getById(long id);

    Parcel registerParcel(String senderId, ParcelRequest request);

    void deleteParcel(long id);

    AddressDto getSourceAddress(long parcelId);

    AddressDto getDestinationAddress(long parcelId);

    Collection<Parcel> getParcelFormWarehouse(long warehouseId, WarehouseType warehouseType);

    Collection<Parcel> getParcelFormWarehouseToReturn(long warehouseId, WarehouseType warehouseType);

    Collection<Parcel> getCouriersParcels(String courierId);

    Collection<Parcel> getObservedParcelByUser(String userId);

    Collection<Parcel> getParcelSendByUser(String userId);

    void pickupParcel(long parcelId, String courierId);

    void returnToWarehouse(long parcelId, String courierId);

    void deliverParcelAtWarehouse(long parcelId, DeliverToWarehouseRequest deliverToWarehouseRequest);

    void deliverParcelToClient(long parcelId, String courierId);

    void assignParcelToCourier(long parcelId, String courierId, String logisticianId);

    void moveCourierArrivalDate(long parcelId, MoveCourierArrivalDateRequest request);

    void setParcelPaid(long parcelId, boolean paid);

    void setParcelFeePaid(long parcelId, boolean paid);

    boolean isMoveable(Parcel parcel);

    void addDeliveryAttempt(long parcelId, String courierId);

    Collection<DeliveryAttempt> getParcelDeliveryAttempts(long id);
}
