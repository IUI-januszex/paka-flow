package pl.com.januszex.paka.flow.parcel.infrastructure.repository;

import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.base.BaseRepositoryAdapter;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelRepositoryPort;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;
import pl.com.januszex.paka.flow.parcel.infrastructure.repository.jpa.ParcelJpaRepository;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.util.Collection;

@Service
class ParcelRepositoryAdapter extends BaseRepositoryAdapter<Parcel> implements ParcelRepositoryPort {

    private final ParcelJpaRepository parcelJpaRepository;

    public ParcelRepositoryAdapter(ParcelJpaRepository parcelJpaRepository) {
        super(parcelJpaRepository);
        this.parcelJpaRepository = parcelJpaRepository;
    }

    @Override
    public Collection<Parcel> findParcelFormWarehouse(long warehouseId, WarehouseType warehouseType) {
        return parcelJpaRepository.findParcelsInWarehouse(warehouseId, warehouseType);
    }

    @Override
    public Collection<Parcel> findObservedParcel(long userId) {
        return parcelJpaRepository.findAllByObserverIdsContains(userId);
    }
}
