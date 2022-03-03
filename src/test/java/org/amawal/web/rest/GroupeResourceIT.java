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
import org.amawal.domain.Groupe;
import org.amawal.repository.GroupeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GroupeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GroupeResourceIT {

    private static final Long DEFAULT_GROUPE_ID = 1L;
    private static final Long UPDATED_GROUPE_ID = 2L;

    private static final String DEFAULT_GROUPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GROUPE_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HABILITATION_LEVEL = 1;
    private static final Integer UPDATED_HABILITATION_LEVEL = 2;

    private static final String ENTITY_API_URL = "/api/groupes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupeMockMvc;

    private Groupe groupe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Groupe createEntity(EntityManager em) {
        Groupe groupe = new Groupe()
            .groupeId(DEFAULT_GROUPE_ID)
            .groupeCode(DEFAULT_GROUPE_CODE)
            .habilitationLevel(DEFAULT_HABILITATION_LEVEL);
        return groupe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Groupe createUpdatedEntity(EntityManager em) {
        Groupe groupe = new Groupe()
            .groupeId(UPDATED_GROUPE_ID)
            .groupeCode(UPDATED_GROUPE_CODE)
            .habilitationLevel(UPDATED_HABILITATION_LEVEL);
        return groupe;
    }

    @BeforeEach
    public void initTest() {
        groupe = createEntity(em);
    }

    @Test
    @Transactional
    void createGroupe() throws Exception {
        int databaseSizeBeforeCreate = groupeRepository.findAll().size();
        // Create the Groupe
        restGroupeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupe)))
            .andExpect(status().isCreated());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeCreate + 1);
        Groupe testGroupe = groupeList.get(groupeList.size() - 1);
        assertThat(testGroupe.getGroupeId()).isEqualTo(DEFAULT_GROUPE_ID);
        assertThat(testGroupe.getGroupeCode()).isEqualTo(DEFAULT_GROUPE_CODE);
        assertThat(testGroupe.getHabilitationLevel()).isEqualTo(DEFAULT_HABILITATION_LEVEL);
    }

    @Test
    @Transactional
    void createGroupeWithExistingId() throws Exception {
        // Create the Groupe with an existing ID
        groupe.setId(1L);

        int databaseSizeBeforeCreate = groupeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupe)))
            .andExpect(status().isBadRequest());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGroupes() throws Exception {
        // Initialize the database
        groupeRepository.saveAndFlush(groupe);

        // Get all the groupeList
        restGroupeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupe.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupeId").value(hasItem(DEFAULT_GROUPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].groupeCode").value(hasItem(DEFAULT_GROUPE_CODE)))
            .andExpect(jsonPath("$.[*].habilitationLevel").value(hasItem(DEFAULT_HABILITATION_LEVEL)));
    }

    @Test
    @Transactional
    void getGroupe() throws Exception {
        // Initialize the database
        groupeRepository.saveAndFlush(groupe);

        // Get the groupe
        restGroupeMockMvc
            .perform(get(ENTITY_API_URL_ID, groupe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(groupe.getId().intValue()))
            .andExpect(jsonPath("$.groupeId").value(DEFAULT_GROUPE_ID.intValue()))
            .andExpect(jsonPath("$.groupeCode").value(DEFAULT_GROUPE_CODE))
            .andExpect(jsonPath("$.habilitationLevel").value(DEFAULT_HABILITATION_LEVEL));
    }

    @Test
    @Transactional
    void getNonExistingGroupe() throws Exception {
        // Get the groupe
        restGroupeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGroupe() throws Exception {
        // Initialize the database
        groupeRepository.saveAndFlush(groupe);

        int databaseSizeBeforeUpdate = groupeRepository.findAll().size();

        // Update the groupe
        Groupe updatedGroupe = groupeRepository.findById(groupe.getId()).get();
        // Disconnect from session so that the updates on updatedGroupe are not directly saved in db
        em.detach(updatedGroupe);
        updatedGroupe.groupeId(UPDATED_GROUPE_ID).groupeCode(UPDATED_GROUPE_CODE).habilitationLevel(UPDATED_HABILITATION_LEVEL);

        restGroupeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGroupe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGroupe))
            )
            .andExpect(status().isOk());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeUpdate);
        Groupe testGroupe = groupeList.get(groupeList.size() - 1);
        assertThat(testGroupe.getGroupeId()).isEqualTo(UPDATED_GROUPE_ID);
        assertThat(testGroupe.getGroupeCode()).isEqualTo(UPDATED_GROUPE_CODE);
        assertThat(testGroupe.getHabilitationLevel()).isEqualTo(UPDATED_HABILITATION_LEVEL);
    }

    @Test
    @Transactional
    void putNonExistingGroupe() throws Exception {
        int databaseSizeBeforeUpdate = groupeRepository.findAll().size();
        groupe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, groupe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGroupe() throws Exception {
        int databaseSizeBeforeUpdate = groupeRepository.findAll().size();
        groupe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGroupe() throws Exception {
        int databaseSizeBeforeUpdate = groupeRepository.findAll().size();
        groupe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groupe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGroupeWithPatch() throws Exception {
        // Initialize the database
        groupeRepository.saveAndFlush(groupe);

        int databaseSizeBeforeUpdate = groupeRepository.findAll().size();

        // Update the groupe using partial update
        Groupe partialUpdatedGroupe = new Groupe();
        partialUpdatedGroupe.setId(groupe.getId());

        partialUpdatedGroupe.groupeCode(UPDATED_GROUPE_CODE).habilitationLevel(UPDATED_HABILITATION_LEVEL);

        restGroupeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroupe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupe))
            )
            .andExpect(status().isOk());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeUpdate);
        Groupe testGroupe = groupeList.get(groupeList.size() - 1);
        assertThat(testGroupe.getGroupeId()).isEqualTo(DEFAULT_GROUPE_ID);
        assertThat(testGroupe.getGroupeCode()).isEqualTo(UPDATED_GROUPE_CODE);
        assertThat(testGroupe.getHabilitationLevel()).isEqualTo(UPDATED_HABILITATION_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdateGroupeWithPatch() throws Exception {
        // Initialize the database
        groupeRepository.saveAndFlush(groupe);

        int databaseSizeBeforeUpdate = groupeRepository.findAll().size();

        // Update the groupe using partial update
        Groupe partialUpdatedGroupe = new Groupe();
        partialUpdatedGroupe.setId(groupe.getId());

        partialUpdatedGroupe.groupeId(UPDATED_GROUPE_ID).groupeCode(UPDATED_GROUPE_CODE).habilitationLevel(UPDATED_HABILITATION_LEVEL);

        restGroupeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroupe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupe))
            )
            .andExpect(status().isOk());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeUpdate);
        Groupe testGroupe = groupeList.get(groupeList.size() - 1);
        assertThat(testGroupe.getGroupeId()).isEqualTo(UPDATED_GROUPE_ID);
        assertThat(testGroupe.getGroupeCode()).isEqualTo(UPDATED_GROUPE_CODE);
        assertThat(testGroupe.getHabilitationLevel()).isEqualTo(UPDATED_HABILITATION_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingGroupe() throws Exception {
        int databaseSizeBeforeUpdate = groupeRepository.findAll().size();
        groupe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, groupe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(groupe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGroupe() throws Exception {
        int databaseSizeBeforeUpdate = groupeRepository.findAll().size();
        groupe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(groupe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGroupe() throws Exception {
        int databaseSizeBeforeUpdate = groupeRepository.findAll().size();
        groupe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(groupe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Groupe in the database
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGroupe() throws Exception {
        // Initialize the database
        groupeRepository.saveAndFlush(groupe);

        int databaseSizeBeforeDelete = groupeRepository.findAll().size();

        // Delete the groupe
        restGroupeMockMvc
            .perform(delete(ENTITY_API_URL_ID, groupe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Groupe> groupeList = groupeRepository.findAll();
        assertThat(groupeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
