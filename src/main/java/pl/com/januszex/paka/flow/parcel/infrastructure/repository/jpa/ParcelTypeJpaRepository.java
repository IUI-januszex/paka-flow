package pl.com.januszex.paka.flow.parcel.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.januszex.paka.flow.parcel.domain.ParcelType;

public interface ParcelTypeJpaRepository extends JpaRepository<ParcelType, Long> {
}
