package org.amawal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.amawal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AmawalWordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmawalWord.class);
        AmawalWord amawalWord1 = new AmawalWord();
        amawalWord1.setId(1L);
        AmawalWord amawalWord2 = new AmawalWord();
        amawalWord2.setId(amawalWord1.getId());
        assertThat(amawalWord1).isEqualTo(amawalWord2);
        amawalWord2.setId(2L);
        assertThat(amawalWord1).isNotEqualTo(amawalWord2);
        amawalWord1.setId(null);
        assertThat(amawalWord1).isNotEqualTo(amawalWord2);
    }
}
