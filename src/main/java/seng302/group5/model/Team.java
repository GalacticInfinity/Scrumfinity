package seng302.group5.model;

import org.mockito.cglib.core.Local;

import java.time.LocalDate;

import java.util.Enumeration;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**Basic model of a Team.
 *
 * Created by Zander on 24/03/2015.
 */
public class Team implements AgileItem {

  private String teamID;
  private String teamDescription;
  private LocalDate startDate;
  private LocalDate endDate;
  private Project currentProject = null;

  private ObservableList<Person> teamMembers = FXCollections.observableArrayList();

  private Vector _roles = new Vector();

  /**
   * Default constructor.
   */
  public Team() {
    this.teamID = "";
    this.teamDescription = "";
  }

  /**
   * Team constructor.
   *
   * @param teamID Unique, non-null team ID.
   * @param teamMembers List of people in the team.
   * @param teamDescription Description of the team.
   */
  public Team(String teamID, ObservableList<Person> teamMembers, String teamDescription) {
    this.teamID = teamID;
    this.teamMembers = teamMembers;
    this.teamDescription = teamDescription;
  }


  /**
   * Constructor to create a clone of an existing person
   *
   * @param clone Person to clone
   */
  public Team(Team clone) {
    this.teamID = clone.getTeamID();
    this.teamDescription = clone.getTeamDescription();
    this.teamMembers.clear();
    this.teamMembers.addAll(clone.getTeamMembers());
    this._roles = clone.get_roles();
  }

  public String getTeamID() {
    return this.teamID;
  }

  public void setTeamID(String teamID) {
    this.teamID = teamID;
  }

  public String getTeamDescription() {
    return this.teamDescription;
  }

  public void setTeamDescription(String teamDescription) {
    this.teamDescription = teamDescription;
  }

  public ObservableList<Person> getTeamMembers() {
    return teamMembers;
  }

  public void setTeamMembers(ObservableList<Person> teamMembers) {
    this.teamMembers = teamMembers;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }
  public void create(){}

  public void addRole(PersonRole role) {
    if (canAddRole(role)) {
      _roles.addElement(role);
    }
    else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Cannot Add Role");
      alert.setHeaderText(null);
      alert.setContentText("There only can be 1 Product Owner and 1 Scrum Master in the team.");
      alert.showAndWait();
    }
  }

  public Vector get_roles() {
    return _roles;
  }

  private boolean canAddRole(PersonRole role) {
    if (role.hasType("ProductOwner") || role.hasType("ScrumMaster") ||
        role.hasType("DevelopmentTeamMember")) {
      Enumeration e = _roles.elements();
      while (e.hasMoreElements()) {
        PersonRole each = (PersonRole) e.nextElement();
        if (each.hasType("ProductOwner") || each.hasType("ScrumMaster")) return false;
      }
    }
      return true;
  }

  public void setCurrentProject(Project project) {
    this.currentProject = project;
  }

  public LocalDate getStartDate() {
    return this.startDate;
  }

  public LocalDate getEndDate() {
    return this.endDate;
  }

  public Project getCurrentProject() {
    return this.currentProject;
  }

  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Team) {
      Team clone = (Team) agileItem;
      this.teamID = clone.getTeamID();
      this.teamDescription = clone.getTeamDescription();
      this.teamMembers.clear();
      this.teamMembers.addAll(clone.getTeamMembers());
      this._roles = clone.get_roles();
    }
  }

  /**
   * Overrides to toString method with the
   * ID of team.
   *
   * @return Unique ID of team.
   */
  @Override
  public String toString() {
    return teamID;
  }

  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj instanceof Team) {
      Team team = (Team) obj;
      result = this.teamID.equals(team.getTeamID());
    }
    return result;
  }
}
