package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.exception.WarehouseNotProvidedException;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.model.AtWarehouse;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;

import java.util.Objects;

@RequiredArgsConstructor
class ParcelAtWarehouseManager implements ParcelStateManager {

    private final WarehouseDao warehouseDao;

    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        if (!request.getNextState().equals(ParcelStateType.AT_WAREHOUSE)) {
            throw new IllegalStateException();
        }
        if (Objects.isNull(request.getWarehouseId()) || Objects.isNull(request.getWarehouseType())) {
            throw new WarehouseNotProvidedException();
        }
    }

    @Override
    public AddressDto getSourceAddress(ParcelState parcelState) {
        AtWarehouse parcelAtWarehouse = cast(parcelState);
        return warehouseDao
                .getById(parcelAtWarehouse.getWarehouseId(), parcelAtWarehouse.getWarehouseType())
                .getAddress();
    }

    @Override
    public AddressDto getDestinationAddress(ParcelState parcelState) {
        Parcel parcel = parcelState.getParcel();
        if (parcel.isMarkedToReturn()) { //TODO think about it more in the future
            return AddressDto.of(parcel.getSenderAddress());
        }
        return AddressDto.of(parcel.getDeliveryAddress());
    }

    private AtWarehouse cast(ParcelState parcelState) {
        assert parcelState instanceof AtWarehouse;
        return (AtWarehouse) parcelState;
    }
}
