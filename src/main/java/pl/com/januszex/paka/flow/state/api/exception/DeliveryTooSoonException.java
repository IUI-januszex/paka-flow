package pl.com.januszex.paka.flow.state.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class DeliveryTooSoonException extends BusinessLogicException {

    public DeliveryTooSoonException(long parcelId, String courierId) {
        super(String.format("Could not assign courier with id %s to parcel %s because delivery is not expected to be today.",
                courierId, parcelId));
    }
}
