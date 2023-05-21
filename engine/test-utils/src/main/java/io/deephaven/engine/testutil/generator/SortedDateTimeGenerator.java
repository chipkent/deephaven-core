package io.deephaven.engine.testutil.generator;

import io.deephaven.time.DateTimeUtils;

import java.time.Instant;
import java.util.Random;

public class SortedDateTimeGenerator extends AbstractSortedGenerator<Instant> {
    private final Instant minTime;
    private final Instant maxTime;

    public SortedDateTimeGenerator(Instant minTime, Instant maxTime) {
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    Instant maxValue() {
        return maxTime;
    }

    Instant minValue() {
        return minTime;
    }

    Instant makeValue(Instant floor, Instant ceiling, Random random) {
        final long longFloor = DateTimeUtils.epochNanos(floor);
        final long longCeiling = DateTimeUtils.epochNanos(ceiling);

        final long range = longCeiling - longFloor + 1L;
        final long nextLong = Math.abs(random.nextLong()) % range;

        return DateTimeUtils.epochNanosToInstant(longFloor + (nextLong % range));
    }

    @Override
    public Class<Instant> getType() {
        return Instant.class;
    }
}
