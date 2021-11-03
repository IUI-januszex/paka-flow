package pl.com.januszex.paka.flow.parcel.api.exception;

import pl.com.januszex.paka.flow.base.exception.NotFoundException;

public class ParcelTypeNotFoundException extends NotFoundException {
    public ParcelTypeNotFoundException(long id) {
        super(String.format("Parcel type with id %s not found", id));
    }
}
