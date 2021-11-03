package pl.com.januszex.paka.flow.state.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
class AtMagazine extends ParcelState {

    private WarehouseType warehouseType;
    private Long warehouseId;

    @Override
    public ParcelStateType getType() {
        return ParcelStateType.AT_MAGAZINE;
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return nextState == ParcelStateType.ASSIGNED_TO_COURIER;
    }
}
