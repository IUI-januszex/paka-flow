package pl.com.januszex.paka.flow.state.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class AtWarehouse extends ParcelState {

    private WarehouseType warehouseType;
    private Long warehouseId;

    @Override
    public ParcelStateType getType() {
        return ParcelStateType.AT_WAREHOUSE;
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return nextState == ParcelStateType.ASSIGNED_TO_COURIER;
    }
}
