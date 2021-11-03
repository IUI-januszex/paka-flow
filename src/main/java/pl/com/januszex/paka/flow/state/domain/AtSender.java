package pl.com.januszex.paka.flow.state.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class AtSender extends ParcelState {

    private WarehouseType assignedWarehouseType;
    private Long assignedWarehouseId;

    @Override
    public ParcelStateType getType() {
        return ParcelStateType.AT_SENDER;
    }

    @Override
    public boolean isNextStateValid(ParcelStateType nextState) {
        return nextState == ParcelStateType.ASSIGNED_TO_COURIER;
    }
}
