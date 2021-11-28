package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.base.DateTimeServicePort;
import pl.com.januszex.paka.flow.parcel.domain.AssignToCourierOperation;
import pl.com.januszex.paka.flow.parcel.domain.NoOperation;
import pl.com.januszex.paka.flow.parcel.domain.Operation;
import pl.com.januszex.paka.flow.state.api.exception.WarehouseNotProvidedException;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.model.AtWarehouse;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackRequestDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
class ParcelAtWarehouseManager implements ParcelStateManager {

    private final WarehouseDao warehouseDao;
    private final DateTimeServicePort dateTimeService;

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
    public void doPostChangeOperations(ParcelState newParcelState) {
        WarehouseTrackDto warehouseTrack = getWarehouseTrack(newParcelState);
        AtWarehouse atWarehouse = cast(newParcelState);
        if (atWarehouse.getWarehouseType() == WarehouseType.LOCAL &&
                atWarehouse.getId().equals(warehouseTrack.getSourceWarehouseId())) {
            log.info("Update courier arrival");
            log.info("Send notification to sender when courier will arrive");
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
        AtWarehouse parcelAtWarehouse = cast(parcelState);
        WarehouseTrackDto track = getWarehouseTrack(parcelState);
        return parcelAtWarehouse.getWarehouseType() == WarehouseType.LOCAL ?
                getDestinationAddressForLocalWarehouse(parcelState, parcelAtWarehouse, track) :
                getDestinationForGlobalWarehouse(track);
    }

    @Override
    public Operation getNextOperation(ParcelState parcelState) {
        if (isParcelNotDeliverable(parcelState)) {
            return new NoOperation();
        }
        return new AssignToCourierOperation();
    }

    private boolean isParcelNotDeliverable(ParcelState parcelState) {
        return getDestinationAddress(parcelState).equals(AddressDto.of(parcelState.getParcel().getDeliveryAddress())) &&
                !dateTimeService.isToday(LocalDateTime.from(parcelState.getParcel().getExpectedCourierArrivalDate()));
    }

    private WarehouseTrackDto getWarehouseTrack(ParcelState parcelState) {
        return warehouseDao.getTrack(WarehouseTrackRequestDto.builder()
                .sourcePostalCode(parcelState.getParcel().getSenderAddress().getPostalCode())
                .destinationPostalCode(parcelState.getParcel().getDeliveryAddress().getPostalCode())
                .build());
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
