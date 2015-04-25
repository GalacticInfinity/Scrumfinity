package seng302.group5.model;

/**
 * Store a role that a team member can have.
 *
 * @author Alex Woo
 */
public class Role {

  private String roleID;
  private String roleName;
  private Skill requiredSkill;
  private int memberLimit;


  /**
   * Simple Constructor.
   *
   * @param roleID   Unique, non-null ID for the role.
   * @param roleName Name of the role.
   */
  public Role(String roleID, String roleName) {
    this.roleID = roleID;
    this.roleName = roleName;
    this.requiredSkill = null;
    this.memberLimit = Integer.MAX_VALUE; // Infinity
  }

  /**
   * Constructor.
   *
   * @param roleID      Unique, non-null ID for role.
   * @param roleName    Name of the role.
   * @param skill       The skill required to be assigned this role.
   * @param memberLimit The number of members allowed to have this role.
   */
  public Role(String roleID, String roleName, Skill skill, int memberLimit) {
    this.roleID = roleID;
    this.roleName = roleName;
    this.requiredSkill = skill;
    this.memberLimit = memberLimit;
  }

  /**
   * Default Constructor.
   */
  public Role() {
    this.roleID = "";
    this.roleName = "";
    this.requiredSkill = null;
    this.memberLimit = Integer.MAX_VALUE;
  }


  public String getRoleID() {
    return roleID;
  }

  public void setRoleID(String roleID) {
    this.roleID = roleID;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public Skill getRequiredSkill() {
    return requiredSkill;
  }

  public void setRequiredSkill(Skill requiredSkill) {
    this.requiredSkill = requiredSkill;
  }

  public int getMemberLimit() {
    return memberLimit;
  }

  public void setMemberLimit(int memberLimit) {
    this.memberLimit = memberLimit;
  }

  /**
   * toString override.
   *
   * @return Role's roleName
   */
  @Override
  public String toString() {
    return roleName;
  }
}
