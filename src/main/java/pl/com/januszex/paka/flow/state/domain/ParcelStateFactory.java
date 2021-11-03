package pl.com.januszex.paka.flow.state.domain;

import lombok.experimental.UtilityClass;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;

import java.time.LocalDateTime;

@UtilityClass
public class ParcelStateFactory {

    public static ParcelState newInstance(ChangeParcelStateRequest request,
                                          ParcelState previousState,
                                          Parcel parcel,
                                          LocalDateTime now) {
        previousState.checkNextState(request.getNextState());

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
            case AT_MAGAZINE:
                AtMagazine atMagazine = new AtMagazine();
                atMagazine.setWarehouseId(request.getWarehouseId());
                parcelState = atMagazine;
                break;
            case ASSIGNED_TO_COURIER:
                AssignedToCourier assignedToCourier = new AssignedToCourier();
                assignedToCourier.setCourierId(request.getCourierId());
                parcelState = assignedToCourier;
                break;
            case AT_SENDER:
                parcelState = new AtSender();
                break;
            case ASSIGNED_TO_MAGAZINE:
                AssignedToMagazine assignedToMagazine = new AssignedToMagazine();
                assignedToMagazine.setWarehouseId(request.getWarehouseId());
                parcelState = assignedToMagazine;
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
