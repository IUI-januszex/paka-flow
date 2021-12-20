package pl.com.januszex.paka.flow.parcel.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ParcelBriefView {
    long id;
    String senderInfo;
    String receiverInfo;
    AddressDto senderAddress;
    AddressDto receiverAddress;
    LocalDate estimatedDeliveryTime;
    BigDecimal parcelFee;
    BigDecimal parcelPrice;
    @JsonProperty("isFeePaid")
    boolean feePaid;
    @JsonProperty("isParcelPaid")
    boolean parcelPaid;
    @JsonProperty("isMoveable")
    boolean moveable;
}
