package seng302.group5.model;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

/**
 * Story model. So that managers can keep track of the things people need to do, a way to record
 * work items as user stories. For now, it just have the most basic detail but we’ll add more
 * in subsequent stories.
 * Contains the information about a single story, which includes the label, name,
 * a description, who the creator is, dependencies, and acceptance criteria.
 *
 * Created by Zander on 5/05/2015.
 */
public class Story implements AgileItem, Comparable<Story> {

  private String label;
  private String storyName;
  private String description;
  private Person creator;
  private List<Story> dependencies;
  private boolean isReady = false;
  private ObservableList<String> acceptanceCriteria;

  /**
   * Empty constructor used for save/load.
   */
  public Story() {
    this.label = "";
    this.storyName = "";
    this.description = "";
    this.creator = null;
    this.acceptanceCriteria = FXCollections.observableArrayList();
    this.dependencies = new ArrayList<>();
  }

  /**
   * Constructor for label, storyName, description, creator
   * @param label Not-null ID/label.
   * @param storyName Long name for story.
   * @param description Description of story.
   * @param creator Person assigned to creation of this story..
   */
  public Story(String label, String storyName, String description, Person creator) {
    this.label = label;
    this.storyName = storyName;
    this.description = description;
    this.creator = creator;
    this.acceptanceCriteria = FXCollections.observableArrayList();
    this.dependencies = new ArrayList<>();
  }

  /**
   * Constructor for label, storyName, description, creator, acceptanceCriteria.
   * @param label Not-null ID/label.
   * @param storyName Long name for story.
   * @param description Description of story.
   * @param creator Person assigned to creation of this story..
   * @param acceptanceCriteria The criteria for the story to be considered done.
   */
  public Story(String label, String storyName, String description, Person creator,
                 ObservableList<String> acceptanceCriteria) {
    this.label = label;
    this.storyName = storyName;
    this.description = description;
    this.creator = creator;
    if (acceptanceCriteria == null) {
      this.acceptanceCriteria = FXCollections.observableArrayList();
    } else {
      this.acceptanceCriteria = acceptanceCriteria;
    }
    this.dependencies = new ArrayList<>();
  }

  /**
   * Consructor for all fields.
   * @param label
   * @param storyName
   * @param description
   * @param creator
   * @param acceptanceCriteria
   * @param dependencies
   */
  public Story(String label, String storyName, String description, Person creator,
               ObservableList<String> acceptanceCriteria, List<Story> dependencies) {
    this.label = label;
    this.storyName = storyName;
    this.description = description;
    this.creator = creator;
    if (acceptanceCriteria == null) {
      this.acceptanceCriteria = FXCollections.observableArrayList();
    } else {
      this.acceptanceCriteria = acceptanceCriteria;
    }
    this.dependencies = dependencies;
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
    this.acceptanceCriteria = FXCollections.observableArrayList();
    if (clone.getAcceptanceCriteria() != null) {
      this.acceptanceCriteria.addAll(clone.getAcceptanceCriteria());
    }
    if (clone.getDependencies() != null) {
      this.dependencies.addAll(clone.getDependencies());
    }
    this.isReady = clone.getIsReady();
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
   * Returns whether or not the story is marked as ready.
   * @return Story is ready or not as a boolean.
   */
  public boolean getIsReady() {
    return this.isReady;
  }

  /**
   * Sets the readiness state of the story.
   * @param isReady Whether story is ready or not as a boolean.
   */
  public void setIsReady(boolean isReady) {
    this.isReady = isReady;
  }

  /**
   * gets the acceptance criteria
   * @return List which contains the ACS
   */
  public ObservableList<String> getAcceptanceCriteria() {
    return acceptanceCriteria;
  }

  /**
   * sets the Acceptance criteria
   * @param acceptanceCriteria List which is the ACS
   */
  public void setAcceptanceCriteria(ObservableList<String> acceptanceCriteria) {
    this.acceptanceCriteria = acceptanceCriteria;
  }

  /**
   * Sets the dependencies
   * @param dependencies List of stories
   */
  public void setDependencies(List<Story> dependencies) {
    this.dependencies = dependencies;
  }

  /**
   * Gets the dependencies
   * @return A list of stories that this story depends on
   */
  public List<Story> getDependencies() {
    return this.dependencies;
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
      this.acceptanceCriteria.clear();
      this.acceptanceCriteria.addAll(clone.getAcceptanceCriteria());
      this.dependencies.clear();
      this.dependencies.addAll(clone.getDependencies());
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
   * Override equals method
   * @param o Object being compared to
   * @return whether objects are equal or not
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Story story = (Story) o;

    if (isReady != story.isReady) {
      return false;
    }
    if (!label.equals(story.label)) {
      return false;
    }
    if (!storyName.equals(story.storyName)) {
      return false;
    }
    if (!description.equals(story.description)) {
      return false;
    }
    if (!creator.equals(story.creator)) {
      return false;
    }
    if (!dependencies.equals(story.dependencies)) {
      return false;
    }
    return acceptanceCriteria.equals(story.acceptanceCriteria);

  }

  /**
   * Hashcode override, generated by intelliJ
   */
  @Override
  public int hashCode() {
    int result = label.hashCode();
    result = 31 * result + storyName.hashCode();
    result = 31 * result + description.hashCode();
    result = 31 * result + creator.hashCode();
    result = 31 * result + dependencies.hashCode();
    result = 31 * result + (isReady ? 1 : 0);
    result = 31 * result + acceptanceCriteria.hashCode();
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
