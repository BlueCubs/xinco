/*
 * Copyright 2004 Carlos Sanchez.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.oness.common.model.temporal;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.sf.oness.common.all.BaseObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.DateUtils;

/**
 * <p>
 * Wrapper around a <code>Calendar</code> object truncated to the field
 * specified (by default SECOND) using DateUtils.truncate(Calendar, int).
 * </p>
 * <p>
 * The MIN_VALUE and MAX_VALUE fields represent negative and positive infinity
 * respectively, the implementation doesn't use Calendar.getMinimum or
 * Calendar.getMaximum because the underlying database may not accept the range
 * of values available in Calendar
 * </p>
 * 
 * @see java.util.Calendar
 * @see org.apache.commons.lang.time.DateUtils#truncate(Calendar,int)
 * 
 * @author Carlos Sanchez
 * @version $Revision: 1.6 $
 */
public class Date extends BaseObject implements Comparable {

    private static final DateFormat formatter = DateFormat.getDateTimeInstance();
    /**
     * The min value (actually 1000-JAN-01 00:00:00)
     */
    public static final Date MIN_VALUE = new Date(1000, Calendar.JANUARY, 1, 0,
            0, 0);
    /**
     * The max value (actually 9999-DEC-31 23:59:59)
     */
    public static final Date MAX_VALUE = new Date(9999, Calendar.DECEMBER, 31,
            23, 59, 59);
    private final static int DEFAULT_TRUNCATE_TO_FIELD = Calendar.SECOND;
    private int truncateToField = DEFAULT_TRUNCATE_TO_FIELD;
    /**
     * Wrapped calendar
     */
    protected Calendar calendar;

    /**
     * Create Date not initialized.
     */
    public Date() {
    }

    /**
     * Create a Date from year, month, day, hour, minute, second
     * 
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     */
    public Date(int year, int month, int day, int hour, int minute, int second) {
        calendar = new GregorianCalendar(year, month, day, hour, minute, second);
        this.truncateToField = Calendar.SECOND;
    }

    /**
     * Create a Date from year, month, day
     * 
     * @param year
     * @param month
     * @param day
     */
    public Date(int year, int month, int day) {
        calendar = new GregorianCalendar(year, month, day);
        this.truncateToField = Calendar.DAY_OF_MONTH;
    }

    /**
     * Create a date from a Calendar truncated to the field specified leaving it
     * as the most significant. <br/>Future changes to the calendar will not
     * affect the newly created object
     * 
     * @param calendar
     * @param truncateToField
     */
    public Date(Calendar calendar, int truncateToField) {
        this.calendar = DateUtils.truncate(calendar, truncateToField);
        this.truncateToField = truncateToField;

    }

    /**
     * Create a date from a Calendar truncated to the default field
     * {@link DEFAULT_TRUNCATE_TO_FIELD}leaving it as the most significant.
     * <br/>Future changes to the calendar will not affect the newly created
     * object
     * 
     * @param calendar
     */
    public Date(Calendar calendar) {
        this.calendar = DateUtils.truncate(calendar, DEFAULT_TRUNCATE_TO_FIELD);
    }

    /**
     * Create a Date for the current time and truncated to the default field
     * {@link DEFAULT_TRUNCATE_TO_FIELD}leaving it as the most significant.
     */
    public static Date now() {
        return new Date(DateUtils.truncate(Calendar.getInstance(),
                DEFAULT_TRUNCATE_TO_FIELD));
    }

    /**
     * Get the <code>Calendar<code> wrapped by this object.
     * Changes to the Calendar will affect this date.
     * @return the calendar
     */
    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * Set the <code>Calendar<code> wrapped by this object.
     * Changes to the Calendar will affect this date.
     * @param calendar
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * @see Calendar#after(java.lang.Object)
     * @param when
     * @return
     */
    public boolean after(Date when) {
        return calendar.after(when.calendar);
    }

    /**
     * @see Calendar#before(java.lang.Object)
     * @param when
     * @return
     */
    public boolean before(Date when) {
        return calendar.before(when.calendar);
    }

    /**
     * @see java.util.Date#compareTo(java.lang.Object)
     * @param arg
     * @return
     */
    public int compareTo(Object arg) {
        Date other = (Date) arg;
        return getTime().compareTo(other.getTime());
    }

    /**
     * Get this Time as a <code>java.util.Date<code> object.
     * @see Calendar#getTime()
     * @return
     */
    public java.util.Date getTime() {
        return calendar.getTime();
    }

    public void addDays(int arg) {
        calendar.add(Calendar.DAY_OF_MONTH, arg);
    }

    public void addYears(int arg) {
        calendar.add(Calendar.YEAR, arg);
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public String toString() {
        if (this.equals(MIN_VALUE)) {
            return "-infinity";
        }
        if (this.equals(MAX_VALUE)) {
            return "+infinity";
        }
        return formatter.format(getTime());
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Date)) {
            return false;
        }
        Date rhs = (Date) o;
        return new EqualsBuilder().append(calendar, rhs.calendar).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 41).append(calendar).toHashCode();
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        return new Date((Calendar) calendar.clone());
    }
}