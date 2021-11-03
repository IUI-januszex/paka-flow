package pl.com.januszex.paka.flow.parcel.api.exception;

import pl.com.januszex.paka.flow.base.exception.NotFoundException;

public class ParcelNotFound extends NotFoundException {
    public ParcelNotFound(long id) {
        super(String.format("Parcel with id %s not exist", id));
    }
}
