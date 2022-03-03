package org.amawal.service.impl;

import java.util.Optional;
import org.amawal.domain.AmawalWord;
import org.amawal.repository.AmawalWordRepository;
import org.amawal.service.AmawalWordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AmawalWord}.
 */
@Service
@Transactional
public class AmawalWordServiceImpl implements AmawalWordService {

    private final Logger log = LoggerFactory.getLogger(AmawalWordServiceImpl.class);

    private final AmawalWordRepository amawalWordRepository;

    public AmawalWordServiceImpl(AmawalWordRepository amawalWordRepository) {
        this.amawalWordRepository = amawalWordRepository;
    }

    @Override
    public AmawalWord save(AmawalWord amawalWord) {
        log.debug("Request to save AmawalWord : {}", amawalWord);
        return amawalWordRepository.save(amawalWord);
    }

    @Override
    public Optional<AmawalWord> partialUpdate(AmawalWord amawalWord) {
        log.debug("Request to partially update AmawalWord : {}", amawalWord);

        return amawalWordRepository
            .findById(amawalWord.getId())
            .map(existingAmawalWord -> {
                if (amawalWord.getWordId() != null) {
                    existingAmawalWord.setWordId(amawalWord.getWordId());
                }
                if (amawalWord.getOrthographeTifinagh() != null) {
                    existingAmawalWord.setOrthographeTifinagh(amawalWord.getOrthographeTifinagh());
                }
                if (amawalWord.getOrthographeLatin() != null) {
                    existingAmawalWord.setOrthographeLatin(amawalWord.getOrthographeLatin());
                }

                return existingAmawalWord;
            })
            .map(amawalWordRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmawalWord> findAll(Pageable pageable) {
        log.debug("Request to get all AmawalWords");
        return amawalWordRepository.findAll(pageable);
    }

    public Page<AmawalWord> findAllWithEagerRelationships(Pageable pageable) {
        return amawalWordRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AmawalWord> findOne(Long id) {
        log.debug("Request to get AmawalWord : {}", id);
        return amawalWordRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AmawalWord : {}", id);
        amawalWordRepository.deleteById(id);
    }
}
