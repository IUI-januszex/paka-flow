package pl.com.januszex.paka.warehouse.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.warehouse.dao.WarehouseDao;
import pl.com.januszex.paka.warehouse.domain.WarehouseDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseTrackRequestDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

@Service
@RequiredArgsConstructor
@Profile("!prod")
class WarehouseMockDao implements WarehouseDao {

    @Override
    public WarehouseDto getById(long id, WarehouseType type) {
        return type == WarehouseType.GLOBAL ? getGlobalLocalById(id) : getLocalById(id);
    }

    @Override
    public WarehouseDto getLocalById(long id) {
        return WarehouseDto.builder()
                .id(id)
                .city("Mock City")
                .postalCode("11-111")
                .number("1")
                .street("mock")
                .build();
    }

    @Override
    public WarehouseDto getGlobalLocalById(long id) {
        return WarehouseDto.builder()
                .id(id)
                .city("Mock City")
                .postalCode("11-111")
                .number("1")
                .street("mock")
                .build();
    }

    @Override
    public WarehouseTrackDto getTrack(WarehouseTrackRequestDto requestDto) {
        return WarehouseTrackDto.builder()
                .sourceWarehouseId(1L)
                .firstGlobalWarehouseId(2L)
                .secondGlobalWarehouseId(3L)
                .destinationWarehouseId(4L)
                .build();
    }
}
