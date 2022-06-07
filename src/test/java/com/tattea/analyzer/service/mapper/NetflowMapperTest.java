package com.tattea.analyzer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NetflowMapperTest {

    private NetflowMapper netflowMapper;

    @BeforeEach
    public void setUp() {
        netflowMapper = new NetflowMapperImpl();
    }
}
