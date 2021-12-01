package pl.com.januszex.paka.users.api.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelDetailView;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;

import java.util.Collection;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourierParcelsResponse {

    Collection<ParcelDetailView> assignedParcels;

    Collection<ParcelDetailView> pickedUpParcels;


    public static CourierParcelsResponse of(Collection<ParcelDetailView> couriersParcels) {
        return new CourierParcelsResponse(
                couriersParcels.stream()
                        .filter(parcel -> parcel.getCurrentSate()
                                .getType()
                                .equals(ParcelStateType.ASSIGNED_TO_COURIER))
                        .collect(Collectors.toList()),
                couriersParcels.stream()
                        .filter(parcel -> parcel.getCurrentSate()
                                .getType()
                                .equals(ParcelStateType.AT_COURIER))
                        .collect(Collectors.toList())
        );
    }

}
