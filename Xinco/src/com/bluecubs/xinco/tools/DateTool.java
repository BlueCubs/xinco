package com.bluecubs.xinco.tools;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class DateTool {

    public static long getDifference(Calendar a, Calendar b, TimeUnit units) {
        return units.convert(b.getTimeInMillis() - a.getTimeInMillis(), MILLISECONDS);
    }
}
