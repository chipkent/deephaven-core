package io.deephaven.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.deephaven.util.QueryConstants.NULL_LONG;

//TODO: Move?

/**
 * TimeLiteralReplacedExpression is a query expression with time, period, and datetime literals replaced by instance variables.
 * This contains the converted formula, instance variable declarations, and a map of types of the instance variables.
 */
public class TimeLiteralReplacedExpression {
    private final String convertedFormula;
    private final String instanceVariablesString;
    private final HashMap<String, Class<?>> newVariables;

    private TimeLiteralReplacedExpression(String convertedFormula, String instanceVariablesString, HashMap<String, Class<?>> newVariables) {
        this.convertedFormula = convertedFormula;
        this.instanceVariablesString = instanceVariablesString;
        this.newVariables = newVariables;
    }

    /**
     * Gets the formula after replacing time, period, and datetime literals with variables.
     *
     * @return formula after replacing time, period, and datetime literals with variables.
     */
    public String getConvertedFormula() {
        return convertedFormula;
    }

    /**
     * An expression that declares new instance variables.
     *
     * @return expression that declares new instance variables.
     */
    public String getInstanceVariablesString() {
        return instanceVariablesString;
    }

    /**
     * Gets a map of names and types of new instance variables.
     *
     * @return a map of names and types of new instance variables.
     */
    public HashMap<String, Class<?>> getNewVariables() {
        return newVariables;
    }

    /**
     * Converts a query expression to a {@link TimeLiteralReplacedExpression}, where the time, period, and datetime literals
     * are replaced by instance variables.
     *
     * @param expression query expression to convert.
     * @return a {@link TimeLiteralReplacedExpression} where time, period, and datetime literals have been replaced by instance variables.
     * @throws Exception If any error occurs or a literal value cannot be parsed.
     */
    // TODO: This should probably be handled in LanguageParser.accept(CharLiteralExpr, StringBuilder).
    public static TimeLiteralReplacedExpression convertExpression(String expression) throws Exception {
        final StringBuilder instanceVariablesString = new StringBuilder();
        final HashMap<String, Class<?>> newVariables = new HashMap<>();

        final StringBuilder convertedFormula = new StringBuilder();

        int localDateIndex = 0;
        int dateTimeIndex = 0;
        int timeIndex = 0;
        int periodIndex = 0;

        final Matcher matcher = Pattern.compile("'[^']*'").matcher(expression);

        while (matcher.find()) {
            String s = expression.substring(matcher.start() + 1, matcher.end() - 1);

            if (s.length() <= 1) {
                // leave chars and also bad empty ones alone
                continue;
            }

            if (DateTimeUtils.parseDateTimeQuiet(s) != null) {
                matcher.appendReplacement(convertedFormula, "_date" + dateTimeIndex);
                instanceVariablesString.append("        private DateTime _date").append(dateTimeIndex)
                        .append("=DateTimeUtils.toDateTime(\"")
                        .append(expression, matcher.start() + 1, matcher.end() - 1).append("\");\n");
                newVariables.put("_date" + dateTimeIndex, DateTime.class);

                dateTimeIndex++;
            } else if (DateTimeUtils.parseDateQuiet(s) != null) {
                matcher.appendReplacement(convertedFormula, "_localDate" + localDateIndex);
                instanceVariablesString.append("        private java.time.LocalDate _localDate").append(localDateIndex)
                        .append("=DateTimeUtils.convertDate(\"").append(expression, matcher.start() + 1, matcher.end() - 1)
                        .append("\");\n");
                newVariables.put("_localDate" + localDateIndex, LocalDate.class);
                localDateIndex++;
            } else if (DateTimeUtils.parseNanosQuiet(s) != NULL_LONG) {
                matcher.appendReplacement(convertedFormula, "_time" + timeIndex);
                instanceVariablesString.append("        private long _time").append(timeIndex)
                        .append("=DateTimeUtils.convertTime(\"").append(expression, matcher.start() + 1, matcher.end() - 1)
                        .append("\");\n");
                newVariables.put("_time" + timeIndex, long.class);

                timeIndex++;
            } else if (DateTimeUtils.parsePeriodQuiet(s) != null) {
                matcher.appendReplacement(convertedFormula, "_period" + periodIndex);
                instanceVariablesString.append("        private Period _period").append(periodIndex)
                        .append("=DateTimeUtils.convertPeriod(\"")
                        .append(expression, matcher.start() + 1, matcher.end() - 1)
                        .append("\");\n");
                newVariables.put("_period" + periodIndex, Period.class);

                periodIndex++;
            } else if (DateTimeUtils.convertLocalTimeQuiet(s) != null) {
                matcher.appendReplacement(convertedFormula, "_localTime" + timeIndex);
                instanceVariablesString.append("        private java.time.LocalTime _localTime").append(timeIndex)
                        .append("=DateTimeUtils.convertLocalTime(\"")
                        .append(expression, matcher.start() + 1, matcher.end() - 1).append("\");\n");
                newVariables.put("_localTime" + timeIndex, LocalTime.class);
                timeIndex++;
            } else {
                throw new Exception("Cannot parse datetime/time/period : " + s);
            }
        }

        matcher.appendTail(convertedFormula);

        return new TimeLiteralReplacedExpression(convertedFormula.toString(), instanceVariablesString.toString(), newVariables);
    }

}
