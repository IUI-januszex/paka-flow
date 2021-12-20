package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.domain.DeliverToClient;
import pl.com.januszex.paka.flow.parcel.domain.DeliverToWarehouse;
import pl.com.januszex.paka.flow.parcel.domain.Operation;
import pl.com.januszex.paka.flow.parcel.domain.OperationType;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.exception.CourierNotProvidedException;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.AssignedToCourier;
import pl.com.januszex.paka.flow.state.model.AtWarehouse;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.notification.api.NotificationServicePort;
import pl.com.januszex.paka.notification.domain.NotificationData;
import pl.com.januszex.paka.users.api.service.CurrentUserServicePort;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackRequestDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
class ParcelAtCourierManager implements ParcelStateManager {

    private final ParcelStateServicePort parcelStateService;

    private final WarehouseDao warehouseDao;

    private final NotificationServicePort notificationService;

    private final CurrentUserServicePort currentUserService;

    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        if (!request.getNextState().equals(ParcelStateType.AT_COURIER)) {
            throw new IllegalStateException();
        }
        if (Objects.isNull(request.getCourierId())) {
            throw new CourierNotProvidedException();
        }
        if (!currentUserService.hasId(request.getCourierId())) {
            throw new AuthorizationServiceException("Cannot pickup parcel as different user");
        }
        AssignedToCourier currentParcelState = (AssignedToCourier) parcelStateService.getCurrentParcelState(request.getParcelId());
        if (!currentParcelState.getCourierId().equals(request.getCourierId())) {
            throw new AuthorizationServiceException(String.format("Parcel with id %s was not assigned to you", request.getParcelId()));

        }
    }

    @Override
    public void doPostChangeOperations(ParcelState newParcelState) {
        if (getNextOperation(newParcelState).getOperationType() == OperationType.DELIVER_TO_CLIENT) {
            notificationService.sendNotification(NotificationData.getCourierWillArriveTodayNotification(newParcelState.getParcel()));
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

    @Override
    public Operation getNextOperation(ParcelState parcelState) {
        WarehouseTrackDto trackDto = getWarehouseTrack(parcelState);
        ParcelState previousState = parcelState.getPreviousState().getPreviousState(); //X -> ASSIGNED -> CURRENT
        if (previousState.getType() == ParcelStateType.AT_SENDER) {
            return new DeliverToWarehouse(trackDto.getSourceWarehouseId(), WarehouseType.LOCAL);
        }

        assert previousState.getType() == ParcelStateType.AT_WAREHOUSE :
                "Invalid state at courier before assignment should be at sender or at warehouse, check state machine!";

        AtWarehouse atWarehouse = (AtWarehouse) previousState;
        if (atWarehouse.getWarehouseType().equals(WarehouseType.LOCAL)) {
            return getAtLocalWarehouseOperation(trackDto, atWarehouse);
        }

        return getAtGlobalWarehouseOperation(trackDto, atWarehouse);
    }

    private Operation getAtGlobalWarehouseOperation(WarehouseTrackDto trackDto, AtWarehouse currentState) {
        if (Objects.nonNull(trackDto.getSecondGlobalWarehouseId()) &&
                !currentState.getId().equals(trackDto.getSecondGlobalWarehouseId())) {
            return new DeliverToWarehouse(trackDto.getSecondGlobalWarehouseId(), WarehouseType.GLOBAL);
        }
        return new DeliverToWarehouse(trackDto.getDestinationWarehouseId(), WarehouseType.LOCAL);
    }

    private Operation getAtLocalWarehouseOperation(WarehouseTrackDto trackDto, AtWarehouse atWarehouse) {
        if (atWarehouse.getWarehouseId().equals(trackDto.getDestinationWarehouseId())) {
            return new DeliverToClient();
        }

        assert Objects.nonNull(trackDto.getFirstGlobalWarehouseId()) :
                "Parcel at local magazine can only be delivered to client or to global warehouse, check warehouse component!";

        return new DeliverToWarehouse(trackDto.getFirstGlobalWarehouseId(), WarehouseType.GLOBAL);
    }

    private WarehouseTrackDto getWarehouseTrack(ParcelState parcelState) {
        Parcel parcel = parcelState.getParcel();
        return warehouseDao.getTrack(WarehouseTrackRequestDto.builder()
                .destinationPostalCode(parcel.getDeliveryAddress().getPostalCode())
                .sourcePostalCode(parcel.getSenderAddress().getPostalCode())
                .build());
    }


    private ParcelState getPreviousParcelState(ParcelState parcelState) {
        return parcelState.getPreviousState();
    }
}
