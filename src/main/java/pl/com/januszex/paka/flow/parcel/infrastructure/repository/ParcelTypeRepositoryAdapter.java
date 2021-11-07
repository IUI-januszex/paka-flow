package pl.com.januszex.paka.flow.parcel.infrastructure.repository;

import org.springframework.stereotype.Repository;
import pl.com.januszex.paka.flow.base.BaseRepositoryAdapter;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelTypeRepositoryPort;
import pl.com.januszex.paka.flow.parcel.infrastructure.repository.jpa.ParcelTypeJpaRepository;
import pl.com.januszex.paka.flow.parcel.model.ParcelType;

import java.util.Collection;

@Repository
class ParcelTypeRepositoryAdapter extends BaseRepositoryAdapter<ParcelType> implements ParcelTypeRepositoryPort {

    private final ParcelTypeJpaRepository parcelTypeJpaRepository;

    public ParcelTypeRepositoryAdapter(ParcelTypeJpaRepository parcelTypeJpaRepository) {
        super(parcelTypeJpaRepository);
        this.parcelTypeJpaRepository = parcelTypeJpaRepository;
    }

    @Override
    public Collection<ParcelType> getActive() {
        return parcelTypeJpaRepository.findAllByActiveTrue();
    }
}
