package pl.com.januszex.paka.flow.parcel.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;

public interface ParcelJpaRepository extends JpaRepository<Parcel, Long> {
}
