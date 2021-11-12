package pl.com.januszex.paka.flow.state.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.com.januszex.paka.flow.state.api.repose.ParcelStateResponse;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
class AtCourier extends ParcelState {

    private String courierId;

    @Override
    public ParcelStateType getType() {
        return ParcelStateType.AT_COURIER;
    }

    @Override
    public ParcelStateResponse toResponse() {
        return ParcelStateResponse
                .builder()
                .type(getType())
                .courierId(courierId)
                .build();
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return nextState == ParcelStateType.DELIVERED ||
                nextState == ParcelStateType.RETURNED ||
                nextState == ParcelStateType.AT_WAREHOUSE;
    }
}
