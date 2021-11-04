package pl.com.januszex.paka.flow.state.model;

import lombok.experimental.UtilityClass;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;

import java.time.LocalDateTime;

@UtilityClass
public class ParcelStateFactory {

    public static ParcelState newInstance(ChangeParcelStateRequest request,
                                          ParcelState previousState,
                                          Parcel parcel,
                                          LocalDateTime now) {
        if (previousState != null) {
            previousState.checkNextState(request.getNextState());
        }

        ParcelState parcelState;
        switch (request.getNextState()) {
            case AT_COURIER:
                AtCourier atCourier = new AtCourier();
                atCourier.setCourierId(request.getCourierId());
                parcelState = atCourier;
                break;
            case RETURNED:
                parcelState = new Returned();
                break;
            case DELIVERED:
                parcelState = new Delivered();
                break;
            case AT_WAREHOUSE:
                AtWarehouse atWarehouse = new AtWarehouse();
                atWarehouse.setWarehouseId(request.getWarehouseId());
                atWarehouse.setWarehouseType(request.getWarehouseType());
                parcelState = atWarehouse;
                break;
            case ASSIGNED_TO_COURIER:
                AssignedToCourier assignedToCourier = new AssignedToCourier();
                assignedToCourier.setCourierId(request.getCourierId());
                parcelState = assignedToCourier;
                break;
            case AT_SENDER:
                AtSender atSender = new AtSender();
                atSender.setWarehouseId(request.getWarehouseId());
                atSender.setWarehouseType(request.getWarehouseType());
                parcelState = atSender;
                break;
            case ASSIGNED_TO_WAREHOUSE:
                AssignedToWarehouse assignedToWarehouse = new AssignedToWarehouse();
                assignedToWarehouse.setWarehouseId(request.getWarehouseId());
                assignedToWarehouse.setWarehouseType(request.getWarehouseType());
                parcelState = assignedToWarehouse;
                break;
            default:
                throw new IllegalArgumentException();
        }

        parcelState.setChangeTime(now);
        parcelState.setCurrent(true);
        parcelState.setParcel(parcel);
        parcelState.setPreviousState(previousState);

        return parcelState;
    }
}
