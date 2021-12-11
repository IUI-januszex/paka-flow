package pl.com.januszex.paka.flow.delivery.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import pl.com.januszex.paka.flow.delivery.model.DeliveryAttempt;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryAttemptResponse {

    long id;

    LocalDateTime dateTime;

    long parcelId;


    public static DeliveryAttemptResponse of(DeliveryAttempt model) {
        return new DeliveryAttemptResponse(model.getId(), model.getDateTime(), model.getParcel().getId());
    }

}
