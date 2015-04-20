package seng302.group5.model;

import java.time.LocalDate;

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
   * Project
   * @param projectID Name of Person
   * @param projectName Description of person
   * @param projectDescription Date of birth of person?
   */
  public Project(String projectID, String projectName, String projectDescription) {
    this.projectID = projectID;
    this.projectName = projectName;
    this.projectDescription = projectDescription;
  }

  /**
   * Constructor to create a clone of an existing project
   * @param clone Project to clone
   */
  public Project(Project clone) {
    this.projectID = clone.getProjectID();
    this.projectName = clone.getProjectName();
    this.projectDescription = clone.getProjectDescription();
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

  public void addTeam(AgileHistory team) {
    this.allocatedTeams.add(team);
  }

  public ObservableList<AgileHistory> getTeam() {
    return this.allocatedTeams;
  }

  public void removeTeam(Team team) {
    this.allocatedTeams.remove(team);
  }

  public void setProjectDescription(String projectDescription) {
    this.projectDescription = projectDescription;
  }

  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Project) {
      Project clone = (Project) agileItem;
      this.projectID = clone.getProjectID();
      this.projectName = clone.getProjectName();
      this.projectDescription = clone.getProjectDescription();
    }
  }

  // New toString method, for list
  @Override
  public String toString() {
    return this.projectID;
  }

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
