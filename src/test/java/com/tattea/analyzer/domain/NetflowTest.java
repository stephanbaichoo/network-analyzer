package com.tattea.analyzer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tattea.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NetflowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Netflow.class);
        Netflow netflow1 = new Netflow();
        netflow1.setId(1L);
        Netflow netflow2 = new Netflow();
        netflow2.setId(netflow1.getId());
        assertThat(netflow1).isEqualTo(netflow2);
        netflow2.setId(2L);
        assertThat(netflow1).isNotEqualTo(netflow2);
        netflow1.setId(null);
        assertThat(netflow1).isNotEqualTo(netflow2);
    }
}
