package chenyunlong.zhangli.utils;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Date utilities.
 *
 * @author johnniang
 * @date 3/18/19
 */
public class DateUtils {

    private DateUtils() {
    }

    /**
     * Gets current date.
     *
     * @return current date
     */
    @NonNull
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Converts from date into a calendar instance.
     *
     * @param date date instance must not be null
     * @return calendar instance
     */
    @NonNull
    public static Calendar convertTo(@NonNull Date date) {
        Assert.notNull(date, "Date must not be null");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Adds dateTime.
     *
     * @param dateTime current dateTime must not be null
     * @param time     time must not be less than 1
     * @param timeUnit time unit must not be null
     * @return added dateTime
     */
    public static LocalDateTime add(@NonNull LocalDateTime dateTime, long time, @NonNull TimeUnit timeUnit) {
        Assert.notNull(dateTime, "Date must not be null");
        Assert.isTrue(time >= 0, "Addition time must not be less than 1");
        Assert.notNull(timeUnit, "Time unit must not be null");

        LocalDateTime result;

        int timeIntValue;

        if (time > Integer.MAX_VALUE) {
            timeIntValue = Integer.MAX_VALUE;
        } else {
            timeIntValue = Long.valueOf(time).intValue();
        }

        // Calc the expiry time
        switch (timeUnit) {
            case DAYS:
                result = dateTime.plusDays(timeIntValue);
                break;
            case HOURS:
                result = dateTime.plusHours(timeIntValue);
                break;
            case MINUTES:
                result = dateTime.plusMinutes(timeIntValue);
                break;
            case SECONDS:
                result = dateTime.plusSeconds(timeIntValue);
                break;
            case MILLISECONDS:
                result = dateTime.plus(timeIntValue, ChronoUnit.MILLIS);
                break;
            default:
                result = dateTime;
        }
        return result;
    }
}
