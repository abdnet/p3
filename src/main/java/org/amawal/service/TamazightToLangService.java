package org.amawal.service;

import java.util.Optional;
import org.amawal.domain.TamazightToLang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TamazightToLang}.
 */
public interface TamazightToLangService {
    /**
     * Save a tamazightToLang.
     *
     * @param tamazightToLang the entity to save.
     * @return the persisted entity.
     */
    TamazightToLang save(TamazightToLang tamazightToLang);

    /**
     * Partially updates a tamazightToLang.
     *
     * @param tamazightToLang the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TamazightToLang> partialUpdate(TamazightToLang tamazightToLang);

    /**
     * Get all the tamazightToLangs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TamazightToLang> findAll(Pageable pageable);

    /**
     * Get the "id" tamazightToLang.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TamazightToLang> findOne(Long id);

    /**
     * Delete the "id" tamazightToLang.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
