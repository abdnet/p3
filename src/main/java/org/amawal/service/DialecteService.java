package org.amawal.service;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.Dialecte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Dialecte}.
 */
public interface DialecteService {
    /**
     * Save a dialecte.
     *
     * @param dialecte the entity to save.
     * @return the persisted entity.
     */
    Dialecte save(Dialecte dialecte);

    /**
     * Partially updates a dialecte.
     *
     * @param dialecte the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dialecte> partialUpdate(Dialecte dialecte);

    /**
     * Get all the dialectes.
     *
     * @return the list of entities.
     */
    List<Dialecte> findAll();

    /**
     * Get all the dialectes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dialecte> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" dialecte.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dialecte> findOne(Long id);

    /**
     * Delete the "id" dialecte.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
