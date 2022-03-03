package org.amawal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.amawal.IntegrationTest;
import org.amawal.domain.Dialecte;
import org.amawal.repository.DialecteRepository;
import org.amawal.service.DialecteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DialecteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DialecteResourceIT {

    private static final Long DEFAULT_DIALECTE_ID = 1L;
    private static final Long UPDATED_DIALECTE_ID = 2L;

    private static final String DEFAULT_DIALECTE = "AAAAAAAAAA";
    private static final String UPDATED_DIALECTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dialectes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DialecteRepository dialecteRepository;

    @Mock
    private DialecteRepository dialecteRepositoryMock;

    @Mock
    private DialecteService dialecteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDialecteMockMvc;

    private Dialecte dialecte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dialecte createEntity(EntityManager em) {
        Dialecte dialecte = new Dialecte().dialecteId(DEFAULT_DIALECTE_ID).dialecte(DEFAULT_DIALECTE);
        return dialecte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dialecte createUpdatedEntity(EntityManager em) {
        Dialecte dialecte = new Dialecte().dialecteId(UPDATED_DIALECTE_ID).dialecte(UPDATED_DIALECTE);
        return dialecte;
    }

    @BeforeEach
    public void initTest() {
        dialecte = createEntity(em);
    }

    @Test
    @Transactional
    void createDialecte() throws Exception {
        int databaseSizeBeforeCreate = dialecteRepository.findAll().size();
        // Create the Dialecte
        restDialecteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dialecte)))
            .andExpect(status().isCreated());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeCreate + 1);
        Dialecte testDialecte = dialecteList.get(dialecteList.size() - 1);
        assertThat(testDialecte.getDialecteId()).isEqualTo(DEFAULT_DIALECTE_ID);
        assertThat(testDialecte.getDialecte()).isEqualTo(DEFAULT_DIALECTE);
    }

    @Test
    @Transactional
    void createDialecteWithExistingId() throws Exception {
        // Create the Dialecte with an existing ID
        dialecte.setId(1L);

        int databaseSizeBeforeCreate = dialecteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDialecteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dialecte)))
            .andExpect(status().isBadRequest());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDialectes() throws Exception {
        // Initialize the database
        dialecteRepository.saveAndFlush(dialecte);

        // Get all the dialecteList
        restDialecteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dialecte.getId().intValue())))
            .andExpect(jsonPath("$.[*].dialecteId").value(hasItem(DEFAULT_DIALECTE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dialecte").value(hasItem(DEFAULT_DIALECTE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDialectesWithEagerRelationshipsIsEnabled() throws Exception {
        when(dialecteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDialecteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dialecteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDialectesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dialecteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDialecteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dialecteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDialecte() throws Exception {
        // Initialize the database
        dialecteRepository.saveAndFlush(dialecte);

        // Get the dialecte
        restDialecteMockMvc
            .perform(get(ENTITY_API_URL_ID, dialecte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dialecte.getId().intValue()))
            .andExpect(jsonPath("$.dialecteId").value(DEFAULT_DIALECTE_ID.intValue()))
            .andExpect(jsonPath("$.dialecte").value(DEFAULT_DIALECTE));
    }

    @Test
    @Transactional
    void getNonExistingDialecte() throws Exception {
        // Get the dialecte
        restDialecteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDialecte() throws Exception {
        // Initialize the database
        dialecteRepository.saveAndFlush(dialecte);

        int databaseSizeBeforeUpdate = dialecteRepository.findAll().size();

        // Update the dialecte
        Dialecte updatedDialecte = dialecteRepository.findById(dialecte.getId()).get();
        // Disconnect from session so that the updates on updatedDialecte are not directly saved in db
        em.detach(updatedDialecte);
        updatedDialecte.dialecteId(UPDATED_DIALECTE_ID).dialecte(UPDATED_DIALECTE);

        restDialecteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDialecte.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDialecte))
            )
            .andExpect(status().isOk());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeUpdate);
        Dialecte testDialecte = dialecteList.get(dialecteList.size() - 1);
        assertThat(testDialecte.getDialecteId()).isEqualTo(UPDATED_DIALECTE_ID);
        assertThat(testDialecte.getDialecte()).isEqualTo(UPDATED_DIALECTE);
    }

    @Test
    @Transactional
    void putNonExistingDialecte() throws Exception {
        int databaseSizeBeforeUpdate = dialecteRepository.findAll().size();
        dialecte.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDialecteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dialecte.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dialecte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDialecte() throws Exception {
        int databaseSizeBeforeUpdate = dialecteRepository.findAll().size();
        dialecte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDialecteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dialecte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDialecte() throws Exception {
        int databaseSizeBeforeUpdate = dialecteRepository.findAll().size();
        dialecte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDialecteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dialecte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDialecteWithPatch() throws Exception {
        // Initialize the database
        dialecteRepository.saveAndFlush(dialecte);

        int databaseSizeBeforeUpdate = dialecteRepository.findAll().size();

        // Update the dialecte using partial update
        Dialecte partialUpdatedDialecte = new Dialecte();
        partialUpdatedDialecte.setId(dialecte.getId());

        partialUpdatedDialecte.dialecteId(UPDATED_DIALECTE_ID);

        restDialecteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDialecte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDialecte))
            )
            .andExpect(status().isOk());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeUpdate);
        Dialecte testDialecte = dialecteList.get(dialecteList.size() - 1);
        assertThat(testDialecte.getDialecteId()).isEqualTo(UPDATED_DIALECTE_ID);
        assertThat(testDialecte.getDialecte()).isEqualTo(DEFAULT_DIALECTE);
    }

    @Test
    @Transactional
    void fullUpdateDialecteWithPatch() throws Exception {
        // Initialize the database
        dialecteRepository.saveAndFlush(dialecte);

        int databaseSizeBeforeUpdate = dialecteRepository.findAll().size();

        // Update the dialecte using partial update
        Dialecte partialUpdatedDialecte = new Dialecte();
        partialUpdatedDialecte.setId(dialecte.getId());

        partialUpdatedDialecte.dialecteId(UPDATED_DIALECTE_ID).dialecte(UPDATED_DIALECTE);

        restDialecteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDialecte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDialecte))
            )
            .andExpect(status().isOk());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeUpdate);
        Dialecte testDialecte = dialecteList.get(dialecteList.size() - 1);
        assertThat(testDialecte.getDialecteId()).isEqualTo(UPDATED_DIALECTE_ID);
        assertThat(testDialecte.getDialecte()).isEqualTo(UPDATED_DIALECTE);
    }

    @Test
    @Transactional
    void patchNonExistingDialecte() throws Exception {
        int databaseSizeBeforeUpdate = dialecteRepository.findAll().size();
        dialecte.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDialecteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dialecte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dialecte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDialecte() throws Exception {
        int databaseSizeBeforeUpdate = dialecteRepository.findAll().size();
        dialecte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDialecteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dialecte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDialecte() throws Exception {
        int databaseSizeBeforeUpdate = dialecteRepository.findAll().size();
        dialecte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDialecteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dialecte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dialecte in the database
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDialecte() throws Exception {
        // Initialize the database
        dialecteRepository.saveAndFlush(dialecte);

        int databaseSizeBeforeDelete = dialecteRepository.findAll().size();

        // Delete the dialecte
        restDialecteMockMvc
            .perform(delete(ENTITY_API_URL_ID, dialecte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dialecte> dialecteList = dialecteRepository.findAll();
        assertThat(dialecteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
