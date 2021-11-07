package pl.com.januszex.paka.warehouse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseTrackRequestDto {

    @JsonProperty("postalCodeSource")
    private String sourcePostalCode;

    @JsonProperty("postalCodeDestination")
    private String destinationPostalCode;
}
