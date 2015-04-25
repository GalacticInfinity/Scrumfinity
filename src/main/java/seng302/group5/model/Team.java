package seng302.group5.model;

import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**Basic model of a Team.
 *
 * Created by Zander on 24/03/2015.
 */
public class Team implements AgileItem {

  private String teamID;
  private String teamDescription;
  private Project currentProject = null;

  private ObservableList<Person> teamMembers = FXCollections.observableArrayList();
  private HashMap<Person, Role> membersRole;

  /**
   * Default constructor.
   */
  public Team() {
    this.teamID = "";
    this.teamDescription = "";
    this.membersRole = new HashMap<>();
  }

  public Team(String teamID, String teamDescription) {
    this.teamID = teamID;
    this.teamDescription = teamDescription;
    this.membersRole = new HashMap<>();
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
    this.membersRole = new HashMap<>();
    for (Person member : this.teamMembers) {
      this.membersRole.put(member, null);
    }
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
    this.membersRole = new HashMap<>();
    for (Person member : this.teamMembers) {
      this.membersRole.put(member, clone.getMembersRole().get(member));
    }
  }

  public HashMap<Person, Role> getMembersRole() {
    return membersRole;
  }

  public void setMembersRole(HashMap<Person, Role> membersRole) {
    this.membersRole = membersRole;
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

  public void addTeamMember(Person person, Role role) {
    this.teamMembers.add(person);
    this.membersRole.put(person, role);
  }

  public void addTeamMember(Person person) {
    this.teamMembers.add(person);
    this.membersRole.put(person, null);
  }

  public void removeTeamMember(Person person) {
    this.teamMembers.remove(person);
    this.membersRole.remove(person);
  }

  public void setCurrentProject(Project project) {
    this.currentProject = project;
  }

  public Project getCurrentProject() {
    return this.currentProject;
  }

  /**
   * Get the description of the team.
   */
  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Team) {
      Team clone = (Team) agileItem;
      this.teamID = clone.getTeamID();
      this.teamDescription = clone.getTeamDescription();
      this.teamMembers.clear();
      this.teamMembers.addAll(clone.getTeamMembers());
      this.membersRole.clear();
      for (Person member : this.teamMembers) {
        this.membersRole.put(member, clone.getMembersRole().get(member));
      }
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

  /**
   * Check if two team's ids are equal
   * @param obj Object to compare to.
   * @return Whether team's ids are equal
   */
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
