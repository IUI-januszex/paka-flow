package pl.com.januszex.paka.flow.parcel.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelBriefView;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelDetailView;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelTypeResponse;
import pl.com.januszex.paka.flow.parcel.domain.*;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.users.api.service.CurrentUserServicePort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelViewCreator {

    private final ParcelStateServicePort parcelStateService;
    private final ParcelServicePort parcelService;
    private final CurrentUserServicePort currentUserService;

    public ParcelBriefView map(Parcel parcel) {
        ParcelState currentParcelState = parcelStateService.getCurrentParcelState(parcel.getId());
        return ParcelBriefView.builder()
                .id(parcel.getId())
                .type(ParcelTypeResponse.of(parcel.getParcelType()))
                .senderInfo(parcel.getSenderDetails())
                .senderAddress(AddressDto.of(parcel.getSenderAddress()))
                .receiverInfo(parcel.getReceiverDetails())
                .receiverAddress(AddressDto.of(parcel.getDeliveryAddress()))
                .estimatedDeliveryTime(parcel.getExpectedCourierArrivalDate())
                .parcelFee(parcel.getParcelFee())
                .parcelPrice(parcel.getParcelPrice())
                .feePaid(parcel.isFeePaid())
                .parcelPaid(parcel.isPaid())
                .moveable(parcelService.isMoveable(parcel))
                .currentState(currentParcelState.toResponse())
                .build();
    }

    public ParcelDetailView mapWithDetails(Parcel parcel) {
        ParcelState currentState = parcelStateService.getCurrentParcelState(parcel.getId());
        return ParcelDetailView.builder()
                .id(parcel.getId())
                .type(ParcelTypeResponse.of(parcel.getParcelType()))
                .senderInfo(parcel.getSenderDetails())
                .senderAddress(AddressDto.of(parcel.getSenderAddress()))
                .receiverInfo(parcel.getReceiverDetails())
                .receiverAddress(AddressDto.of(parcel.getDeliveryAddress()))
                .expectedCourierArrivalDate(parcel.getExpectedCourierArrivalDate())
                .parcelFee(parcel.getParcelFee())
                .parcelPrice(parcel.getParcelPrice())
                .currentSate(currentState.toResponse())
                .sourceAddress(parcelStateService.getSourceAddress(currentState))
                .destinationAddress(parcelStateService.getDestinationAddress(currentState))
                .operations(getOperations(parcel, currentState))
                .moveable(parcelService.isMoveable(parcel))
                .build();

    }

    private Collection<Operation> getOperations(Parcel parcel, ParcelState currentState) {
        List<Operation> operations = new ArrayList<>();
        Operation nextOperation = parcelStateService.getNextOperation(currentState);
        if (!nextOperation.getOperationType().equals(OperationType.NO_OPERATION) &&
                currentUserService.isWorker()) {
            operations.add(nextOperation);
        }
        if (currentUserService.isClient() &&
                currentState.getType().equals(ParcelStateType.AT_SENDER)) {
            operations.add(new EditOperation());
            operations.add(new DeleteOperation());
        }
        if (currentUserService.isCourier() &&
                nextOperation.getOperationType().equals(OperationType.DELIVER_TO_CLIENT)) {
            operations.add(new DeliveryAttempt());
            operations.add(new ReturnToWarehouse());
            if (!parcel.isFeePaid() && parcel.isFeePayable()) {
                operations.add(new PayFeeOperation(parcel.getParcelFee()));
            }
            if (!parcel.isPaid() && parcel.isParcelPayable()) {
                operations.add(new PayParcelRequest(parcel.getParcelPrice()));
            }
        }
        if (currentUserService.isCourier() &&
                currentState.getPreviousState().getType().equals(ParcelStateType.AT_SENDER) &&
                (!parcel.isFeePaid() && parcel.isFeePayable())) {
            operations.add(new PayFeeOperation(parcel.getParcelFee()));
        }
        return operations;
    }

}
