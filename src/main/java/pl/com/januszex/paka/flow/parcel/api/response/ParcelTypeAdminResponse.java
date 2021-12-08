package pl.com.januszex.paka.flow.parcel.api.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.com.januszex.paka.flow.parcel.model.ParcelType;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ParcelTypeAdminResponse extends ParcelTypeResponse {

    private final int parcelCount;

    public ParcelTypeAdminResponse(long id,
                                   String name,
                                   String description,
                                   BigDecimal price,
                                   boolean active,
                                   int parcelCount) {
        super(id, name, description, price, active);
        this.parcelCount = parcelCount;
    }

    public static ParcelTypeAdminResponse of(ParcelType parcelType) {
        return ParcelTypeAdminResponse.builder()
                .id(parcelType.getId())
                .name(parcelType.getName())
                .description(parcelType.getDescription())
                .price(parcelType.getPrice())
                .active(parcelType.isActive())
                .parcelCount(parcelType.getParcels().size())
                .build();
    }
}
