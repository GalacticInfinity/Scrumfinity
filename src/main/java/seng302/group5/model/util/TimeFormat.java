package seng302.group5.model.util;

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
  public static int parseTime(String inputString) {
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
}
