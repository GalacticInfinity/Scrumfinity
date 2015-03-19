package seng302.group5.model;

/**
 * Created by @author Alex Woo
 */
public class Project implements AgileItem {

  private String projectID;
  private String projectName;
  private String projectDescription;

  /**
   * Project constructor given ID, name, and projectDescription.
   *
   * @param projectID Name of Person
   * @param projectName        Description of person
   * @param projectDescription     Date of birth of person?
   */
  public Project(String projectID, String projectName, String projectDescription) {
    this.projectID = projectID;
    this.projectName = projectName;
    this.projectDescription = projectDescription;
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

  public void delete(){
  }

  public void create(){
  }
  // New toString method, for list
  @Override
  public String toString() {
    return this.projectID;
  }

}
