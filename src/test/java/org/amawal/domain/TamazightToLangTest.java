package org.amawal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.amawal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TamazightToLangTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TamazightToLang.class);
        TamazightToLang tamazightToLang1 = new TamazightToLang();
        tamazightToLang1.setId(1L);
        TamazightToLang tamazightToLang2 = new TamazightToLang();
        tamazightToLang2.setId(tamazightToLang1.getId());
        assertThat(tamazightToLang1).isEqualTo(tamazightToLang2);
        tamazightToLang2.setId(2L);
        assertThat(tamazightToLang1).isNotEqualTo(tamazightToLang2);
        tamazightToLang1.setId(null);
        assertThat(tamazightToLang1).isNotEqualTo(tamazightToLang2);
    }
}
