package pl.com.januszex.paka.flow.parcel.api.repository;

import pl.com.januszex.paka.flow.base.BaseRepositoryPort;
import pl.com.januszex.paka.flow.parcel.domain.ParcelType;

import java.util.Collection;

public interface ParcelTypeRepositoryPort extends BaseRepositoryPort<ParcelType> {

    Collection<ParcelType> getActive();

}
