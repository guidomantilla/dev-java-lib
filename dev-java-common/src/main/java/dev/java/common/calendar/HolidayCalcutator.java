/*
 *
 */
package dev.java.common.calendar;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class HolidayCalcutator.
 */
public class HolidayCalcutator {

	/**
	 * Instantiates a new holiday calcutator.
	 */
	public HolidayCalcutator() {

	}

	/**
	 * Calculate holiday map by year.
	 *
	 * @param year the year
	 * @return the map
	 */
	public Map<Calendar, String> calculateHolidayMapByYear(int year) {

		Map<Calendar, String> holidayMap = new TreeMap<>();

		Calendar calendar = createDefaultCalendar(year, 1, 1);
		holidayMap.put(calendar, "Dia de Año Nuevo");

		calendar = createDefaultCalendar(year, 5, 1);
		holidayMap.put(calendar, "Dia del Trabajo");

		calendar = createDefaultCalendar(year, 7, 20);
		holidayMap.put(calendar, "Dia de la Independencia");

		calendar = createDefaultCalendar(year, 8, 7);
		holidayMap.put(calendar, "Dia de la Batalla de Boyaca");

		calendar = createDefaultCalendar(year, 12, 8);
		holidayMap.put(calendar, "Dia de la Inmaculada Concepcion");

		calendar = createDefaultCalendar(year, 12, 25);
		holidayMap.put(calendar, "Dia de Navidad");

		calendar = calculateEmilianiCalendar(year, 1, 6);
		holidayMap.put(calendar, "Dia de los Reyes Magos Navidad");

		calendar = calculateEmilianiCalendar(year, 3, 19);
		holidayMap.put(calendar, "Dia de San Jose");

		calendar = calculateEmilianiCalendar(year, 6, 29);
		holidayMap.put(calendar, "Dia de San Pedro y San Pablo");

		calendar = calculateEmilianiCalendar(year, 8, 15);
		holidayMap.put(calendar, "Dia de Asuncion de la Virgen");

		calendar = calculateEmilianiCalendar(year, 10, 12);
		holidayMap.put(calendar, "Dia del Descubrimiento de America");

		calendar = calculateEmilianiCalendar(year, 11, 1);
		holidayMap.put(calendar, "Dia de Todos los Santos");

		calendar = calculateEmilianiCalendar(year, 11, 11);
		holidayMap.put(calendar, "Dia de la Independencia de Cartagena");

		// Calendar domingoResureccion = calculateComputusCalendar(anio);
		calendar = calculateComputusBasedCalendar(year, -3);
		holidayMap.put(calendar, "Jueves Santo");

		calendar = calculateComputusBasedCalendar(year, -2);
		holidayMap.put(calendar, "Viernes Santo");

		calendar = calculateEmilianiComputusBasedCalendar(year, 39);
		holidayMap.put(calendar, "Dia de la Ascencion del Señor");

		calendar = calculateEmilianiComputusBasedCalendar(year, 60);
		holidayMap.put(calendar, "Corpus Cristi");

		calendar = calculateEmilianiComputusBasedCalendar(year, 68);
		holidayMap.put(calendar, "Dia del Sagrado Corazon");

		return holidayMap;
	}

	/**
	 * Calculate holiday array by year.
	 *
	 * @param year the year
	 * @return the calendar[]
	 */
	public Calendar[] calculateHolidayArrayByYear(int year) {

		Map<Calendar, String> holidayMap = calculateHolidayMapByYear(year);

		Calendar[] holidayArray = new Calendar[0];
		holidayArray = holidayMap.keySet().toArray(holidayArray);

		return holidayArray;
	}

	/**
	 * Calculate emiliani computus based calendar.
	 *
	 * @param anio the anio
	 * @param days the days
	 * @return the calendar
	 */
	private Calendar calculateEmilianiComputusBasedCalendar(int anio, int days) {

		Calendar calendar = calculateComputusCalendar(anio);

		calendar.add(Calendar.DAY_OF_YEAR, days);

		int mes = calendar.get(Calendar.MONTH) + 1;
		int dia = calendar.get(Calendar.DAY_OF_MONTH);

		calendar = calculateEmilianiCalendar(anio, mes, dia);

		return calendar;
	}

	/**
	 * Calculate computus based calendar.
	 *
	 * @param anio the anio
	 * @param days the days
	 * @return the calendar
	 */
	private Calendar calculateComputusBasedCalendar(int anio, int days) {

		Calendar calendar = calculateComputusCalendar(anio);

		calendar.add(Calendar.DAY_OF_YEAR, days);

		return calendar;
	}

	/**
	 * Calculate computus calendar.
	 *
	 * @param anio the anio
	 * @return the calendar
	 */
	private Calendar calculateComputusCalendar(int anio) {

		int a = anio % 19;
		int b = (int) Math.floor(anio / 100);
		int c = anio % 100;

		int d = (int) Math.floor(b / 4);
		int e = b % 4;

		int f = (int) Math.floor((b + 8) / 25);
		int g = (int) Math.floor((b - f + 1) / 3);

		int h = (19 * a + b - d - g + 15) % 30;

		int i = (int) Math.floor(c / 4);
		int k = c % 4;

		int l = (32 + 2 * e + 2 * i - h - k) % 7;

		int m = (int) Math.floor((a + 11 * h + 22 * l) / 451);

		int n = h + l - 7 * m + 114;

		int mes = n / 31;
		int dia = 1 + n % 31;

		Calendar calendar = createDefaultCalendar(anio, mes, dia);

		return calendar;
	}

	/**
	 * Calculate emiliani calendar.
	 *
	 * @param anio the anio
	 * @param mes  the mes
	 * @param dia  the dia
	 * @return the calendar
	 */
	private Calendar calculateEmilianiCalendar(int anio, int mes, int dia) {

		Calendar calendar = createDefaultCalendar(anio, mes, dia);

		int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		if (diaSemana != Calendar.MONDAY) {
			int dias = (Calendar.SATURDAY - diaSemana + 2) % 7;
			calendar.add(Calendar.DAY_OF_YEAR, dias);
		}

		return calendar;
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
