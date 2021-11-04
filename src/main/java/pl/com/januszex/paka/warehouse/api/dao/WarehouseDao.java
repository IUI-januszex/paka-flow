package pl.com.januszex.paka.warehouse.api.dao;

import pl.com.januszex.paka.warehouse.domain.WarehouseDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

public interface WarehouseDao {
    WarehouseDto getById(long id, WarehouseType type);

    WarehouseDto getLocalById(long id);

    WarehouseDto getGlobalLocalById(long id);
}
