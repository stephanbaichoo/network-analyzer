package com.tattea.analyzer.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tattea.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NetflowDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NetflowDTO.class);
        NetflowDTO netflowDTO1 = new NetflowDTO();
        netflowDTO1.setId(1L);
        NetflowDTO netflowDTO2 = new NetflowDTO();
        assertThat(netflowDTO1).isNotEqualTo(netflowDTO2);
        netflowDTO2.setId(netflowDTO1.getId());
        assertThat(netflowDTO1).isEqualTo(netflowDTO2);
        netflowDTO2.setId(2L);
        assertThat(netflowDTO1).isNotEqualTo(netflowDTO2);
        netflowDTO1.setId(null);
        assertThat(netflowDTO1).isNotEqualTo(netflowDTO2);
    }
}
