package pl.com.januszex.paka.flow.state.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class WarehouseNotProvidedException extends BusinessLogicException {
    public WarehouseNotProvidedException() {
        super("Provided warehouse id is null");
    }
}
