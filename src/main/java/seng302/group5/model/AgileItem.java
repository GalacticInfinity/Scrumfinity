package seng302.group5.model;

/**
 * Created by Michael on 3/17/2015.
 *
 * An idea for being able to show different observable lists, may scrap alltogether
 */
public interface AgileItem {

  void copyValues(AgileItem agileItem);

  /**
   * What will display in the list.
   * @return String representation of item.
   */
  String toString();

  /**
   * The function which will be used to compare AgileItems. Assumes IDs are unique and non null.
   * @param obj Object to compare to.
   * @return Whether IDs are equal or not.
   */
  boolean equals(Object obj);

}
