package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.state.api.exception.WarehouseNotProvidedException;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.model.AtSender;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;

import java.util.Objects;

@RequiredArgsConstructor
class ParcelAtSenderManager implements ParcelStateManager {

    private final WarehouseDao warehouseDao;

    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        if (!request.getNextState().equals(ParcelStateType.AT_SENDER)) {
            throw new IllegalStateException();
        }
        if (Objects.isNull(request.getWarehouseId()) || Objects.isNull(request.getWarehouseType())) {
            throw new WarehouseNotProvidedException();
        }
    }

    @Override
    public AddressDto getSourceAddress(ParcelState parcelState) {
        return AddressDto.of(parcelState.getParcel().getSenderAddress());
    }

    @Override
    public AddressDto getDestinationAddress(ParcelState parcelState) {
        AtSender parcelAtSender = cast(parcelState);
        return warehouseDao
                .getById(parcelAtSender.getWarehouseId(), parcelAtSender.getWarehouseType())
                .getAddress();
    }

    private AtSender cast(ParcelState parcelState) {
        assert parcelState instanceof AtSender;
        return (AtSender) parcelState;
    }
}
