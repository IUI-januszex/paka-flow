package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.state.api.exception.MagazineNotProvidedException;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.domain.AssignedToWarehouse;
import pl.com.januszex.paka.flow.state.domain.ParcelState;
import pl.com.januszex.paka.flow.state.domain.ParcelStateType;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;

import java.util.Objects;

@RequiredArgsConstructor
class ParcelAssignedToWarehouseManager implements ParcelStateManager {

    private final WarehouseDao warehouseDao;
    private final ParcelStateServicePort parcelStateService;

    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        if (!request.getNextState().equals(ParcelStateType.ASSIGNED_TO_WAREHOUSE)) {
            throw new IllegalStateException();
        }
        if (Objects.isNull(request.getWarehouseId()) || Objects.isNull(request.getWarehouseType())) {
            throw new MagazineNotProvidedException();
        }
    }

    @Override
    public AddressDto getSourceAddress(ParcelState parcelState) {
        ParcelState previousParcelState = parcelStateService.getById(parcelState.getPreviousState().getId());
        return parcelStateService.getSourceAddress(previousParcelState);
    }

    @Override
    public AddressDto getDestinationAddress(ParcelState parcelState) {
        AssignedToWarehouse parcelAssigned = cast(parcelState);
        return warehouseDao
                .getById(parcelAssigned.getWarehouseId(), parcelAssigned.getWarehouseType())
                .getAddress();
    }

    private AssignedToWarehouse cast(ParcelState parcelState) {
        assert parcelState instanceof AssignedToWarehouse;
        return (AssignedToWarehouse) parcelState;
    }
}
