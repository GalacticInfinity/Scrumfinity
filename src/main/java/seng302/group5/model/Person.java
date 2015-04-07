package seng302.group5.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Basic model of a Person.
 *
 * Created by Zander on 17/03/2015.
 */
public class Person implements AgileItem {

  private String personID;
  private String firstName;
  private String lastName;

  private Boolean assignedToTeam = false;
  private Team team = null;

  private ObservableList<Skill> skillSet = FXCollections.observableArrayList();

  /**
   * Default constructor.
   */
  public Person() {
    personID = "";
    firstName = "";
    lastName = "";
  }

  /**
   * Person constructor.
   *
   * @param personID Unique, non-null person ID.
   * @param firstName First name of person.
   * @param lastName Last name of person.
   */
  public Person(String personID, String firstName, String lastName, ObservableList<Skill> skills) {
    this.personID = personID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.skillSet = skills;
  }

  /**
   * Constructor to create a clone of an existing person
   *
   * @param clone Person to clone
   */
  public Person(Person clone) {
    this.personID = clone.getPersonID();
    this.firstName = clone.getFirstName();
    this.lastName = clone.getLastName();
    this.skillSet = clone.getSkillSet();
  }

  public String getPersonID() {
    return personID;
  }

  public void setPersonID(String personID) {
    this.personID = personID;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getTeam() {
       return team.getTeamID();
    }


  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public ObservableList<Skill> getSkillSet() { return skillSet;}

  public void setSkillSet(ObservableList<Skill> skillSet) {
    this.skillSet = skillSet;
  }

  /**
   * Assigns this person to a team.
   * @param team Team object that person is assigned to.
   */
  public void assignToTeam(Team team) {
    this.assignedToTeam = true;
    this.team = team;
  }

  /**
   * Removes this person from a team.
   */
  public void removeFromTeam() {
    this.assignedToTeam = false;
    this.team = null;
  }

  /**
   * Checks if person is in a team.
   * @return True, if person is in team. False, if not.
   */
  public Boolean isInTeam() {
    return assignedToTeam;
  }

  public void delete(){}

  public void create(){}

  /**
   * Overrides to toString method with the
   * ID of person.
   *
   * @return Unique ID of person.
   */
  @Override
  public String toString() {
    return personID;
  }
}
