package org.amawal.service;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.Langue;

/**
 * Service Interface for managing {@link Langue}.
 */
public interface LangueService {
    /**
     * Save a langue.
     *
     * @param langue the entity to save.
     * @return the persisted entity.
     */
    Langue save(Langue langue);

    /**
     * Partially updates a langue.
     *
     * @param langue the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Langue> partialUpdate(Langue langue);

    /**
     * Get all the langues.
     *
     * @return the list of entities.
     */
    List<Langue> findAll();

    /**
     * Get the "id" langue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Langue> findOne(Long id);

    /**
     * Delete the "id" langue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
