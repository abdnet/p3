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
import org.amawal.domain.AmawalWord;
import org.amawal.repository.AmawalWordRepository;
import org.amawal.service.AmawalWordService;
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
 * Integration tests for the {@link AmawalWordResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AmawalWordResourceIT {

    private static final Long DEFAULT_WORD_ID = 1L;
    private static final Long UPDATED_WORD_ID = 2L;

    private static final String DEFAULT_ORTHOGRAPHE_TIFINAGH = "AAAAAAAAAA";
    private static final String UPDATED_ORTHOGRAPHE_TIFINAGH = "BBBBBBBBBB";

    private static final String DEFAULT_ORTHOGRAPHE_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_ORTHOGRAPHE_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/amawal-words";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmawalWordRepository amawalWordRepository;

    @Mock
    private AmawalWordRepository amawalWordRepositoryMock;

    @Mock
    private AmawalWordService amawalWordServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmawalWordMockMvc;

    private AmawalWord amawalWord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmawalWord createEntity(EntityManager em) {
        AmawalWord amawalWord = new AmawalWord()
            .wordId(DEFAULT_WORD_ID)
            .orthographeTifinagh(DEFAULT_ORTHOGRAPHE_TIFINAGH)
            .orthographeLatin(DEFAULT_ORTHOGRAPHE_LATIN);
        return amawalWord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmawalWord createUpdatedEntity(EntityManager em) {
        AmawalWord amawalWord = new AmawalWord()
            .wordId(UPDATED_WORD_ID)
            .orthographeTifinagh(UPDATED_ORTHOGRAPHE_TIFINAGH)
            .orthographeLatin(UPDATED_ORTHOGRAPHE_LATIN);
        return amawalWord;
    }

    @BeforeEach
    public void initTest() {
        amawalWord = createEntity(em);
    }

    @Test
    @Transactional
    void createAmawalWord() throws Exception {
        int databaseSizeBeforeCreate = amawalWordRepository.findAll().size();
        // Create the AmawalWord
        restAmawalWordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amawalWord)))
            .andExpect(status().isCreated());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeCreate + 1);
        AmawalWord testAmawalWord = amawalWordList.get(amawalWordList.size() - 1);
        assertThat(testAmawalWord.getWordId()).isEqualTo(DEFAULT_WORD_ID);
        assertThat(testAmawalWord.getOrthographeTifinagh()).isEqualTo(DEFAULT_ORTHOGRAPHE_TIFINAGH);
        assertThat(testAmawalWord.getOrthographeLatin()).isEqualTo(DEFAULT_ORTHOGRAPHE_LATIN);
    }

    @Test
    @Transactional
    void createAmawalWordWithExistingId() throws Exception {
        // Create the AmawalWord with an existing ID
        amawalWord.setId(1L);

        int databaseSizeBeforeCreate = amawalWordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmawalWordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amawalWord)))
            .andExpect(status().isBadRequest());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAmawalWords() throws Exception {
        // Initialize the database
        amawalWordRepository.saveAndFlush(amawalWord);

        // Get all the amawalWordList
        restAmawalWordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amawalWord.getId().intValue())))
            .andExpect(jsonPath("$.[*].wordId").value(hasItem(DEFAULT_WORD_ID.intValue())))
            .andExpect(jsonPath("$.[*].orthographeTifinagh").value(hasItem(DEFAULT_ORTHOGRAPHE_TIFINAGH)))
            .andExpect(jsonPath("$.[*].orthographeLatin").value(hasItem(DEFAULT_ORTHOGRAPHE_LATIN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAmawalWordsWithEagerRelationshipsIsEnabled() throws Exception {
        when(amawalWordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAmawalWordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(amawalWordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAmawalWordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(amawalWordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAmawalWordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(amawalWordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAmawalWord() throws Exception {
        // Initialize the database
        amawalWordRepository.saveAndFlush(amawalWord);

        // Get the amawalWord
        restAmawalWordMockMvc
            .perform(get(ENTITY_API_URL_ID, amawalWord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(amawalWord.getId().intValue()))
            .andExpect(jsonPath("$.wordId").value(DEFAULT_WORD_ID.intValue()))
            .andExpect(jsonPath("$.orthographeTifinagh").value(DEFAULT_ORTHOGRAPHE_TIFINAGH))
            .andExpect(jsonPath("$.orthographeLatin").value(DEFAULT_ORTHOGRAPHE_LATIN));
    }

    @Test
    @Transactional
    void getNonExistingAmawalWord() throws Exception {
        // Get the amawalWord
        restAmawalWordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAmawalWord() throws Exception {
        // Initialize the database
        amawalWordRepository.saveAndFlush(amawalWord);

        int databaseSizeBeforeUpdate = amawalWordRepository.findAll().size();

        // Update the amawalWord
        AmawalWord updatedAmawalWord = amawalWordRepository.findById(amawalWord.getId()).get();
        // Disconnect from session so that the updates on updatedAmawalWord are not directly saved in db
        em.detach(updatedAmawalWord);
        updatedAmawalWord
            .wordId(UPDATED_WORD_ID)
            .orthographeTifinagh(UPDATED_ORTHOGRAPHE_TIFINAGH)
            .orthographeLatin(UPDATED_ORTHOGRAPHE_LATIN);

        restAmawalWordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAmawalWord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAmawalWord))
            )
            .andExpect(status().isOk());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeUpdate);
        AmawalWord testAmawalWord = amawalWordList.get(amawalWordList.size() - 1);
        assertThat(testAmawalWord.getWordId()).isEqualTo(UPDATED_WORD_ID);
        assertThat(testAmawalWord.getOrthographeTifinagh()).isEqualTo(UPDATED_ORTHOGRAPHE_TIFINAGH);
        assertThat(testAmawalWord.getOrthographeLatin()).isEqualTo(UPDATED_ORTHOGRAPHE_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingAmawalWord() throws Exception {
        int databaseSizeBeforeUpdate = amawalWordRepository.findAll().size();
        amawalWord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmawalWordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amawalWord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amawalWord))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAmawalWord() throws Exception {
        int databaseSizeBeforeUpdate = amawalWordRepository.findAll().size();
        amawalWord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmawalWordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amawalWord))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAmawalWord() throws Exception {
        int databaseSizeBeforeUpdate = amawalWordRepository.findAll().size();
        amawalWord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmawalWordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amawalWord)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAmawalWordWithPatch() throws Exception {
        // Initialize the database
        amawalWordRepository.saveAndFlush(amawalWord);

        int databaseSizeBeforeUpdate = amawalWordRepository.findAll().size();

        // Update the amawalWord using partial update
        AmawalWord partialUpdatedAmawalWord = new AmawalWord();
        partialUpdatedAmawalWord.setId(amawalWord.getId());

        partialUpdatedAmawalWord.wordId(UPDATED_WORD_ID);

        restAmawalWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmawalWord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmawalWord))
            )
            .andExpect(status().isOk());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeUpdate);
        AmawalWord testAmawalWord = amawalWordList.get(amawalWordList.size() - 1);
        assertThat(testAmawalWord.getWordId()).isEqualTo(UPDATED_WORD_ID);
        assertThat(testAmawalWord.getOrthographeTifinagh()).isEqualTo(DEFAULT_ORTHOGRAPHE_TIFINAGH);
        assertThat(testAmawalWord.getOrthographeLatin()).isEqualTo(DEFAULT_ORTHOGRAPHE_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateAmawalWordWithPatch() throws Exception {
        // Initialize the database
        amawalWordRepository.saveAndFlush(amawalWord);

        int databaseSizeBeforeUpdate = amawalWordRepository.findAll().size();

        // Update the amawalWord using partial update
        AmawalWord partialUpdatedAmawalWord = new AmawalWord();
        partialUpdatedAmawalWord.setId(amawalWord.getId());

        partialUpdatedAmawalWord
            .wordId(UPDATED_WORD_ID)
            .orthographeTifinagh(UPDATED_ORTHOGRAPHE_TIFINAGH)
            .orthographeLatin(UPDATED_ORTHOGRAPHE_LATIN);

        restAmawalWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmawalWord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmawalWord))
            )
            .andExpect(status().isOk());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeUpdate);
        AmawalWord testAmawalWord = amawalWordList.get(amawalWordList.size() - 1);
        assertThat(testAmawalWord.getWordId()).isEqualTo(UPDATED_WORD_ID);
        assertThat(testAmawalWord.getOrthographeTifinagh()).isEqualTo(UPDATED_ORTHOGRAPHE_TIFINAGH);
        assertThat(testAmawalWord.getOrthographeLatin()).isEqualTo(UPDATED_ORTHOGRAPHE_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingAmawalWord() throws Exception {
        int databaseSizeBeforeUpdate = amawalWordRepository.findAll().size();
        amawalWord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmawalWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, amawalWord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amawalWord))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAmawalWord() throws Exception {
        int databaseSizeBeforeUpdate = amawalWordRepository.findAll().size();
        amawalWord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmawalWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amawalWord))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAmawalWord() throws Exception {
        int databaseSizeBeforeUpdate = amawalWordRepository.findAll().size();
        amawalWord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmawalWordMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(amawalWord))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmawalWord in the database
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAmawalWord() throws Exception {
        // Initialize the database
        amawalWordRepository.saveAndFlush(amawalWord);

        int databaseSizeBeforeDelete = amawalWordRepository.findAll().size();

        // Delete the amawalWord
        restAmawalWordMockMvc
            .perform(delete(ENTITY_API_URL_ID, amawalWord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AmawalWord> amawalWordList = amawalWordRepository.findAll();
        assertThat(amawalWordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
