package org.amawal.repository;

import org.amawal.domain.Groupe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Groupe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {}
