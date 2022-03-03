package org.amawal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.amawal.domain.WordInfo;
import org.amawal.repository.WordInfoRepository;
import org.amawal.service.WordInfoService;
import org.amawal.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.amawal.domain.WordInfo}.
 */
@RestController
@RequestMapping("/api")
public class WordInfoResource {

    private final Logger log = LoggerFactory.getLogger(WordInfoResource.class);

    private static final String ENTITY_NAME = "wordInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WordInfoService wordInfoService;

    private final WordInfoRepository wordInfoRepository;

    public WordInfoResource(WordInfoService wordInfoService, WordInfoRepository wordInfoRepository) {
        this.wordInfoService = wordInfoService;
        this.wordInfoRepository = wordInfoRepository;
    }

    /**
     * {@code POST  /word-infos} : Create a new wordInfo.
     *
     * @param wordInfo the wordInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wordInfo, or with status {@code 400 (Bad Request)} if the wordInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/word-infos")
    public ResponseEntity<WordInfo> createWordInfo(@RequestBody WordInfo wordInfo) throws URISyntaxException {
        log.debug("REST request to save WordInfo : {}", wordInfo);
        if (wordInfo.getId() != null) {
            throw new BadRequestAlertException("A new wordInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WordInfo result = wordInfoService.save(wordInfo);
        return ResponseEntity
            .created(new URI("/api/word-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /word-infos/:id} : Updates an existing wordInfo.
     *
     * @param id the id of the wordInfo to save.
     * @param wordInfo the wordInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wordInfo,
     * or with status {@code 400 (Bad Request)} if the wordInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wordInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/word-infos/{id}")
    public ResponseEntity<WordInfo> updateWordInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WordInfo wordInfo
    ) throws URISyntaxException {
        log.debug("REST request to update WordInfo : {}, {}", id, wordInfo);
        if (wordInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wordInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wordInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WordInfo result = wordInfoService.save(wordInfo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wordInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /word-infos/:id} : Partial updates given fields of an existing wordInfo, field will ignore if it is null
     *
     * @param id the id of the wordInfo to save.
     * @param wordInfo the wordInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wordInfo,
     * or with status {@code 400 (Bad Request)} if the wordInfo is not valid,
     * or with status {@code 404 (Not Found)} if the wordInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the wordInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/word-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WordInfo> partialUpdateWordInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WordInfo wordInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update WordInfo partially : {}, {}", id, wordInfo);
        if (wordInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wordInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wordInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WordInfo> result = wordInfoService.partialUpdate(wordInfo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wordInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /word-infos} : get all the wordInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wordInfos in body.
     */
    @GetMapping("/word-infos")
    public List<WordInfo> getAllWordInfos() {
        log.debug("REST request to get all WordInfos");
        return wordInfoService.findAll();
    }

    /**
     * {@code GET  /word-infos/:id} : get the "id" wordInfo.
     *
     * @param id the id of the wordInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wordInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/word-infos/{id}")
    public ResponseEntity<WordInfo> getWordInfo(@PathVariable Long id) {
        log.debug("REST request to get WordInfo : {}", id);
        Optional<WordInfo> wordInfo = wordInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wordInfo);
    }

    /**
     * {@code DELETE  /word-infos/:id} : delete the "id" wordInfo.
     *
     * @param id the id of the wordInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/word-infos/{id}")
    public ResponseEntity<Void> deleteWordInfo(@PathVariable Long id) {
        log.debug("REST request to delete WordInfo : {}", id);
        wordInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
