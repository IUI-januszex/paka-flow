package pl.com.januszex.paka.flow.parcel.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.state.api.repose.ParcelStateResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ParcelBriefView {
    private long id;
    private ParcelTypeResponse type;
    private String senderInfo;
    private String receiverInfo;
    private AddressDto senderAddress;
    private AddressDto receiverAddress;
    private LocalDate estimatedDeliveryTime;
    private BigDecimal parcelFee;
    private BigDecimal parcelPrice;
    @JsonProperty("isFeePaid")
    private boolean feePaid;
    @JsonProperty("isParcelPaid")
    private boolean parcelPaid;
    @JsonProperty("isMoveable")
    boolean moveable;
    private ParcelStateResponse currentState;
}
