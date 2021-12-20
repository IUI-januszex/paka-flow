package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelFeeNotPaid;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelNotPaid;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.domain.NoOperation;
import pl.com.januszex.paka.flow.parcel.domain.Operation;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.AtCourier;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.notification.api.NotificationServicePort;
import pl.com.januszex.paka.notification.domain.NotificationData;
import pl.com.januszex.paka.security.AuthorizationException;
import pl.com.januszex.paka.users.api.service.CurrentUserServicePort;

@RequiredArgsConstructor
public class ParcelDeliveredManager implements ParcelStateManager {

    private final ParcelServicePort parcelService;
    private final ParcelStateServicePort parcelStateService;
    private final CurrentUserServicePort currentUserServicePort;
    private final NotificationServicePort notificationService;

    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        if (!request.getNextState().equals(ParcelStateType.DELIVERED)) {
            throw new IllegalStateException();
        }
        checkParcelPaid(request.getParcelId());
        AtCourier currentParcelState = (AtCourier) parcelStateService.getCurrentParcelState(request.getParcelId());
        if (!currentParcelState.getCourierId().equals(request.getCourierId()) &&
                !currentUserServicePort.hasId(request.getCourierId())) {
            throw new AuthorizationException("Current user has not right to deliver this parcel");
        }
    }

    @Override
    public void doPostChangeOperations(ParcelState newParcelState) {
        Parcel parcel = newParcelState.getParcel();
        notificationService.sendNotification(NotificationData.getDeliveredNotificationForReceiver(parcel));
        notificationService.sendNotification(NotificationData.getDeliveredNotificationForSender(parcel));
    }

    @Override
    public AddressDto getSourceAddress(ParcelState parcelState) {
        return AddressDto.of(parcelState.getParcel().getSenderAddress());
    }

    @Override
    public AddressDto getDestinationAddress(ParcelState parcelState) {
        return AddressDto.of(parcelState.getParcel().getDeliveryAddress());
    }

    @Override
    public Operation getNextOperation(ParcelState parcelState) {
        return new NoOperation();
    }

    private void checkParcelPaid(long parcelId) {
        Parcel parcel = parcelService.getById(parcelId);
        if (!parcel.isFeePaid() && parcel.isFeePayable()) {
            throw new ParcelFeeNotPaid(parcelId);
        }
        if (!parcel.isPaid() && parcel.isParcelPayable()) {
            throw new ParcelNotPaid(parcelId);
        }
    }
}
