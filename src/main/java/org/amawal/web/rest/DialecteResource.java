package org.amawal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.amawal.domain.Dialecte;
import org.amawal.repository.DialecteRepository;
import org.amawal.service.DialecteService;
import org.amawal.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.amawal.domain.Dialecte}.
 */
@RestController
@RequestMapping("/api")
public class DialecteResource {

    private final Logger log = LoggerFactory.getLogger(DialecteResource.class);

    private static final String ENTITY_NAME = "dialecte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DialecteService dialecteService;

    private final DialecteRepository dialecteRepository;

    public DialecteResource(DialecteService dialecteService, DialecteRepository dialecteRepository) {
        this.dialecteService = dialecteService;
        this.dialecteRepository = dialecteRepository;
    }

    /**
     * {@code POST  /dialectes} : Create a new dialecte.
     *
     * @param dialecte the dialecte to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dialecte, or with status {@code 400 (Bad Request)} if the dialecte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dialectes")
    public ResponseEntity<Dialecte> createDialecte(@RequestBody Dialecte dialecte) throws URISyntaxException {
        log.debug("REST request to save Dialecte : {}", dialecte);
        if (dialecte.getId() != null) {
            throw new BadRequestAlertException("A new dialecte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dialecte result = dialecteService.save(dialecte);
        return ResponseEntity
            .created(new URI("/api/dialectes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dialectes/:id} : Updates an existing dialecte.
     *
     * @param id the id of the dialecte to save.
     * @param dialecte the dialecte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dialecte,
     * or with status {@code 400 (Bad Request)} if the dialecte is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dialecte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dialectes/{id}")
    public ResponseEntity<Dialecte> updateDialecte(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dialecte dialecte
    ) throws URISyntaxException {
        log.debug("REST request to update Dialecte : {}, {}", id, dialecte);
        if (dialecte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dialecte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dialecteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dialecte result = dialecteService.save(dialecte);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dialecte.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dialectes/:id} : Partial updates given fields of an existing dialecte, field will ignore if it is null
     *
     * @param id the id of the dialecte to save.
     * @param dialecte the dialecte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dialecte,
     * or with status {@code 400 (Bad Request)} if the dialecte is not valid,
     * or with status {@code 404 (Not Found)} if the dialecte is not found,
     * or with status {@code 500 (Internal Server Error)} if the dialecte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dialectes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dialecte> partialUpdateDialecte(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dialecte dialecte
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dialecte partially : {}, {}", id, dialecte);
        if (dialecte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dialecte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dialecteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dialecte> result = dialecteService.partialUpdate(dialecte);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dialecte.getId().toString())
        );
    }

    /**
     * {@code GET  /dialectes} : get all the dialectes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dialectes in body.
     */
    @GetMapping("/dialectes")
    public List<Dialecte> getAllDialectes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Dialectes");
        return dialecteService.findAll();
    }

    /**
     * {@code GET  /dialectes/:id} : get the "id" dialecte.
     *
     * @param id the id of the dialecte to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dialecte, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dialectes/{id}")
    public ResponseEntity<Dialecte> getDialecte(@PathVariable Long id) {
        log.debug("REST request to get Dialecte : {}", id);
        Optional<Dialecte> dialecte = dialecteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dialecte);
    }

    /**
     * {@code DELETE  /dialectes/:id} : delete the "id" dialecte.
     *
     * @param id the id of the dialecte to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dialectes/{id}")
    public ResponseEntity<Void> deleteDialecte(@PathVariable Long id) {
        log.debug("REST request to delete Dialecte : {}", id);
        dialecteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
