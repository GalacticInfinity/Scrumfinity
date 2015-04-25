package seng302.group5.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by @author Alex Woo
 */
public class Project implements AgileItem {

  private String projectID;
  private String projectName;
  private String projectDescription;
  private ObservableList<AgileHistory> allocatedTeams = FXCollections.observableArrayList();


  /**
   * Default constructor
   */
  public Project() {
    projectID = "";
    projectName = "";
    projectDescription = "";
  }

  /**
   * Constructor.
   *
   * @param projectID          Name of Person, Unique, Non-Null, can't be greater then 8 characters.
   * @param projectName        Description of person
   * @param projectDescription Date of birth of person?
   */
  public Project(String projectID, String projectName, String projectDescription) {
    this.projectID = projectID;
    this.projectName = projectName;
    this.projectDescription = projectDescription;
  }

  /**
   * Constructor to create a clone of an existing project
   *
   * @param clone Project to clone
   */
  public Project(Project clone) {
    this.projectID = clone.getProjectID();
    this.projectName = clone.getProjectName();
    this.projectDescription = clone.getProjectDescription();
    this.allocatedTeams.clear();
    this.allocatedTeams.addAll(clone.getAllocatedTeams());
  }

  public String getProjectID() {
    return projectID;
  }

  public void setProjectID(String name) {
    this.projectID = name;
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

  /**
   * Copies the Project input person's fields into current object.
   *
   * @param agileItem project who's fields are to be copied
   */
  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Project) {
      Project clone = (Project) agileItem;
      this.projectID = clone.getProjectID();
      this.projectName = clone.getProjectName();
      this.projectDescription = clone.getProjectDescription();
      this.allocatedTeams.clear();
      this.allocatedTeams.addAll(clone.getAllocatedTeams());
    }
  }

  /**
   * toString method just return projectID
   */
  @Override
  public String toString() {
    return this.projectID;
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
      result = this.projectID.equals(project.getProjectID());
    }
    return result;
  }

}
