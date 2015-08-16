package seng302.group5.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Task class is a way to log and store effort spent by people on a story.
 * Contains a list of people assigned, and also a map between people assigned and
 *
 * Created by Michael on 7/8/2015.
 */
public class Task implements AgileItem, Comparable<Task> {

  private String label;
  private String description;
  private int estimation;
  private String impediments;
  private Status status;
  private List<Person> assignedPeople;
  private Map<Person, Integer> spentEffort;


  /**
   * Default constructor for task. Sets all String to empty strings, initializes list/maps.
   * Status by default is Status.NOT_STARTED. Estimate is 0.0.
   */
  public Task() {
    this.label = "";
    this.description = "";
    this.estimation = 0;
    this.impediments = "";
    this.status = Status.NOT_STARTED;
    assignedPeople = new ArrayList<>();
    spentEffort = new IdentityHashMap<>();
  }

  public Task(String label, String description, Integer estimation, Status status,
              List<Person> persons) {
    this.label = label;
    this.description = description;
    this.status = status;
    this.estimation = estimation;
    this.impediments = "";
    this.assignedPeople = new ArrayList<>();
    this.spentEffort = new IdentityHashMap<>();
    addAllTaskPeople(persons);
  }

  /**
   * Cloning function, copies all of clone's values in the
   * @param clone Task to be cloned
   */
  public Task(Task clone) {
    this.label = clone.getLabel();
    this.description = clone.getTaskDescription();
    this.estimation = clone.getTaskEstimation();
    this.impediments = clone.getImpediments();
    this.status = clone.getStatus();
    assignedPeople = new ArrayList<>();
    assignedPeople.addAll(clone.getTaskPeople());
    spentEffort = new IdentityHashMap<>();
    spentEffort.putAll(clone.getSpentEffort());
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
  }

  public String getTaskDescription() {
    return description;
  }

  public void setTaskDescription(String description) {
    this.description = description;
  }

  public Integer getTaskEstimation() {
    return estimation;
  }

  public void setTaskEstimation(Integer estimation) {
    this.estimation = estimation;
  }

  public String getImpediments() {
    return impediments;
  }

  public void setImpediments(String impediments) {
    this.impediments = impediments;
  }

  /**
   * Return an unmodifiable list of assignedPeople
   * @return people assigned to the task
   */
  public List<Person> getTaskPeople() {
    return Collections.unmodifiableList(assignedPeople);
  }

  public void removeTaskPerson(Person person) {
    assignedPeople.remove(person);
    spentEffort.remove(person);
  }

  public void addTaskPerson(Person person) {
    assignedPeople.add(person);
    spentEffort.put(person, 0);
  }

  public void removeAllTaskPeople() {
    assignedPeople.clear();
    spentEffort.clear();
  }

  public void addAllTaskPeople(List<Person> peopleList) {
    for (Person person : peopleList) {
      addTaskPerson(person);
    }
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Status getStatus() {
    return this.status;
  }

  public Map<Person, Integer> getSpentEffort() {
    return Collections.unmodifiableMap(spentEffort);
  }

  /**
   * Adding effort one person at a time
   * @param person who did their effort
   * @param effort their effort (in hours)
   */
  public void updateSpentEffort(Person person, Integer effort) {
    if (spentEffort.containsKey(person)) {
      spentEffort.remove(person);
    }
    spentEffort.put(person, effort);
  }

  /**
   * Adds all spent effort for a map. Deletes the previous map.
   * @param effortMap effortMap to be copied in
   */
  public void updateSpentEffort(Map<Person, Integer> effortMap) {
    spentEffort.clear();
    spentEffort.putAll(effortMap);
  }

  /**
   * Copy values from an existing AgileItem object to the current AgileItem
   *
   * @param agileItem The AgileItem object to copy values from
   */
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Task) {
      Task clone = (Task) agileItem;
      this.label = clone.getLabel();
      this.description = clone.getTaskDescription();
      this.estimation = clone.getTaskEstimation();
      this.impediments = clone.getImpediments();
      this.status = clone.getStatus();
      removeAllTaskPeople();
      for (Person person : clone.getTaskPeople()) {
        this.assignedPeople.add(person);
      }
      for (Map.Entry<Person, Integer> entry : clone.getSpentEffort().entrySet()) {
        spentEffort.put(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * Overrides the toString method with the Task label.
   *
   * @return Label of task.
   */
  @Override
  public String toString() {
    return this.label;
  }

  /**
   * Equals to check if the fields of two task objects are the same.
   * @param o Object to check against
   * @return Boolean
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Task task = (Task) o;

    if (estimation != task.estimation) {
      return false;
    }
    if (!label.equals(task.label)) {
      return false;
    }
    if (description != null ? !description.equals(task.description) : task.description != null) {
      return false;
    }
    if (status != task.status) {
      return false;
    }
    if (impediments != null ? !impediments.equals(task.impediments) : task.impediments != null) {
      return false;
    }
    if (assignedPeople != null ? !assignedPeople.equals(task.assignedPeople)
                               : task.assignedPeople != null) {
      return false;
    }
    return !(spentEffort != null ? !spentEffort.equals(task.spentEffort)
                                 : task.spentEffort != null);

  }

  @Override
  public int hashCode() {
    int result = label.hashCode();
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + estimation;
    result = 31 * result + (impediments != null ? impediments.hashCode() : 0);
    result = 31 * result + status.hashCode();
    result = 31 * result + (assignedPeople != null ? assignedPeople.hashCode() : 0);
    result = 31 * result + (spentEffort != null ? spentEffort.hashCode() : 0);
    return result;
  }

  /**
   * CompareTo method for list sorting
   * @param o Object to compare to
   * @return int befre or after in list
   */
  @Override
  public int compareTo(Task o) {
    return this.label.toLowerCase().compareTo(o.label.toLowerCase());
  }
}
