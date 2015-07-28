package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Alex Woo
 */
public class SprintTest {

  private String sprintGoal;
  private String sprintDescription;
  private String sprintFullName;
  private Team sprintTeam;
  private Backlog sprintBacklog;
  private Project sprintProject;
  private Release sprintRelease;

  private LocalDate sprintStart;
  private LocalDate sprintEnd;

  private List<Story> sprintStories;

  private Sprint sprint;
  private Sprint sprint1;
  private Sprint sprint2;

  private Person person1;
  private Person person2;

  @Before
  public void setUp() throws Exception {
    sprintGoal = "This is a goal!";
    sprintDescription = "Omg describe me like one of your french girls";
    sprintFullName = "The prisoner of alakazam baybays";
    sprintTeam = null;
    sprintBacklog = null;
    sprintProject = null;
    sprintRelease = null;
    sprintStart = null;
    sprintEnd = null;
    sprintStories = new ArrayList<>();

    sprint = new Sprint(sprintGoal, sprintFullName, sprintDescription, sprintBacklog, sprintProject,
                        sprintTeam, sprintRelease, sprintStart, sprintEnd, sprintStories);

    sprint1 = new Sprint(sprintGoal, sprintFullName, sprintDescription, sprintBacklog,
                         sprintProject, sprintTeam, sprintRelease, sprintStart, sprintEnd,
                         sprintStories);


    String sprintGoal2 = "This is a goalololololol!";
    sprint2 = new Sprint(sprintGoal2, sprintFullName, sprintDescription, sprintBacklog,
                         sprintProject, sprintTeam, sprintRelease, sprintStart, sprintEnd,
                         sprintStories);
  }


  @Test
  public void testToString() {
    assertEquals(sprint.toString(), sprint.getSprintGoal());
  }

  @Test
  public void equalsTrueCase() {
    assertTrue(sprint.equals(sprint1));
  }

  @Test
  public void testFalseCase() {
    assertTrue(!sprint.equals(sprint2));
  }

  @Test
  public void testAddStory() {
    ObservableList<Skill> skillSet = FXCollections.observableArrayList();
    Person person1 = new Person("Person 1", "Person", "1", skillSet);
    Story story = new Story("First Story", "Story one", "The First Story", person1);

    assertTrue(sprint1.getSprintStories().isEmpty());

  }
}

