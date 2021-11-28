package pl.com.januszex.paka.warehouse.api.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelDetailView;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;

import java.util.Collection;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WarehouseParcelsResponse {
    Collection<ParcelDetailView> parcelsAteWarehouse;
    Collection<ParcelDetailView> parcelsAssigned;

    public static WarehouseParcelsResponse of(Collection<ParcelDetailView> parcels) {
        return new WarehouseParcelsResponse(parcels.stream()
                .filter(parcel -> parcel.getCurrentSate().getType().equals(ParcelStateType.AT_WAREHOUSE))
                .collect(Collectors.toList()),
                parcels.stream()
                        .filter(parcel -> parcel.getCurrentSate().getType().equals(ParcelStateType.AT_SENDER))
                        .collect(Collectors.toList()));

    }
}
