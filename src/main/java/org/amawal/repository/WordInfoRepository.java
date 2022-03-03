package org.amawal.repository;

import org.amawal.domain.WordInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WordInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WordInfoRepository extends JpaRepository<WordInfo, Long> {}
