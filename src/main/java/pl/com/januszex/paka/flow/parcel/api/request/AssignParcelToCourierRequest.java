package pl.com.januszex.paka.flow.parcel.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AssignParcelToCourierRequest {

    @NotBlank(message = "Provide courier id")
    private String courierId;
}
