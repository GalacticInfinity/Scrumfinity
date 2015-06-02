package seng302.group5.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Unit tests for estimate class (no constructors, getters or setters)
 * Created by Michael on 6/2/2015.
 */
public class EstimateTest {

  public Estimate estimate;
  public String label;
  public List<String> estimateNames;

  @Test
  public void testToString() {
    estimateNames = new ArrayList<>();
    estimateNames.add("Duck");
    estimateNames.add("Fish");
    estimate = new Estimate("Fibbo", estimateNames);

    assertEquals("Fibbo", estimate.toString());
  }

  @Test
  public void testCompareTo() throws Exception {
    Estimate before = new Estimate("aaa",new ArrayList<>());
    Estimate after = new Estimate("z",new ArrayList<>());
    Estimate middle = new Estimate("g",new ArrayList<>());
    Estimate same = new Estimate("g",new ArrayList<>());

    assertTrue(before.compareTo(middle) < 0);
    assertTrue(after.compareTo(middle) > 0);
    assertTrue(middle.compareTo(same) == 0);
  }
}
