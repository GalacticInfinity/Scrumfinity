package seng302.group5.model;

/**
 * @author Liang Ma
 */
public class Skill implements AgileItem{

  private String skillName;
  private String skillDescription;

  /**
   * Default constructor for skill
   */
  public Skill () {
    this.skillName = "";
    this.skillDescription = "";
  }

  /**
   * Skill constructor with skill skillName only.
   *
   * @param skillName short skillName of a skill
   */
  public Skill(String skillName) {
    this.skillName = skillName;
  }

  /**
   * Skill constructor with both skill skillName and skill skillDescription.
   *
   * @param skillName short skillName of a skill
   * @param skillDescription Description of the skill
   */
  public Skill(String skillName, String skillDescription) {
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
   * Get the description of the skill.
   * @return description of the skill
   */
  public String getSkillDescription() {
    return this.skillDescription;
  }

  @Override
  public void create() {

  }

  @Override
  public void delete() {

  }

  /**
   * Return a formatted skill name along with its description.
   * @return skill name and description
   */
  @Override
  public String toString() {
    return this.getSkillName();
  }
}
