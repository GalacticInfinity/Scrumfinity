package seng302.group5.model;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This is the Sprint model.
 * This will contain:
 * Goal, Description, Full name, Team, Backlog, Project, Release, start and end date and a list
 * of stories.
 *
 * The project is found through the backlog. This logic should be implemented in the controller.
 *
 * Created by Alex Woo
 */
public class Sprint implements AgileItem, Comparable<Sprint> {

  private String sprintGoal;
  private String sprintDescription;
  private String sprintFullName;
  private Team sprintTeam;
  private Backlog sprintBacklog;
  private Project sprintProject;
  private Release sprintRelease;

  private LocalDate sprintStart;
  private LocalDate sprintEnd;

  private ObservableList<Story> sprintStories;

  /**
   * Empty constructor used for save/load.
   */
  public Sprint() {
    sprintGoal = "";
    sprintDescription = "";
    sprintFullName = "";
    sprintTeam = null;
    sprintBacklog = null;
    sprintProject = null;
    sprintRelease = null;
    sprintStart = null;
    sprintEnd = null;
    sprintStories = FXCollections.observableArrayList();
  }

  /**
   * This is the main constructor for the Sprint model.
   * It takes in all the needed variables to fill in the models variables.
   *
   *
   * @param sprintGoal String, not-null. This also works as the label.
   * @param sprintDescription String
   * @param sprintFullName - String
   * @param sprintTeam - Team
   * @param sprintBacklog - Backlog
   * @param sprintRelease - Release
   * @param sprintStart - LocalDate
   * @param sprintEnd - LocalDate
   * @param sprintStories - ObserverableList<Story>
   */
  public Sprint(String sprintGoal, String sprintDescription, String sprintFullName,
                Team sprintTeam, Backlog sprintBacklog, Release sprintRelease,
                LocalDate sprintStart, LocalDate sprintEnd, ObservableList<Story> sprintStories,
                Project sprintProject) {
    this.sprintGoal = sprintGoal;
    this.sprintDescription = sprintDescription;
    this.sprintFullName = sprintFullName;
    this.sprintTeam = sprintTeam;
    this.sprintBacklog = sprintBacklog;
    this.sprintRelease = sprintRelease;
    this.sprintStart = sprintStart;
    this.sprintEnd = sprintEnd;
    this.sprintStories = sprintStories;
    this.sprintProject = sprintProject;
  }

  /**
   * Constructor to create a clone of existing Sprint.
   *
   * @param clone Sprint to clone.
   */
  public Sprint(Sprint clone) {
    this.sprintGoal = clone.getSprintGoal();
    this.sprintDescription = clone.getSprintDescription();
    this.sprintFullName = clone.getSprintFullName();
    this.sprintTeam = clone.getSprintTeam();
    this.sprintBacklog = clone.getSprintBacklog();
    this.sprintRelease = clone.getSprintRelease();
    this.sprintStart = clone.getSprintStart();
    this.sprintEnd = clone.getSprintEnd();
    this.sprintStories.addAll(clone.getSprintStories());
    this.sprintProject = clone.getSprintProject();
  }

  @Override
  public String getLabel() {
    return sprintGoal;
  }

  @Override
  public void setLabel(String label) {
    sprintGoal = label;
  }

  public String getSprintGoal() {
    return sprintGoal;
  }

  public void setSprintGoal(String sprintGoal) {
    this.sprintGoal = sprintGoal;
  }

  public String getSprintDescription() {
    return sprintDescription;
  }

  public void setSprintDescription(String sprintDescription) {
    this.sprintDescription = sprintDescription;
  }

  public String getSprintFullName() {
    return sprintFullName;
  }

  public void setSprintFullName(String sprintFullName) {
    this.sprintFullName = sprintFullName;
  }

  public Team getSprintTeam() {
    return sprintTeam;
  }

  public void setSprintTeam(Team sprintTeam) {
    this.sprintTeam = sprintTeam;
  }

  public Backlog getSprintBacklog() {
    return sprintBacklog;
  }

  public void setSprintBacklog(Backlog sprintBacklog) {
    this.sprintBacklog = sprintBacklog;
  }

  public Project getSprintProject() {
    return sprintProject;
  }

  public void setSprintProject(Project sprintProject) {
    this.sprintProject = sprintProject;
  }

  public Release getSprintRelease() {
    return sprintRelease;
  }

  public void setSprintRelease(Release sprintRelease) {
    this.sprintRelease = sprintRelease;
  }

  public LocalDate getSprintStart() {
    return sprintStart;
  }

