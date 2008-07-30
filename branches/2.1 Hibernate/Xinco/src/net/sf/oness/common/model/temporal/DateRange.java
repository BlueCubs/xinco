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

import java.util.Arrays;
import java.util.Calendar;
import net.sf.oness.common.all.BaseObject;

/**
 * Range of dates
 * 
 * @author Carlos Sanchez
 * @version $Revision: 1.5 $
 */
public class DateRange extends BaseObject {

    private Date start,  end;
    public static DateRange EMPTY = new DateRange(new Date(1980,
            Calendar.JANUARY, 1, 0, 0, 0), new Date(1980, Calendar.JANUARY, 1,
            0, 0, 0));

    public DateRange() {
    }

    /**
     * Create a date range from start to end
     * 
     * @param start
     *            start date
     * @param end
     *            end date
     */
    public DateRange(Date start, Date end) {
        setStart(start);
        setEnd(end);
    }

    /**
     * Set the start date
     * 
     * @param start
     *            start date
     */
    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStart() {
        return start;
    }

    /**
     * Set the end date
     * 
     * @param end
     *            end date
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }

    public boolean isEmpty() {
        return getStart().equals(getEnd()) || getStart().after(getEnd());
    }

    public boolean isOpen() {
        return (getStart().equals(Date.MIN_VALUE)) || (getEnd().equals(Date.MAX_VALUE));
    }

    public boolean includes(Date arg) {
        return !arg.before(getStart()) && !arg.after(getEnd());
    }

    public void endNow() {
        setEnd(Date.now());
    }

    public static DateRange upTo(Date end) {
        return new DateRange(Date.MIN_VALUE, end);
    }

    public static DateRange startingOn(Date start) {
        return new DateRange(start, Date.MAX_VALUE);
    }

    public static DateRange startingNow() {
        return new DateRange(Date.now(), Date.MAX_VALUE);
    }

    public static DateRange startingAndEndingNow() {
        Date d = Date.now();
        return new DateRange(d, d);
    }

    public boolean overlaps(DateRange arg) {
        return arg.includes(getStart()) || arg.includes(getEnd()) || this.includes(arg);
    }

    public boolean includes(DateRange arg) {
        return this.includes(arg.getStart()) && this.includes(arg.getEnd());
    }

    public DateRange gap(DateRange arg) {
        if (this.overlaps(arg)) {
            return DateRange.EMPTY;
        }
        DateRange lower, higher;
        if (this.compareTo(arg) < 0) {
            lower = this;
            higher = arg;
        } else {
            lower = arg;
            higher = this;
        }
        Date startD = (Date) lower.getEnd().clone();
        Date endD = (Date) higher.getStart().clone();
        startD.addDays(1);
        endD.addDays(-1);
        return new DateRange(startD, endD);
    }

    public int compareTo(Object arg) {
        DateRange other = (DateRange) arg;
        if (!getStart().equals(other.getStart())) {
            return getStart().compareTo(other.getStart());
        }
        return getEnd().compareTo(other.getEnd());
    }

    public boolean abuts(DateRange arg) {
        return !this.overlaps(arg) && this.gap(arg).isEmpty();
    }

    public boolean partitionedBy(DateRange[] args) {
        if (!isContiguous(args)) {
            return false;
        }
        return this.equals(DateRange.combination(args));
    }

    public static DateRange combination(DateRange[] args) {
        Arrays.sort(args);
        if (!isContiguous(args)) {
            throw new IllegalArgumentException("Unable to combine date ranges");
        }
        return new DateRange(args[0].getStart(), args[args.length - 1].getEnd());
    }

    public static boolean isContiguous(DateRange[] args) {
        Arrays.sort(args);
        for (int i = 0; i < args.length - 1; i++) {
            if (!args[i].abuts(args[i + 1])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the length of this range in days.
     * 
     * @return The number of days in this range. Zero is returned if the dates
     *         are the same. Long.MAX_VALUE is returned if this is an open
     *         range.
     */
    public long lengthInDays() {
        if (isOpen()) {
            return Long.MAX_VALUE;
        }
        Calendar startD = getStart().getCalendar();
        Calendar endD = getEnd().getCalendar();
        int days = endD.get(Calendar.DAY_OF_YEAR) - startD.get(Calendar.DAY_OF_YEAR);
        int y2 = endD.get(Calendar.YEAR);
        if (startD.get(Calendar.YEAR) != y2) {
            startD = (Calendar) startD.clone();
            do {
                days += startD.getActualMaximum(Calendar.DAY_OF_YEAR);
                startD.add(Calendar.YEAR, 1);
            } while (startD.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * Calculates the length of this range in minutes.
     * 
     * @return The number of minutes in this range. Zero is returned if the
     *         dates are the same. Long.MAX_VALUE is returned if this is an open
     *         range.
     */
    public long lengthInMinutes() {
        return length(Calendar.MINUTE);
    }

    /**
     * Calculate length in milliseconds, seconds, minutes or hours, based in the
     * field parameter.
     * 
     * @param field
     *            One of Calendar.MILLISECOND, Calendar.SECOND, Calendar.MINUTE
     *            or Calendar.HOUR
     * @return the length. Long.MAX_VALUE is returned if this is an open range.
     */
    private long length(int field) {
        if (isOpen()) {
            return Long.MAX_VALUE;
        }
        long milliseconds = getEnd().getCalendar().getTimeInMillis() - getStart().getCalendar().getTimeInMillis();
        if (field == Calendar.MILLISECOND) {
            return milliseconds;
        }
        long seconds = milliseconds / 1000;
        if (field == Calendar.SECOND) {
            return seconds;
        }
        long minutes = seconds / 60;
        if (field == Calendar.MINUTE) {
            return minutes;
        }
        long hours = minutes / 60;
        if (field == Calendar.HOUR) {
            return hours;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return getStart() + " - " + getEnd();
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        Date s = (start == null) ? null : (Date) start.clone();
        Date e = (end == null) ? null : (Date) end.clone();
        return new DateRange(s, e);
    }
}