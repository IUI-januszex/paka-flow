package pl.com.januszex.paka.flow.parcel.api.repository;

import pl.com.januszex.paka.flow.base.BaseRepositoryPort;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.util.Collection;

public interface ParcelRepositoryPort extends BaseRepositoryPort<Parcel> {

    Collection<Parcel> findParcelFormWarehouse(long warehouseId, WarehouseType warehouseType);

    Collection<Parcel> findObservedParcel(long userId);

}
