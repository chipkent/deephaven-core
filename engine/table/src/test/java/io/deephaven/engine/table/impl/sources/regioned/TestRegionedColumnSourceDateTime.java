/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.table.impl.sources.regioned;

import io.deephaven.chunk.attributes.Values;
import io.deephaven.time.DateTimeUtils;
import io.deephaven.util.QueryConstants;
import io.deephaven.engine.table.ColumnSource;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class TestRegionedColumnSourceDateTime
        extends TstRegionedColumnSourceReferencing<Instant, Values, ColumnRegionLong<Values>> {

    private static final ZoneId TZ_NY = ZoneId.of("America/New_York");

    public TestRegionedColumnSourceDateTime() {
        super(ColumnRegionLong.class);
    }

    private static final Instant[] TEST_DATES = new Instant[] {
            null,
            DateTimeUtils.now(),
            DateTimeUtils.dateTimeAtMidnight(DateTimeUtils.now(), TZ_NY),
            DateTimeUtils.parseInstant("2013-01-15T12:19:32.000 NY"),
            DateTimeUtils.parseInstant("2013-01-15T09:30:00.000 NY"),
            DateTimeUtils.parseInstant("2013-01-15T16:00:00.000 NY"),
            DateTimeUtils.parseInstant("2013-01-15T16:15:00.000 NY"),
            DateTimeUtils.parseInstant("2000-01-01T00:00:00.000 NY"),
            DateTimeUtils.parseInstant("1999-12-31T23:59:59.000 NY"),
            DateTimeUtils.parseInstant("1981-02-22T19:50:00.000 NY")
    };

    private ColumnSource<Long> SUT_AS_LONG;

    private void assertLookup(final long elementIndex,
            final int expectedRegionIndex,
            final Instant output,
            final boolean prev,
            final boolean reinterpreted) {
        checking(new Expectations() {
            {
                oneOf(cr[expectedRegionIndex]).getLong(elementIndex);
                will(returnValue(output == null ? QueryConstants.NULL_LONG : DateTimeUtils.epochNanos(output)));
            }
        });
        if (reinterpreted) {
            assertEquals(output == null ? QueryConstants.NULL_LONG : DateTimeUtils.epochNanos(output),
                    prev ? SUT_AS_LONG.getPrevLong(elementIndex) : SUT_AS_LONG.getLong(elementIndex));
        } else {
            assertEquals(output, prev ? SUT.getPrev(elementIndex) : SUT.get(elementIndex));
        }
        assertIsSatisfied();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        SUT = new RegionedColumnSourceDateTime();
        assertEquals(Instant.class, SUT.getType());
        SUT_AS_LONG = SUT.reinterpret(long.class);
        assertEquals(long.class, SUT_AS_LONG.getType());
        assertEquals(RegionedColumnSourceDateTime.class, SUT_AS_LONG.reinterpret(Instant.class).getClass());
    }

    @Override
    public void testGet() {
        fillRegions();

        // noinspection ConstantConditions
        assertLookup(0L, 0, TEST_DATES[0], false, false);
        assertLookup(RegionedColumnSource.getLastRowKey(0), 0, TEST_DATES[1], false, false);

        assertLookup(RegionedColumnSource.getFirstRowKey(1) + 1, 1, TEST_DATES[2], false, false);
        assertLookup(RegionedColumnSource.getLastRowKey(1) - 1, 1, TEST_DATES[3], false, false);

        assertLookup(RegionedColumnSource.getFirstRowKey(4) + 2, 4, TEST_DATES[4], false, false);
        assertLookup(RegionedColumnSource.getLastRowKey(4) - 2, 4, TEST_DATES[5], false, false);

        assertLookup(RegionedColumnSource.getFirstRowKey(8) + 3, 8, TEST_DATES[6], false, false);
        assertLookup(RegionedColumnSource.getLastRowKey(8) - 3, 8, TEST_DATES[7], false, false);

        assertLookup(RegionedColumnSource.getFirstRowKey(9) + 4, 9, TEST_DATES[8], false, false);
        assertLookup(RegionedColumnSource.getLastRowKey(9) - 4, 9, TEST_DATES[9], false, false);
    }

    @Override
    public void testGetPrev() {
        fillRegions();

        // noinspection ConstantConditions
        assertLookup(0L, 0, TEST_DATES[0], true, false);
        assertLookup(RegionedColumnSource.getLastRowKey(0), 0, TEST_DATES[1], true, false);

        assertLookup(RegionedColumnSource.getFirstRowKey(1) + 1, 1, TEST_DATES[2], true, false);
        assertLookup(RegionedColumnSource.getLastRowKey(1) - 1, 1, TEST_DATES[3], true, false);

        assertLookup(RegionedColumnSource.getFirstRowKey(4) + 2, 4, TEST_DATES[4], true, false);
        assertLookup(RegionedColumnSource.getLastRowKey(4) - 2, 4, TEST_DATES[5], true, false);

        assertLookup(RegionedColumnSource.getFirstRowKey(8) + 3, 8, TEST_DATES[6], true, false);
        assertLookup(RegionedColumnSource.getLastRowKey(8) - 3, 8, TEST_DATES[7], true, false);

        assertLookup(RegionedColumnSource.getFirstRowKey(9) + 4, 9, TEST_DATES[8], true, false);
        assertLookup(RegionedColumnSource.getLastRowKey(9) - 4, 9, TEST_DATES[9], true, false);
    }

    @Test
    public void testGetReinterpreted() {
        fillRegions();

        // noinspection ConstantConditions
        assertLookup(0L, 0, TEST_DATES[0], false, true);
        assertLookup(RegionedColumnSource.getLastRowKey(0), 0, TEST_DATES[1], false, true);

        assertLookup(RegionedColumnSource.getFirstRowKey(1) + 1, 1, TEST_DATES[2], false, true);
        assertLookup(RegionedColumnSource.getLastRowKey(1) - 1, 1, TEST_DATES[3], false, true);

        assertLookup(RegionedColumnSource.getFirstRowKey(4) + 2, 4, TEST_DATES[4], false, true);
        assertLookup(RegionedColumnSource.getLastRowKey(4) - 2, 4, TEST_DATES[5], false, true);

        assertLookup(RegionedColumnSource.getFirstRowKey(8) + 3, 8, TEST_DATES[6], false, true);
        assertLookup(RegionedColumnSource.getLastRowKey(8) - 3, 8, TEST_DATES[7], false, true);

        assertLookup(RegionedColumnSource.getFirstRowKey(9) + 4, 9, TEST_DATES[8], false, true);
        assertLookup(RegionedColumnSource.getLastRowKey(9) - 4, 9, TEST_DATES[9], false, true);
    }

    @Test
    public void testGetPrevReinterpreted() {
        fillRegions();

        // noinspection ConstantConditions
        assertLookup(0L, 0, TEST_DATES[0], true, true);
        assertLookup(RegionedColumnSource.getLastRowKey(0), 0, TEST_DATES[1], true, true);

        assertLookup(RegionedColumnSource.getFirstRowKey(1) + 1, 1, TEST_DATES[2], true, true);
        assertLookup(RegionedColumnSource.getLastRowKey(1) - 1, 1, TEST_DATES[3], true, true);

        assertLookup(RegionedColumnSource.getFirstRowKey(4) + 2, 4, TEST_DATES[4], true, true);
        assertLookup(RegionedColumnSource.getLastRowKey(4) - 2, 4, TEST_DATES[5], true, true);

        assertLookup(RegionedColumnSource.getFirstRowKey(8) + 3, 8, TEST_DATES[6], true, true);
        assertLookup(RegionedColumnSource.getLastRowKey(8) - 3, 8, TEST_DATES[7], true, true);

        assertLookup(RegionedColumnSource.getFirstRowKey(9) + 4, 9, TEST_DATES[8], true, true);
        assertLookup(RegionedColumnSource.getLastRowKey(9) - 4, 9, TEST_DATES[9], true, true);
    }
}
