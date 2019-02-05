package chushka.repository;

import java.util.List;

public interface GenericRepository<E, K> {

    E save(E entity);

    // K stands for key
    E findById(K id);

    List<E> findAll();
}
