package seng302.group5.model.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests to ensure the time formatting parser works correctly
 */
public class TimeFormatTest {

  @Test
  public void testEmptyString() {
    String inputString = "";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(0, result);
  }

  @Test
  public void testValidHoursMinutesString() {
    String inputString = "1h30m";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(90, result);
  }

  @Test
  public void testValidLongHoursMinutesString() {
    String inputString = "100h30m";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(6030, result);
  }

  @Test
  public void testValidHoursString() {
    String inputString = "2h";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(120, result);
  }

  @Test
  public void testValidLongHoursString() {
    String inputString = "150h";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(9000, result);
  }

  @Test
  public void testValidMinutesString() {
    String inputString = "59m";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(59, result);
  }

  @Test
  public void testValidLongMinutesString() {
    String inputString = "9001m";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(9001, result);
  }

  @Test
  public void testRandomString() {
    String inputString = "Hello World";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(-1, result);
  }

  @Test
  public void testSingleCharHString() {
    String inputString = "h";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(-1, result);
  }

  @Test
  public void testSingleCharMString() {
    String inputString = "m";
    int result = TimeFormat.parseMinutes(inputString);
    assertEquals(-1, result);
  }

  @Test
  public void testValidHoursMinutesInt() {
    int inputInt = 1000;
    String result = TimeFormat.parseTime(inputInt);
    assertEquals("16h40m", result);
  }

  @Test
  public void testValidHoursInt() {
    int inputInt = 960;
    String result = TimeFormat.parseTime(inputInt);
    assertEquals("16h", result);
  }

  @Test
  public void testValidMinutesInt() {
    int inputInt = 38;
    String result = TimeFormat.parseTime(inputInt);
    assertEquals("38m", result);
  }

  @Test
  public void testZeroMinutesInt() {
    int inputInt = 0;
    String result = TimeFormat.parseTime(inputInt);
    assertEquals("0m", result);
  }

  @Test
  public void testInvalidMinutesInt() {
    int inputInt = -1;
    String result = TimeFormat.parseTime(inputInt);
    assertEquals("", result);
  }
}
