package pl.com.januszex.paka.flow.state.api.service;

import pl.com.januszex.paka.flow.state.domain.ParcelState;
import pl.com.januszex.paka.flow.state.infrastructure.service.manager.ParcelStateManager;

public interface ParcelStateServicePort extends ParcelStateManager {
    ParcelState getCurrentParcelState(long parcelId);

    ParcelState getById(long id);
}
