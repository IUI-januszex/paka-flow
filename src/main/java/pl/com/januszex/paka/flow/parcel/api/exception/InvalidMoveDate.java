package pl.com.januszex.paka.flow.parcel.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class InvalidMoveDate extends BusinessLogicException {

    public InvalidMoveDate() {
        super("Please provide a valid move date");
    }

}
