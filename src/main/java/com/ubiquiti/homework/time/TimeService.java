package com.ubiquiti.homework.time;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TimeService {

    private final Clock clock;

    public LocalDateTime currentDateTime() {
        return LocalDateTime.now(clock);
    }

    public LocalDateTime minutesBeforeNow(long minutes) {
        return currentDateTime().minusMinutes(minutes);
    }
}
