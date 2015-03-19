package seng302.group5.model;

/**
 * @author Liang Ma
 */
public class Skills {

  private String skillName;
  private String skillDescription;

  /**
   * Skill constructor with skill skillName only.
   *
   * @param skillName short skillName of a skill
   */
  public Skills(String skillName) {
    this.skillName = skillName;
  }

  /**
   * Skill constructor with both skill skillName and skill skillDescription.
   *
   * @param skillName short skillName of a skill
   * @param skillDescription Description of the skill
   */
  public Skills(String skillName, String skillDescription) {
    this.skillName = skillName;
    this.skillDescription = skillDescription;
  }

  /**
   * Set skillName for a skill.
   * @param skillName skillName of the skill
   */
  public void setSkillName(String skillName) {
    this.skillName = skillName;
  }

  /**
   * Get skillName of a skill.
   * @return skillName of the skill
   */
  public String getSkillName() {
    return skillName;
  }

  /**
   * Set the skillDescription of a skill.
   * @param skillDescription skillDescription of the skill
   */
  public void setSkillDescription(String skillDescription) {
    this.skillDescription = skillDescription;
  }

  /**
   * Get the skillDescription of the skill.
   * @return skillDescription of the skill
   */
  public String getSkillDescription() {
    return this.skillDescription;
  }

  /**
   * Return a formatted string of the skill.
   * @return
   */
  @Override
  public String toString() {
    return this.getSkillName() + ": " + this.getSkillDescription() + ".";
  }
}
