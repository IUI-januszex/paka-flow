package pl.com.januszex.paka.flow.parcel.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class ParcelAlreadyObserved extends BusinessLogicException {
    public ParcelAlreadyObserved(long parcelId, String userId) {
        super(String.format("Parcel with id %s is already observed by user with id %s", parcelId, userId));
    }
}
