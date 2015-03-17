package seng302.group5.model;

import java.util.ArrayList;

/**
 * @author Liang Ma
 */
public class Skills {

  private String name;
  private String description;

  /**
   * Skill constructor with skill name only.
   *
   * @param name short name of a skill
   */
  public Skills(String name) {
    this.name = name;
  }

  /**
   * Skill constructor with both skill name and skill description.
   *
   * @param name short name of a skill
   * @param description Description of the skill
   */
  public Skills(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Set name for a skill.
   * @param name name of the skill
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get name of a skill.
   * @return name of the skill
   */
  public String getName() {
    return name;
  }

  /**
   * Set the description of a skill.
   * @param description description of the skill
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the description of the skill.
   * @return description of the skill
   */
  public String getDescription() {
    return this.description;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return this.getName() + ": " + this.getDescription() + ".";
  }
}
