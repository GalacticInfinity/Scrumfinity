package seng302.group5.model;

/**
 * @author Liang Ma
 *
 *To set a person's role we do the following
 * Person person;
 * ProductOwner pOwner = (ProductOwner) person.roleOf("Product Owner");
 *
 */
public class PersonRole {

  public PersonRole(){}

  public boolean hasType(String value) {
    return false;
  }

  public static class ProductOwner extends PersonRole {

    public ProductOwner() {super();}

    public boolean hasType(String value) {
      if (value.equalsIgnoreCase("Product Owner")) {
        return true;
      } else {
        return super.hasType(value);
      }
    }
  }

  public static class ScrumMaster extends PersonRole {

    public boolean hasType(String value) {
      if (value.equalsIgnoreCase("Scrum Master")) {
        return true;
      } else {
        return super.hasType(value);
      }
    }
  }

  public static class DevelopmentTeamMember extends PersonRole {

    public boolean hasType(String value) {
      if (value.equalsIgnoreCase("Development Team Member")) {
        return true;
      } else {
        return super.hasType(value);
      }
    }
  }
}

