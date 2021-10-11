package pl.com.januszex.paka.flow.parcel.api.response;

import lombok.Builder;
import lombok.Value;
import pl.com.januszex.paka.flow.parcel.domain.ParcelType;

import java.math.BigDecimal;

@Value
@Builder
public class ParcelTypeResponse {
    long id;
    String name;
    String description;
    BigDecimal price;

    public static ParcelTypeResponse of(ParcelType parcelType) {
        return ParcelTypeResponse.builder()
                .id(parcelType.getId())
                .name(parcelType.getName())
                .description(parcelType.getDescription())
                .price(parcelType.getPrice())
                .build();
    }
}
