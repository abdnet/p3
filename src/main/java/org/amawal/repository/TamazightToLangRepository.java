package org.amawal.repository;

import org.amawal.domain.TamazightToLang;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TamazightToLang entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TamazightToLangRepository extends JpaRepository<TamazightToLang, Long> {}
