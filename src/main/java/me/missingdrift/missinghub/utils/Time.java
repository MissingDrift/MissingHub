package me.missigdrift.missinghub.utils;

import java.util.HashMap;

public class Time {

    private static final long TICK_MS = 50;
    private static final long SECOND_MS = 1000;
    private static final long MINUTE_MS = SECOND_MS * 60;
    private static final long HOUR_MS = MINUTE_MS * 60;
    private static final long DAY_MS = HOUR_MS * 24;
    private static final long WEEK_MS = DAY_MS * 7;
    private static final long MONTH_MS = WEEK_MS * 4;
    private static final long YEAR_MS = MONTH_MS * 12;

    private static final HashMap<String, Long> unitMultipliers = new HashMap<>();

    private static void addTimeMultiplier(long multiplier, String... keys) {
        for(String key : keys) {
            unitMultipliers.put(key, multiplier);
        }
    }

    static {
        addTimeMultiplier(1, "ms", "milli", "millis", "millisecond", "milliseconds");
        addTimeMultiplier(TICK_MS, "t", "tick", "ticks");
        addTimeMultiplier(SECOND_MS, "s", "sec", "secs", "second", "seconds");
        addTimeMultiplier(MINUTE_MS, "m", "min", "mins", "minute", "minutes");
        addTimeMultiplier(HOUR_MS, "h", "hour", "hours");
        addTimeMultiplier(DAY_MS, "d", "day", "days");
        addTimeMultiplier(WEEK_MS, "w", "week", "weeks");
        addTimeMultiplier(MONTH_MS, "M", "month", "months");
        addTimeMultiplier(YEAR_MS, "y", "year", "years");
    }

    private long milliseconds;

    private Time(long milliseconds) {
        if(milliseconds < 0) {
            throw new IllegalArgumentException("Number of milliseconds cannot be less than 0");
        }

        this.milliseconds = milliseconds;
    }

    public long toMilliseconds() {
        return this.milliseconds;
    }

    @Override
    public String toString() {
        return toString(this.milliseconds);
    }

    public static Time parseString(String timeString) throws TimeParseException {
        if(timeString == null) {
            throw new IllegalArgumentException("timeString cannot be null");
        }

        if(timeString.isEmpty()) {
            throw new TimeParseException("Empty time string");
        }

        long totalMilliseconds = 0;

        boolean readingNumber = true;

        StringBuilder number = new StringBuilder();
        StringBuilder unit = new StringBuilder();

        for(char c : timeString.toCharArray()) {
            if(c == ' ' || c == ',') {
                readingNumber = false;
                continue;
            }

            if(c == '.' || (c >='0' && c <= '9')) {
                if(!readingNumber) {
                    totalMilliseconds += parseTimeComponent(number.toString(), unit.toString());

                    number.setLength(0);
                    unit.setLength(0);

                    readingNumber = true;
                }

                number.append(c);
            } else {
                readingNumber = false;
                unit.append(c);
            }
        }

        if(readingNumber) {
            throw new TimeParseException("Number \"" + number + "\" not matched with unit at end of string");
        } else {
            totalMilliseconds += parseTimeComponent(number.toString(), unit.toString());
        }

        return new Time(totalMilliseconds);
    }

    private static double parseTimeComponent(String magnitudeString, String unit) throws TimeParseException {
        if(magnitudeString.isEmpty()) {
            throw new TimeParseException("Missing number for unit \"" + unit + "\"");
        }

        long magnitude;

        try {
            magnitude = Long.parseLong(magnitudeString);
        } catch(NumberFormatException e) {
            throw new TimeParseException("Unable to parse number \"" + magnitudeString + "\"", e);
        }

        if(unit.length() > 3 && unit.substring(unit.length() - 3).equals("and")) {
            unit = unit.substring(0, unit.length() - 3);
        }

        Long unitMultiplier = unitMultipliers.get(unit);

        if(unitMultiplier == null) {
            throw new TimeParseException("Unknown time unit \"" + unit + "\"");
        }

        return magnitude * unitMultiplier;
    }

    private static long appendTime(long time, long unitInMS, String name, StringBuilder builder) {
        long timeInUnits = (time - (time % unitInMS)) / unitInMS;

        if(timeInUnits > 0) {
            builder.append(", ").append(timeInUnits).append(' ').append(name);
        }

        return time - timeInUnits * unitInMS;
    }

    public static String toString(long time) {
        StringBuilder timeString = new StringBuilder();

        time = appendTime(time, YEAR_MS, "years", timeString);
        time = appendTime(time, MONTH_MS, "months", timeString);
        time = appendTime(time, WEEK_MS, "weeks", timeString);
        time = appendTime(time, DAY_MS, "days", timeString);
        time = appendTime(time, HOUR_MS, "hours", timeString);
        time = appendTime(time, MINUTE_MS, "minutes", timeString);
        time = appendTime(time, SECOND_MS, "seconds", timeString);

        if(timeString.length() == 0) {
            if(time == 0) {
                return "0 seconds";
            } else {
                return time + " ms";
            }
        }

        return timeString.substring(2);
    }

    public static class TimeParseException extends RuntimeException {

        public TimeParseException(String reason) {
            super(reason);
        }

        public TimeParseException(String reason, Throwable cause) {
            super(reason, cause);
        }

    }

    public static boolean check(String string){
        String s = string.replaceAll("[0123456789]", "");
        return s.equals("s") || s.equals("m") || s.equals("h") || s.equals("d") || s.equals("w") || s.equals("M") || s.equals("y");
    }
}