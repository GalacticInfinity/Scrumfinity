package seng302.group5.model.util;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.ListMainPaneController;
import seng302.group5.controller.MenuBarController;
import seng302.group5.model.Backlog;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Skill;
import seng302.group5.model.Story;
import seng302.group5.model.Team;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoHandler;
import seng302.group5.model.undoredo.UndoRedoObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Unit testing for revert function.
 * @author Alex Woo, Liang Ma
 */
public class RevertHandlerTest {

  private String personLabel;
  private String firstName;
  private String lastName;
  private ObservableList<Skill> skillSet;

  private String skillName;
  private String skillDescription;

  private String teamLabel;
  private ObservableList<Person> teamMembers;
  private String teamDescription;

  private String projectLabel;
  private String projectName;
  private String projectDescription;

  private String releaseName;
  private String releaseDescription;
  private LocalDate releaseDate;
  private String releaseNotes;
  private Project projectRelease;

  private String storyLabel;
  private String storyLongName;
  private String storyDescription;
  private Person storyCreator;
  private ObservableList<String> storyACs;

  private String backlogLabel;
  private String backlogName;
  private String backlogDescription;
  private Person productOwner;
  private List<Story> backlogStories;

  private Person person;
  private Skill skill;
  private Team team;
  private Project project;
  private Release release;
  private Story story;
  private Backlog backlog;

  private UndoRedoHandler undoRedoHandler;
  private Main mainApp;

  @Before
  public void setUp() throws Exception {

    ListMainPaneController listMainPaneController = mock(ListMainPaneController.class);
    MenuBarController menuBarController = mock(MenuBarController.class);
    Stage primaryStage = mock(Stage.class);

    mainApp = new Main();
    mainApp.setLMPC(listMainPaneController);
    mainApp.setMBC(menuBarController);
    mainApp.setPrimaryStage(primaryStage);

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
    teamLabel = "TheMen";
    teamMembers = FXCollections.observableArrayList();
    teamDescription = "This is a manly team";
    team = new Team(teamLabel, teamMembers, teamDescription);

    mainApp.addTeam(team);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.TEAM_CREATE);
    undoRedoObject.setAgileItem(team);
    undoRedoObject.addDatum(new Team(team));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void newTeamWithMember() {
    teamLabel = "TheMen";
    teamMembers = FXCollections.observableArrayList();
    teamMembers.add(person);
    teamDescription = "This is a manly team";
    team = new Team(teamLabel, teamMembers, teamDescription);
    person.assignToTeam(team);

    mainApp.addTeam(team);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.TEAM_CREATE);
    undoRedoObject.setAgileItem(team);
    undoRedoObject.addDatum(new Team(team));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void newProject() {
    projectLabel = "proj";
    projectName = "The Project's Name";
    projectDescription = "This is a description for the project";
    project = new Project(projectLabel, projectName, projectDescription);

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

  private void newStory() {
    storyLabel = "testS";
    storyLongName ="testStory!";
    storyDescription ="This is a story and it is good";
    storyCreator = null;

    story = new Story(storyLabel, storyLongName, storyDescription, storyCreator, null); //null is fine

    mainApp.addStory(story);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.STORY_CREATE);
    undoRedoObject.setAgileItem(story);
    undoRedoObject.addDatum(new Story(story));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void newStoryWithAC() {
    storyLabel = "testS";
    storyLongName ="testStory!";
    storyDescription ="This is a story and it is good";
    storyCreator = null;
    storyACs = FXCollections.observableArrayList();
    storyACs.add("FRREEEDOOOMMMMM!!!!!");

    story = new Story(storyLabel, storyLongName, storyDescription, storyCreator, storyACs);

    mainApp.addStory(story);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.STORY_CREATE);
    undoRedoObject.setAgileItem(story);
    undoRedoObject.addDatum(new Story(story));

    undoRedoHandler.newAction(undoRedoObject);
  }


