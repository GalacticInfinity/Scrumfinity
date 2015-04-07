package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.junit.Assert.*;

/**
 * Created by Shingy on 3/04/2015.
 */
public class PersonTest {

  private String personID;
  private String firstName;
  private String lastName;
  private ObservableList<Skill> skillSet = FXCollections.observableArrayList();

  private Person person;

  @Before
  public void setUp() throws Exception {
    personID = "ssc55";
    firstName = "Su-Shing";
    lastName = "Chen";
    skillSet.addAll(new Skill("C", "C skill"));
    skillSet.add(new Skill("Java", "java Skill"));
    person = new Person(personID, firstName, lastName, skillSet);
  }

  @Test
  public void testAssignToTeam() throws Exception {
    // TODO
  }

  @Test
  public void testRemoveFromTeam() throws Exception {
    // TODO
  }

  @Test
  public void testIsInTeam() throws Exception {
    // TODO
  }

  @Test
  public void testToString() throws Exception {
    String result = person.getPersonID();
    assertEquals(personID, result);
  }
}
