package com.ubiquiti.homework.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {

    private static final Instant NOW = Instant.now();

    @Mock private Clock clock;

    @InjectMocks private TimeService service;

    @BeforeEach
    void beforeEach() {
        when(clock.instant()).thenReturn(NOW);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }

    @Test
    void currentDateTime() {
        LocalDateTime result = service.currentDateTime();

        assertThat(result).isEqualTo(LocalDateTime.now(clock));
    }

    @Test
    void before() {
        long minutes = 5;

        LocalDateTime expected = LocalDateTime.now(clock).minusMinutes(minutes);

        LocalDateTime actual = service.minutesBeforeNow(minutes);

        assertThat(actual).isEqualTo(expected);
    }
}
