package pl.com.januszex.paka.flow.parcel.domain;

import lombok.Getter;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

@Getter
public class DeliverToWarehouse extends Operation {

    private final long warehouseId;
    private final WarehouseType warehouseType;

    public DeliverToWarehouse(long warehouseId, WarehouseType warehouseType) {
        super(OperationType.DELIVER_TO_WAREHOUSE);
        this.warehouseId = warehouseId;
        this.warehouseType = warehouseType;
    }
}
