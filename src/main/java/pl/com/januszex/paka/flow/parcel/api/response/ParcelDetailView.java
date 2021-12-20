package pl.com.januszex.paka.flow.parcel.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.domain.Operation;
import pl.com.januszex.paka.flow.state.api.repose.ParcelStateResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Data
@Builder
public class ParcelDetailView {
    long id;
    String senderInfo;
    String receiverInfo;
    AddressDto senderAddress;
    AddressDto receiverAddress;
    LocalDate expectedCourierArrivalDate;
    BigDecimal parcelFee;
    BigDecimal parcelPrice;
    @JsonProperty("isFeePaid")
    boolean feePaid;
    @JsonProperty("isParcelPaid")
    boolean isParcelPaid;
    @JsonProperty("isMoveable")
    boolean moveable;
    ParcelStateResponse currentSate;
    AddressDto sourceAddress;
    AddressDto destinationAddress;
    Collection<Operation> operations;
}
