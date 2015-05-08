package seng302.group5.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Agile history class to store any item in program (currently on teams) and the start date/end date
 * for allocation.
 * Created by Michael on 4/13/2015.
 */
public class AgileHistory implements Comparable<AgileHistory> {

  AgileItem agileItem;
  LocalDate startDate;
  LocalDate endDate;

  /**
   * Create an agile history item that stores an agile item and a start and end date.
   *
   * @param agileItem An Agileitem that the start and end date will be assigned to.
   * @param startDate The Start date assigned to the Agileitem.
   * @param endDate   The End Date assigned to the Agileitem
   */
  public AgileHistory(AgileItem agileItem, LocalDate startDate, LocalDate endDate) {
    this.agileItem = agileItem;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public AgileHistory() {
    this.agileItem = null;
    this.startDate = null;
    this.endDate = null;
  }

  public AgileItem getAgileItem() {
    return agileItem;
  }

  public void setAgileItem(AgileItem agileItem) {
    this.agileItem = agileItem;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  /**
   * Makes the toString() methord return the date in a readable format
   *
   * @return Formatted string
   */
  @Override
  public String toString() {
    String dateFormat = "dd/MM/yyyy";
    String theString = agileItem.toString() + ": " + startDate.format(DateTimeFormatter.ofPattern(dateFormat));

    if (endDate != null) {
      theString += " - " + endDate.format(DateTimeFormatter.ofPattern(dateFormat));
    } else {
      theString += " - no end date";
    }
    return theString;
  }

  @Override
  public int compareTo(AgileHistory o) {
    int result = agileItem.getLabel().compareToIgnoreCase(o.getAgileItem().getLabel());
    if (result == 0) {
      if (startDate.isBefore(o.getStartDate())) {
        return -1;
      } else {
        return 1;
      }
    } else {
      return result;
    }
  }
}
