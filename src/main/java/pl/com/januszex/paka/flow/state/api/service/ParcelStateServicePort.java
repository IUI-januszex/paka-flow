package pl.com.januszex.paka.flow.state.api.service;

import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.infrastructure.service.manager.ParcelStateManager;
import pl.com.januszex.paka.flow.state.model.ParcelState;

public interface ParcelStateServicePort extends ParcelStateManager {
    ParcelState getCurrentParcelState(long parcelId);

    ParcelState getById(long id);

    ParcelState changeParcelState(ChangeParcelStateRequest request);

    ParcelState getInitState(long localWarehouseId, Parcel parcel);
}
