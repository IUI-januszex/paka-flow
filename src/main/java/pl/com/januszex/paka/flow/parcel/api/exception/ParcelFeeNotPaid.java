package pl.com.januszex.paka.flow.parcel.api.exception;

import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;

public class ParcelFeeNotPaid extends BusinessLogicException {
    public ParcelFeeNotPaid(long id) {
        super(String.format("The fee of the parcel with id %s is not paid", id));
    }
}
