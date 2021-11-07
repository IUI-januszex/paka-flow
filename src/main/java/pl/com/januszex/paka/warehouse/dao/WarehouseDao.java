package pl.com.januszex.paka.warehouse.dao;

import pl.com.januszex.paka.warehouse.domain.WarehouseDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackRequestDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

public interface WarehouseDao {
    WarehouseDto getById(long id, WarehouseType type);

    WarehouseDto getLocalById(long id);

    WarehouseDto getGlobalLocalById(long id);

    WarehouseTrackDto getTrack(WarehouseTrackRequestDto requestDto);
}
