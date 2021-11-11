package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.domain.Operation;
import pl.com.januszex.paka.flow.state.model.ParcelState;

public interface ParcelStateManager {

    void validateChangeStateData(ChangeParcelStateRequest request);

    AddressDto getSourceAddress(ParcelState parcelState);

    AddressDto getDestinationAddress(ParcelState parcelState);

    Operation getNextOperation(ParcelState parcelState);
}
