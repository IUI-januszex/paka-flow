package pl.com.januszex.paka.flow.parcel.infrastructure.repository;

import pl.com.januszex.paka.flow.base.BaseRepositoryAdapter;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelRepositoryPort;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;
import pl.com.januszex.paka.flow.parcel.infrastructure.repository.jpa.ParcelJpaRepository;

public class ParcelRepositoryAdapter extends BaseRepositoryAdapter<Parcel> implements ParcelRepositoryPort {

    private final ParcelJpaRepository parcelJpaRepository;

    public ParcelRepositoryAdapter(ParcelJpaRepository parcelJpaRepository) {
        super(parcelJpaRepository);
        this.parcelJpaRepository = parcelJpaRepository;
    }
}
