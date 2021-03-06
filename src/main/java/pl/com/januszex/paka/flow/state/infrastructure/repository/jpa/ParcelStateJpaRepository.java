package pl.com.januszex.paka.flow.state.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.com.januszex.paka.flow.state.model.ParcelState;

import java.util.Optional;

public interface ParcelStateJpaRepository extends JpaRepository<ParcelState, Long> {

    @Query("select p from ParcelState p where p.parcel.id = ?1 and p.current = true")
    Optional<ParcelState> getCurrentState(long parcelId);
}
