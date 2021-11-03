package pl.com.januszex.paka.flow.parcel.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Value
public class ParcelTypeRequest {
    @NotBlank(message = "Provide name")
    String name;
    @NotBlank(message = "Provide description")
    String description;
    @NotNull(message = "Provide price")
    @Positive(message = "Price must be positive number")
    BigDecimal price;

    @JsonCreator
    public ParcelTypeRequest(String name,
                             String description,
                             BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
