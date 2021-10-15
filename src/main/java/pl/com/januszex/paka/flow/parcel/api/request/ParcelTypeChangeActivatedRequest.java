package pl.com.januszex.paka.flow.parcel.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParcelTypeChangeActivatedRequest {
    private boolean active;

    @JsonCreator
    public ParcelTypeChangeActivatedRequest(@JsonProperty(required = true, value = "isActive") boolean active) {
        this.active = active;
    }
}
