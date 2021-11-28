package pl.com.januszex.paka.flow.parcel.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelDetailView;
import pl.com.januszex.paka.flow.parcel.domain.*;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.users.service.CurrentUserServicePort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelViewCreator {

    private final ParcelStateServicePort parcelStateService;
    private final CurrentUserServicePort currentUserService;

    public ParcelDetailView mapWithDetails(Parcel parcel) {
        ParcelState currentState = parcelStateService.getCurrentParcelState(parcel.getId());
        return ParcelDetailView.builder()
                .id(parcel.getId())
                .senderInfo(parcel.getSenderDetails())
                .receiverInfo(parcel.getReceiverDetails())
                .expectedCourierArrivalDate(parcel.getExpectedCourierArrivalDate())
                .parcelFee(parcel.getParcelFee())
                .parcelPrice(parcel.getParcelPrice())
                .currentSate(currentState.toResponse())
                .sourceAddress(parcelStateService.getSourceAddress(currentState))
                .destinationAddress(parcelStateService.getDestinationAddress(currentState))
                .operations(getOperations(parcel, currentState))
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
        if (currentUserService.isCourier()) {
            if (!parcel.isFeePaid() && parcel.isFeePaid()) {
                operations.add(new PayFeeOperation(parcel.getParcelFee()));
            }
            if (!parcel.isPaid() && parcel.isParcelPayable()) {
                operations.add(new PayParcelOperation(parcel.getParcelPrice()));
            }
        }
        return operations;
    }

}
