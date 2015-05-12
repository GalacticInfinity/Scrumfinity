package seng302.group5.model;

/**
 * Story model. So that managers can keep track of the things people need to do, a way to record
 * work items as user stories. For now, it just have the most basic detail but we’ll add more
 * in subsequent stories.
 *
 * Created by Zander on 5/05/2015.
 */
public class Story implements AgileItem, Comparable<Story> {

  private String label;
  private String storyName;
  private String description;
  private Person creator;

  /**
   * Empty constructor used for save/load.
   */
  public Story() {
    this.label = "";
    this.storyName = "";
    this.description = "";
    this.creator = null;
  }

  /**
   * Constructor for all fields.
   * @param label Not-null ID/label.
   * @param storyName Long name for story.
   * @param description Description of story.
   * @param creator Person assigned to creation of this story.
   */
  public Story(String label, String storyName, String description, Person creator) {
    this.label = label;
    this.storyName = storyName;
    this.description = description;
    this.creator = creator;
  }

  /**
   * Constructor to create a clone of existing story.
   *
   * @param clone Story to clone.
   */
  public Story(Story clone) {
    this.label = clone.getLabel();
    this.storyName = clone.getStoryName();
    this.description = clone.getDescription();
    this.creator = clone.getCreator();
  }

  /**
   * Gets label of story.
   * @return Label of story.
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Sets label of story.
   * @param label New label as String type.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Gets long name of story.
   * @return Long name of story.
   */
  public String getStoryName() {
    return this.storyName;
  }

  /**
   * Sets long name of story.
   * @param storyName New long name as String type.
   */
  public void setStoryName(String storyName) {
    this.storyName = storyName;
  }

  /**
   * Gets description of story.
   * @return Description of story.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets description of story.
   * @param description Description of story as String type.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets Person assigned as creator of story.
   * @return Person who created story.
   */
  public Person getCreator() {
    return this.creator;
  }

  /**
   * Sets Person assigned as creator of story.
   * @param creator Person as Person type.
   */
  public void setCreator(Person creator) {
    this.creator = creator;
  }

  /**
   * Copies the story input fields into current object.
   * @param agileItem Story that's fields are to be copied.
   */
  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Story) {
      Story clone = (Story) agileItem;
      this.label = clone.getLabel();
      this.storyName = clone.getStoryName();
      this.description = clone.getDescription();
      this.creator = clone.getCreator();
    }
  }

  /**
   * Overrides the toString method with story label.
   * @return Label of story.
   */
  @Override
  public String toString() {
    return this.label;
  }

  /**
   * Checks if two stories labels are equal.
   * @param obj Object to compare to.
   * @return true if both story labels are equal.
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj instanceof Story) {
      Story story = (Story) obj;
      result = this.label.equals(story.getLabel());
    }

    return result;
  }

  /**
   * Compare this story label to story o's label
   * @param o story you wish to compare to
   * @return whether it is greater or lesser.
   */
  @Override
  public int compareTo(Story o) {
    return this.label.toLowerCase().compareTo(o.label.toLowerCase());
  }
}
