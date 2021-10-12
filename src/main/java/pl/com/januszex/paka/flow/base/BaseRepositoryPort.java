package pl.com.januszex.paka.flow.base;

import java.util.Collection;
import java.util.Optional;

public interface BaseRepositoryPort<T> {
    Optional<T> getById(Long id);

    Collection<T> getAll();

    T add(T entity);

    T update(T entity);

    void delete(T entity);
}
