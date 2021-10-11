package pl.com.januszex.paka.flow.parcel.infrastructure.repository;

import org.springframework.stereotype.Repository;
import pl.com.januszex.paka.flow.base.BaseRepositoryAdapter;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelTypeRepositoryPort;
import pl.com.januszex.paka.flow.parcel.domain.ParcelType;
import pl.com.januszex.paka.flow.parcel.infrastructure.repository.jpa.ParcelTypeJpaRepository;

@Repository
class ParcelTypeRepositoryAdapter extends BaseRepositoryAdapter<ParcelType> implements ParcelTypeRepositoryPort {

    public ParcelTypeRepositoryAdapter(ParcelTypeJpaRepository parcelTypeJpaRepository) {
        super(parcelTypeJpaRepository);
    }

}
