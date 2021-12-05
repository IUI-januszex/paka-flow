package pl.com.januszex.paka.flow.parcel.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class InvalidPinException extends BusinessLogicException {
    public InvalidPinException() {
        super("Invalid PIN");
    }
}
