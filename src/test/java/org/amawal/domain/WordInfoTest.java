package org.amawal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.amawal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WordInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordInfo.class);
        WordInfo wordInfo1 = new WordInfo();
        wordInfo1.setId(1L);
        WordInfo wordInfo2 = new WordInfo();
        wordInfo2.setId(wordInfo1.getId());
        assertThat(wordInfo1).isEqualTo(wordInfo2);
        wordInfo2.setId(2L);
        assertThat(wordInfo1).isNotEqualTo(wordInfo2);
        wordInfo1.setId(null);
        assertThat(wordInfo1).isNotEqualTo(wordInfo2);
    }
}