  public void setSprintStart(LocalDate sprintStart) {
    this.sprintStart = sprintStart;
  }

  public LocalDate getSprintEnd() {
    return sprintEnd;
  }

  public void setSprintEnd(LocalDate sprintEnd) {
    this.sprintEnd = sprintEnd;
  }

  public ObservableList<Story> getSprintStories() {
    return sprintStories;
  }

  public void setSprintStories(ObservableList<Story> sprintStories) {
    this.sprintStories = sprintStories;
  }


  /**
   * Copies the Sprint input fields into current object.
   * @param agileItem Sprint that's fields are to be copied.
   */
  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Story) {
      Sprint clone = (Sprint) agileItem;
      this.sprintGoal = clone.getSprintGoal();
      this.sprintDescription = clone.getSprintDescription();
      this.sprintFullName = clone.getSprintFullName();
      this.sprintTeam = clone.getSprintTeam();
      this.sprintBacklog = clone.getSprintBacklog();
      this.sprintRelease = clone.getSprintRelease();
      this.sprintStart = clone.getSprintStart();
      this.sprintEnd = clone.getSprintEnd();
      this.sprintStories.clear();
      this.sprintStories.addAll(clone.getSprintStories());
      this.sprintProject = clone.getSprintProject();
    }
  }

  /**
   * Overrides the toString method with the sprint Goal which acts like a label.
   * @return Label of sprint/ Sprint Goal.
   */
  @Override
  public String toString() {
    return this.sprintGoal;
  }

  /**
   * This is a equals method generated by intellij
   * @param o - The object to be checked if equal or not.
   * @return True or False depending on whether it is equal or not
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Sprint sprint = (Sprint) o;

    if (sprintBacklog != null ? !sprintBacklog.equals(sprint.sprintBacklog)
                              : sprint.sprintBacklog != null) {
      return false;
    }
    if (sprintDescription != null ? !sprintDescription.equals(sprint.sprintDescription)
                                  : sprint.sprintDescription != null) {
      return false;
    }
    if (sprintEnd != null ? !sprintEnd.equals(sprint.sprintEnd) : sprint.sprintEnd != null) {
      return false;
    }
    if (sprintFullName != null ? !sprintFullName.equals(sprint.sprintFullName)
                               : sprint.sprintFullName != null) {
      return false;
    }
    if (!sprintGoal.equals(sprint.sprintGoal)) {
      return false;
    }
    if (sprintProject != null ? !sprintProject.equals(sprint.sprintProject)
                              : sprint.sprintProject != null) {
      return false;
    }
    if (sprintRelease != null ? !sprintRelease.equals(sprint.sprintRelease)
                              : sprint.sprintRelease != null) {
      return false;
    }
    if (sprintStart != null ? !sprintStart.equals(sprint.sprintStart)
                            : sprint.sprintStart != null) {
      return false;
    }
    if (sprintStories != null ? !sprintStories.equals(sprint.sprintStories)
                              : sprint.sprintStories != null) {
      return false;
    }
    if (sprintTeam != null ? !sprintTeam.equals(sprint.sprintTeam) : sprint.sprintTeam != null) {
      return false;
    }

    return true;
  }

  /**
   * Intellij generated hashCode redo so equals works properly
   * @return the new hashcode
   */
  @Override
  public int hashCode() {
    int result = sprintGoal.hashCode();
    result = 31 * result + (sprintDescription != null ? sprintDescription.hashCode() : 0);
    result = 31 * result + (sprintFullName != null ? sprintFullName.hashCode() : 0);
    result = 31 * result + (sprintTeam != null ? sprintTeam.hashCode() : 0);
    result = 31 * result + (sprintBacklog != null ? sprintBacklog.hashCode() : 0);
    result = 31 * result + (sprintProject != null ? sprintProject.hashCode() : 0);
    result = 31 * result + (sprintRelease != null ? sprintRelease.hashCode() : 0);
    result = 31 * result + (sprintStart != null ? sprintStart.hashCode() : 0);
    result = 31 * result + (sprintEnd != null ? sprintEnd.hashCode() : 0);
    result = 31 * result + (sprintStories != null ? sprintStories.hashCode() : 0);
    return result;
  }

  /**
   * Compare this Sprints goal/label to sprint o's goal/label
   * @param o sprint you wish to compare to
   * @return whether it is greater or lesser.
   */
  @Override
  public int compareTo(Sprint o) {
    return this.sprintGoal.toLowerCase().compareTo(o.sprintGoal.toLowerCase());
  }
}
