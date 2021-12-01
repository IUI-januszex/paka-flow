package pl.com.januszex.paka.flow.state.infrastructure.repository;

import org.springframework.stereotype.Repository;
import pl.com.januszex.paka.flow.base.BaseRepositoryAdapter;
import pl.com.januszex.paka.flow.state.api.repository.ParcelStateRepositoryPort;
import pl.com.januszex.paka.flow.state.infrastructure.repository.jpa.ParcelStateJpaRepository;
import pl.com.januszex.paka.flow.state.model.ParcelState;

import java.util.Optional;

@Repository
public class ParcelStateRepositoryAdapter extends BaseRepositoryAdapter<ParcelState> implements ParcelStateRepositoryPort {

    private final ParcelStateJpaRepository parcelStateJpaRepository;

    public ParcelStateRepositoryAdapter(ParcelStateJpaRepository parcelStateJpaRepository) {
        super(parcelStateJpaRepository);
        this.parcelStateJpaRepository = parcelStateJpaRepository;
    }

    @Override
    public Optional<ParcelState> getCurrentParcelState(long parcelId) {
        return parcelStateJpaRepository.getCurrentState(parcelId);
    }
}
