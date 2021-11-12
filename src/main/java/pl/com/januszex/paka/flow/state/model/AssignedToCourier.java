package pl.com.januszex.paka.flow.state.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.com.januszex.paka.flow.state.api.repose.ParcelStateResponse;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
class AssignedToCourier extends ParcelState {

    private String courierId;

    @Override
    public ParcelStateType getType() {
        return ParcelStateType.ASSIGNED_TO_COURIER;
    }

    @Override
    public ParcelStateResponse toResponse() {
        return ParcelStateResponse.builder()
                .type(getType())
                .changeTime(getChangeTime())
                .courierId(courierId)
                .build();
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return nextState == ParcelStateType.AT_COURIER;
    }
}
