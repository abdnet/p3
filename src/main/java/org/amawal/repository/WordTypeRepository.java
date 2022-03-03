package org.amawal.repository;

import org.amawal.domain.WordType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WordType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WordTypeRepository extends JpaRepository<WordType, Long> {}
