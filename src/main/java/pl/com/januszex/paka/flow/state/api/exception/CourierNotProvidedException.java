package pl.com.januszex.paka.flow.state.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class CourierNotProvidedException extends BusinessLogicException {
    public CourierNotProvidedException() {
        super("Provided courier id is null");
    }
}
