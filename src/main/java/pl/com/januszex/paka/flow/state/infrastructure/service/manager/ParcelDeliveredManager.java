package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelFeeNotPaid;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelNotPaid;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;

@RequiredArgsConstructor
public class ParcelDeliveredManager implements ParcelStateManager {

    private final ParcelServicePort parcelService;

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

    private void checkParcelPaid(long parcelId) {
        Parcel parcel = parcelService.getById(parcelId);
        if (!parcel.isFeePaid()) {
            throw new ParcelFeeNotPaid(parcelId);
        }
        if (!parcel.isPaid()) {
            throw new ParcelNotPaid(parcelId);
        }
    }
}
