package pl.com.januszex.paka.flow.address.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Value
public class AddressRequest {

    @NotBlank(message = "Provide postal code")
    @Pattern(regexp = "[0-9]{2}-[0-9]{3}", message = "Postal code is invalid")
    String postalCode;

    @NotBlank(message = "Provide city")
    String city;

    @NotBlank(message = "Provide street")
    String street;

    @NotBlank(message = "Provide building number")
    String buildingNumber;

    String flatNumber;

    @JsonCreator
    public AddressRequest(String postalCode,
                          String city,
                          String street,
                          String buildingNumber,
                          String flatNumber) {
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.flatNumber = flatNumber;
    }
}
