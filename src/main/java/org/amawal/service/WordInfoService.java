package org.amawal.service;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.WordInfo;

/**
 * Service Interface for managing {@link WordInfo}.
 */
public interface WordInfoService {
    /**
     * Save a wordInfo.
     *
     * @param wordInfo the entity to save.
     * @return the persisted entity.
     */
    WordInfo save(WordInfo wordInfo);

    /**
     * Partially updates a wordInfo.
     *
     * @param wordInfo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WordInfo> partialUpdate(WordInfo wordInfo);

    /**
     * Get all the wordInfos.
     *
     * @return the list of entities.
     */
    List<WordInfo> findAll();

    /**
     * Get the "id" wordInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WordInfo> findOne(Long id);

    /**
     * Delete the "id" wordInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
