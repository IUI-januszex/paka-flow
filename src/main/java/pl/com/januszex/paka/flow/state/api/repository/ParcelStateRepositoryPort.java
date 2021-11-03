package pl.com.januszex.paka.flow.state.api.repository;

import pl.com.januszex.paka.flow.base.BaseRepositoryPort;
import pl.com.januszex.paka.flow.state.domain.ParcelState;

public interface ParcelStateRepositoryPort extends BaseRepositoryPort<ParcelState> {
    ParcelState getCurrentParcelState(long parcelId);
}
