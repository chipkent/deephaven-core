/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.table.impl.sources;

import io.deephaven.engine.table.ColumnSource;
import io.deephaven.time.DateTimeUtils;

import java.time.Instant;

/**
 * Reinterpret result {@link ColumnSource} implementations that translates {@code long} to {@link Instant} values.
 */
public class LongAsDateTimeColumnSource extends BoxedLongAsTimeSource<Instant> {
    public LongAsDateTimeColumnSource(ColumnSource<Long> alternateColumnSource) {
        super(Instant.class, alternateColumnSource);
    }

    @Override
    protected Instant makeValue(long val) {
        return DateTimeUtils.epochNanosToInstant(val);
    }
}