package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

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

  private ObservableList<Story> sprintStories;

  private Sprint sprint;
  private Sprint sprint1;
  private Sprint sprint2;

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
    sprintStories = FXCollections.observableArrayList();

    sprint = new Sprint(sprintGoal,sprintDescription,sprintFullName,sprintTeam,sprintBacklog,
                        sprintRelease,sprintStart,sprintEnd,sprintStories,sprintProject);

    sprint1 = new Sprint(sprintGoal,sprintDescription,sprintFullName,sprintTeam,sprintBacklog,
                         sprintRelease,sprintStart,sprintEnd,sprintStories,sprintProject);


    String sprintGoal2 = "This is a goalololololol!";
    sprint2 = new Sprint(sprintGoal2,sprintDescription,sprintFullName,sprintTeam,sprintBacklog,
                         sprintRelease,sprintStart,sprintEnd,sprintStories,sprintProject);
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
}
