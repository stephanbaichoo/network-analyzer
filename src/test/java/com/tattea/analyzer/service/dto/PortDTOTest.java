package com.tattea.analyzer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tattea.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PortDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PortDTO.class);
        PortDTO portDTO1 = new PortDTO();
        portDTO1.setId(1L);
        PortDTO portDTO2 = new PortDTO();
        assertThat(portDTO1).isNotEqualTo(portDTO2);
        portDTO2.setId(portDTO1.getId());
        assertThat(portDTO1).isEqualTo(portDTO2);
        portDTO2.setId(2L);
        assertThat(portDTO1).isNotEqualTo(portDTO2);
        portDTO1.setId(null);
        assertThat(portDTO1).isNotEqualTo(portDTO2);
    }
}
