package org.amawal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.amawal.IntegrationTest;
import org.amawal.domain.WordInfo;
import org.amawal.repository.WordInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WordInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WordInfoResourceIT {

    private static final Long DEFAULT_WORD_INFO_ID = 1L;
    private static final Long UPDATED_WORD_INFO_ID = 2L;

    private static final Instant DEFAULT_CONTRIBUTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONTRIBUTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALIDATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALIDATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ETAT = 1;
    private static final Integer UPDATED_ETAT = 2;

    private static final String ENTITY_API_URL = "/api/word-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WordInfoRepository wordInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWordInfoMockMvc;

    private WordInfo wordInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WordInfo createEntity(EntityManager em) {
        WordInfo wordInfo = new WordInfo()
            .wordInfoId(DEFAULT_WORD_INFO_ID)
            .contributionDate(DEFAULT_CONTRIBUTION_DATE)
            .validationDate(DEFAULT_VALIDATION_DATE)
            .etat(DEFAULT_ETAT);
        return wordInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WordInfo createUpdatedEntity(EntityManager em) {
        WordInfo wordInfo = new WordInfo()
            .wordInfoId(UPDATED_WORD_INFO_ID)
            .contributionDate(UPDATED_CONTRIBUTION_DATE)
            .validationDate(UPDATED_VALIDATION_DATE)
            .etat(UPDATED_ETAT);
        return wordInfo;
    }

    @BeforeEach
    public void initTest() {
        wordInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createWordInfo() throws Exception {
        int databaseSizeBeforeCreate = wordInfoRepository.findAll().size();
        // Create the WordInfo
        restWordInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wordInfo)))
            .andExpect(status().isCreated());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeCreate + 1);
        WordInfo testWordInfo = wordInfoList.get(wordInfoList.size() - 1);
        assertThat(testWordInfo.getWordInfoId()).isEqualTo(DEFAULT_WORD_INFO_ID);
        assertThat(testWordInfo.getContributionDate()).isEqualTo(DEFAULT_CONTRIBUTION_DATE);
        assertThat(testWordInfo.getValidationDate()).isEqualTo(DEFAULT_VALIDATION_DATE);
        assertThat(testWordInfo.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void createWordInfoWithExistingId() throws Exception {
        // Create the WordInfo with an existing ID
        wordInfo.setId(1L);

        int databaseSizeBeforeCreate = wordInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wordInfo)))
            .andExpect(status().isBadRequest());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWordInfos() throws Exception {
        // Initialize the database
        wordInfoRepository.saveAndFlush(wordInfo);

        // Get all the wordInfoList
        restWordInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wordInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].wordInfoId").value(hasItem(DEFAULT_WORD_INFO_ID.intValue())))
            .andExpect(jsonPath("$.[*].contributionDate").value(hasItem(DEFAULT_CONTRIBUTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].validationDate").value(hasItem(DEFAULT_VALIDATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)));
    }

    @Test
    @Transactional
    void getWordInfo() throws Exception {
        // Initialize the database
        wordInfoRepository.saveAndFlush(wordInfo);

        // Get the wordInfo
        restWordInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, wordInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wordInfo.getId().intValue()))
            .andExpect(jsonPath("$.wordInfoId").value(DEFAULT_WORD_INFO_ID.intValue()))
            .andExpect(jsonPath("$.contributionDate").value(DEFAULT_CONTRIBUTION_DATE.toString()))
            .andExpect(jsonPath("$.validationDate").value(DEFAULT_VALIDATION_DATE.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT));
    }

    @Test
    @Transactional
    void getNonExistingWordInfo() throws Exception {
        // Get the wordInfo
        restWordInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWordInfo() throws Exception {
        // Initialize the database
        wordInfoRepository.saveAndFlush(wordInfo);

        int databaseSizeBeforeUpdate = wordInfoRepository.findAll().size();

        // Update the wordInfo
        WordInfo updatedWordInfo = wordInfoRepository.findById(wordInfo.getId()).get();
        // Disconnect from session so that the updates on updatedWordInfo are not directly saved in db
        em.detach(updatedWordInfo);
        updatedWordInfo
            .wordInfoId(UPDATED_WORD_INFO_ID)
            .contributionDate(UPDATED_CONTRIBUTION_DATE)
            .validationDate(UPDATED_VALIDATION_DATE)
            .etat(UPDATED_ETAT);

        restWordInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWordInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWordInfo))
            )
            .andExpect(status().isOk());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeUpdate);
        WordInfo testWordInfo = wordInfoList.get(wordInfoList.size() - 1);
        assertThat(testWordInfo.getWordInfoId()).isEqualTo(UPDATED_WORD_INFO_ID);
        assertThat(testWordInfo.getContributionDate()).isEqualTo(UPDATED_CONTRIBUTION_DATE);
        assertThat(testWordInfo.getValidationDate()).isEqualTo(UPDATED_VALIDATION_DATE);
        assertThat(testWordInfo.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void putNonExistingWordInfo() throws Exception {
        int databaseSizeBeforeUpdate = wordInfoRepository.findAll().size();
        wordInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWordInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wordInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wordInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWordInfo() throws Exception {
        int databaseSizeBeforeUpdate = wordInfoRepository.findAll().size();
        wordInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wordInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWordInfo() throws Exception {
        int databaseSizeBeforeUpdate = wordInfoRepository.findAll().size();
        wordInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wordInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWordInfoWithPatch() throws Exception {
        // Initialize the database
        wordInfoRepository.saveAndFlush(wordInfo);

        int databaseSizeBeforeUpdate = wordInfoRepository.findAll().size();

        // Update the wordInfo using partial update
        WordInfo partialUpdatedWordInfo = new WordInfo();
        partialUpdatedWordInfo.setId(wordInfo.getId());

        partialUpdatedWordInfo.wordInfoId(UPDATED_WORD_INFO_ID).contributionDate(UPDATED_CONTRIBUTION_DATE);

        restWordInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWordInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWordInfo))
            )
            .andExpect(status().isOk());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeUpdate);
        WordInfo testWordInfo = wordInfoList.get(wordInfoList.size() - 1);
        assertThat(testWordInfo.getWordInfoId()).isEqualTo(UPDATED_WORD_INFO_ID);
        assertThat(testWordInfo.getContributionDate()).isEqualTo(UPDATED_CONTRIBUTION_DATE);
        assertThat(testWordInfo.getValidationDate()).isEqualTo(DEFAULT_VALIDATION_DATE);
        assertThat(testWordInfo.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void fullUpdateWordInfoWithPatch() throws Exception {
        // Initialize the database
        wordInfoRepository.saveAndFlush(wordInfo);

        int databaseSizeBeforeUpdate = wordInfoRepository.findAll().size();

        // Update the wordInfo using partial update
        WordInfo partialUpdatedWordInfo = new WordInfo();
        partialUpdatedWordInfo.setId(wordInfo.getId());

        partialUpdatedWordInfo
            .wordInfoId(UPDATED_WORD_INFO_ID)
            .contributionDate(UPDATED_CONTRIBUTION_DATE)
            .validationDate(UPDATED_VALIDATION_DATE)
            .etat(UPDATED_ETAT);

        restWordInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWordInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWordInfo))
            )
            .andExpect(status().isOk());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeUpdate);
        WordInfo testWordInfo = wordInfoList.get(wordInfoList.size() - 1);
        assertThat(testWordInfo.getWordInfoId()).isEqualTo(UPDATED_WORD_INFO_ID);
        assertThat(testWordInfo.getContributionDate()).isEqualTo(UPDATED_CONTRIBUTION_DATE);
        assertThat(testWordInfo.getValidationDate()).isEqualTo(UPDATED_VALIDATION_DATE);
        assertThat(testWordInfo.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void patchNonExistingWordInfo() throws Exception {
        int databaseSizeBeforeUpdate = wordInfoRepository.findAll().size();
        wordInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWordInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wordInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wordInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWordInfo() throws Exception {
        int databaseSizeBeforeUpdate = wordInfoRepository.findAll().size();
        wordInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wordInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWordInfo() throws Exception {
        int databaseSizeBeforeUpdate = wordInfoRepository.findAll().size();
        wordInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wordInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WordInfo in the database
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWordInfo() throws Exception {
        // Initialize the database
        wordInfoRepository.saveAndFlush(wordInfo);

        int databaseSizeBeforeDelete = wordInfoRepository.findAll().size();

        // Delete the wordInfo
        restWordInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, wordInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WordInfo> wordInfoList = wordInfoRepository.findAll();
        assertThat(wordInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
