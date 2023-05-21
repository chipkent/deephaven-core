/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.plot.example_plots;

import io.deephaven.time.DateTimeUtils;
import io.deephaven.plot.Figure;
import io.deephaven.plot.PlotStyle;
import io.deephaven.time.DateTime;

import java.time.Instant;

import static io.deephaven.plot.PlottingConvenience.plot;

/**
 * XY plot with DateTime axis
 */
public class SimpleXYDateTime {
    public static void main(String[] args) {

        final long dateTime = DateTimeUtils.parseInstant("2018-02-01T09:30:00 NY").getNanos();
        final Instant[] dates = new Instant[] {DateTimeUtils.epochNanosToInstant(dateTime),
                DateTimeUtils.epochNanosToInstant(dateTime + DateTimeUtils.HOUR),
                DateTimeUtils.epochNanosToInstant(dateTime + 2 * DateTimeUtils.HOUR),
                DateTimeUtils.epochNanosToInstant(dateTime + 3 * DateTimeUtils.HOUR),
                DateTimeUtils.epochNanosToInstant(dateTime + 4 * DateTimeUtils.HOUR),
                DateTimeUtils.epochNanosToInstant(dateTime + 5 * DateTimeUtils.HOUR),
                DateTimeUtils.epochNanosToInstant(dateTime + 6 * DateTimeUtils.HOUR),
                DateTimeUtils.epochNanosToInstant(dateTime + 6 * DateTimeUtils.HOUR + 30 * DateTimeUtils.MINUTE),
        };

        final double[] data = new double[] {1, 2, 3, 4, 5, 6, 7, 8};

        Figure axs2 = plot("Test2", dates, data)
                .xBusinessTime()
                .plotStyle(PlotStyle.SCATTER)
                .linesVisible(true)
                .xFormatPattern("HH:mm");

        ExamplePlotUtils.display(axs2);
    }
}
