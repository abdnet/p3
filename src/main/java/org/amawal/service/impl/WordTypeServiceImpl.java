package org.amawal.service.impl;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.WordType;
import org.amawal.repository.WordTypeRepository;
import org.amawal.service.WordTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WordType}.
 */
@Service
@Transactional
public class WordTypeServiceImpl implements WordTypeService {

    private final Logger log = LoggerFactory.getLogger(WordTypeServiceImpl.class);

    private final WordTypeRepository wordTypeRepository;

    public WordTypeServiceImpl(WordTypeRepository wordTypeRepository) {
        this.wordTypeRepository = wordTypeRepository;
    }

    @Override
    public WordType save(WordType wordType) {
        log.debug("Request to save WordType : {}", wordType);
        return wordTypeRepository.save(wordType);
    }

    @Override
    public Optional<WordType> partialUpdate(WordType wordType) {
        log.debug("Request to partially update WordType : {}", wordType);

        return wordTypeRepository
            .findById(wordType.getId())
            .map(existingWordType -> {
                if (wordType.getTypeId() != null) {
                    existingWordType.setTypeId(wordType.getTypeId());
                }
                if (wordType.getType() != null) {
                    existingWordType.setType(wordType.getType());
                }

                return existingWordType;
            })
            .map(wordTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WordType> findAll() {
        log.debug("Request to get all WordTypes");
        return wordTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WordType> findOne(Long id) {
        log.debug("Request to get WordType : {}", id);
        return wordTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WordType : {}", id);
        wordTypeRepository.deleteById(id);
    }
}
