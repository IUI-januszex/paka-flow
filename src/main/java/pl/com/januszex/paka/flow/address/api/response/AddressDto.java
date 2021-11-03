package pl.com.januszex.paka.flow.address.api.response;

import lombok.Builder;
import lombok.Value;
import pl.com.januszex.paka.flow.address.domain.Address;

@Value
@Builder
public class AddressDto {
    String postalCode;
    String city;
    String street;
    String buildingNumber;
    String flatNumber;

    public static AddressDto of(Address model) {
        return new AddressDto(model.getPostalCode(),
                model.getCity(),
                model.getStreet(),
                model.getBuildingNumber(),
                model.getFlatNumber());
    }
}
