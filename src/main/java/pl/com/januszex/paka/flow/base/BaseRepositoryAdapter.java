package pl.com.januszex.paka.flow.base;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepositoryAdapter<T> implements BaseRepositoryPort<T> {

    private final JpaRepository<T, Long> repository;

    @Override
    public Optional<T> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Collection<T> getAll() {
        return repository.findAll();
    }

    @Override
    public T update(T entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }
}
