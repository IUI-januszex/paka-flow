package pl.com.januszex.paka.flow.parcel.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.state.api.repose.ParcelStateResponse;
import pl.com.januszex.paka.flow.state.domain.Operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ParcelDetailView {
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
    ParcelStateResponse currentSate;
    AddressDto sourceAddress;
    AddressDto destinationAddress;
    Operation nextOperation;
}
