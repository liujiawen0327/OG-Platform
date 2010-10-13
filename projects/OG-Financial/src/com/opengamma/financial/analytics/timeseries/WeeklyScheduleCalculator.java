/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.timeseries;

import java.util.ArrayList;
import java.util.List;

import javax.time.calendar.LocalDate;

import org.apache.commons.lang.Validate;

import com.opengamma.financial.convention.calendar.Calendar;

/**
 * 
 */
public class WeeklyScheduleCalculator extends Schedule {

  @Override
  public LocalDate[] getScheduleWorkingDaysOnly(final LocalDate startDate, final LocalDate endDate, final boolean fromEnd, final Calendar holidayCalendar) {
    Validate.notNull(startDate, "start date");
    Validate.notNull(endDate, "end date");
    Validate.isTrue(startDate.isBefore(endDate) || startDate.equals(endDate));
    final List<LocalDate> dates = new ArrayList<LocalDate>();
    if (fromEnd) {
      LocalDate date = endDate;
      while (!date.isBefore(startDate)) {
        if (holidayCalendar.isWorkingDay(date)) {
          dates.add(date);
        }
        date = date.minusDays(7);
      }
      return getReversedDates(dates);
    }
    LocalDate date = startDate;
    while (!date.isAfter(endDate)) {
      if (holidayCalendar.isWorkingDay(date)) {
        dates.add(date);
      }
      date = date.plusDays(7);
    }
    return dates.toArray(EMPTY_ARRAY);
  }
}
