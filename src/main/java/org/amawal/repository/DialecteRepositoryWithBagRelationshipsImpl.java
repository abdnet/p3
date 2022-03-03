package org.amawal.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.amawal.domain.Dialecte;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DialecteRepositoryWithBagRelationshipsImpl implements DialecteRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Dialecte> fetchBagRelationships(Optional<Dialecte> dialecte) {
        return dialecte.map(this::fetchRegions);
    }

    @Override
    public Page<Dialecte> fetchBagRelationships(Page<Dialecte> dialectes) {
        return new PageImpl<>(fetchBagRelationships(dialectes.getContent()), dialectes.getPageable(), dialectes.getTotalElements());
    }

    @Override
    public List<Dialecte> fetchBagRelationships(List<Dialecte> dialectes) {
        return Optional.of(dialectes).map(this::fetchRegions).get();
    }

    Dialecte fetchRegions(Dialecte result) {
        return entityManager
            .createQuery(
                "select dialecte from Dialecte dialecte left join fetch dialecte.regions where dialecte is :dialecte",
                Dialecte.class
            )
            .setParameter("dialecte", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Dialecte> fetchRegions(List<Dialecte> dialectes) {
        return entityManager
            .createQuery(
                "select distinct dialecte from Dialecte dialecte left join fetch dialecte.regions where dialecte in :dialectes",
                Dialecte.class
            )
            .setParameter("dialectes", dialectes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
