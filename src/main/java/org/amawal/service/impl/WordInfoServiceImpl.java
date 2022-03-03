package org.amawal.service.impl;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.WordInfo;
import org.amawal.repository.WordInfoRepository;
import org.amawal.service.WordInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WordInfo}.
 */
@Service
@Transactional
public class WordInfoServiceImpl implements WordInfoService {

    private final Logger log = LoggerFactory.getLogger(WordInfoServiceImpl.class);

    private final WordInfoRepository wordInfoRepository;

    public WordInfoServiceImpl(WordInfoRepository wordInfoRepository) {
        this.wordInfoRepository = wordInfoRepository;
    }

    @Override
    public WordInfo save(WordInfo wordInfo) {
        log.debug("Request to save WordInfo : {}", wordInfo);
        return wordInfoRepository.save(wordInfo);
    }

    @Override
    public Optional<WordInfo> partialUpdate(WordInfo wordInfo) {
        log.debug("Request to partially update WordInfo : {}", wordInfo);

        return wordInfoRepository
            .findById(wordInfo.getId())
            .map(existingWordInfo -> {
                if (wordInfo.getWordInfoId() != null) {
                    existingWordInfo.setWordInfoId(wordInfo.getWordInfoId());
                }
                if (wordInfo.getContributionDate() != null) {
                    existingWordInfo.setContributionDate(wordInfo.getContributionDate());
                }
                if (wordInfo.getValidationDate() != null) {
                    existingWordInfo.setValidationDate(wordInfo.getValidationDate());
                }
                if (wordInfo.getEtat() != null) {
                    existingWordInfo.setEtat(wordInfo.getEtat());
                }

                return existingWordInfo;
            })
            .map(wordInfoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WordInfo> findAll() {
        log.debug("Request to get all WordInfos");
        return wordInfoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WordInfo> findOne(Long id) {
        log.debug("Request to get WordInfo : {}", id);
        return wordInfoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WordInfo : {}", id);
        wordInfoRepository.deleteById(id);
    }
}
