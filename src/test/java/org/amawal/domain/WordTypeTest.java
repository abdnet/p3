package org.amawal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.amawal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WordTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordType.class);
        WordType wordType1 = new WordType();
        wordType1.setId(1L);
        WordType wordType2 = new WordType();
        wordType2.setId(wordType1.getId());
        assertThat(wordType1).isEqualTo(wordType2);
        wordType2.setId(2L);
        assertThat(wordType1).isNotEqualTo(wordType2);
        wordType1.setId(null);
        assertThat(wordType1).isNotEqualTo(wordType2);
    }
}
