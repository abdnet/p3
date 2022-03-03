package org.amawal.service;

import java.util.Optional;
import org.amawal.domain.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Utilisateur}.
 */
public interface UtilisateurService {
    /**
     * Save a utilisateur.
     *
     * @param utilisateur the entity to save.
     * @return the persisted entity.
     */
    Utilisateur save(Utilisateur utilisateur);

    /**
     * Partially updates a utilisateur.
     *
     * @param utilisateur the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Utilisateur> partialUpdate(Utilisateur utilisateur);

    /**
     * Get all the utilisateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Utilisateur> findAll(Pageable pageable);

    /**
     * Get the "id" utilisateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Utilisateur> findOne(Long id);

    /**
     * Delete the "id" utilisateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
