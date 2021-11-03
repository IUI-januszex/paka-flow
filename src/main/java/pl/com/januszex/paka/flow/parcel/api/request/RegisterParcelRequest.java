package pl.com.januszex.paka.flow.parcel.api.request;

import lombok.Data;
import pl.com.januszex.paka.flow.address.api.request.AddressRequest;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
public class RegisterParcelRequest {

    @NotNull(message = "Provide sender address")
    @Valid
    private AddressRequest senderAddress;

    @NotNull(message = "Provide delivery address")
    @Valid
    private AddressRequest deliveryAddress;

    @NotNull(message = "Provide parcel type")
    private Long parcelType;

    @NotNull(message = "Provide warehouse id")
    private Long warehouseId;

    @NotBlank(message = "Provide receiver information")
    private String receiverDetails;

    @Email(message = "Provide valid receiver email address")
    private String receiverEmailAddress;

    private BigDecimal price;
}