  private void newBacklog() {
    backlogLabel = "Backlog";
    backlogName = "This is a backlog";
    backlogDescription = "Once upon a time...BAM!";
    productOwner = person;
    backlog = new Backlog(backlogLabel, backlogName, backlogDescription, productOwner, null); //TODO ADDED NULL SO IT COMPILED WITH BACKLOGS HAVING ESTIMATE SCALES
    backlog.addStory(story);
    backlogStories = backlog.getStories();
    mainApp.addBacklog(backlog);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.BACKLOG_CREATE);
    undoRedoObject.setAgileItem(backlog);
    undoRedoObject.addDatum(new Backlog(backlog));

    undoRedoHandler.newAction(undoRedoObject);
  }



  @Test
  public void testPersonRevert() throws Exception {

    assertTrue(mainApp.getPeople().isEmpty());

    newPerson();

    assertEquals(1, mainApp.getPeople().size());

    mainApp.revert();

    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());


  }

  @Test
  public void testProjectRevert() throws Exception {

    assertTrue(mainApp.getProjects().isEmpty());

    newProject();

    assertEquals(1, mainApp.getProjects().size());

    mainApp.revert();

    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testSkillRevert() throws Exception {

    assertTrue(mainApp.getSkills().isEmpty());

    newSkill();

    assertEquals(1, mainApp.getSkills().size());

    mainApp.revert();

    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testTeamRevert() throws Exception {

    assertTrue(mainApp.getTeams().isEmpty());

    newTeam();

    assertEquals(1, mainApp.getTeams().size());

    mainApp.revert();

    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testReleaseRevert() throws Exception {

    assertTrue(mainApp.getReleases().isEmpty());

    newRelease();

    assertEquals(1, mainApp.getReleases().size());

    mainApp.revert();

    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testStoryRevert() throws Exception {

    assertTrue(mainApp.getStories().isEmpty());

    newStory();

    assertEquals(1, mainApp.getStories().size());

    mainApp.revert();

    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testStoryWithACRevert() throws Exception {

    assertTrue(mainApp.getStories().isEmpty());

    newStoryWithAC();

    assertEquals(1, mainApp.getStories().size());

    mainApp.revert();

    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testStoryWith2ACRevert() throws Exception {

    assertTrue(mainApp.getStories().isEmpty());

    newStoryWithAC();
    newStoryWithAC();

    assertEquals(2, mainApp.getStories().size());

    mainApp.revert();

    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testStoryWithACAndNotRevert() throws Exception {

    assertTrue(mainApp.getStories().isEmpty());

    newStoryWithAC();
    newStory();

    assertEquals(2, mainApp.getStories().size());

    mainApp.revert();

    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testStoryWith2ACAndRandomObjectsRevert() throws Exception {

    assertTrue(mainApp.getStories().isEmpty());

    newPerson();
    newStoryWithAC();
    newStoryWithAC();
    newTeam();

    assertEquals(2, mainApp.getStories().size());

    mainApp.revert();

    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testBacklogRevert() throws Exception {

    assertTrue(mainApp.getBacklogs().isEmpty());

    newPerson();
    newStory();
    newBacklog();

    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getStories().size());
    assertEquals(1, mainApp.getBacklogs().size());

    mainApp.revert();

    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(mainApp.getBacklogs().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

  }

  @Test
  public void testEmptyRevert() throws Exception {
    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(mainApp.getBacklogs().isEmpty());

    mainApp.revert();

    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(mainApp.getBacklogs().isEmpty());
  }

  @Test
  public void testMultipleRevert() throws Exception {

    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(mainApp.getBacklogs().isEmpty());

    newStory();
    newProject();
    newSkill();
    newPersonWithSkill();
    newPerson();
    newProject();
    newRelease();
    newTeam();
    newTeam();
    newTeamWithMember();
    newSkill();
    newSkill();
    newStory();
    newBacklog();

    assertEquals(2, mainApp.getStories().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(3, mainApp.getTeams().size());
    assertEquals(2, mainApp.getPeople().size());
    assertEquals(3, mainApp.getSkills().size());
    assertEquals(2, mainApp.getProjects().size());
    assertEquals(1, mainApp.getBacklogs().size());

    mainApp.revert();

    assertTrue(mainApp.getStories().isEmpty());
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(mainApp.getBacklogs().isEmpty());
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
