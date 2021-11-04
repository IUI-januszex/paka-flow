package pl.com.januszex.paka.flow.parcel.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.januszex.paka.flow.parcel.model.ParcelType;

import java.util.Collection;

public interface ParcelTypeJpaRepository extends JpaRepository<ParcelType, Long> {

    Collection<ParcelType> findAllByActiveTrue();
}
