package org.amawal.service;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.Groupe;

/**
 * Service Interface for managing {@link Groupe}.
 */
public interface GroupeService {
    /**
     * Save a groupe.
     *
     * @param groupe the entity to save.
     * @return the persisted entity.
     */
    Groupe save(Groupe groupe);

    /**
     * Partially updates a groupe.
     *
     * @param groupe the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Groupe> partialUpdate(Groupe groupe);

    /**
     * Get all the groupes.
     *
     * @return the list of entities.
     */
    List<Groupe> findAll();

    /**
     * Get the "id" groupe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Groupe> findOne(Long id);

    /**
     * Delete the "id" groupe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
