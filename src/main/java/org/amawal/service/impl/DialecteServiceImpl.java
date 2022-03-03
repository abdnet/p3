package org.amawal.service.impl;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.Dialecte;
import org.amawal.repository.DialecteRepository;
import org.amawal.service.DialecteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dialecte}.
 */
@Service
@Transactional
public class DialecteServiceImpl implements DialecteService {

    private final Logger log = LoggerFactory.getLogger(DialecteServiceImpl.class);

    private final DialecteRepository dialecteRepository;

    public DialecteServiceImpl(DialecteRepository dialecteRepository) {
        this.dialecteRepository = dialecteRepository;
    }

    @Override
    public Dialecte save(Dialecte dialecte) {
        log.debug("Request to save Dialecte : {}", dialecte);
        return dialecteRepository.save(dialecte);
    }

    @Override
    public Optional<Dialecte> partialUpdate(Dialecte dialecte) {
        log.debug("Request to partially update Dialecte : {}", dialecte);

        return dialecteRepository
            .findById(dialecte.getId())
            .map(existingDialecte -> {
                if (dialecte.getDialecteId() != null) {
                    existingDialecte.setDialecteId(dialecte.getDialecteId());
                }
                if (dialecte.getDialecte() != null) {
                    existingDialecte.setDialecte(dialecte.getDialecte());
                }

                return existingDialecte;
            })
            .map(dialecteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dialecte> findAll() {
        log.debug("Request to get all Dialectes");
        return dialecteRepository.findAllWithEagerRelationships();
    }

    public Page<Dialecte> findAllWithEagerRelationships(Pageable pageable) {
        return dialecteRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dialecte> findOne(Long id) {
        log.debug("Request to get Dialecte : {}", id);
        return dialecteRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dialecte : {}", id);
        dialecteRepository.deleteById(id);
    }
}
