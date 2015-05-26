package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Zander on 6/05/2015.
 */
public class StoryTest {

  private String storyLabel;
  private String storyLongName;
  private String storyDescription;
  private Person storyCreator;
  private List<String> storyAC;

  private Story story;

  @Before
  public void setUp() throws Exception {
    this.storyLabel = "Story";
    this.storyLongName = "An awesome story";
    this.storyDescription = "Once upon a time...";
    this.storyCreator = new Person("John", "John", "Doe", null);
    this.storyAC = new ArrayList<String>();

    this.story = new Story(storyLabel, storyLongName, storyDescription, storyCreator, storyAC);
  }

  @Test
  public void testToString() throws Exception {
    String result = story.toString();
    assertEquals(storyLabel, result);
  }
}
