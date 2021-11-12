package pl.com.januszex.paka.flow.parcel.api.request;

import lombok.Data;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import javax.validation.constraints.NotNull;

@Data
public class DeliverToWarehouseRequest {

    @NotNull(message = "Provide warehouse id")
    private Long warehouseId;

    @NotNull(message = "Provide warehouse type")
    private WarehouseType warehouseType;
}
