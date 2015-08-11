package seng302.group5.model.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests to ensure the time formatting parser works correctly
 */
public class TimeFormatTest {

  @Test
  public void testEmpty() {
    String inputString = "";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(0, result);
  }

  @Test
  public void testValidHoursMinutes() {
    String inputString = "1h30m";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(90, result);
  }

  @Test
  public void testValidLongHoursMinutes() {
    String inputString = "100h30m";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(6030, result);
  }

  @Test
  public void testValidHours() {
    String inputString = "2h";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(120, result);
  }

  @Test
  public void testValidLongHours() {
    String inputString = "150h";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(9000, result);
  }

  @Test
  public void testValidMinutes() {
    String inputString = "59m";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(59, result);
  }

  @Test
  public void testValidLongMinutes() {
    String inputString = "9001m";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(9001, result);
  }

  @Test
  public void testRandomString() {
    String inputString = "Hello World";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(-1, result);
  }

  @Test
  public void testSingleCharH() {
    String inputString = "h";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(-1, result);
  }

  @Test
  public void testSingleCharM() {
    String inputString = "m";
    int result = TimeFormat.parseTime(inputString);
    assertEquals(-1, result);
  }
}
