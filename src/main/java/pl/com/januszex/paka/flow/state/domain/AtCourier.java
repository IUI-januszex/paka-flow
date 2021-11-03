package pl.com.januszex.paka.flow.state.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
class AtCourier extends ParcelState {

    private Long courierId;

    @Override
    public ParcelStateType getType() {
        return ParcelStateType.AT_COURIER;
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return nextState == ParcelStateType.DELIVERED ||
                nextState == ParcelStateType.RETURNED ||
                nextState == ParcelStateType.AT_WAREHOUSE;
    }
}
