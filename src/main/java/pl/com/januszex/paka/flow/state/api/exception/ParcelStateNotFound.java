package pl.com.januszex.paka.flow.state.api.exception;

import pl.com.januszex.paka.flow.base.exception.NotFoundException;

public class ParcelStateNotFound extends NotFoundException {
    public ParcelStateNotFound(long id) {
        super(String.format("Parcel state with id %s not found", id));
    }
}
