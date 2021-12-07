package pl.com.januszex.paka.flow.parcel.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParcelPaidRequest {

    @JsonProperty("isPaid")
    private boolean paid;

}
