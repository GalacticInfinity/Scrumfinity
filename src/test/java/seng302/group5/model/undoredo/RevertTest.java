package seng302.group5.model.undoredo;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.time.LocalDate;
import java.time.Month;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.ListMainPaneController;
import seng302.group5.controller.MenuBarController;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Skill;
import seng302.group5.model.Team;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Unit testing for revert function.
 * @author Alex Woo, Liang Ma
 */
public class RevertTest {

  private String personLabel;
  private String firstName;
  private String lastName;
  private ObservableList<Skill> skillSet;

  private String skillName;
  private String skillDescription;

  private String teamID;
  private ObservableList<Person> teamMembers;
  private String teamDescription;

  private String projectID;
  private String projectName;
  private String projectDescription;

  private String releaseName;
  private String releaseDescription;
  private LocalDate releaseDate;
  private String releaseNotes;
  private Project projectRelease;

  private Person person;
  private Skill skill;
  private Team team;
  private Project project;
  private Release release;

  private UndoRedoHandler undoRedoHandler;
  private Main mainApp;

  @Before
  public void setUp() throws Exception {

    ListMainPaneController listMainPaneController = mock(ListMainPaneController.class);

    mainApp = new Main();
    mainApp.setLMPC(listMainPaneController);

    undoRedoHandler = mainApp.getUndoRedoHandler();
  }

  private void newPerson() {
    personLabel = "ssc55";
    firstName = "Su-Shing";
    lastName = "Chen";
    skillSet = FXCollections.observableArrayList();
    person = new Person(personLabel, firstName, lastName, skillSet);

    mainApp.addPerson(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_CREATE);
    undoRedoObject.setAgileItem(person);
    undoRedoObject.addDatum(new Person(person));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void newPersonWithSkill() {
    personLabel = "ssc55";
    firstName = "Su-Shing";
    lastName = "Chen";
    skillSet = FXCollections.observableArrayList();
    skillSet.add(skill);
    person = new Person(personLabel, firstName, lastName, skillSet);
    skillSet = FXCollections.observableArrayList(skillSet); // save a copy

    mainApp.addPerson(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_CREATE);
    undoRedoObject.setAgileItem(person);
    undoRedoObject.addDatum(new Person(person));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void newSkill() {
    skillName = "C#";
    skillDescription = "Person can program in the C# language";
    skill = new Skill(skillName, skillDescription);

    mainApp.addSkill(skill);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.SKILL_CREATE);
    undoRedoObject.setAgileItem(skill);
    undoRedoObject.addDatum(new Skill(skill));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void newTeam() {
    teamID = "TheMen";
    teamMembers = FXCollections.observableArrayList();
    teamDescription = "This is a manly team";
    team = new Team(teamID, teamMembers, teamDescription);

    mainApp.addTeam(team);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.TEAM_CREATE);
    undoRedoObject.setAgileItem(team);
    undoRedoObject.addDatum(new Team(team));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void newTeamWithMember() {
    teamID = "TheMen";
    teamMembers = FXCollections.observableArrayList();
    teamMembers.add(person);
    teamDescription = "This is a manly team";
    team = new Team(teamID, teamMembers, teamDescription);
    person.assignToTeam(team);

    mainApp.addTeam(team);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.TEAM_CREATE);
    undoRedoObject.setAgileItem(team);
    undoRedoObject.addDatum(new Team(team));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void newProject() {
    projectID = "proj";
    projectName = "The Project's Name";
    projectDescription = "This is a description for the project";
    project = new Project(projectID, projectName, projectDescription);

    mainApp.addProject(project);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PROJECT_CREATE);
    undoRedoObject.setAgileItem(project);
    undoRedoObject.addDatum(new Project(project));

    undoRedoHandler.newAction(undoRedoObject);
  }


  private void newRelease() {
    releaseName = "TheRelease";
    releaseDescription = "The descriptioning";
    releaseDate = LocalDate.of(1994, Month.JANUARY, 06);
    releaseNotes = "Wagga wagga";
    projectRelease = project;

    release = new Release(releaseName, releaseDescription, releaseNotes,
                          releaseDate, projectRelease);

    mainApp.addRelease(release);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.RELEASE_CREATE);
    undoRedoObject.setAgileItem(release);
    undoRedoObject.addDatum(new Release(release));

    undoRedoHandler.newAction(undoRedoObject);
  }



  @Test
  public void testPersonRevert() throws Exception {

    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();

    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    mainApp.revert();

    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());


  }

  @Test
  public void testProjectRevert() throws Exception {

    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newProject();

    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    mainApp.revert();

    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    
  }

  @Test
  public void testClearStacks() throws Exception {
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    newPerson();
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    // Add a random person to the redo stack
    Person tempPerson = new Person(personLabel, firstName, lastName, skillSet);
    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_CREATE);
    undoRedoObject.addDatum(new Person(tempPerson));
    undoRedoHandler.getRedoStack().add(undoRedoObject);

    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    // Clear stacks
    mainApp.revert();
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }
}
