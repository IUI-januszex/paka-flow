package pl.com.januszex.paka.flow.state.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class AssignedToCourier extends ParcelState {

    @Column(nullable = false)
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
