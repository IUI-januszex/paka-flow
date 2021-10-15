package pl.com.januszex.paka.flow.parcel.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class UpdateOrDeleteParcelTypeNotPossibleException extends BusinessLogicException {
    public UpdateOrDeleteParcelTypeNotPossibleException(long id) {
        super(String.format("Cannot perform operation because parcel type with id %s has parcel assigned", id));
    }
}
