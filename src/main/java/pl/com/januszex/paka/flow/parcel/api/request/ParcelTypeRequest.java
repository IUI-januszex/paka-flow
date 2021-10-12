package pl.com.januszex.paka.flow.parcel.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class ParcelTypeRequest {
    @NotBlank(message = "Provide name")
    private String name;
    @NotBlank(message = "Provide description")
    private String description;
    @NotNull(message = "Provide price")
    @Positive(message = "Price must be positive number")
    private BigDecimal price;

    @JsonCreator
    public ParcelTypeRequest(String name,
                             String description,
                             BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
