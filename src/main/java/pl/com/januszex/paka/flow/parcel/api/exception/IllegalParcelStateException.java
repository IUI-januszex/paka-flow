package pl.com.januszex.paka.flow.parcel.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class IllegalParcelStateException extends BusinessLogicException {
    public IllegalParcelStateException(long id) {
        super(String.format("Parcel with id %s has illegal state to perform operation", id));
    }
}
