package seng302.group5.model;

import java.time.LocalDate;

/**
 * Created by Michael on 4/13/2015.
 */
public class AgileHistory {
  AgileItem agileItem;
  LocalDate startDate;
  LocalDate endDate;

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
}
