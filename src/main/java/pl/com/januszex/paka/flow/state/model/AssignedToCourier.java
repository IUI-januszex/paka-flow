package pl.com.januszex.paka.flow.state.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
class AssignedToCourier extends ParcelState {

    private Long courierId;

    @Override
    public ParcelStateType getType() {
        return ParcelStateType.ASSIGNED_TO_COURIER;
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return nextState == ParcelStateType.AT_COURIER;
    }
}
