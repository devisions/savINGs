package org.devisions.labs.savings.vx.commons;

import java.time.Duration;


/**
 * Utility methods related to date and time.
 *
 * @author devisions
 */
public class DateTimeUtils {

  /** Get a human readable (printer friendly) format of a {@link Duration} value. */
  public static String humanReadableDuration(Duration duration) {

    return duration.toString()
      .substring(2)
      .replaceAll("(\\d[HMS])(?!$)", "$1 ")
      .toLowerCase();
  }

}
