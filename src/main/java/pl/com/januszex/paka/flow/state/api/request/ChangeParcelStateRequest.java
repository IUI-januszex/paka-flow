package pl.com.januszex.paka.flow.state.api.request;

import lombok.Data;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

@Data
public class ChangeParcelStateRequest {

    private ParcelStateType nextState;

    private String courierId;

    private WarehouseType warehouseType;

    private Long warehouseId;

    private Long parcelId;
}
