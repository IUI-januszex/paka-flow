package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelFeeNotPaid;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelNotPaid;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.domain.ParcelState;
import pl.com.januszex.paka.flow.state.domain.ParcelStateType;

public class ParcelDeliveredManager implements ParcelStateManager {
    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        if (!request.getNextState().equals(ParcelStateType.DELIVERED)) {
            throw new IllegalStateException();
        }
        checkParcelPaid(request.getParcelId());
    }

    @Override
    public AddressDto getSourceAddress(ParcelState parcelState) {
        return AddressDto.of(parcelState.getParcel().getSenderAddress());
    }

    @Override
    public AddressDto getDestinationAddress(ParcelState parcelState) {
        return AddressDto.of(parcelState.getParcel().getDeliveryAddress());
    }

    private void checkParcelPaid(long parcelId) { //FIXME
        Parcel parcel = new Parcel();
        if (!parcel.isFeePaid()) {
            throw new ParcelFeeNotPaid(parcelId);
        }
        if (!parcel.isPaid()) {
            throw new ParcelNotPaid(parcelId);
        }
    }
}
