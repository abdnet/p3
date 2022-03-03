package org.amawal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.amawal.domain.WordType;
import org.amawal.repository.WordTypeRepository;
import org.amawal.service.WordTypeService;
import org.amawal.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.amawal.domain.WordType}.
 */
@RestController
@RequestMapping("/api")
public class WordTypeResource {

    private final Logger log = LoggerFactory.getLogger(WordTypeResource.class);

    private static final String ENTITY_NAME = "wordType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WordTypeService wordTypeService;

    private final WordTypeRepository wordTypeRepository;

    public WordTypeResource(WordTypeService wordTypeService, WordTypeRepository wordTypeRepository) {
        this.wordTypeService = wordTypeService;
        this.wordTypeRepository = wordTypeRepository;
    }

    /**
     * {@code POST  /word-types} : Create a new wordType.
     *
     * @param wordType the wordType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wordType, or with status {@code 400 (Bad Request)} if the wordType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/word-types")
    public ResponseEntity<WordType> createWordType(@RequestBody WordType wordType) throws URISyntaxException {
        log.debug("REST request to save WordType : {}", wordType);
        if (wordType.getId() != null) {
            throw new BadRequestAlertException("A new wordType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WordType result = wordTypeService.save(wordType);
        return ResponseEntity
            .created(new URI("/api/word-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /word-types/:id} : Updates an existing wordType.
     *
     * @param id the id of the wordType to save.
     * @param wordType the wordType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wordType,
     * or with status {@code 400 (Bad Request)} if the wordType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wordType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/word-types/{id}")
    public ResponseEntity<WordType> updateWordType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WordType wordType
    ) throws URISyntaxException {
        log.debug("REST request to update WordType : {}, {}", id, wordType);
        if (wordType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wordType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wordTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WordType result = wordTypeService.save(wordType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wordType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /word-types/:id} : Partial updates given fields of an existing wordType, field will ignore if it is null
     *
     * @param id the id of the wordType to save.
     * @param wordType the wordType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wordType,
     * or with status {@code 400 (Bad Request)} if the wordType is not valid,
     * or with status {@code 404 (Not Found)} if the wordType is not found,
     * or with status {@code 500 (Internal Server Error)} if the wordType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/word-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WordType> partialUpdateWordType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WordType wordType
    ) throws URISyntaxException {
        log.debug("REST request to partial update WordType partially : {}, {}", id, wordType);
        if (wordType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wordType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wordTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WordType> result = wordTypeService.partialUpdate(wordType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wordType.getId().toString())
        );
    }

    /**
     * {@code GET  /word-types} : get all the wordTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wordTypes in body.
     */
    @GetMapping("/word-types")
    public List<WordType> getAllWordTypes() {
        log.debug("REST request to get all WordTypes");
        return wordTypeService.findAll();
    }

    /**
     * {@code GET  /word-types/:id} : get the "id" wordType.
     *
     * @param id the id of the wordType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wordType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/word-types/{id}")
    public ResponseEntity<WordType> getWordType(@PathVariable Long id) {
        log.debug("REST request to get WordType : {}", id);
        Optional<WordType> wordType = wordTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wordType);
    }

    /**
     * {@code DELETE  /word-types/:id} : delete the "id" wordType.
     *
     * @param id the id of the wordType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/word-types/{id}")
    public ResponseEntity<Void> deleteWordType(@PathVariable Long id) {
        log.debug("REST request to delete WordType : {}", id);
        wordTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
