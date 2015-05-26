package seng302.group5.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model's a backlog containing stories
 */
public class Backlog implements AgileItem, Comparable<Backlog> {

  private String label;
  private String backlogName;
  private String description;
  private Person productOwner;
  private List<Story> stories;
//  private Estimate estimateType // TODO:
//  private Map<Story, Estimate> // TODO Is it estimate or String?

  /**
   * Default constructor.
   */
  public Backlog() {
    this.label = "";
    this.backlogName = "";
    this.description = "";
    this.productOwner = null;
    this.stories = new ArrayList<>();
  }

  /**
   * Constructor for backlogs.
   *
   * @param label Label of backlog
   * @param backlogName Full name of backlog
   * @param description Description of backlog
   * @param productOwner Product owner of backlog
   */
  public Backlog(String label, String backlogName, String description, Person productOwner) {
    this.label = label;
    this.backlogName = backlogName;
    this.description = description;
    this.productOwner = productOwner;
    this.stories = new ArrayList<>();
  }

  /**
   * Constructor to create a clone of an existing backlog
   *
   * @param clone Backlog to clone
   */
  public Backlog(Backlog clone) {
    this.label = clone.getLabel();
    this.backlogName = clone.getBacklogName();
    this.description = clone.getDescription();
    this.productOwner = clone.getProductOwner();
    this.stories = new ArrayList<>();
    this.stories.addAll(clone.getStories());
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
  }

  public String getBacklogName() {
    return backlogName;
  }

  public void setBacklogName(String backlogName) {
    this.backlogName = backlogName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Person getProductOwner() {
    return productOwner;
  }

  public void setProductOwner(Person productOwner) {
    this.productOwner = productOwner;
  }

  public List<Story> getStories() {
    return Collections.unmodifiableList(stories);
  }

//  TODO: make it work. will it be Estimate or String?
//  /**
//   * Add a story to the backlog and assign an estimate
//   *
//   * @param story Story to add
//   * @param estimate Estimate to assign
//   */
//  public void addStory(Story story, Estimate estimate) {
//    this.teamMembers.add(person);
//    this.membersRole.put(person, role);
//  }

  /**
   * Add a story to the backlog with the default estimate.
   *
   * @param story Story to add
   */
  public void addStory(Story story) {
    stories.add(story);
  }

  /**
   * Remove a story from the backlog.
   *
   * @param story Story to remove
   */
  public void removeStory(Story story) {
    stories.remove(story);
  }

  /**
   * Copies the input backlog's fields into current object.
   *
   * @param agileItem The AgileItem object to copy values from
   */
  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Backlog) {
      Backlog clone = (Backlog) agileItem;
      this.label = clone.getLabel();
      this.backlogName = clone.getBacklogName();
      this.description = clone.getDescription();
      this.productOwner = clone.getProductOwner();
      this.stories.clear();
      this.stories.addAll(clone.getStories());
    }
  }

  /**
   * Overrides to toString method with the label of backlog.
   *
   * @return Unique label of team.
   */
  @Override
  public String toString() {
    return label;
  }

  /**
   * Check if two backlog objects are equal by checking all fields
   *
   * @param obj Backlog to compare to
   * @return Whether backlogs are equal
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Backlog backlog = (Backlog) obj;

    if (!label.equals(backlog.label)) {
      return false;
    }
    if (!backlogName.equals(backlog.backlogName)) {
      return false;
    }
    if (!description.equals(backlog.description)) {
      return false;
    }
    if (productOwner != null ? !productOwner.equals(backlog.productOwner)
                             : backlog.productOwner != null) {
      return false;
    }
    return stories.equals(backlog.stories);

  }

  /**
   * Return the hashcode for the backlog.
   *
   * @return The hashcode
   */
  @Override
  public int hashCode() {
    int result = label.hashCode();
    result = 31 * result + backlogName.hashCode();
    result = 31 * result + description.hashCode();
    result = 31 * result + (productOwner != null ? productOwner.hashCode() : 0);
    result = 31 * result + stories.hashCode();
    return result;
  }

  /**
   * Compares the backlog labels.
   *
   * @param o the backlog you wish to compare to.
   * @return return whether its greater or lesser.
   */
  @Override
  public int compareTo(Backlog o) {
    return this.label.toLowerCase().compareTo(o.label.toLowerCase());
  }
}
