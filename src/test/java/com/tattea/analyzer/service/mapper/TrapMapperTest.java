package com.tattea.analyzer.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrapMapperTest {

    private TrapMapper trapMapper;

    @BeforeEach
    public void setUp() {
        trapMapper = new TrapMapperImpl();
    }
}
