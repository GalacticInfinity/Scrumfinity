package seng302.group5.model;

import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by @author Alex Woo
 */
public class Project implements AgileItem, Comparable<Project>  {

  private String label;
  private String projectName;
  private String projectDescription;
  private ObservableList<AgileHistory> allocatedTeams = FXCollections.observableArrayList();
  private Backlog backlog;


  /**
   * Default constructor
   */
  public Project() {
    label = "";
    projectName = "";
    projectDescription = "";
  }

  /**
   * Constructor.
   *
   * @param label              Name of Person, Unique, Non-Null, can't be greater then 8 characters.
   * @param projectName        Description of person
   * @param projectDescription Date of birth of person?
   */
  public Project(String label, String projectName, String projectDescription) {
    this.label = label;
    this.projectName = projectName;
    this.projectDescription = projectDescription;
  }

  /**
   * Constructor to create a clone of an existing project
   *
   * @param clone Project to clone
   */
  public Project(Project clone) {
    this.label = clone.getLabel();
    this.projectName = clone.getProjectName();
    this.projectDescription = clone.getProjectDescription();
    this.allocatedTeams.clear();
    this.allocatedTeams.addAll(clone.getAllocatedTeams());
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
  }

  public String getProjectName() {
    return this.projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getProjectDescription() {
    return projectDescription;
  }

  public void setProjectDescription(String projectDescription) {
    this.projectDescription = projectDescription;
  }

  public ObservableList<AgileHistory> getAllocatedTeams() {
    return this.allocatedTeams;
  }

  /**
   * Add a team to the project as an AgileHistory object containing the team and dates
   *
   * @param team AgileHistory object to add
   */
  public void addTeam(AgileHistory team) {
    this.allocatedTeams.add(team);
  }

  /**
   * Remove a team to the project as an AgileHistory object containing the team and dates
   *
   * @param team AgileHistory object to remove
   */
  public void removeTeam(AgileHistory team) {
    this.allocatedTeams.remove(team);
  }

  public Backlog getBacklog() {
    return backlog;
  }

  public void setBacklog(Backlog backlog) {
    this.backlog = backlog;
  }

  /**
   * Copies the Project input person's fields into current object.
   *
   * @param agileItem project who's fields are to be copied
   */
  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Project) {
      Project clone = (Project) agileItem;
      this.label = clone.getLabel();
      this.projectName = clone.getProjectName();
      this.projectDescription = clone.getProjectDescription();
      this.allocatedTeams.clear();
      this.allocatedTeams.addAll(clone.getAllocatedTeams());
    }
  }

  /**
   * toString method just return label
   */
  @Override
  public String toString() {
    return this.label;
  }

  /**
   * Check if two project's ids are equal
   *
   * @param obj Object to compare to.
   * @return Whether project's ids are equal
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj instanceof Project) {
      Project project = (Project) obj;
      result = this.label.equals(project.getLabel());
    }
    return result;
  }

  /**
   * Compare the project label to o's project label
   * @param o the project you wish to compare to
   * @return whether it is greater or lesser
   */
  @Override
  public int compareTo(Project o) {
    return this.label.toLowerCase().compareTo(o.label.toLowerCase());
  }
}
