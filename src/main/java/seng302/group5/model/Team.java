package seng302.group5.model;

import java.util.Enumeration;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**Basic model of a Team.
 *
 * Created by Zander on 24/03/2015.
 */
public class Team implements AgileItem {

  private String teamID;
  private String teamDescription;

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

  public void delete(){}

  public void create(){}

  public void addRole(PersonRole role) {
    if (canAddRole(role)) {
      _roles.addElement(role);
    }
  }

  private boolean canAddRole(PersonRole role) {
    if (role.hasType("JobRole")){
      Enumeration e = _roles.elements();
      while (e.hasMoreElements()) {
        PersonRole each = (PersonRole) e.nextElement();
        if (each.hasType("JobRole")) return false;
      }
    }
      return true;
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
}
