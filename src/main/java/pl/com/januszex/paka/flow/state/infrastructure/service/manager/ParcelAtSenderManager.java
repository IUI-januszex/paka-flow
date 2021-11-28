package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.base.DateTimeServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelCourierArrivalServicePort;
import pl.com.januszex.paka.flow.parcel.domain.AssignToCourierOperation;
import pl.com.januszex.paka.flow.parcel.domain.Operation;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.exception.WarehouseNotProvidedException;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.model.AtSender;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.notification.api.NotificationServicePort;
import pl.com.januszex.paka.notification.domain.NotificationData;
import pl.com.januszex.paka.notification.domain.NotificationWithPinData;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;

import java.time.LocalDate;
import java.util.Objects;

@RequiredArgsConstructor
class ParcelAtSenderManager implements ParcelStateManager {

    private final WarehouseDao warehouseDao;
    private final NotificationServicePort notificationService;
    private final ParcelCourierArrivalServicePort parcelCourierArrivalService;
    private final DateTimeServicePort dateTimeServicePort;

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
    public void doPostChangeOperations(ParcelState newParcelState) {
        Parcel parcel = newParcelState.getParcel();
        LocalDate arrivalDate = dateTimeServicePort.addWorkdays(parcelCourierArrivalService.getDaysToArriveToSender());
        parcel.setExpectedCourierArrivalDate(arrivalDate);
        notificationService.sendNotification(NotificationWithPinData.getParcelRegisteredSender(parcel));
        notificationService.sendNotification(NotificationData.getRegisterNotificationForReceiver(parcel));
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

    @Override
    public Operation getNextOperation(ParcelState parcelState) {
        return new AssignToCourierOperation();
    }

    private AtSender cast(ParcelState parcelState) {
        assert parcelState instanceof AtSender;
        return (AtSender) parcelState;
    }
}
