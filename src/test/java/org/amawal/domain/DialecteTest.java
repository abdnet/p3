package org.amawal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.amawal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DialecteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dialecte.class);
        Dialecte dialecte1 = new Dialecte();
        dialecte1.setId(1L);
        Dialecte dialecte2 = new Dialecte();
        dialecte2.setId(dialecte1.getId());
        assertThat(dialecte1).isEqualTo(dialecte2);
        dialecte2.setId(2L);
        assertThat(dialecte1).isNotEqualTo(dialecte2);
        dialecte1.setId(null);
        assertThat(dialecte1).isNotEqualTo(dialecte2);
    }
}
