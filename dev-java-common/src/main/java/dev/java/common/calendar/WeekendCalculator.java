/*
 *
 */
package dev.java.common.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class WeekendCalculator.
 */
public class WeekendCalculator {

	/**
	 * Instantiates a new weekend calculator.
	 */
	public WeekendCalculator() {

	}

	/**
	 * Calculate weekend map by year.
	 *
	 * @param year the year
	 * @return the map
	 */
	public Map<Calendar, String> calculateWeekendMapByYear(int year) {

		Map<Calendar, String> weekendMap = new TreeMap<>();

		int days = 365;

		if (isLeap(year)) {
			days++;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
		Calendar calendar = createDefaultCalendar(year, 1, 1);
		for (int i = 0; i < days; i++) {

			calendar.add(Calendar.DAY_OF_YEAR, 1);

			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				weekendMap.put((Calendar) calendar.clone(), formatter.format(calendar.getTime()));
			}

			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				weekendMap.put((Calendar) calendar.clone(), formatter.format(calendar.getTime()));
			}
		}

		return weekendMap;
	}

	/**
	 * Calculate weekend array by year.
	 *
	 * @param year the year
	 * @return the calendar[]
	 */
	public Calendar[] calculateWeekendArrayByYear(int year) {

		Map<Calendar, String> weekendMap = calculateWeekendMapByYear(year);

		Calendar[] weekendArray = new Calendar[0];
		weekendArray = weekendMap.keySet().toArray(weekendArray);

		return weekendArray;
	}

	/**
	 * Checks if is leap.
	 *
	 * @param year the year
	 * @return true, if is leap
	 */
	public boolean isLeap(int year) {
		return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
	}

	/**
	 * Creates the default calendar.
	 *
	 * @param anio the anio
	 * @param mes  the mes
	 * @param dia  the dia
	 * @return the calendar
	 */
	private Calendar createDefaultCalendar(int anio, int mes, int dia) {

		Calendar calendar = Calendar.getInstance();

		calendar.set(anio, mes - 1, dia);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

}
