package org.amawal.service.impl;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.Langue;
import org.amawal.repository.LangueRepository;
import org.amawal.service.LangueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Langue}.
 */
@Service
@Transactional
public class LangueServiceImpl implements LangueService {

    private final Logger log = LoggerFactory.getLogger(LangueServiceImpl.class);

    private final LangueRepository langueRepository;

    public LangueServiceImpl(LangueRepository langueRepository) {
        this.langueRepository = langueRepository;
    }

    @Override
    public Langue save(Langue langue) {
        log.debug("Request to save Langue : {}", langue);
        return langueRepository.save(langue);
    }

    @Override
    public Optional<Langue> partialUpdate(Langue langue) {
        log.debug("Request to partially update Langue : {}", langue);

        return langueRepository
            .findById(langue.getId())
            .map(existingLangue -> {
                if (langue.getLangueId() != null) {
                    existingLangue.setLangueId(langue.getLangueId());
                }
                if (langue.getLangue() != null) {
                    existingLangue.setLangue(langue.getLangue());
                }

                return existingLangue;
            })
            .map(langueRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Langue> findAll() {
        log.debug("Request to get all Langues");
        return langueRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Langue> findOne(Long id) {
        log.debug("Request to get Langue : {}", id);
        return langueRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Langue : {}", id);
        langueRepository.deleteById(id);
    }
}
