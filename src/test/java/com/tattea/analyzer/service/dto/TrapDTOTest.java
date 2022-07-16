package com.tattea.analyzer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tattea.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrapDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrapDTO.class);
        TrapDTO trapDTO1 = new TrapDTO();
        trapDTO1.setId(1L);
        TrapDTO trapDTO2 = new TrapDTO();
        assertThat(trapDTO1).isNotEqualTo(trapDTO2);
        trapDTO2.setId(trapDTO1.getId());
        assertThat(trapDTO1).isEqualTo(trapDTO2);
        trapDTO2.setId(2L);
        assertThat(trapDTO1).isNotEqualTo(trapDTO2);
        trapDTO1.setId(null);
        assertThat(trapDTO1).isNotEqualTo(trapDTO2);
    }
}
