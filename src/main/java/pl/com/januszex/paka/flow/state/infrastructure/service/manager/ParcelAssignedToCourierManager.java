package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import lombok.RequiredArgsConstructor;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.base.DateTimeServicePort;
import pl.com.januszex.paka.flow.parcel.domain.Operation;
import pl.com.januszex.paka.flow.parcel.domain.PickupOperation;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.exception.CourierNotProvidedException;
import pl.com.januszex.paka.flow.state.api.exception.DeliveryTooSoonException;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;

import java.util.Objects;

@RequiredArgsConstructor
class ParcelAssignedToCourierManager implements ParcelStateManager {

    private final ParcelStateServicePort parcelStateService;
    private final DateTimeServicePort dateTimeService;

    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        if (!request.getNextState().equals(ParcelStateType.ASSIGNED_TO_COURIER)) {
            throw new IllegalStateException();
        }
        if (Objects.isNull(request.getCourierId())) {
            throw new CourierNotProvidedException();
        }

        verifyArrivalDate(request);
    }

    private void verifyArrivalDate(ChangeParcelStateRequest request) {
        ParcelState currentSate = parcelStateService.getCurrentParcelState(request.getParcelId());
        Parcel parcel = currentSate.getParcel();
        if (isArrivalToCourier(currentSate) && isExpectedDateNotToday(parcel)) {
            throw new DeliveryTooSoonException(request.getParcelId(), request.getCourierId());
        }

        if (isNextAddressDeliveryAddress(currentSate) && isExpectedDateNotToday(parcel)) {
            throw new DeliveryTooSoonException(request.getParcelId(), request.getCourierId());
        }
    }

    @Override
    public void doPostChangeOperations(ParcelState newParcelState) {
        // do nothing
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
        return new PickupOperation();
    }


    private ParcelState getPreviousParcelState(ParcelState parcelState) {
        return parcelStateService.getById(parcelState.getPreviousState().getId());
    }

    private boolean isArrivalToCourier(ParcelState currentSate) {
        return currentSate.getType().equals(ParcelStateType.AT_SENDER);
    }

    private boolean isNextAddressDeliveryAddress(ParcelState currentSate) {
        AddressDto deliveryAddress = AddressDto.of(currentSate.getParcel().getDeliveryAddress());
        return deliveryAddress.equals(parcelStateService.getDestinationAddress(currentSate));
    }

    private boolean isExpectedDateNotToday(Parcel parcel) {
        return !dateTimeService.isToday(parcel.getExpectedCourierArrivalDate());
    }
}
