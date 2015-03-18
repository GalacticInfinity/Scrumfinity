package seng302.group5.model;

/**
 * Created by @author Alex Woo
 */
public class Project implements AgileItem {

  private String uniqueShortName;
  private String longName;
  private String description;

  public void setUniqueShortName(String name) {
    this.uniqueShortName = name;
  }

  public String getUniqueShortName() {
    return uniqueShortName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public String getLongName() {
    return this.longName;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  /**
   * Project
   * @param uniqueShortName Name of Person
   * @param longName Description of person
   * @param description Date of birth of person?
   */
  public Project(String uniqueShortName, String longName, String description) {
    this.uniqueShortName = uniqueShortName;
    this.longName = longName;
    this.description = description;
  }

  public void delete(){
  }

  public void create(){
  }
  // New toString method, for list
  @Override
  public String toString() {
    return this.uniqueShortName;
  };

}
