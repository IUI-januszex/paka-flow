package pl.com.januszex.paka.warehouse.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.com.januszex.paka.flow.configuration.RestServiceUrls;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;
import pl.com.januszex.paka.warehouse.api.dto.WarehouseDto;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.net.URI;

@Service
@RequiredArgsConstructor
class WarehouseRestDao implements WarehouseDao {

    private final RestServiceUrls restServiceUrls;
    private final RestTemplate restTemplate;

    @Override
    public WarehouseDto getById(long id, WarehouseType type) {
        return type == WarehouseType.GLOBAL ? getGlobalLocalById(id) : getLocalById(id);
    }

    @Override
    public WarehouseDto getLocalById(long id) {
        URI uri = UriComponentsBuilder.fromUri(URI.create(restServiceUrls.getPakaWarehouseApiUrl()))
                .fragment("/LocalWarehouse")
                .fragment("{id}")
                .build(id);
        return restTemplate.getForObject(uri, WarehouseDto.class);
    }

    @Override
    public WarehouseDto getGlobalLocalById(long id) {
        URI uri = UriComponentsBuilder.fromUri(URI.create(restServiceUrls.getPakaWarehouseApiUrl()))
                .fragment("/GlobalWarehouse")
                .fragment("{id}")
                .build(id);
        return restTemplate.getForObject(uri, WarehouseDto.class);
    }
}
