package seng302.group5.model.util;

import java.time.LocalTime;

/**
 * Class for managing the time formatting in Scrumfinity, i.e. [x]h[y]m where x and y are integers.
 *
 * @author Su-Shing Chen, Alex Woo
 */
public class TimeFormat {

  /**
   * Get the number of minutes from a string that follows the Scrumfinity time format. Ensures
   * that string follows the specified format and converts any hours into minutes.
   *
   * @param inputString The time formatted string to parse.
   * @return The time in minutes, -1 if input is invalid.
   */
  public static int parseMinutes(String inputString) {
    String timeRegex = "([0-9]+h)?([0-9]+m)?";
    inputString = inputString.trim();
    int hours = 0;
    int minutes = 0;
    int result = -1;
    if (inputString.matches(timeRegex)) {
      char[] charArray = inputString.toCharArray();
      String accumulator = "";
      for (char c : charArray) {
        if (c == 'h') {
          hours = Integer.parseInt(accumulator);
          accumulator = "";
        } else if (c == 'm') {
          minutes = Integer.parseInt(accumulator);
        } else {
          accumulator += c;
        }
      }
      result = hours * 60 + minutes;
    }

    return result;
  }

  /**
   * Get the time formatted string from an integer number of minutes. For zero it will return
   * 0m and for negative numbers it will be an empty string.
   *
   * @param totalMinutes total minutes to parse into h/m
   * @return Time formatted string.
   */
  public static String parseDuration(int totalMinutes) {
    int hours = totalMinutes / 60;
    int minutes = totalMinutes % 60;
    String result = "";
    if (totalMinutes == 0) {
      result = "0m";
    } else {
      if (hours > 0) {
        result += hours + "h";
      }
      if (minutes > 0) {
        result += minutes + "m";
      }
    }
    return result;
  }

  public static LocalTime parseLocalTime(String inputString) {
    return null;
  }

  public static String parseTimeString(LocalTime localTime) {
    return "";
  }
}
