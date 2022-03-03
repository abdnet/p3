package org.amawal.repository;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.AmawalWord;
import org.springframework.data.domain.Page;

public interface AmawalWordRepositoryWithBagRelationships {
    Optional<AmawalWord> fetchBagRelationships(Optional<AmawalWord> amawalWord);

    List<AmawalWord> fetchBagRelationships(List<AmawalWord> amawalWords);

    Page<AmawalWord> fetchBagRelationships(Page<AmawalWord> amawalWords);
}
