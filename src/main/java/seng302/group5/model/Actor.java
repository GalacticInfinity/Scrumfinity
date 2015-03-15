package seng302.group5.model;

import java.util.GregorianCalendar;

/**
 * Created by @author Alex Woo
 */
public class Actor {

  private String name;
  private String actorDescription;
  private GregorianCalendar DOB;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setActorDescription(String description) {
    this.actorDescription = description;
  }

  public String getActorDescription() {
    return this.actorDescription;
  }

  public void setDOB(GregorianCalendar DOB) {
    this.DOB = DOB;
  }

  public GregorianCalendar getDOB() {
    return DOB;
  }

  /**
   * Actor constructor
   *
   * @param name Name of Person
   * @param actorDescription Description of person
   * @param DOB Date of birth of person?
   */
  public Actor(String name, String actorDescription, GregorianCalendar DOB) {
    this.name = name;
    this.actorDescription = actorDescription;
    this.DOB = DOB;
  }

  @Override
  public String toString() {
    return this.name;
  };

}
