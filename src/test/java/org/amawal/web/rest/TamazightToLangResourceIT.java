package org.amawal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.amawal.IntegrationTest;
import org.amawal.domain.TamazightToLang;
import org.amawal.repository.TamazightToLangRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TamazightToLangResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TamazightToLangResourceIT {

    private static final Long DEFAULT_TRADUCTION_ID = 1L;
    private static final Long UPDATED_TRADUCTION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/tamazight-to-langs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TamazightToLangRepository tamazightToLangRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTamazightToLangMockMvc;

    private TamazightToLang tamazightToLang;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TamazightToLang createEntity(EntityManager em) {
        TamazightToLang tamazightToLang = new TamazightToLang().traductionId(DEFAULT_TRADUCTION_ID);
        return tamazightToLang;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TamazightToLang createUpdatedEntity(EntityManager em) {
        TamazightToLang tamazightToLang = new TamazightToLang().traductionId(UPDATED_TRADUCTION_ID);
        return tamazightToLang;
    }

    @BeforeEach
    public void initTest() {
        tamazightToLang = createEntity(em);
    }

    @Test
    @Transactional
    void createTamazightToLang() throws Exception {
        int databaseSizeBeforeCreate = tamazightToLangRepository.findAll().size();
        // Create the TamazightToLang
        restTamazightToLangMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tamazightToLang))
            )
            .andExpect(status().isCreated());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeCreate + 1);
        TamazightToLang testTamazightToLang = tamazightToLangList.get(tamazightToLangList.size() - 1);
        assertThat(testTamazightToLang.getTraductionId()).isEqualTo(DEFAULT_TRADUCTION_ID);
    }

    @Test
    @Transactional
    void createTamazightToLangWithExistingId() throws Exception {
        // Create the TamazightToLang with an existing ID
        tamazightToLang.setId(1L);

        int databaseSizeBeforeCreate = tamazightToLangRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTamazightToLangMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tamazightToLang))
            )
            .andExpect(status().isBadRequest());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTamazightToLangs() throws Exception {
        // Initialize the database
        tamazightToLangRepository.saveAndFlush(tamazightToLang);

        // Get all the tamazightToLangList
        restTamazightToLangMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tamazightToLang.getId().intValue())))
            .andExpect(jsonPath("$.[*].traductionId").value(hasItem(DEFAULT_TRADUCTION_ID.intValue())));
    }

    @Test
    @Transactional
    void getTamazightToLang() throws Exception {
        // Initialize the database
        tamazightToLangRepository.saveAndFlush(tamazightToLang);

        // Get the tamazightToLang
        restTamazightToLangMockMvc
            .perform(get(ENTITY_API_URL_ID, tamazightToLang.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tamazightToLang.getId().intValue()))
            .andExpect(jsonPath("$.traductionId").value(DEFAULT_TRADUCTION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTamazightToLang() throws Exception {
        // Get the tamazightToLang
        restTamazightToLangMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTamazightToLang() throws Exception {
        // Initialize the database
        tamazightToLangRepository.saveAndFlush(tamazightToLang);

        int databaseSizeBeforeUpdate = tamazightToLangRepository.findAll().size();

        // Update the tamazightToLang
        TamazightToLang updatedTamazightToLang = tamazightToLangRepository.findById(tamazightToLang.getId()).get();
        // Disconnect from session so that the updates on updatedTamazightToLang are not directly saved in db
        em.detach(updatedTamazightToLang);
        updatedTamazightToLang.traductionId(UPDATED_TRADUCTION_ID);

        restTamazightToLangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTamazightToLang.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTamazightToLang))
            )
            .andExpect(status().isOk());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeUpdate);
        TamazightToLang testTamazightToLang = tamazightToLangList.get(tamazightToLangList.size() - 1);
        assertThat(testTamazightToLang.getTraductionId()).isEqualTo(UPDATED_TRADUCTION_ID);
    }

    @Test
    @Transactional
    void putNonExistingTamazightToLang() throws Exception {
        int databaseSizeBeforeUpdate = tamazightToLangRepository.findAll().size();
        tamazightToLang.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTamazightToLangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tamazightToLang.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tamazightToLang))
            )
            .andExpect(status().isBadRequest());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTamazightToLang() throws Exception {
        int databaseSizeBeforeUpdate = tamazightToLangRepository.findAll().size();
        tamazightToLang.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTamazightToLangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tamazightToLang))
            )
            .andExpect(status().isBadRequest());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTamazightToLang() throws Exception {
        int databaseSizeBeforeUpdate = tamazightToLangRepository.findAll().size();
        tamazightToLang.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTamazightToLangMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tamazightToLang))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTamazightToLangWithPatch() throws Exception {
        // Initialize the database
        tamazightToLangRepository.saveAndFlush(tamazightToLang);

        int databaseSizeBeforeUpdate = tamazightToLangRepository.findAll().size();

        // Update the tamazightToLang using partial update
        TamazightToLang partialUpdatedTamazightToLang = new TamazightToLang();
        partialUpdatedTamazightToLang.setId(tamazightToLang.getId());

        restTamazightToLangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTamazightToLang.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTamazightToLang))
            )
            .andExpect(status().isOk());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeUpdate);
        TamazightToLang testTamazightToLang = tamazightToLangList.get(tamazightToLangList.size() - 1);
        assertThat(testTamazightToLang.getTraductionId()).isEqualTo(DEFAULT_TRADUCTION_ID);
    }

    @Test
    @Transactional
    void fullUpdateTamazightToLangWithPatch() throws Exception {
        // Initialize the database
        tamazightToLangRepository.saveAndFlush(tamazightToLang);

        int databaseSizeBeforeUpdate = tamazightToLangRepository.findAll().size();

        // Update the tamazightToLang using partial update
        TamazightToLang partialUpdatedTamazightToLang = new TamazightToLang();
        partialUpdatedTamazightToLang.setId(tamazightToLang.getId());

        partialUpdatedTamazightToLang.traductionId(UPDATED_TRADUCTION_ID);

        restTamazightToLangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTamazightToLang.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTamazightToLang))
            )
            .andExpect(status().isOk());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeUpdate);
        TamazightToLang testTamazightToLang = tamazightToLangList.get(tamazightToLangList.size() - 1);
        assertThat(testTamazightToLang.getTraductionId()).isEqualTo(UPDATED_TRADUCTION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTamazightToLang() throws Exception {
        int databaseSizeBeforeUpdate = tamazightToLangRepository.findAll().size();
        tamazightToLang.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTamazightToLangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tamazightToLang.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tamazightToLang))
            )
            .andExpect(status().isBadRequest());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTamazightToLang() throws Exception {
        int databaseSizeBeforeUpdate = tamazightToLangRepository.findAll().size();
        tamazightToLang.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTamazightToLangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tamazightToLang))
            )
            .andExpect(status().isBadRequest());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTamazightToLang() throws Exception {
        int databaseSizeBeforeUpdate = tamazightToLangRepository.findAll().size();
        tamazightToLang.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTamazightToLangMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tamazightToLang))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TamazightToLang in the database
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTamazightToLang() throws Exception {
        // Initialize the database
        tamazightToLangRepository.saveAndFlush(tamazightToLang);

        int databaseSizeBeforeDelete = tamazightToLangRepository.findAll().size();

        // Delete the tamazightToLang
        restTamazightToLangMockMvc
            .perform(delete(ENTITY_API_URL_ID, tamazightToLang.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TamazightToLang> tamazightToLangList = tamazightToLangRepository.findAll();
        assertThat(tamazightToLangList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
