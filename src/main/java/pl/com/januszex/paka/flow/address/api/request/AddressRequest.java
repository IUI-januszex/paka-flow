package pl.com.januszex.paka.flow.address.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AddressRequest {

    @NotBlank(message = "Provide postal code")
    @Pattern(regexp = "[0-9]{2}-[0-9]{3}", message = "Postal code is invalid")
    private String postalCode;

    @NotBlank(message = "Provide city")
    private String city;

    @NotBlank(message = "Provide street")
    private String street;

    @NotBlank(message = "Provide building number")
    private String buildingNumber;

    private String flatNumber;
}
