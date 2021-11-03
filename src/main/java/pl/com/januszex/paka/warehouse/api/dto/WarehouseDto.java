package pl.com.januszex.paka.warehouse.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseDto {
    @JsonProperty(value = "idWarehouse")
    private Long id;
    private String city;
    private String street;
    private String number;
    private String postalCode;


    public AddressDto getAddress() {
        return AddressDto.builder()
                .city(city)
                .street(street)
                .buildingNumber(number)
                .postalCode(postalCode)
                .build();
    }
}
