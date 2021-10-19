package pl.com.januszex.paka.flow.state.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity

public class AtMagazine extends ParcelState {

    @Column(nullable = false)
    private Long magazineId;

    @Override
    public ParcelStateType getType() {
        return ParcelStateType.AT_MAGAZINE;
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return nextState == ParcelStateType.ASSIGNED_TO_COURIER;
    }
}
