/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.table.impl.sources;

import io.deephaven.engine.table.impl.DefaultChunkSource;
import io.deephaven.engine.table.impl.MutableColumnSourceGetDefaults;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.time.DateTimeUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Array-backed ColumnSource for DateTimes. Allows reinterpret as long.
 */
public class DateTimeSparseArraySource extends NanosBasedTimeSparseArraySource<DateTime>
        implements MutableColumnSourceGetDefaults.ForLongAsDateTime, DefaultChunkSource<Values>, ConvertableTimeSource {

    public DateTimeSparseArraySource() {
        super(Instant.class);
    }

    public DateTimeSparseArraySource(final @NotNull LongSparseArraySource nanoSource) {
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
}
