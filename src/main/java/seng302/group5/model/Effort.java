package seng302.group5.model;

import java.time.LocalDate;

public class Effort implements AgileItem, Comparable<Effort> {

  private String effortLabel;
  private Person worker;
  private double time;
  private String comments;
  private LocalDate endTime;

  /**
   * Empty constructor for an Effort object.
   */
  public Effort() {
    this.effortLabel = "";
    this.worker = null;
    this.time = 0.0;
    this.comments = "";
    this.endTime = null;
  }

  /**
   * Default constructor for an Effort object including all fields.
   * @param worker The Person logging the effort.
   * @param time The time they are logging (as a double).
   * @param comments Any comments about the logged Effort.
   */
  public Effort(Person worker, double time, String comments) {
    this.effortLabel = "";
    this.worker = worker;
    this.time = time;
    this.comments = comments;
  }

  /**
   * A constructor to create a clone of an existing Effort object.
   * @param clone The Effort item for cloning.
   */
  public Effort(Effort clone) {
    this.effortLabel = clone.getLabel();
    this.worker = clone.getWorker();
    this.time = clone.getTime();
    this.comments = clone.getComments();
    this.endTime = clone.getEndTime();
  }

  public String getLabel() {
    return effortLabel;
  }

  public void setLabel(String effortLabel) {
    this.effortLabel = effortLabel;
  }

  public Person getWorker() {
    return worker;
  }

  public void setWorker(Person worker) {
    this.worker = worker;
  }

  public double getTime() {
    return time;
  }

  public void setTime(double time) {
    this.time = time;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public LocalDate getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDate endTime) {
    this.endTime = endTime;
  }

  /**
   * Copies the values from an Effort AgileItem into a new Effort object.
   * @param agileItem The AgileItem object to copy values from.
   */
  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Effort) {
      Effort clone = (Effort) agileItem;
      this.effortLabel = clone.getLabel();
      this.worker = clone.getWorker();
      this.time = clone.getTime();
      this.comments = clone.getComments();
      this.endTime = clone.getEndTime();
    }
  }

  /**
   * Compares this Effort with another for equality.
   * @param o The Effort objcet to compare with this one.
   * @return Whether or not this Effort and the other Effort are equal.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Effort effort = (Effort) o;

    if (!effortLabel.equals(effort.getLabel())) {
      return false;
    }
    if (!comments.equals(effort.getComments())) {
      return false;
    }
    if (time != effort.getTime()) {
      return false;
    }
    if (!worker.equals(effort.getWorker())) {
      return false;
    }
    if (!endTime.equals(effort.getEndTime())) {
      return false;
    }
    return true;
  }

  /**
   * Generates the hashcode for an Effort object.
   * @return The hashcode.
   */
  @Override
  public int hashCode() {
    int result = effortLabel.hashCode();
    result = 31 * result + worker.hashCode();
    result = 31 * result + Double.valueOf(time).hashCode();
    result = 31 * result + comments.hashCode();
    result = 31 * result + endTime.hashCode();
    return result;
  }

  /**
   * Compares the the endDate of this Effort with another.
   * @param o The Effort to compare to.
   * @return Whether this Efforts endDate is greater or lesser than the other.
   */
  @Override
  public int compareTo(Effort o) {
    return this.endTime.compareTo(o.getEndTime());
  }
}
