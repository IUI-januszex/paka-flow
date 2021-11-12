package pl.com.januszex.paka.flow.parcel.api.request;

import lombok.Data;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Data
public class DeliverToWarehouseRequest {

    @NotNull(message = "Provide warehouse id")
    private Long warehouseId;

    @NotNull(message = "Provide warehouse type")
    private WarehouseType warehouseType;

    @NotNull(message = "Provide parcel ids")
    @Size(min = 1, message = "Provide at least one parcel id")
    private Collection<Long> parcelIds;
}
