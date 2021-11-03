package pl.com.januszex.paka.flow.state.api.request;

import lombok.Data;
import pl.com.januszex.paka.flow.state.domain.ParcelStateType;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

@Data
public class ChangeParcelStateRequest {

    private ParcelStateType nextState;

    private Long courierId;

    private WarehouseType warehouseType;

    private Long warehouseId;
}
