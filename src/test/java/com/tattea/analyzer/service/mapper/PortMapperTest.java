package com.tattea.analyzer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PortMapperTest {

    private PortMapper portMapper;

    @BeforeEach
    public void setUp() {
        portMapper = new PortMapperImpl();
    }
}
