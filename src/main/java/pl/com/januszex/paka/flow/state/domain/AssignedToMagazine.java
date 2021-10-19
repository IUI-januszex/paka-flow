package pl.com.januszex.paka.flow.state.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
class AssignedToMagazine extends ParcelState {

    private Long magazineId;

    @Override
    public ParcelStateType getType() {
        return ParcelStateType.ASSIGNED_TO_MAGAZINE;
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return nextState == ParcelStateType.AT_COURIER;
    }
}
