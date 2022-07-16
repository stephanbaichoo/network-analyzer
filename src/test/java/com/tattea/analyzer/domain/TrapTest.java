package com.tattea.analyzer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tattea.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrapTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trap.class);
        Trap trap1 = new Trap();
        trap1.setId(1L);
        Trap trap2 = new Trap();
        trap2.setId(trap1.getId());
        assertThat(trap1).isEqualTo(trap2);
        trap2.setId(2L);
        assertThat(trap1).isNotEqualTo(trap2);
        trap1.setId(null);
        assertThat(trap1).isNotEqualTo(trap2);
    }
}
