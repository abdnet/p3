package org.amawal.repository;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.Dialecte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dialecte entity.
 */
@Repository
public interface DialecteRepository extends DialecteRepositoryWithBagRelationships, JpaRepository<Dialecte, Long> {
    default Optional<Dialecte> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Dialecte> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Dialecte> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
