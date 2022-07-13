package com.tattea.analyzer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tattea.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Host.class);
        Host host1 = new Host();
        host1.setId(1L);
        Host host2 = new Host();
        host2.setId(host1.getId());
        assertThat(host1).isEqualTo(host2);
        host2.setId(2L);
        assertThat(host1).isNotEqualTo(host2);
        host1.setId(null);
        assertThat(host1).isNotEqualTo(host2);
    }
}
