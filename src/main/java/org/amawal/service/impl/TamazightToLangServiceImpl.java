package org.amawal.service.impl;

import java.util.Optional;
import org.amawal.domain.TamazightToLang;
import org.amawal.repository.TamazightToLangRepository;
import org.amawal.service.TamazightToLangService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TamazightToLang}.
 */
@Service
@Transactional
public class TamazightToLangServiceImpl implements TamazightToLangService {

    private final Logger log = LoggerFactory.getLogger(TamazightToLangServiceImpl.class);

    private final TamazightToLangRepository tamazightToLangRepository;

    public TamazightToLangServiceImpl(TamazightToLangRepository tamazightToLangRepository) {
        this.tamazightToLangRepository = tamazightToLangRepository;
    }

    @Override
    public TamazightToLang save(TamazightToLang tamazightToLang) {
        log.debug("Request to save TamazightToLang : {}", tamazightToLang);
        return tamazightToLangRepository.save(tamazightToLang);
    }

    @Override
    public Optional<TamazightToLang> partialUpdate(TamazightToLang tamazightToLang) {
        log.debug("Request to partially update TamazightToLang : {}", tamazightToLang);

        return tamazightToLangRepository
            .findById(tamazightToLang.getId())
            .map(existingTamazightToLang -> {
                if (tamazightToLang.getTraductionId() != null) {
                    existingTamazightToLang.setTraductionId(tamazightToLang.getTraductionId());
                }

                return existingTamazightToLang;
            })
            .map(tamazightToLangRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TamazightToLang> findAll(Pageable pageable) {
        log.debug("Request to get all TamazightToLangs");
        return tamazightToLangRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TamazightToLang> findOne(Long id) {
        log.debug("Request to get TamazightToLang : {}", id);
        return tamazightToLangRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TamazightToLang : {}", id);
        tamazightToLangRepository.deleteById(id);
    }
}
