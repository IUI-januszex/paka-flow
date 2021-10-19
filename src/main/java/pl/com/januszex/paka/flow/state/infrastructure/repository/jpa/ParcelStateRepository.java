package pl.com.januszex.paka.flow.state.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.januszex.paka.flow.state.domain.ParcelState;

public interface ParcelStateRepository extends JpaRepository<ParcelState, Long> {
}
