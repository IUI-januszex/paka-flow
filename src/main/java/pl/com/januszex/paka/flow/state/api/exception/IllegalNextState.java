package pl.com.januszex.paka.flow.state.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;

public class IllegalNextState extends BusinessLogicException {
    public IllegalNextState(ParcelStateType currentState, ParcelStateType nextState) {
        super(String.format("Changing parcel state form %s to %s is illegal.", currentState, nextState));
    }
}
