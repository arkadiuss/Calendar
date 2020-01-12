package logic.util;

import java.time.LocalDateTime;

public class DateUtils {
    public static boolean IsBetween(LocalDateTime date, LocalDateTime startDate, LocalDateTime endDate) {
        return (date.isBefore(endDate) &&
                date.isAfter(startDate)) ||
                date.isEqual(startDate) ||
                date.isEqual(endDate);
    }

    public static boolean IsCoincident(LocalDateTime range1Start, LocalDateTime range1End,
                                        LocalDateTime range2Start, LocalDateTime range2End) {
        return IsBetween(range1Start, range2Start, range2End) || IsBetween(range1End, range2Start, range2End) ||
                IsBetween(range2Start, range1Start, range1End) || IsBetween(range2End, range1Start, range1End);
    }

    public static boolean IsBetweenExclusive(LocalDateTime date, LocalDateTime startDate, LocalDateTime endDate) {
        return (date.isBefore(endDate) &&
                date.isAfter(startDate));
    }

    public static boolean IsCoincidentExclusive(LocalDateTime range1Start, LocalDateTime range1End,
                                       LocalDateTime range2Start, LocalDateTime range2End) {
        return IsBetweenExclusive(range1Start, range2Start, range2End) || IsBetweenExclusive(range1End, range2Start, range2End) ||
                IsBetweenExclusive(range2Start, range1Start, range1End) || IsBetweenExclusive(range2End, range1Start, range1End) ||
                range1Start.equals(range2Start) || range1End.equals(range2End);
    }
}
