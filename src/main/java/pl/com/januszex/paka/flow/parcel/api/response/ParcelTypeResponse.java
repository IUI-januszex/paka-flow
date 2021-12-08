package pl.com.januszex.paka.flow.parcel.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.com.januszex.paka.flow.parcel.model.ParcelType;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@SuperBuilder
public class ParcelTypeResponse {
    private final long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    @JsonProperty(value = "isActive")
    private final boolean active;

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
