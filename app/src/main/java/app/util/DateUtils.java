package app.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    public static boolean IsBetween(LocalDateTime date, LocalDateTime startDate, LocalDateTime endDate) {
        return (date.isBefore(endDate) &&
                date.isAfter(startDate)) ||
                date.isEqual(startDate) ||
                date.isEqual(endDate);
    }

    public static boolean IsCoincident(LocalDateTime range1Start, LocalDateTime range1End,
                                       LocalDateTime range2Start, LocalDateTime range2End) {
        return IsBetween(range1Start, range2Start, range2End) || IsBetween(range1End, range2Start, range2End)
                ||IsBetween(range2Start, range1Start, range1End) || IsBetween(range2End, range1Start, range1End);
    }

    public static double differenceInHours(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        long minutes = localDateTime1.until(localDateTime2, ChronoUnit.MINUTES);
        double hours = minutes / 60.0;
        return hours;
    }

    public static LocalDateTime minimum(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        if (localDateTime1.isBefore(localDateTime2)) {
            return localDateTime1;
        }
        return localDateTime2;
    }

    public static LocalDateTime maximum(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        if (localDateTime1.isBefore(localDateTime2)) {
            return localDateTime2;
        }
        return localDateTime1;
    }

}
