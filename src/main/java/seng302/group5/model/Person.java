package seng302.group5.model;

/**
 * Basic model of a Person.
 *
 * Created by Zander on 17/03/2015.
 */
public class Person {

  private String uniqueShortName;
  private String firstName;
  private String lastName;

  /**
   * Person constructor
   *
   * @param uniqueShortName Unique, non-null person ID
   * @param firstName First name of person
   * @param lastName Last name of person
   */
  public Person(String uniqueShortName, String firstName, String lastName) {
    this.uniqueShortName = uniqueShortName;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   * Returns the unique short name of person.
   *
   * @return Unique short name of person.
   */
  public String getUniqueShortName() {
    return uniqueShortName;
  }

  public void setUniqueShortName(String uniqueShortName) {
    this.uniqueShortName = uniqueShortName;
  }

  /**
   * returns the first name of person.
   *
   * @return first name of person.
   */
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Returns the last name of person.
   *
   * @return Last name of person.
   */
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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
    return uniqueShortName;
  }
}
