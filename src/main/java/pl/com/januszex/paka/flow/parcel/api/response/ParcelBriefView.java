package pl.com.januszex.paka.flow.parcel.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ParcelBriefView {
    long id;
    String senderInfo;
    String receiverInfo;
    LocalDateTime estimatedDeliveryTime;
    BigDecimal parcelFee;
    BigDecimal parcelPrice;
    @JsonProperty("isFeePaid")
    boolean feePaid;
    @JsonProperty("isParcelPaid")
    boolean isParcelPaid;
    @JsonProperty("isMoveable")
    boolean moveable;
}
