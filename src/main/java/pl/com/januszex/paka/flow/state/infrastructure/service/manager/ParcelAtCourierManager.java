package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.state.api.exception.CourierNotProvidedException;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;

import java.util.Objects;

@RequiredArgsConstructor
class ParcelAtCourierManager implements ParcelStateManager {

    private final ParcelStateServicePort parcelStateService;

    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        if (!request.getNextState().equals(ParcelStateType.AT_COURIER)) {
            throw new IllegalStateException();
        }
        if (Objects.isNull(request.getCourierId())) {
            throw new CourierNotProvidedException();
        }
    }

    @Override
    public AddressDto getSourceAddress(ParcelState parcelState) {
        return parcelStateService.getSourceAddress(getPreviousParcelState(parcelState));
    }

    @Override
    public AddressDto getDestinationAddress(ParcelState parcelState) {
        return parcelStateService.getDestinationAddress(getPreviousParcelState(parcelState));
    }


    private ParcelState getPreviousParcelState(ParcelState parcelState) {
        return parcelStateService.getById(parcelState.getPreviousState().getId());
    }
}
