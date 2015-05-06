package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Zander on 6/05/2015.
 */
public class StoryTest {

  private String storyLabel;
  private String storyLongName;
  private String storyDescription;
  private Person storyCreator;

  private Story story;

  @Before
  public void setUp() throws Exception {
    this.storyLabel = "Story";
    this.storyLongName = "An awesome story";
    this.storyDescription = "Once upon a time...";
    this.storyCreator = new Person("John", "John", "Doe", null);

    this.story = new Story(storyLabel, storyLongName, storyDescription, storyCreator);
  }

  @Test
  public void testToString() throws Exception {
    String result = story.toString();
    assertEquals(storyLabel, result);
  }
}
