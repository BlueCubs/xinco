package com.bluecubs.xinco.core;

/**
 * Data status enum.
 *
 * <p>WARNING: DB column {@code status_number} uses values 1–5 (not enum ordinals 0–4). The mapping
 * is:
 *
 * <ul>
 *   <li>1 = {@link #OPEN}
 *   <li>2 = {@link #LOCKED}
 *   <li>3 = {@link #ARCHIVED}
 *   <li>4 = {@link #CHECKED_OUT}
 *   <li>5 = {@link #PUBLISHED}
 * </ul>
 *
 * Always use {@link #toDbValue()} / {@link #fromDbValue(int)} when reading or writing {@code
 * status_number} — never {@code ordinal()} directly.
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public enum XincoDataStatus {
  OPEN,
  LOCKED,
  ARCHIVED,
  CHECKED_OUT,
  PUBLISHED;

  /**
   * @return the DB column value for this status ({@code ordinal() + 1})
   */
  public int toDbValue() {
    return ordinal() + 1;
  }

  /**
   * @param dbValue DB column value (1–5)
   * @return the corresponding enum constant
   * @throws IllegalArgumentException if dbValue is outside 1–5
   */
  public static XincoDataStatus fromDbValue(int dbValue) {
    return values()[dbValue - 1];
  }
}
