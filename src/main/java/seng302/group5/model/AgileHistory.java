package seng302.group5.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Agile history class to store any item in program (currently on teams) and the start date/end
 * date for allocation.
 * Created by Michael on 4/13/2015.
 */
public class AgileHistory {
  AgileItem agileItem;
  LocalDate startDate;
  LocalDate endDate;

  public AgileHistory(AgileItem agileItem, LocalDate startDate, LocalDate endDate) {
    this.agileItem = agileItem;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public AgileHistory() {
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
   * @return Formatted string
   */
  @Override
  public String toString() {
    String dateFormat = "dd/MM/yyyy";
    return agileItem.toString() + ": " + startDate.format(DateTimeFormatter.ofPattern(dateFormat)) +
           " - " + endDate.format(DateTimeFormatter.ofPattern(dateFormat));
  }
}
