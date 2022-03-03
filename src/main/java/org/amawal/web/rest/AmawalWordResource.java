package org.amawal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.amawal.domain.AmawalWord;
import org.amawal.repository.AmawalWordRepository;
import org.amawal.service.AmawalWordService;
import org.amawal.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.amawal.domain.AmawalWord}.
 */
@RestController
@RequestMapping("/api")
public class AmawalWordResource {

    private final Logger log = LoggerFactory.getLogger(AmawalWordResource.class);

    private static final String ENTITY_NAME = "amawalWord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AmawalWordService amawalWordService;

    private final AmawalWordRepository amawalWordRepository;

    public AmawalWordResource(AmawalWordService amawalWordService, AmawalWordRepository amawalWordRepository) {
        this.amawalWordService = amawalWordService;
        this.amawalWordRepository = amawalWordRepository;
    }

    /**
     * {@code POST  /amawal-words} : Create a new amawalWord.
     *
     * @param amawalWord the amawalWord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new amawalWord, or with status {@code 400 (Bad Request)} if the amawalWord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/amawal-words")
    public ResponseEntity<AmawalWord> createAmawalWord(@RequestBody AmawalWord amawalWord) throws URISyntaxException {
        log.debug("REST request to save AmawalWord : {}", amawalWord);
        if (amawalWord.getId() != null) {
            throw new BadRequestAlertException("A new amawalWord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AmawalWord result = amawalWordService.save(amawalWord);
        return ResponseEntity
            .created(new URI("/api/amawal-words/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /amawal-words/:id} : Updates an existing amawalWord.
     *
     * @param id the id of the amawalWord to save.
     * @param amawalWord the amawalWord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amawalWord,
     * or with status {@code 400 (Bad Request)} if the amawalWord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the amawalWord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/amawal-words/{id}")
    public ResponseEntity<AmawalWord> updateAmawalWord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AmawalWord amawalWord
    ) throws URISyntaxException {
        log.debug("REST request to update AmawalWord : {}, {}", id, amawalWord);
        if (amawalWord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amawalWord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amawalWordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AmawalWord result = amawalWordService.save(amawalWord);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amawalWord.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /amawal-words/:id} : Partial updates given fields of an existing amawalWord, field will ignore if it is null
     *
     * @param id the id of the amawalWord to save.
     * @param amawalWord the amawalWord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amawalWord,
     * or with status {@code 400 (Bad Request)} if the amawalWord is not valid,
     * or with status {@code 404 (Not Found)} if the amawalWord is not found,
     * or with status {@code 500 (Internal Server Error)} if the amawalWord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/amawal-words/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AmawalWord> partialUpdateAmawalWord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AmawalWord amawalWord
    ) throws URISyntaxException {
        log.debug("REST request to partial update AmawalWord partially : {}, {}", id, amawalWord);
        if (amawalWord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amawalWord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amawalWordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AmawalWord> result = amawalWordService.partialUpdate(amawalWord);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amawalWord.getId().toString())
        );
    }

    /**
     * {@code GET  /amawal-words} : get all the amawalWords.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amawalWords in body.
     */
    @GetMapping("/amawal-words")
    public ResponseEntity<List<AmawalWord>> getAllAmawalWords(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of AmawalWords");
        Page<AmawalWord> page;
        if (eagerload) {
            page = amawalWordService.findAllWithEagerRelationships(pageable);
        } else {
            page = amawalWordService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /amawal-words/:id} : get the "id" amawalWord.
     *
     * @param id the id of the amawalWord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amawalWord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amawal-words/{id}")
    public ResponseEntity<AmawalWord> getAmawalWord(@PathVariable Long id) {
        log.debug("REST request to get AmawalWord : {}", id);
        Optional<AmawalWord> amawalWord = amawalWordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(amawalWord);
    }

    /**
     * {@code DELETE  /amawal-words/:id} : delete the "id" amawalWord.
     *
     * @param id the id of the amawalWord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/amawal-words/{id}")
    public ResponseEntity<Void> deleteAmawalWord(@PathVariable Long id) {
        log.debug("REST request to delete AmawalWord : {}", id);
        amawalWordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
