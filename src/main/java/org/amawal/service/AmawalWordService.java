package org.amawal.service;

import java.util.Optional;
import org.amawal.domain.AmawalWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link AmawalWord}.
 */
public interface AmawalWordService {
    /**
     * Save a amawalWord.
     *
     * @param amawalWord the entity to save.
     * @return the persisted entity.
     */
    AmawalWord save(AmawalWord amawalWord);

    /**
     * Partially updates a amawalWord.
     *
     * @param amawalWord the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AmawalWord> partialUpdate(AmawalWord amawalWord);

    /**
     * Get all the amawalWords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AmawalWord> findAll(Pageable pageable);

    /**
     * Get all the amawalWords with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AmawalWord> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" amawalWord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AmawalWord> findOne(Long id);

    /**
     * Delete the "id" amawalWord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
