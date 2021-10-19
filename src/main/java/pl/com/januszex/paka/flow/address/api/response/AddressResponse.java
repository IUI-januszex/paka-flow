package pl.com.januszex.paka.flow.address.api.response;

import lombok.Value;
import pl.com.januszex.paka.flow.address.domain.Address;

@Value
public class AddressResponse {
    String postalCode;
    String city;
    String street;
    String buildingNumber;
    String flatNumber;

    public static AddressResponse of(Address model) {
        return new AddressResponse(model.getPostalCode(),
                model.getCity(),
                model.getStreet(),
                model.getBuildingNumber(),
                model.getFlatNumber());
    }
}
