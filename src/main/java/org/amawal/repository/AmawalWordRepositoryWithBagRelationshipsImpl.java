package org.amawal.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.amawal.domain.AmawalWord;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AmawalWordRepositoryWithBagRelationshipsImpl implements AmawalWordRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<AmawalWord> fetchBagRelationships(Optional<AmawalWord> amawalWord) {
        return amawalWord.map(this::fetchDialectes);
    }

    @Override
    public Page<AmawalWord> fetchBagRelationships(Page<AmawalWord> amawalWords) {
        return new PageImpl<>(fetchBagRelationships(amawalWords.getContent()), amawalWords.getPageable(), amawalWords.getTotalElements());
    }

    @Override
    public List<AmawalWord> fetchBagRelationships(List<AmawalWord> amawalWords) {
        return Optional.of(amawalWords).map(this::fetchDialectes).get();
    }

    AmawalWord fetchDialectes(AmawalWord result) {
        return entityManager
            .createQuery(
                "select amawalWord from AmawalWord amawalWord left join fetch amawalWord.dialectes where amawalWord is :amawalWord",
                AmawalWord.class
            )
            .setParameter("amawalWord", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<AmawalWord> fetchDialectes(List<AmawalWord> amawalWords) {
        return entityManager
            .createQuery(
                "select distinct amawalWord from AmawalWord amawalWord left join fetch amawalWord.dialectes where amawalWord in :amawalWords",
                AmawalWord.class
            )
            .setParameter("amawalWords", amawalWords)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
