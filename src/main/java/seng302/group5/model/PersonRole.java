package seng302.group5.model;

/**
 * @author Liang Ma
 *
 *To set a person's role we do the following
 * Person person;
 * ProductOwner pOwner = (ProductOwner) person.roleOf("ProductOwner");
 *
 */
public class PersonRole {

  public boolean hasType(String value) {
    return false;
  }

  public class ProductOwner extends PersonRole {

    public boolean hasType(String value) {
      if (value.equalsIgnoreCase("Product Owner")) {
        return true;
      } else {
        return super.hasType(value);
      }
    }
  }

  public class ScrumMaster extends PersonRole {

    public boolean hasType(String value) {
      if (value.equalsIgnoreCase("ScrumMaster")) {
        return true;
      } else {
        return super.hasType(value);
      }
    }
  }

  public class DevelopmentTeamMember extends PersonRole {

    public boolean hasType(String value) {
      if (value.equalsIgnoreCase("DevelopmentTeamMember")) {
        return true;
      } else {
        return super.hasType(value);
      }
    }
  }
}

