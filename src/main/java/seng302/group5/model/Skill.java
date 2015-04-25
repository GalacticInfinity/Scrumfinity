package seng302.group5.model;

/**
 * @author Liang Ma
 */
public class Skill implements AgileItem {

  private String skillID;
  private String skillDescription;

  /**
   * Default constructor for skill
   */
  public Skill() {
    this.skillID = "";
    this.skillDescription = "";
  }

  /**
   * Skill constructor with skill skillName only.
   *
   * @param skillName short skillName of a skill
   */
  public Skill(String skillName) {
    this.skillID = skillName;
  }

  /**
   * Skill constructor with both skill skillName and skill skillDescription.
   *
   * @param skillName        short skillName of a skill
   * @param skillDescription Description of the skill
   */
  public Skill(String skillName, String skillDescription) {
    this.skillID = skillName;
    this.skillDescription = skillDescription;
  }

  /**
   * Constructor to create a clone of an existing skill
   *
   * @param clone Skill to clone
   */
  public Skill(Skill clone) {
    this.skillID = clone.getSkillName();
    this.skillDescription = clone.getSkillDescription();
  }

  /**
   * Set skillName for a skill.
   *
   * @param skillName skillName of the skill
   */
  public void setSkillName(String skillName) {
    this.skillID = skillName;
  }

  /**
   * Get skillName of a skill.
   *
   * @return skillName of the skill
   */
  public String getSkillName() {
    return skillID;
  }

  /**
   * Set the skillDescription of a skill.
   *
   * @param skillDescription skillDescription of the skill
   */
  public void setSkillDescription(String skillDescription) {
    this.skillDescription = skillDescription;
  }

  /**
   * Get the description of the skill.
   *
   * @return description of the skill
   */
  public String getSkillDescription() {
    return this.skillDescription;
  }

  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Skill) {
      Skill clone = (Skill) agileItem;
      this.skillID = clone.getSkillName();
      this.skillDescription = clone.getSkillDescription();
    }
  }

  /**
   * Return a formatted skill name along with its description.
   *
   * @return skill name and description
   */
  @Override
  public String toString() {
    return this.getSkillName();
  }

  /**
   * Check if two skill's ids are equal
   *
   * @param obj Object to compare to.
   * @return Whether skill's ids are equal
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj instanceof Skill) {
      Skill skill = (Skill) obj;
      result = this.skillID.equals(skill.getSkillName());
    }
    return result;
  }
}
