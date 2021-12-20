package pl.com.januszex.paka.flow.state.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.com.januszex.paka.flow.state.api.repose.ParcelStateResponse;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
class Delivered extends ParcelState {
    @Override
    public ParcelStateType getType() {
        return ParcelStateType.DELIVERED;
    }

    @Override
    public ParcelStateResponse toResponse() {
        return ParcelStateResponse.builder()
                .type(getType())
                .changeTime(getChangeTime())
                .build();
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return false;
    }
}
