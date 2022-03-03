package org.amawal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.amawal.domain.TamazightToLang;
import org.amawal.repository.TamazightToLangRepository;
import org.amawal.service.TamazightToLangService;
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
 * REST controller for managing {@link org.amawal.domain.TamazightToLang}.
 */
@RestController
@RequestMapping("/api")
public class TamazightToLangResource {

    private final Logger log = LoggerFactory.getLogger(TamazightToLangResource.class);

    private static final String ENTITY_NAME = "tamazightToLang";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TamazightToLangService tamazightToLangService;

    private final TamazightToLangRepository tamazightToLangRepository;

    public TamazightToLangResource(TamazightToLangService tamazightToLangService, TamazightToLangRepository tamazightToLangRepository) {
        this.tamazightToLangService = tamazightToLangService;
        this.tamazightToLangRepository = tamazightToLangRepository;
    }

    /**
     * {@code POST  /tamazight-to-langs} : Create a new tamazightToLang.
     *
     * @param tamazightToLang the tamazightToLang to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tamazightToLang, or with status {@code 400 (Bad Request)} if the tamazightToLang has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tamazight-to-langs")
    public ResponseEntity<TamazightToLang> createTamazightToLang(@RequestBody TamazightToLang tamazightToLang) throws URISyntaxException {
        log.debug("REST request to save TamazightToLang : {}", tamazightToLang);
        if (tamazightToLang.getId() != null) {
            throw new BadRequestAlertException("A new tamazightToLang cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TamazightToLang result = tamazightToLangService.save(tamazightToLang);
        return ResponseEntity
            .created(new URI("/api/tamazight-to-langs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tamazight-to-langs/:id} : Updates an existing tamazightToLang.
     *
     * @param id the id of the tamazightToLang to save.
     * @param tamazightToLang the tamazightToLang to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tamazightToLang,
     * or with status {@code 400 (Bad Request)} if the tamazightToLang is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tamazightToLang couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tamazight-to-langs/{id}")
    public ResponseEntity<TamazightToLang> updateTamazightToLang(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TamazightToLang tamazightToLang
    ) throws URISyntaxException {
        log.debug("REST request to update TamazightToLang : {}, {}", id, tamazightToLang);
        if (tamazightToLang.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tamazightToLang.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tamazightToLangRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TamazightToLang result = tamazightToLangService.save(tamazightToLang);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tamazightToLang.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tamazight-to-langs/:id} : Partial updates given fields of an existing tamazightToLang, field will ignore if it is null
     *
     * @param id the id of the tamazightToLang to save.
     * @param tamazightToLang the tamazightToLang to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tamazightToLang,
     * or with status {@code 400 (Bad Request)} if the tamazightToLang is not valid,
     * or with status {@code 404 (Not Found)} if the tamazightToLang is not found,
     * or with status {@code 500 (Internal Server Error)} if the tamazightToLang couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tamazight-to-langs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TamazightToLang> partialUpdateTamazightToLang(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TamazightToLang tamazightToLang
    ) throws URISyntaxException {
        log.debug("REST request to partial update TamazightToLang partially : {}, {}", id, tamazightToLang);
        if (tamazightToLang.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tamazightToLang.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tamazightToLangRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TamazightToLang> result = tamazightToLangService.partialUpdate(tamazightToLang);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tamazightToLang.getId().toString())
        );
    }

    /**
     * {@code GET  /tamazight-to-langs} : get all the tamazightToLangs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tamazightToLangs in body.
     */
    @GetMapping("/tamazight-to-langs")
    public ResponseEntity<List<TamazightToLang>> getAllTamazightToLangs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TamazightToLangs");
        Page<TamazightToLang> page = tamazightToLangService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tamazight-to-langs/:id} : get the "id" tamazightToLang.
     *
     * @param id the id of the tamazightToLang to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tamazightToLang, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tamazight-to-langs/{id}")
    public ResponseEntity<TamazightToLang> getTamazightToLang(@PathVariable Long id) {
        log.debug("REST request to get TamazightToLang : {}", id);
        Optional<TamazightToLang> tamazightToLang = tamazightToLangService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tamazightToLang);
    }

    /**
     * {@code DELETE  /tamazight-to-langs/:id} : delete the "id" tamazightToLang.
     *
     * @param id the id of the tamazightToLang to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tamazight-to-langs/{id}")
    public ResponseEntity<Void> deleteTamazightToLang(@PathVariable Long id) {
        log.debug("REST request to delete TamazightToLang : {}", id);
        tamazightToLangService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
