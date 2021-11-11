package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.exception.WarehouseNotProvidedException;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.domain.AssignToCourierOperation;
import pl.com.januszex.paka.flow.state.domain.Operation;
import pl.com.januszex.paka.flow.state.model.AtWarehouse;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.warehouse.dao.WarehouseDao;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackRequestDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

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
        AtWarehouse parcelAtWarehouse = cast(parcelState);
        WarehouseTrackDto track = warehouseDao.getTrack(WarehouseTrackRequestDto.builder()
                .sourcePostalCode(parcelState.getParcel().getSenderAddress().getPostalCode())
                .destinationPostalCode(parcelState.getParcel().getDeliveryAddress().getPostalCode())
                .build());
        return parcelAtWarehouse.getWarehouseType() == WarehouseType.LOCAL ?
                getDestinationAddressForLocalWarehouse(parcelState, parcelAtWarehouse, track) :
                getDestinationForGlobalWarehouse(track);
    }

    @Override
    public Operation getNextOperation(ParcelState parcelState) {
        return new AssignToCourierOperation();
    }

    private AddressDto getDestinationForGlobalWarehouse(WarehouseTrackDto track) {
        if (Objects.isNull(track.getSecondGlobalWarehouseId())) {
            return warehouseDao.getLocalById(track.getDestinationWarehouseId())
                    .getAddress();
        }
        return warehouseDao.getGlobalLocalById(track.getSecondGlobalWarehouseId())
                .getAddress();
    }

    private AddressDto getDestinationAddressForLocalWarehouse(ParcelState parcelState,
                                                              AtWarehouse parcelAtWarehouse,
                                                              WarehouseTrackDto track) {
        if (Objects.isNull(track.getFirstGlobalWarehouseId())) {
            return AddressDto.of(parcelState.getParcel().getDeliveryAddress());
        }
        if (parcelAtWarehouse.getWarehouseId().equals(track.getDestinationWarehouseId())) {
            return AddressDto.of(parcelState.getParcel().getDeliveryAddress());
        }
        return warehouseDao.getGlobalLocalById(track.getFirstGlobalWarehouseId())
                .getAddress();
    }

    private AtWarehouse cast(ParcelState parcelState) {
        assert parcelState instanceof AtWarehouse;
        return (AtWarehouse) parcelState;
    }
}
