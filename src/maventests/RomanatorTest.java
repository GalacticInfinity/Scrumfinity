package maventests;

import org.junit.Test;

import testpackage.Romanator;

import static org.junit.Assert.*;

public class RomanatorTest {

  @Test
  public void testingTest() {
    Romanator meow = new Romanator(10, 5);
    assertEquals(meow.returnNum1(), 10);
    assertEquals(meow.returnNum2(), 5);
  }
}