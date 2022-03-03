package org.amawal.repository;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.Dialecte;
import org.springframework.data.domain.Page;

public interface DialecteRepositoryWithBagRelationships {
    Optional<Dialecte> fetchBagRelationships(Optional<Dialecte> dialecte);

    List<Dialecte> fetchBagRelationships(List<Dialecte> dialectes);

    Page<Dialecte> fetchBagRelationships(Page<Dialecte> dialectes);
}
