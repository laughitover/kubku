package com.laughitover.kubku.commonUtils;

import com.google.common.base.Preconditions;
import com.laughitover.kubku.model.Duration;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期相关方法
 */
public class DateUtils {

    public static final long ONE_DAY = 1000 * 60 * 60 * 24;
    public static final int TIMEZONE_OFFSET = TimeZone.getDefault().getRawOffset();

    private static final ThreadLocal<SimpleDateFormat> DAY_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 从 1970-1-1 开始到参数表示的时间为止的天数
     */
    public static int daysFromEpoch(long time) {
        return (int) ((time + TIMEZONE_OFFSET) / ONE_DAY);
    }

    /**
     * 从 1970-1-1 开始到参数表示的时间为止的天数
     */
    public static int daysFromEpoch(Date dt) {
        Preconditions.checkNotNull(dt, "null date!");
        return daysFromEpoch(dt.getTime());
    }

    /**
     * yyyy-MM-dd HH:mm:ss 方式格式化时间
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.get().format(date);
    }

    public static String formatDay(Date date) {
        if (date == null) {
            return "";
        }
        return DAY_FORMAT.get().format(date);
    }

    /**
     * 判断日期格式是否为yyyy-MM-dd
     * @param str
     * @return
     */
    public static boolean validDay(String str){
        try{
            Date date = DAY_FORMAT.get().parse(str);
            return str.equals(DAY_FORMAT.get().format(date));
        }catch(Exception e){
            return false;
        }
    }

    public static int minuteOfDay(Date dt) {
        Preconditions.checkNotNull(dt, "null date!");
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
    }

    public static int daysDifference(Date from, Date to) {
        return daysFromEpoch(to) - daysFromEpoch(from);
    }

    public static boolean isSameDate(Date date1, Date date2) {
        return daysDifference(date1, date2) == 0;
    }

    /**
     * 获取到期日期
     * @param begin    开始时间
     * @param duration 期限
     */
    public static Date dayAfter(Date begin, Duration duration) {
        Calendar c = Calendar.getInstance();
        c.setTime(begin);
        if (duration.getDays() != null) {
            c.add(Calendar.DATE, duration.getDays());
        }
        if (duration.getMonths() != null) {
            c.add(Calendar.MONTH, duration.getMonths());
        }
        return c.getTime();
    }

    public static boolean isMinuteAfter(Date from, Date to) {
        return minuteOfDay(from) > minuteOfDay(to);
    }

    public static Date getZeroTimeInDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 字符串转化为日期
     */
    public static Date parse(final String str, final String pattern) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串转化为日期
     */
    public static Date parseDay(final String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            return DAY_FORMAT.get().parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDefaultDate() {
        Date defaultDate;
        try {
            defaultDate = new SimpleDateFormat("yyyy-MM-dd").parse("0001-01-01");
        } catch (ParseException e) {
            throw new RuntimeException();
        }
        return defaultDate;
    }
}
