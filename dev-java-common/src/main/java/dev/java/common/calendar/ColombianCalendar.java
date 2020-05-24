/*
 *
 */
package dev.java.common.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class ColombianCalendar.
 */
public class ColombianCalendar {

    /**
     * The weekend calculator.
     */
    private final dev.java.common.calendar.WeekendCalculator weekendCalculator;

    /**
     * The holiday calcutator.
     */
    private final dev.java.common.calendar.HolidayCalcutator holidayCalcutator;

    /**
     * The formatter.
     */
    private final SimpleDateFormat formatter;

    /**
     * Instantiates a new colombian calendar.
     */
    public ColombianCalendar() {
        this.weekendCalculator = new dev.java.common.calendar.WeekendCalculator();
        this.holidayCalcutator = new dev.java.common.calendar.HolidayCalcutator();
        this.formatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * Contains calendar.
     *
     * @param calendarMap the calendar map
     * @param calendar    the calendar
     * @return true, if successful
     */
    private boolean containsCalendar(Map<Calendar, String> calendarMap, Calendar calendar) {

        boolean flag = false;

        String string00 = formatter.format(calendar.getTime());
        for (Calendar calendar2 : calendarMap.keySet()) {

            String string01 = formatter.format(calendar2.getTime());
            if (string00.equals(string01)) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    /**
     * Calculate business date.
     *
     * @param date the date
     * @param days the days
     * @return the calendar
     */
    public Calendar calculateBusinessDate(Calendar date, int days) {

        int year = date.get(Calendar.YEAR);
        Map<Calendar, String> notBusinessDay = new TreeMap<>();
        notBusinessDay.putAll(calculateNotBusinessDayMapByYear(year));
        notBusinessDay.putAll(calculateNotBusinessDayMapByYear(year + 1));

        int cont = 0;
        while (cont < days) {

            date.add(Calendar.DAY_OF_YEAR, 1);
            if (!containsCalendar(notBusinessDay, date)) {
                cont++;
            }
        }
        return date;
    }

    /**
     * Calculate business date difference.
     *
     * @param init the init
     * @param end  the end
     * @return the int
     */
    public int calculateBusinessDateDifference(Calendar init, Calendar end) {

        init.set(Calendar.HOUR, 0);
        init.set(Calendar.MINUTE, 0);
        init.set(Calendar.SECOND, 0);
        init.set(Calendar.MILLISECOND, 0);

        end.set(Calendar.HOUR, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        /**
         *
         */
        int yearInit = init.get(Calendar.YEAR);
        int yearEnd = end.get(Calendar.YEAR);

        Map<Calendar, String> notBusinessDay = new TreeMap<>();
        for (int year = yearInit; year <= yearEnd; year++) {
            notBusinessDay.putAll(calculateNotBusinessDayMapByYear(year));
        }

        int plazo = 0;
        while (init.compareTo(end) < 0) {

            init.add(Calendar.DAY_OF_YEAR, 1);
            if (!containsCalendar(notBusinessDay, init)) {
                plazo++;
            }
        }

        return plazo;
    }

    /**
     * Calculate not business day array by year.
     *
     * @param year the year
     * @return the calendar[]
     */
    public Calendar[] calculateNotBusinessDayArrayByYear(int year) {

        Map<Calendar, String> notBusinessDayMap = calculateNotBusinessDayMapByYear(year);

        Calendar[] notBusinessDayArray = new Calendar[0];
        notBusinessDayArray = notBusinessDayMap.keySet().toArray(notBusinessDayArray);

        return notBusinessDayArray;
    }

    /**
     * Calculate not business day map by year.
     *
     * @param year the year
     * @return the map
     */
    public Map<Calendar, String> calculateNotBusinessDayMapByYear(int year) {

        Map<Calendar, String> notBusinessDay = new TreeMap<>();

        /**
         *
         */
        Map<Calendar, String> weekendMap = weekendCalculator.calculateWeekendMapByYear(year);
        Map<Calendar, String> holidayMap = holidayCalcutator.calculateHolidayMapByYear(year);

        notBusinessDay.putAll(weekendMap);
        notBusinessDay.putAll(holidayMap);

        return notBusinessDay;
    }

    /**
     * Calculate weekend map by year.
     *
     * @param year the year
     * @return the map
     */
    public Map<Calendar, String> calculateWeekendMapByYear(int year) {
        return weekendCalculator.calculateWeekendMapByYear(year);
    }

    /**
     * Calculate weekend array by year.
     *
     * @param year the year
     * @return the calendar[]
     */
    public Calendar[] calculateWeekendArrayByYear(int year) {
        return weekendCalculator.calculateWeekendArrayByYear(year);
    }

    /**
     * Calculate holiday map by year.
     *
     * @param year the year
     * @return the map
     */
    public Map<Calendar, String> calculateHolidayMapByYear(int year) {
        return holidayCalcutator.calculateHolidayMapByYear(year);
    }

    /**
     * Calculate holiday array by year.
     *
     * @param year the year
     * @return the calendar[]
     */
    public Calendar[] calculateHolidayArrayByYear(int year) {
        return holidayCalcutator.calculateHolidayArrayByYear(year);
    }
}
