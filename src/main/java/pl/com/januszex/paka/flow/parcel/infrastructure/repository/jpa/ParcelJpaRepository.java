package pl.com.januszex.paka.flow.parcel.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.com.januszex.paka.flow.parcel.model.Parcel;

import java.util.Collection;

public interface ParcelJpaRepository extends JpaRepository<Parcel, Long> {

    @Query(value = "SELECT * FROM PARCEL p WHERE p.ID IN (SELECT po.PARCEL_ID FROM PARCEL_OBSERVER_IDS po WHERE po.OBSERVER_IDS = ?1)",
    nativeQuery = true)
    Collection<Parcel> findAllByObserverIdsContains(String userId);

    Collection<Parcel> findAllBySendingUserId(String sendingUserId);

    @Query(value = "SELECT * FROM PARCEL p WHERE p.ID IN " +
            "(SELECT ps.PARCEL_ID FROM PARCEL_STATE ps " +
            "WHERE ps.CURRENT AND ps.COURIER_ID = ?1)",
            nativeQuery = true)
    Collection<Parcel> findParcelsOfCourier(String courierId);

    @Query(value = "SELECT * FROM PARCEL p WHERE p.ID IN " +
            "(SELECT ps.PARCEL_ID FROM PARCEL_STATE ps " +
            "WHERE ps.CURRENT AND ps.WAREHOUSE_ID = ?1 AND WAREHOUSE_TYPE = ?2)",
            nativeQuery = true)
    Collection<Parcel> findParcelsInWarehouse(long warehouse, long warehouseType);
}
