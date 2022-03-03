package org.amawal.repository;

import org.amawal.domain.Langue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Langue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LangueRepository extends JpaRepository<Langue, Long> {}
