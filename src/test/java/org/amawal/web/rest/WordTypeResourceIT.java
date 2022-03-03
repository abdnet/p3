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
import org.amawal.domain.WordType;
import org.amawal.repository.WordTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WordTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WordTypeResourceIT {

    private static final Long DEFAULT_TYPE_ID = 1L;
    private static final Long UPDATED_TYPE_ID = 2L;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/word-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WordTypeRepository wordTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWordTypeMockMvc;

    private WordType wordType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WordType createEntity(EntityManager em) {
        WordType wordType = new WordType().typeId(DEFAULT_TYPE_ID).type(DEFAULT_TYPE);
        return wordType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WordType createUpdatedEntity(EntityManager em) {
        WordType wordType = new WordType().typeId(UPDATED_TYPE_ID).type(UPDATED_TYPE);
        return wordType;
    }

    @BeforeEach
    public void initTest() {
        wordType = createEntity(em);
    }

    @Test
    @Transactional
    void createWordType() throws Exception {
        int databaseSizeBeforeCreate = wordTypeRepository.findAll().size();
        // Create the WordType
        restWordTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wordType)))
            .andExpect(status().isCreated());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeCreate + 1);
        WordType testWordType = wordTypeList.get(wordTypeList.size() - 1);
        assertThat(testWordType.getTypeId()).isEqualTo(DEFAULT_TYPE_ID);
        assertThat(testWordType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createWordTypeWithExistingId() throws Exception {
        // Create the WordType with an existing ID
        wordType.setId(1L);

        int databaseSizeBeforeCreate = wordTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wordType)))
            .andExpect(status().isBadRequest());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWordTypes() throws Exception {
        // Initialize the database
        wordTypeRepository.saveAndFlush(wordType);

        // Get all the wordTypeList
        restWordTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wordType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeId").value(hasItem(DEFAULT_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getWordType() throws Exception {
        // Initialize the database
        wordTypeRepository.saveAndFlush(wordType);

        // Get the wordType
        restWordTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, wordType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wordType.getId().intValue()))
            .andExpect(jsonPath("$.typeId").value(DEFAULT_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingWordType() throws Exception {
        // Get the wordType
        restWordTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWordType() throws Exception {
        // Initialize the database
        wordTypeRepository.saveAndFlush(wordType);

        int databaseSizeBeforeUpdate = wordTypeRepository.findAll().size();

        // Update the wordType
        WordType updatedWordType = wordTypeRepository.findById(wordType.getId()).get();
        // Disconnect from session so that the updates on updatedWordType are not directly saved in db
        em.detach(updatedWordType);
        updatedWordType.typeId(UPDATED_TYPE_ID).type(UPDATED_TYPE);

        restWordTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWordType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWordType))
            )
            .andExpect(status().isOk());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeUpdate);
        WordType testWordType = wordTypeList.get(wordTypeList.size() - 1);
        assertThat(testWordType.getTypeId()).isEqualTo(UPDATED_TYPE_ID);
        assertThat(testWordType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingWordType() throws Exception {
        int databaseSizeBeforeUpdate = wordTypeRepository.findAll().size();
        wordType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWordTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wordType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wordType))
            )
            .andExpect(status().isBadRequest());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWordType() throws Exception {
        int databaseSizeBeforeUpdate = wordTypeRepository.findAll().size();
        wordType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wordType))
            )
            .andExpect(status().isBadRequest());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWordType() throws Exception {
        int databaseSizeBeforeUpdate = wordTypeRepository.findAll().size();
        wordType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wordType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWordTypeWithPatch() throws Exception {
        // Initialize the database
        wordTypeRepository.saveAndFlush(wordType);

        int databaseSizeBeforeUpdate = wordTypeRepository.findAll().size();

        // Update the wordType using partial update
        WordType partialUpdatedWordType = new WordType();
        partialUpdatedWordType.setId(wordType.getId());

        partialUpdatedWordType.type(UPDATED_TYPE);

        restWordTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWordType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWordType))
            )
            .andExpect(status().isOk());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeUpdate);
        WordType testWordType = wordTypeList.get(wordTypeList.size() - 1);
        assertThat(testWordType.getTypeId()).isEqualTo(DEFAULT_TYPE_ID);
        assertThat(testWordType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWordTypeWithPatch() throws Exception {
        // Initialize the database
        wordTypeRepository.saveAndFlush(wordType);

        int databaseSizeBeforeUpdate = wordTypeRepository.findAll().size();

        // Update the wordType using partial update
        WordType partialUpdatedWordType = new WordType();
        partialUpdatedWordType.setId(wordType.getId());

        partialUpdatedWordType.typeId(UPDATED_TYPE_ID).type(UPDATED_TYPE);

        restWordTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWordType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWordType))
            )
            .andExpect(status().isOk());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeUpdate);
        WordType testWordType = wordTypeList.get(wordTypeList.size() - 1);
        assertThat(testWordType.getTypeId()).isEqualTo(UPDATED_TYPE_ID);
        assertThat(testWordType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWordType() throws Exception {
        int databaseSizeBeforeUpdate = wordTypeRepository.findAll().size();
        wordType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWordTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wordType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wordType))
            )
            .andExpect(status().isBadRequest());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWordType() throws Exception {
        int databaseSizeBeforeUpdate = wordTypeRepository.findAll().size();
        wordType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wordType))
            )
            .andExpect(status().isBadRequest());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWordType() throws Exception {
        int databaseSizeBeforeUpdate = wordTypeRepository.findAll().size();
        wordType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wordType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WordType in the database
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWordType() throws Exception {
        // Initialize the database
        wordTypeRepository.saveAndFlush(wordType);

        int databaseSizeBeforeDelete = wordTypeRepository.findAll().size();

        // Delete the wordType
        restWordTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, wordType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WordType> wordTypeList = wordTypeRepository.findAll();
        assertThat(wordTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
