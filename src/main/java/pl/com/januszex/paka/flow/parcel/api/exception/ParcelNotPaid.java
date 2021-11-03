package pl.com.januszex.paka.flow.parcel.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class ParcelNotPaid extends BusinessLogicException {
    public ParcelNotPaid(long id) {
        super(String.format("Parcel with id %s is not paid", id));
    }
}
