package pl.com.januszex.paka.flow.parcel.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(value = "isActive")
    boolean active;

    public static ParcelTypeResponse of(ParcelType parcelType) {
        return ParcelTypeResponse.builder()
                .id(parcelType.getId())
                .name(parcelType.getName())
                .description(parcelType.getDescription())
                .price(parcelType.getPrice())
                .active(parcelType.isActive())
                .build();
    }
}
