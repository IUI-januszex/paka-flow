package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelFeeNotPaid;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.domain.NoOperation;
import pl.com.januszex.paka.flow.state.domain.Operation;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;

@RequiredArgsConstructor
public class ParcelReturnedManager implements ParcelStateManager {

    private final ParcelServicePort parcelService;

    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        if (!request.getNextState().equals(ParcelStateType.DELIVERED)) {
            throw new IllegalStateException();
        }
        checkParcelFee(request.getParcelId());
    }

    @Override
    public void doPostChangeOperations(ParcelState newParcelState) {
        //notify sender
    }

    @Override
    public AddressDto getSourceAddress(ParcelState parcelState) {
        return AddressDto.of(parcelState.getParcel().getSenderAddress());
    }

    @Override
    public AddressDto getDestinationAddress(ParcelState parcelState) {
        return AddressDto.of(parcelState.getParcel().getSenderAddress());
    }

    @Override
    public Operation getNextOperation(ParcelState parcelState) {
        return new NoOperation();
    }

    private void checkParcelFee(long parcelId) {
        Parcel parcel = parcelService.getById(parcelId);
        if (!parcel.isFeePaid()) {
            throw new ParcelFeeNotPaid(parcelId);
        }
    }
}
