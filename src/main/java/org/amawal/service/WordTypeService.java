package org.amawal.service;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.WordType;

/**
 * Service Interface for managing {@link WordType}.
 */
public interface WordTypeService {
    /**
     * Save a wordType.
     *
     * @param wordType the entity to save.
     * @return the persisted entity.
     */
    WordType save(WordType wordType);

    /**
     * Partially updates a wordType.
     *
     * @param wordType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WordType> partialUpdate(WordType wordType);

    /**
     * Get all the wordTypes.
     *
     * @return the list of entities.
     */
    List<WordType> findAll();

    /**
     * Get the "id" wordType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WordType> findOne(Long id);

    /**
     * Delete the "id" wordType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
