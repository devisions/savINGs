package org.devisions.labs.savings.vx.commons.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;


/**
 * A fixed clock used during unit testing to be able to pass "workingHours" validation
 * that is part of the logic for storing a new savings account.
 *
 * @author devisions
 */
public class FixedClock extends Clock {

    private Instant workingTime;
    private final ZoneId DEFAULT_TZONE = ZoneId.systemDefault();

    private static final Logger logger = LoggerFactory.getLogger(FixedClock.class);

    /**
     * Instantiate FixedClock based on a provided time that has the format
     * "yyyy-MM-dd hh:mm:ss" (the DateTimeFormatter style).
     */
    public FixedClock(String mockWorkingTimeUTC) {

        DateTimeFormatter dtf = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd H:m:s")
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .toFormatter()
            .withZone(ZoneId.of("UTC"));
        workingTime = dtf.parse(mockWorkingTimeUTC, Instant::from);
        logger.debug("workingTime is set to '{}'.", workingTime.toString());

    }

    @Override
    public ZoneId getZone() {
        return DEFAULT_TZONE;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return Clock.fixed(workingTime, zone);
    }

    @Override
    public Instant instant() {
        return workingTime;
    }

}
