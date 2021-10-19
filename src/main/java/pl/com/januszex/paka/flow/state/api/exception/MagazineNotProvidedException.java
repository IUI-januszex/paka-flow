package pl.com.januszex.paka.flow.state.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class MagazineNotProvidedException extends BusinessLogicException {
    public MagazineNotProvidedException() {
        super("Provided magazine id is null");
    }
}
