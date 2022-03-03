package org.amawal.repository;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.AmawalWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AmawalWord entity.
 */
@Repository
public interface AmawalWordRepository extends AmawalWordRepositoryWithBagRelationships, JpaRepository<AmawalWord, Long> {
    default Optional<AmawalWord> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<AmawalWord> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<AmawalWord> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
