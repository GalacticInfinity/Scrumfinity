package seng302.group5.model;

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
    for (Person member : clone.getTeamMembers()) {
      this.teamMembers.add(new Person(member, this));
    }
  }

  /**
   * Constructor to create a clone of an existing team if one member is known.
   * This is to avoid stack overflows since the Team and Person clone functions call each other.
   *
   * @param clone Person to clone
   * @param person One of the members
   */
  public Team(Team clone, Person person) {
    this.teamID = clone.getTeamID();
    this.teamDescription = clone.getTeamDescription();
    for (Person member : clone.getTeamMembers()) {
      if (member.equals(person)) {
        this.teamMembers.add(person);
      } else {
        this.teamMembers.add(new Person(member, this));
      }
    }
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
