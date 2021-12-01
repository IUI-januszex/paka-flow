package pl.com.januszex.paka.flow.state.api.repository;

import pl.com.januszex.paka.flow.base.BaseRepositoryPort;
import pl.com.januszex.paka.flow.state.model.ParcelState;

import java.util.Optional;

public interface ParcelStateRepositoryPort extends BaseRepositoryPort<ParcelState> {
    Optional<ParcelState> getCurrentParcelState(long parcelId);
}
