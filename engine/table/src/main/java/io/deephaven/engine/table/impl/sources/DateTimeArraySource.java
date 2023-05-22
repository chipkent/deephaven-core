/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.table.impl.sources;

import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.impl.MutableColumnSourceGetDefaults;
import io.deephaven.time.DateTime;
import io.deephaven.time.DateTimeUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Array-backed {@link ColumnSource} for DateTimes. Allows reinterpretation to long and {@link java.time.Instant}.
 */
public class DateTimeArraySource extends NanosBasedTimeArraySource<Instant>
        implements MutableColumnSourceGetDefaults.ForLongAsDateTime, ConvertableTimeSource {
    public DateTimeArraySource() {
        super(Instant.class);
    }

    public DateTimeArraySource(final @NotNull LongArraySource nanoSource) {
        super(Instant.class, nanoSource);
    }

    @Override
    protected Instant makeValue(long nanos) {
        return DateTimeUtils.epochNanosToInstant(nanos);
    }

    @Override
    protected long toNanos(Instant value) {
        return DateTimeUtils.epochNanos(value);
    }

    @Override
    public ColumnSource<Instant> toDateTime() {
        return this;
    }
}
