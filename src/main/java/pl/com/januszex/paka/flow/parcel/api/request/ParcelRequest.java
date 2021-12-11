package pl.com.januszex.paka.flow.parcel.api.request;

import lombok.Data;
import pl.com.januszex.paka.flow.address.api.request.AddressRequest;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
public class ParcelRequest {

    @NotNull(message = "Provide sender address")
    @Valid
    private AddressRequest senderAddress;

    @NotNull(message = "Provide delivery address")
    @Valid
    private AddressRequest deliveryAddress;

    @NotNull(message = "Provide parcel type")
    private Long parcelType;

    @NotBlank(message = "Provide sender information")
    private String senderDetails;

    @Email(message = "Provide valid sender email address")
    private String senderEmailAddress;

    @NotBlank(message = "Provide valid sender phone number")
    private String senderPhoneNumber;

    @NotBlank(message = "Provide receiver information")
    private String receiverDetails;

    @Email(message = "Provide valid receiver email address")
    private String receiverEmailAddress;

    @NotBlank(message = "Provide valid receiver phone number")
    private String receiverPhoneNumber;

    private BigDecimal price;
}
