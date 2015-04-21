package seng302.group5.model.undoredo;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.controller.ListMainPaneController;
import seng302.group5.model.AgileItem;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Skill;
import seng302.group5.model.Team;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for undo and redo handler
 */
public class UndoRedoHandlerTest {

  private String personID;
  private String firstName;
  private String lastName;
  private ObservableList<Skill> skillSet;
  private String newPersonID;
  private String newFirstName;
  private String newLastName;
  private ObservableList<Skill> newSkillSet;

  private String skillName;
  private String skillDescription;
  private String newSkillName;
  private String newSkillDescription;

  private String teamID;
  private ObservableList<Person> teamMembers;
  private String teamDescription;
  private String newTeamID;
  private String newTeamDescription;
  private ObservableList<Person> newTeamMembers;

  private String projectID;
  private String projectName;
  private String projectDescription;
  private String newProjectID;
  private String newProjectName;
  private String newProjectDescription;

  private String releaseName;
  private String releaseDescription;
  private LocalDate releaseDate;
  private String releaseNotes;
  private Project projectRelease;
  private String newReleaseName;
  private String newReleaseDescription;
  private LocalDate newReleaseDate;
  private String newReleaseNotes;
  private Project newProjectRelease;

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
    personID = "ssc55";
    firstName = "Su-Shing";
    lastName = "Chen";
    skillSet = FXCollections.observableArrayList();
    person = new Person(personID, firstName, lastName, skillSet);

    mainApp.addPerson(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_CREATE);
    undoRedoObject.setAgileItem(person);
    undoRedoObject.addDatum(new Person(person));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void deleteNewestPerson() {
    if (person.getTeam() != null) {
      person.getTeam().getTeamMembers().remove(person);
    }
    mainApp.deletePerson(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_DELETE);
    undoRedoObject.setAgileItem(person);
    undoRedoObject.addDatum(new Person(person));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void editNewestPerson() {
    Person lastPerson = new Person(person);

    newPersonID = "apw76";
    newFirstName = "Alex";
    newLastName = "Woo";
    newSkillSet = FXCollections.observableArrayList();

    person.setPersonID(newPersonID);
    person.setFirstName(newFirstName);
    person.setLastName(newLastName);
    person.setSkillSet(newSkillSet);

    Person newPerson = new Person(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_EDIT);
    undoRedoObject.setAgileItem(person);
    undoRedoObject.addDatum(lastPerson);
    undoRedoObject.addDatum(newPerson);

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void newPersonWithSkill() {
    personID = "ssc55";
    firstName = "Su-Shing";
    lastName = "Chen";
    skillSet = FXCollections.observableArrayList();
    skillSet.add(skill);
    person = new Person(personID, firstName, lastName, skillSet);
    skillSet = FXCollections.observableArrayList(skillSet); // save a copy

    mainApp.addPerson(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_CREATE);
    undoRedoObject.setAgileItem(person);
    undoRedoObject.addDatum(new Person(person));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void editNewestPersonWithSkill() {
    Person lastPerson = new Person(person);

    newPersonID = "apw76";
    newFirstName = "Alex";
    newLastName = "Woo";
    newSkillSet = FXCollections.observableArrayList();
    newSkillSet.add(skill);

    person.setPersonID(newPersonID);
    person.setFirstName(newFirstName);
    person.setLastName(newLastName);
    person.setSkillSet(newSkillSet);
    newSkillSet = FXCollections.observableArrayList(newSkillSet); // save a copy

    Person newPerson = new Person(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_EDIT);
    undoRedoObject.setAgileItem(person);
    undoRedoObject.addDatum(lastPerson);
    undoRedoObject.addDatum(newPerson);

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

  private void deleteNewestSkill() {
    ArrayList<Person> skillUsers = new ArrayList<>();
    for (Person skillPerson : mainApp.getPeople()) {
      //check if they have the skill
      if (skillPerson.getSkillSet().contains(skill)) {
        skillUsers.add(skillPerson);
      }
    }
    for (Person skillUser : skillUsers) {
      skillUser.getSkillSet().remove(skill);
    }
    mainApp.deleteSkill(skill);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.SKILL_DELETE);
    undoRedoObject.setAgileItem(skill);
    undoRedoObject.addDatum(new Skill(skill));
    for (Person skillUser : skillUsers) {
      // Add data so users can get the skill back after undo
      undoRedoObject.addDatum(skillUser);
    }

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void editNewestSkill() {
    Skill lastSkill = new Skill(skill);

    newSkillName = "Python";
    newSkillDescription = "Person can program in the Python language";
    skill.setSkillName(newSkillName);
    skill.setSkillDescription(newSkillDescription);

    Skill newSkill = new Skill(skill);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.SKILL_EDIT);
    undoRedoObject.setAgileItem(skill);
    undoRedoObject.addDatum(lastSkill);
    undoRedoObject.addDatum(newSkill);

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

  private void deleteNewestTeam() {
    for (Person teamPerson : team.getTeamMembers()) {
      mainApp.deletePerson(teamPerson);
    }
    mainApp.deleteTeam(team);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.TEAM_DELETE);
    undoRedoObject.setAgileItem(team);
    undoRedoObject.addDatum(new Team(team));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void editNewestTeam() {
    Team lastTeam = new Team(team);

    newTeamID = "TheWomen";
    newTeamDescription = "This is a womanly team";
    newTeamMembers = FXCollections.observableArrayList();

    team.setTeamID(newTeamID);
    team.setTeamDescription(newTeamDescription);

    Team newTeam = new Team(team);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.TEAM_EDIT);
    undoRedoObject.setAgileItem(team);
    undoRedoObject.addDatum(lastTeam);
    undoRedoObject.addDatum(newTeam);

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

  private void editNewestTeamWithMember() {
    Team lastTeam = new Team(team);

    newTeamID = "TheWomen";
    newTeamDescription = "This is a womanly team";
    newTeamMembers = FXCollections.observableArrayList();
    newTeamMembers.add(person);
    person.assignToTeam(team);

    team.setTeamID(newTeamID);
    team.setTeamDescription(newTeamDescription);
    team.setTeamMembers(newTeamMembers);

    Team newTeam = new Team(team);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.TEAM_EDIT);
    undoRedoObject.setAgileItem(team);
    undoRedoObject.addDatum(lastTeam);
    undoRedoObject.addDatum(newTeam);

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

  private void deleteNewestProject() {
    mainApp.deleteProject(project);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PROJECT_DELETE);
    undoRedoObject.setAgileItem(project);
    undoRedoObject.addDatum(new Project(project));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void editNewestProject() {
    Project lastProject = new Project(project);

    newProjectID = "New Proj";
    newProjectName = "The New Project's Name";
    newProjectDescription = "This is a new description";

    project.setProjectID(newProjectID);
    project.setProjectName(newProjectName);
    project.setProjectDescription(newProjectDescription);

    Project newProject = new Project(project);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PROJECT_EDIT);
    undoRedoObject.setAgileItem(project);
    undoRedoObject.addDatum(lastProject);
    undoRedoObject.addDatum(newProject);

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

  private void deleteNewestRelease() {
    mainApp.deleteRelease(release);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.RELEASE_DELETE);
    undoRedoObject.setAgileItem(release);
    undoRedoObject.addDatum(new Release(release));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void editNewestRelease() {
    Release lastRelease = new Release(release);

    newReleaseName = "NewRelease";
    newReleaseDescription = "The new descriptioning";
    newReleaseDate = LocalDate.of(1994, Month.JULY, 06);
    newReleaseNotes = "New Wagga wagga wagga";
    newProjectRelease = project;

    release.setReleaseName(newReleaseName);
    release.setReleaseDescription(newReleaseDescription);
    release.setReleaseDate(newReleaseDate);
    release.setReleaseNotes(newReleaseNotes);
    release.setProjectRelease(projectRelease);

    Release newRelease = new Release(release);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.RELEASE_EDIT);
    undoRedoObject.setAgileItem(release);
    undoRedoObject.addDatum(lastRelease);
    undoRedoObject.addDatum(newRelease);

    undoRedoHandler.newAction(undoRedoObject);
  }

  @Test
  public void testPeekUndoStack() throws Exception {
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();

    UndoRedoObject peekObject = undoRedoHandler.peekUndoStack();
    ArrayList<AgileItem> data = peekObject.getData();
    Person peekPerson = (Person) data.get(0);

    assertEquals(Action.PERSON_CREATE, peekObject.getAction());
    assertEquals(personID, peekPerson.getPersonID());
    assertEquals(firstName, peekPerson.getFirstName());
    assertEquals(lastName, peekPerson.getLastName());
  }

  @Test
  public void testClearStacks() throws Exception {
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    newPerson();
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    // Add a random person to the redo stack
    Person tempPerson = new Person(personID, firstName, lastName, skillSet);
    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_CREATE);
    undoRedoObject.addDatum(new Person(tempPerson));
    undoRedoHandler.getRedoStack().add(undoRedoObject);

    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    // Clear stacks
    undoRedoHandler.clearStacks();
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testPersonCreateUndo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
  }

  @Test
  public void testPersonCreateRedo() throws Exception {
    Person before;
    Person after;

    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    before = mainApp.getPeople().get(0);
    assertNotNull(before);

    undoRedoHandler.undo();
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    after = mainApp.getPeople().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testPersonDeleteUndo() throws Exception {
    Person before;
    Person after;

    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    before = mainApp.getPeople().get(0);
    assertNotNull(before);

    deleteNewestPerson();
    assertEquals(0, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    after = mainApp.getPeople().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testPersonDeleteRedo() throws Exception {
    Person before;
    Person after;

    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    before = mainApp.getPeople().get(0);
    assertNotNull(before);

    deleteNewestPerson();
    assertEquals(0, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());
    after = mainApp.getPeople().get(0);
    assertNotNull(after);

    assertSame(before, after);

    undoRedoHandler.redo();
    assertEquals(0, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testPersonEditUndo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newSkill();
    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Person createdPerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(personID, createdPerson.getPersonID());
    assertEquals(firstName, createdPerson.getFirstName());
    assertEquals(lastName, createdPerson.getLastName());
    assertTrue(createdPerson.getSkillSet().isEmpty());  // no skills yet

    editNewestPersonWithSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    Person editedPerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(newPersonID, editedPerson.getPersonID());
    assertEquals(newFirstName, editedPerson.getFirstName());
    assertEquals(newLastName, editedPerson.getLastName());
    assertEquals(newSkillSet, editedPerson.getSkillSet());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Person undonePerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(personID, undonePerson.getPersonID());
    assertEquals(firstName, undonePerson.getFirstName());
    assertEquals(lastName, undonePerson.getLastName());
    assertTrue(undonePerson.getSkillSet().isEmpty());  // no skills now

    assertEquals(1, mainApp.getSkills().size());
  }

  @Test
  public void testPersonEditRedo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Person createdPerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(personID, createdPerson.getPersonID());
    assertEquals(firstName, createdPerson.getFirstName());
    assertEquals(lastName, createdPerson.getLastName());
    assertTrue(createdPerson.getSkillSet().isEmpty());  // no skills yet

    editNewestPersonWithSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Person editedPerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(newPersonID, editedPerson.getPersonID());
    assertEquals(newFirstName, editedPerson.getFirstName());
    assertEquals(newLastName, editedPerson.getLastName());
    assertEquals(newSkillSet, editedPerson.getSkillSet());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Person undonePerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(personID, undonePerson.getPersonID());
    assertEquals(firstName, undonePerson.getFirstName());
    assertEquals(lastName, undonePerson.getLastName());
    assertTrue(undonePerson.getSkillSet().isEmpty());  // no skills now

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Person redonePerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(newPersonID, redonePerson.getPersonID());
    assertEquals(newFirstName, redonePerson.getFirstName());
    assertEquals(newLastName, redonePerson.getLastName());
    assertEquals(newSkillSet, redonePerson.getSkillSet()); // has skills again
  }

  /**
   * Undo an edit for a skill which a person already has
   */
  @Test
  public void testPersonEditExistingSkillUndo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newSkill();
    newPersonWithSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Skill oldSkill = person.getSkillSet().get(0);
    assertEquals(skillName, oldSkill.getSkillName());
    assertEquals(skillDescription, oldSkill.getSkillDescription());

    editNewestSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    Skill newSkill = person.getSkillSet().get(0);
    assertEquals(newSkillName, newSkill.getSkillName());
    assertEquals(newSkillDescription, newSkill.getSkillDescription());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Skill undoneSkill = person.getSkillSet().get(0);
    assertEquals(skillName, undoneSkill.getSkillName());
    assertEquals(skillDescription, undoneSkill.getSkillDescription());
  }

  /**
   * Redo an edit for a skill which a person already has
   */
  @Test
  public void testPersonEditExistingSkillRedo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    newPersonWithSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Skill oldSkill = person.getSkillSet().get(0);
    assertEquals(skillName, oldSkill.getSkillName());
    assertEquals(skillDescription, oldSkill.getSkillDescription());

    editNewestSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Skill newSkill = person.getSkillSet().get(0);
    assertEquals(newSkillName, newSkill.getSkillName());
    assertEquals(newSkillDescription, newSkill.getSkillDescription());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Skill undoneSkill = person.getSkillSet().get(0);
    assertEquals(skillName, undoneSkill.getSkillName());
    assertEquals(skillDescription, undoneSkill.getSkillDescription());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(0, undoRedoHandler.getRedoStack().size());

    Skill redoneSkill = person.getSkillSet().get(0);
    assertEquals(newSkillName, redoneSkill.getSkillName());
    assertEquals(newSkillDescription, redoneSkill.getSkillDescription());
  }

  /**
   * Redo an edit for a skill which a person already has but undo back to beginning
   */
  @Test
  public void testPersonEditExistingSkillRedoDeep() throws Exception {
    // Object references will change. Avoid person variable.
    Skill skillOfPerson;
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    newPersonWithSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    skillOfPerson = mainApp.getPeople().get(0).getSkillSet().get(0);
    assertEquals(skillName, skillOfPerson.getSkillName());
    assertEquals(skillDescription, skillOfPerson.getSkillDescription());

    editNewestSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    skillOfPerson = mainApp.getPeople().get(0).getSkillSet().get(0);
    assertEquals(newSkillName, skillOfPerson.getSkillName());
    assertEquals(newSkillDescription, skillOfPerson.getSkillDescription());

    undoRedoHandler.undo(); // edit skill
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    skillOfPerson = mainApp.getPeople().get(0).getSkillSet().get(0);
    assertEquals(skillName, skillOfPerson.getSkillName());
    assertEquals(skillDescription, skillOfPerson.getSkillDescription());

    undoRedoHandler.undo(); // new person
    undoRedoHandler.undo(); // new skill
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
    undoRedoHandler.redo(); // new skill
    undoRedoHandler.redo(); // new person

    skillOfPerson = mainApp.getPeople().get(0).getSkillSet().get(0);
    assertEquals(skillName, skillOfPerson.getSkillName());
    assertEquals(skillDescription, skillOfPerson.getSkillDescription());

    undoRedoHandler.redo(); // edit skill
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(0, undoRedoHandler.getRedoStack().size());

    skillOfPerson = mainApp.getPeople().get(0).getSkillSet().get(0);
    assertEquals(newSkillName, skillOfPerson.getSkillName());
    assertEquals(newSkillDescription, skillOfPerson.getSkillDescription());
  }

  @Test
  public void testSkillCreateUndo() throws Exception {
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
  }

  @Test
  public void testSkillCreateRedo() throws Exception {
    Skill before;
    Skill after;

    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    before = mainApp.getSkills().get(0);
    assertNotNull(before);

    undoRedoHandler.undo();
    assertTrue(mainApp.getSkills().isEmpty());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    after = mainApp.getSkills().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testSkillDeleteUndo() throws Exception {
    Skill before;
    Skill after;

    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    before = mainApp.getSkills().get(0);
    assertNotNull(before);

    deleteNewestSkill();
    assertEquals(0, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    after = mainApp.getSkills().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testSkillDeleteRedo() throws Exception {
    Skill before;
    Skill after;

    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    before = mainApp.getSkills().get(0);
    assertNotNull(before);

    deleteNewestSkill();
    assertEquals(0, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());
    after = mainApp.getSkills().get(0);
    assertNotNull(after);

    assertSame(before, after);

    undoRedoHandler.redo();
    assertEquals(0, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSkillEditUndo() throws Exception {
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    Skill createdSkill = mainApp.getSkills().get(mainApp.getSkills().size() - 1);
    assertEquals(skillName, createdSkill.getSkillName());
    assertEquals(skillDescription, createdSkill.getSkillDescription());

    editNewestSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Skill editedSkill = mainApp.getSkills().get(mainApp.getSkills().size() - 1);
    assertEquals(newSkillName, editedSkill.getSkillName());
    assertEquals(newSkillDescription, editedSkill.getSkillDescription());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    Skill undoneSkill = mainApp.getSkills().get(mainApp.getSkills().size() - 1);
    assertEquals(skillName, undoneSkill.getSkillName());
    assertEquals(skillDescription, undoneSkill.getSkillDescription());
  }

  @Test
  public void testSkillEditRedo() throws Exception {
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Skill createdSkill = mainApp.getSkills().get(mainApp.getSkills().size() - 1);
    assertEquals(skillName, createdSkill.getSkillName());
    assertEquals(skillDescription, createdSkill.getSkillDescription());

    editNewestSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Skill editedSkill = mainApp.getSkills().get(mainApp.getSkills().size() - 1);
    assertEquals(newSkillName, editedSkill.getSkillName());
    assertEquals(newSkillDescription, editedSkill.getSkillDescription());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Skill undoneSkill = mainApp.getSkills().get(mainApp.getSkills().size() - 1);
    assertEquals(skillName, undoneSkill.getSkillName());
    assertEquals(skillDescription, undoneSkill.getSkillDescription());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Skill redoneSkill = mainApp.getSkills().get(mainApp.getSkills().size() - 1);
    assertEquals(newSkillName, redoneSkill.getSkillName());
    assertEquals(newSkillDescription, redoneSkill.getSkillDescription());
  }


  @Test
  public void testTeamCreateUndo() throws Exception {
    //testing creating and undoing an empty team
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());

    newTeam();

    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();

    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
  }

  @Test
  public void testTeamCreateRedo() throws Exception {
    Team before;
    Team after;

    //testing creating and redoing an empty team
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());

    newTeam();

    assertEquals(1, mainApp.getTeams().size());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());
    before = mainApp.getTeams().get(0);
    assertNotNull(before);

    undoRedoHandler.undo();

    assertTrue(mainApp.getTeams().isEmpty());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getTeams().size());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());
    after = mainApp.getTeams().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testTeamDeleteUndo() throws Exception {
    Team before;
    Team after;

    //testing deleting and undoing an empty team
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());

    newTeam();

    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    before = mainApp.getTeams().get(0);
    assertNotNull(before);

    deleteNewestTeam();

    assertTrue(mainApp.getTeams().isEmpty());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();

    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    after = mainApp.getTeams().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testTeamDeleteRedo() throws Exception {
    Team before;
    Team after;

    //testing deleting and redoing an empty team
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());

    newTeam();

    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());
    before = mainApp.getTeams().get(0);
    assertNotNull(before);

    deleteNewestTeam();

    assertTrue(mainApp.getTeams().isEmpty());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());

    undoRedoHandler.undo();

    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());
    after = mainApp.getTeams().get(0);
    assertNotNull(after);

    assertSame(before, after);

    undoRedoHandler.redo();
    assertTrue(mainApp.getTeams().isEmpty());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());
  }

  @Test
  public void testTeamEditUndo() throws Exception {
    //testing editing and redoing an empty team
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newTeam();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    Team createdTeam = mainApp.getTeams().get(mainApp.getTeams().size() - 1);
    assertEquals(teamID, createdTeam.getTeamID());
    assertEquals(teamMembers, createdTeam.getTeamMembers());
    assertEquals(teamDescription, createdTeam.getTeamDescription());

    editNewestTeam();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Team editedTeam = mainApp.getTeams().get(mainApp.getTeams().size() - 1);
    assertEquals(newTeamID, editedTeam.getTeamID());
    assertEquals(newTeamMembers, editedTeam.getTeamMembers());
    assertEquals(newTeamDescription, editedTeam.getTeamDescription());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    Team undoneTeam = mainApp.getTeams().get(mainApp.getTeams().size() - 1);
    assertEquals(teamID, undoneTeam.getTeamID());
    assertEquals(teamMembers, undoneTeam.getTeamMembers());
    assertEquals(teamDescription, undoneTeam.getTeamDescription());
  }

  @Test
  public void testTeamEditRedo() throws Exception {
    //testing editing and redoing an empty team
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newTeam();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Team createdTeam = mainApp.getTeams().get(mainApp.getTeams().size() - 1);
    assertEquals(teamID, createdTeam.getTeamID());
    assertEquals(teamMembers, createdTeam.getTeamMembers());
    assertEquals(teamDescription, createdTeam.getTeamDescription());

    editNewestTeam();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Team editedTeam = mainApp.getTeams().get(mainApp.getTeams().size() - 1);
    assertEquals(newTeamID, editedTeam.getTeamID());
    assertEquals(newTeamMembers, editedTeam.getTeamMembers());
    assertEquals(newTeamDescription, editedTeam.getTeamDescription());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Team undoneTeam = mainApp.getTeams().get(mainApp.getTeams().size() - 1);
    assertEquals(teamID, undoneTeam.getTeamID());
    assertEquals(teamMembers, undoneTeam.getTeamMembers());
    assertEquals(teamDescription, undoneTeam.getTeamDescription());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Team redoneTeam = mainApp.getTeams().get(mainApp.getTeams().size() - 1);
    assertEquals(newTeamID, redoneTeam.getTeamID());
    assertEquals(newTeamMembers, redoneTeam.getTeamMembers());
    assertEquals(newTeamDescription, redoneTeam.getTeamDescription());
  }

  /**
   * Undo an edit for a person which is part of a team
   */
  @Test
  public void testTeamEditExistingMemberUndo() throws Exception {
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    newTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Person oldPerson = team.getTeamMembers().get(0);
    assertEquals(personID, oldPerson.getPersonID());
    assertEquals(firstName, oldPerson.getFirstName());
    assertEquals(lastName, oldPerson.getLastName());

    editNewestPerson();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    Person newPerson = team.getTeamMembers().get(0);
    assertEquals(newPersonID, newPerson.getPersonID());
    assertEquals(newFirstName, newPerson.getFirstName());
    assertEquals(newLastName, newPerson.getLastName());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Person undonePerson = team.getTeamMembers().get(0);
    assertEquals(personID, undonePerson.getPersonID());
    assertEquals(firstName, undonePerson.getFirstName());
    assertEquals(lastName, undonePerson.getLastName());
  }

  /**
   * Redo an edit for a person which is part of a team
   */
  @Test
  public void testTeamEditExistingMemberRedo() throws Exception {
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    newTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Person oldPerson = team.getTeamMembers().get(0);
    assertEquals(personID, oldPerson.getPersonID());
    assertEquals(firstName, oldPerson.getFirstName());
    assertEquals(lastName, oldPerson.getLastName());

    editNewestPerson();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    Person newPerson = team.getTeamMembers().get(0);
    assertEquals(newPersonID, newPerson.getPersonID());
    assertEquals(newFirstName, newPerson.getFirstName());
    assertEquals(newLastName, newPerson.getLastName());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Person undonePerson = team.getTeamMembers().get(0);
    assertEquals(personID, undonePerson.getPersonID());
    assertEquals(firstName, undonePerson.getFirstName());
    assertEquals(lastName, undonePerson.getLastName());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(0, undoRedoHandler.getRedoStack().size());

    Person redonePerson = team.getTeamMembers().get(0);
    assertEquals(newPersonID, redonePerson.getPersonID());
    assertEquals(newFirstName, redonePerson.getFirstName());
    assertEquals(newLastName, redonePerson.getLastName());
  }

  /**
   * Redo an edit for a person which is part of a team  but undo back to beginning
   */
  @Test
  public void testTeamEditExistingMemberRedoDeep() throws Exception {
    Person teamMember;

    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    newTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    teamMember = mainApp.getTeams().get(0).getTeamMembers().get(0);
    assertEquals(personID, teamMember.getPersonID());
    assertEquals(firstName, teamMember.getFirstName());
    assertEquals(lastName, teamMember.getLastName());

    editNewestPerson();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    teamMember = mainApp.getTeams().get(0).getTeamMembers().get(0);
    assertEquals(newPersonID, teamMember.getPersonID());
    assertEquals(newFirstName, teamMember.getFirstName());
    assertEquals(newLastName, teamMember.getLastName());

    undoRedoHandler.undo(); // edit person
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    teamMember = mainApp.getTeams().get(0).getTeamMembers().get(0);
    assertEquals(personID, teamMember.getPersonID());
    assertEquals(firstName, teamMember.getFirstName());
    assertEquals(lastName, teamMember.getLastName());

    undoRedoHandler.undo(); // new team
    undoRedoHandler.undo(); // new person
    assertTrue(undoRedoHandler.getUndoStack().empty());
    undoRedoHandler.redo(); // new person
    undoRedoHandler.redo(); // new team

    teamMember = mainApp.getTeams().get(0).getTeamMembers().get(0);
    assertEquals(personID, teamMember.getPersonID());
    assertEquals(firstName, teamMember.getFirstName());
    assertEquals(lastName, teamMember.getLastName());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(0, undoRedoHandler.getRedoStack().size());

    teamMember = mainApp.getTeams().get(0).getTeamMembers().get(0);
    assertEquals(newPersonID, teamMember.getPersonID());
    assertEquals(newFirstName, teamMember.getFirstName());
    assertEquals(newLastName, teamMember.getLastName());
  }

  @Test
  public void testProjectCreateUndo() throws Exception {
    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newProject();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
  }

  @Test
  public void testProjectCreateRedo() throws Exception {
    Project before;
    Project after;

    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newProject();
    assertEquals(1, mainApp.getProjects().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    before = mainApp.getProjects().get(0);
    assertNotNull(before);

    undoRedoHandler.undo();
    assertTrue(mainApp.getProjects().isEmpty());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    after = mainApp.getProjects().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testProjectDeleteUndo() throws Exception {
    Project before;
    Project after;

    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newProject();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    before = mainApp.getProjects().get(0);
    assertNotNull(before);

    deleteNewestProject();
    assertEquals(0, mainApp.getProjects().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    after = mainApp.getProjects().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testProjectDeleteRedo() throws Exception {
    Project before;
    Project after;

    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newProject();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    before = mainApp.getProjects().get(0);
    assertNotNull(before);

    deleteNewestProject();
    assertEquals(0, mainApp.getProjects().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());
    after = mainApp.getProjects().get(0);
    assertNotNull(after);

    assertSame(before, after);

    undoRedoHandler.redo();
    assertEquals(0, mainApp.getProjects().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testProjectEditUndo() throws Exception {
    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newProject();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    Project createdProject = mainApp.getProjects().get(mainApp.getProjects().size() - 1);
    assertEquals(projectID, createdProject.getProjectID());
    assertEquals(projectName, createdProject.getProjectName());
    assertEquals(projectDescription, createdProject.getProjectDescription());

    editNewestProject();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Project editedProject = mainApp.getProjects().get(mainApp.getProjects().size() - 1);
    assertEquals(newProjectID, createdProject.getProjectID());
    assertEquals(newProjectName, editedProject.getProjectName());
    assertEquals(newProjectDescription, editedProject.getProjectDescription());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    Project undoneProject = mainApp.getProjects().get(mainApp.getProjects().size() - 1);
    assertEquals(projectID, createdProject.getProjectID());
    assertEquals(projectName, undoneProject.getProjectName());
    assertEquals(projectDescription, undoneProject.getProjectDescription());
  }

  @Test
  public void testProjectEditRedo() throws Exception {
    assertTrue(mainApp.getProjects().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newProject();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Project createdProject = mainApp.getProjects().get(mainApp.getProjects().size() - 1);
    assertEquals(projectID, createdProject.getProjectID());
    assertEquals(projectName, createdProject.getProjectName());
    assertEquals(projectDescription, createdProject.getProjectDescription());

    editNewestProject();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Project editedProject = mainApp.getProjects().get(mainApp.getProjects().size() - 1);
    assertEquals(newProjectName, editedProject.getProjectName());
    assertEquals(newProjectDescription, editedProject.getProjectDescription());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Project undoneProject = mainApp.getProjects().get(mainApp.getProjects().size() - 1);
    assertEquals(projectID, createdProject.getProjectID());
    assertEquals(projectName, undoneProject.getProjectName());
    assertEquals(projectDescription, undoneProject.getProjectDescription());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Project redoneProject = mainApp.getProjects().get(mainApp.getProjects().size() - 1);
    assertEquals(newProjectID, createdProject.getProjectID());
    assertEquals(newProjectName, redoneProject.getProjectName());
    assertEquals(newProjectDescription, redoneProject.getProjectDescription());
  }

  @Test
  public void testReleaseCreateUndo() throws Exception {
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newRelease();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
  }

  @Test
  public void testReleaseCreateRedo() throws Exception {
    Release before;
    Release after;

    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newRelease();
    assertEquals(1, mainApp.getReleases().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    before = mainApp.getReleases().get(0);
    assertNotNull(before);

    undoRedoHandler.undo();
    assertTrue(mainApp.getReleases().isEmpty());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    after = mainApp.getReleases().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testReleaseDeleteUndo() throws Exception {
    Release before;
    Release after;

    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newRelease();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    before = mainApp.getReleases().get(0);
    assertNotNull(before);

    deleteNewestRelease();
    assertEquals(0, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    after = mainApp.getReleases().get(0);
    assertNotNull(after);

    assertSame(before, after);
  }

  @Test
  public void testReleaseDeleteRedo() throws Exception {
    Release before;
    Release after;

    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newRelease();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
    before = mainApp.getReleases().get(0);
    assertNotNull(before);

    deleteNewestRelease();
    assertEquals(0, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());
    after = mainApp.getReleases().get(0);
    assertNotNull(after);

    assertSame(before, after);

    undoRedoHandler.redo();
    assertEquals(0, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testReleaseEditUndo() throws Exception {
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newRelease();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    Release createdRelease = mainApp.getReleases().get(mainApp.getReleases().size() - 1);
    assertEquals(releaseName, createdRelease.getReleaseName());
    assertEquals(releaseDescription, createdRelease.getReleaseDescription());
    assertEquals(releaseDate, createdRelease.getReleaseDate());
    assertEquals(releaseNotes, createdRelease.getReleaseNotes());
    assertEquals(projectRelease, createdRelease.getProjectRelease());

    editNewestRelease();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Release editedRelease = mainApp.getReleases().get(mainApp.getReleases().size() - 1);
    assertEquals(newReleaseName, editedRelease.getReleaseName());
    assertEquals(newReleaseDescription, editedRelease.getReleaseDescription());
    assertEquals(newReleaseDate, editedRelease.getReleaseDate());
    assertEquals(newReleaseNotes, editedRelease.getReleaseNotes());
    assertEquals(newProjectRelease, editedRelease.getProjectRelease());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    Release undoneRelease = mainApp.getReleases().get(mainApp.getReleases().size() - 1);
    assertEquals(releaseName, undoneRelease.getReleaseName());
    assertEquals(releaseDescription, undoneRelease.getReleaseDescription());
    assertEquals(releaseDate, undoneRelease.getReleaseDate());
    assertEquals(releaseNotes, undoneRelease.getReleaseNotes());
    assertEquals(projectRelease, undoneRelease.getProjectRelease());
  }

  @Test
  public void testReleaseEditRedo() throws Exception {
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newRelease();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Release createdRelease = mainApp.getReleases().get(mainApp.getReleases().size() - 1);
    assertEquals(releaseName, createdRelease.getReleaseName());
    assertEquals(releaseDescription, createdRelease.getReleaseDescription());
    assertEquals(releaseDate, createdRelease.getReleaseDate());
    assertEquals(releaseNotes, createdRelease.getReleaseNotes());
    assertEquals(projectRelease, createdRelease.getProjectRelease());

    editNewestRelease();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Release editedRelease = mainApp.getReleases().get(mainApp.getReleases().size() - 1);
    assertEquals(newReleaseName, editedRelease.getReleaseName());
    assertEquals(newReleaseDescription, editedRelease.getReleaseDescription());
    assertEquals(newReleaseDate, editedRelease.getReleaseDate());
    assertEquals(newReleaseNotes, editedRelease.getReleaseNotes());
    assertEquals(newProjectRelease, editedRelease.getProjectRelease());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Release undoneRelease = mainApp.getReleases().get(mainApp.getReleases().size() - 1);
    assertEquals(releaseName, undoneRelease.getReleaseName());
    assertEquals(releaseDescription, undoneRelease.getReleaseDescription());
    assertEquals(releaseDate, undoneRelease.getReleaseDate());
    assertEquals(releaseNotes, undoneRelease.getReleaseNotes());
    assertEquals(projectRelease, undoneRelease.getProjectRelease());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Release redoneRelease = mainApp.getReleases().get(mainApp.getReleases().size() - 1);
    assertEquals(newReleaseName, redoneRelease.getReleaseName());
    assertEquals(newReleaseDescription, redoneRelease.getReleaseDescription());
    assertEquals(newReleaseDate, redoneRelease.getReleaseDate());
    assertEquals(newReleaseNotes, redoneRelease.getReleaseNotes());
    assertEquals(newProjectRelease, redoneRelease.getProjectRelease());
  }

  /**
   * Undo an edit for a Release which is part of a Project
   */
  @Test
  public void testReleaseEditExistingProjectUndo() throws Exception {
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newProject();
    newRelease();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Release oldRelease = mainApp.getReleases().get(0);
    assertEquals(releaseName, oldRelease.getReleaseName());
    assertEquals(releaseDescription, oldRelease.getReleaseDescription());
    assertEquals(releaseDate, oldRelease.getReleaseDate());
    assertEquals(releaseNotes, oldRelease.getReleaseNotes());
    assertEquals(projectRelease, oldRelease.getProjectRelease());

    editNewestRelease();
    Release newRelease = mainApp.getReleases().get(0);
    assertEquals(newReleaseName, newRelease.getReleaseName());
    assertEquals(newReleaseDescription, newRelease.getReleaseDescription());
    assertEquals(newReleaseDate, newRelease.getReleaseDate());
    assertEquals(newReleaseNotes, newRelease.getReleaseNotes());
    assertEquals(newProjectRelease, newRelease.getProjectRelease());
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Release undoneRelease = mainApp.getReleases().get(0);
    assertEquals(releaseName, undoneRelease.getReleaseName());
    assertEquals(releaseDescription, undoneRelease.getReleaseDescription());
    assertEquals(releaseDate, undoneRelease.getReleaseDate());
    assertEquals(releaseNotes, undoneRelease.getReleaseNotes());
    assertEquals(projectRelease, undoneRelease.getProjectRelease());

  }

  /**
   * Redo an edit for a release which is part of a project
   */
  @Test
  public void testReleaseEditExistingProjectRedo() throws Exception {
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newProject();
    newRelease();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Release oldRelease = mainApp.getReleases().get(0);
    assertEquals(releaseName, oldRelease.getReleaseName());
    assertEquals(releaseDescription, oldRelease.getReleaseDescription());
    assertEquals(releaseDate, oldRelease.getReleaseDate());
    assertEquals(releaseNotes, oldRelease.getReleaseNotes());
    assertEquals(projectRelease, oldRelease.getProjectRelease());

    editNewestRelease();
    Release newRelease = mainApp.getReleases().get(0);
    assertEquals(newReleaseName, newRelease.getReleaseName());
    assertEquals(newReleaseDescription, newRelease.getReleaseDescription());
    assertEquals(newReleaseDate, newRelease.getReleaseDate());
    assertEquals(newReleaseNotes, newRelease.getReleaseNotes());
    assertEquals(newProjectRelease, newRelease.getProjectRelease());

    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Release undoneRelease = mainApp.getReleases().get(0);
    assertEquals(releaseName, undoneRelease.getReleaseName());
    assertEquals(releaseDescription, undoneRelease.getReleaseDescription());
    assertEquals(releaseDate, undoneRelease.getReleaseDate());
    assertEquals(releaseNotes, undoneRelease.getReleaseNotes());
    assertEquals(projectRelease, undoneRelease.getProjectRelease());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Release redoneRelease = mainApp.getReleases().get(0);
    assertEquals(newReleaseName, redoneRelease.getReleaseName());
    assertEquals(newReleaseDescription, redoneRelease.getReleaseDescription());
    assertEquals(newReleaseDate, redoneRelease.getReleaseDate());
    assertEquals(newReleaseNotes, redoneRelease.getReleaseNotes());
    assertEquals(newProjectRelease, redoneRelease.getProjectRelease());
  }

  /**
   * Redo an edit for a release which is part of a project  but undo back to beginning
   */
  @Test
  public void testReleaseEditExistingProjectRedoDeep() throws Exception {
    assertTrue(mainApp.getReleases().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newProject();
    newRelease();
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Release oldRelease = mainApp.getReleases().get(0);
    assertEquals(releaseName, oldRelease.getReleaseName());
    assertEquals(releaseDescription, oldRelease.getReleaseDescription());
    assertEquals(releaseDate, oldRelease.getReleaseDate());
    assertEquals(releaseNotes, oldRelease.getReleaseNotes());
    assertEquals(projectRelease, oldRelease.getProjectRelease());

    editNewestRelease();
    Release newRelease = mainApp.getReleases().get(0);
    assertEquals(newReleaseName, newRelease.getReleaseName());
    assertEquals(newReleaseDescription, newRelease.getReleaseDescription());
    assertEquals(newReleaseDate, newRelease.getReleaseDate());
    assertEquals(newReleaseNotes, newRelease.getReleaseNotes());
    assertEquals(newProjectRelease, newRelease.getProjectRelease());
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo(); //edit release
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    Release undoneRelease = mainApp.getReleases().get(0);
    assertEquals(releaseName, undoneRelease.getReleaseName());
    assertEquals(releaseDescription, undoneRelease.getReleaseDescription());
    assertEquals(releaseDate, undoneRelease.getReleaseDate());
    assertEquals(releaseNotes, undoneRelease.getReleaseNotes());
    assertEquals(projectRelease, undoneRelease.getProjectRelease());

    undoRedoHandler.undo(); // new release
    undoRedoHandler.undo(); // new project
    assertTrue(undoRedoHandler.getUndoStack().empty());
    undoRedoHandler.redo(); // new project
    undoRedoHandler.redo(); // new release

    oldRelease = mainApp.getReleases().get(0); //before the edit redo is done
    assertEquals(releaseName, oldRelease.getReleaseName());
    assertEquals(releaseDescription, oldRelease.getReleaseDescription());
    assertEquals(releaseDate, oldRelease.getReleaseDate());
    assertEquals(releaseNotes, oldRelease.getReleaseNotes());
    assertEquals(projectRelease, oldRelease.getProjectRelease());

    undoRedoHandler.redo(); //redoes the edit
    assertEquals(1, mainApp.getProjects().size());
    assertEquals(1, mainApp.getReleases().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    newRelease = mainApp.getReleases().get(0);
    assertEquals(newReleaseName, newRelease.getReleaseName());
    assertEquals(newReleaseDescription, newRelease.getReleaseDescription());
    assertEquals(newReleaseDate, newRelease.getReleaseDate());
    assertEquals(newReleaseNotes, newRelease.getReleaseNotes());
    assertEquals(newProjectRelease, newRelease.getProjectRelease());
  }


  @Test
  public void testSpecialDeleteSkillUndo() throws Exception {
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newSkill();
    newPersonWithSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, person.getSkillSet().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    deleteNewestSkill();
    assertEquals(1, mainApp.getPeople().size(), 1);
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(person.getSkillSet().isEmpty());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, person.getSkillSet().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
  }

  @Test
  public void testSpecialDeleteSkillRedo() throws Exception {
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    newPersonWithSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, person.getSkillSet().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    deleteNewestSkill();
    assertEquals(1, mainApp.getPeople().size(), 1);
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(person.getSkillSet().isEmpty());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, person.getSkillSet().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getPeople().size(), 1);
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(person.getSkillSet().isEmpty());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSpecialDeleteSkillRedoDeep() throws Exception {
    // Object references will change. Avoid person variable.
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    newPerson();
    editNewestPersonWithSkill();

    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, mainApp.getPeople().get(0).getSkillSet().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    deleteNewestSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getPeople().get(0).getSkillSet().isEmpty());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo(); // deleteNewestSkill
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, mainApp.getPeople().get(0).getSkillSet().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.undo(); // edit person
    undoRedoHandler.undo(); // new person
    undoRedoHandler.undo(); // new skill
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
    undoRedoHandler.redo(); // new skill
    undoRedoHandler.redo(); // new person
    undoRedoHandler.redo(); // edit person
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, mainApp.getPeople().get(0).getSkillSet().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo(); // deleteNewestSkill
    assertEquals(1, mainApp.getPeople().size());
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getPeople().get(0).getSkillSet().isEmpty());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSpecialDeleteSkillRedoDeeper() throws Exception {
    // Object references will change. Avoid person variable.
    Skill skillInList;
    Person personInList;

    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    newPerson();
    editNewestPersonWithSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, mainApp.getPeople().get(0).getSkillSet().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    skillInList = mainApp.getSkills().get(0);
    personInList = mainApp.getPeople().get(0);
    assertEquals(skillInList, personInList.getSkillSet().get(0));

    editNewestSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, mainApp.getPeople().get(0).getSkillSet().size());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    skillInList = mainApp.getSkills().get(0);
    personInList = mainApp.getPeople().get(0);
    assertEquals(skillInList, personInList.getSkillSet().get(0));

    deleteNewestSkill();
    assertEquals(1, mainApp.getPeople().size());
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getPeople().get(0).getSkillSet().isEmpty());
    assertEquals(5, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo(); // deleteNewestSkill
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, mainApp.getPeople().get(0).getSkillSet().size());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    skillInList = mainApp.getSkills().get(0);
    personInList = mainApp.getPeople().get(0);
    assertEquals(skillInList, personInList.getSkillSet().get(0));

    undoRedoHandler.undo(); // edit skill
    undoRedoHandler.undo(); // edit person
    undoRedoHandler.undo(); // new person
    undoRedoHandler.undo(); // new skill
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
    undoRedoHandler.redo(); // new skill
    undoRedoHandler.redo(); // new person
    assertTrue(mainApp.getPeople().get(0).getSkillSet().isEmpty());

    undoRedoHandler.redo(); // edit person
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, mainApp.getPeople().get(0).getSkillSet().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(2, undoRedoHandler.getRedoStack().size());

    skillInList = mainApp.getSkills().get(0);
    personInList = mainApp.getPeople().get(0);
    assertEquals(skillInList, personInList.getSkillSet().get(0));

    undoRedoHandler.redo(); // edit skill
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, mainApp.getPeople().get(0).getSkillSet().size());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    skillInList = mainApp.getSkills().get(0);
    personInList = mainApp.getPeople().get(0);
    assertEquals(skillInList, personInList.getSkillSet().get(0));

    undoRedoHandler.redo(); // deleteNewestSkill
    assertEquals(1, mainApp.getPeople().size());
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(mainApp.getPeople().get(0).getSkillSet().isEmpty());
    assertEquals(5, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSpecialDeletePersonUndo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    newTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, team.getTeamMembers().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    deleteNewestPerson();
    assertEquals(1, mainApp.getTeams().size());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(team.getTeamMembers().isEmpty());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, team.getTeamMembers().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
  }

  @Test
  public void testSpecialDeletePersonRedo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    newTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, team.getTeamMembers().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    deleteNewestPerson();
    assertEquals(1, mainApp.getTeams().size());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(team.getTeamMembers().isEmpty());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, team.getTeamMembers().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getTeams().size());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(team.getTeamMembers().isEmpty());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSpecialDeletePersonRedoDeep() throws Exception {
    // Object references will change. Avoid team variable.
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    newTeam();
    editNewestTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    deleteNewestPerson();
    assertEquals(1, mainApp.getTeams().size());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().get(0).getTeamMembers().isEmpty());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo(); // deleteNewestPerson
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.undo(); // edit team
    undoRedoHandler.undo(); // new team
    undoRedoHandler.undo(); // new person
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
    undoRedoHandler.redo(); // new person
    undoRedoHandler.redo(); // new team
    undoRedoHandler.redo(); // edit team
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo(); // deleteNewestPerson
    assertEquals(1, mainApp.getTeams().size());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().get(0).getTeamMembers().isEmpty());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSpecialDeletePersonRedoDeeper() throws Exception {
    // Object references will change. Avoid team variable.
    Person personInList;
    Team teamInList;

    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    newTeam();
    editNewestTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    personInList = mainApp.getPeople().get(0);
    teamInList = mainApp.getTeams().get(0);
    assertEquals(personInList, teamInList.getTeamMembers().get(0));
    assertEquals(teamInList, personInList.getTeam());

    editNewestPerson();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    personInList = mainApp.getPeople().get(0);
    teamInList = mainApp.getTeams().get(0);
    assertEquals(personInList, teamInList.getTeamMembers().get(0));
    assertEquals(teamInList, personInList.getTeam());

    deleteNewestPerson();
    assertEquals(1, mainApp.getTeams().size());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().get(0).getTeamMembers().isEmpty());
    assertEquals(5, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo(); // deleteNewestPerson
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    personInList = mainApp.getPeople().get(0);
    teamInList = mainApp.getTeams().get(0);
    assertEquals(personInList, teamInList.getTeamMembers().get(0));
    assertEquals(teamInList, personInList.getTeam());

    undoRedoHandler.undo(); // edit person
    undoRedoHandler.undo(); // edit team
    undoRedoHandler.undo(); // new team
    undoRedoHandler.undo(); // new person
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
    undoRedoHandler.redo(); // new person
    undoRedoHandler.redo(); // new team
    assertTrue(mainApp.getTeams().get(0).getTeamMembers().isEmpty());

    undoRedoHandler.redo(); // edit team
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(2, undoRedoHandler.getRedoStack().size());

    personInList = mainApp.getPeople().get(0);
    teamInList = mainApp.getTeams().get(0);
    assertEquals(personInList, teamInList.getTeamMembers().get(0));
    assertEquals(teamInList, personInList.getTeam());

    undoRedoHandler.redo(); // edit person
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    personInList = mainApp.getPeople().get(0);
    teamInList = mainApp.getTeams().get(0);
    assertEquals(personInList, teamInList.getTeamMembers().get(0));
    assertEquals(teamInList, personInList.getTeam());

    undoRedoHandler.redo(); // deleteNewestPerson
    assertEquals(1, mainApp.getTeams().size());
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().get(0).getTeamMembers().isEmpty());
    assertEquals(5, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSpecialDeleteTeamUndo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    newTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, team.getTeamMembers().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    deleteNewestTeam();
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertEquals(3, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, team.getTeamMembers().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
  }

  @Test
  public void testSpecialDeleteTeamRedo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    newTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, team.getTeamMembers().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    deleteNewestTeam();
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, team.getTeamMembers().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSpecialDeleteTeamRedoDeep() throws Exception {
    // Object references will change. Avoid team variable.
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    newTeam();
    editNewestTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    deleteNewestTeam();
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo(); // deleteNewestTeam
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.undo(); // edit team
    undoRedoHandler.undo(); // new team
    undoRedoHandler.undo(); // new person
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
    undoRedoHandler.redo(); // new person
    undoRedoHandler.redo(); // new team

    undoRedoHandler.redo(); // edit team
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Person personInList = mainApp.getPeople().get(0);
    Team teamInList = mainApp.getTeams().get(0);
    assertEquals(personInList, teamInList.getTeamMembers().get(0));
    assertEquals(teamInList, personInList.getTeam());

    undoRedoHandler.redo(); // deleteNewestTeam
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSpecialDeleteTeamRedoDeeper() throws Exception {
    // Object references will change. Avoid team variable.
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    newTeam();
    editNewestTeamWithMember();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    editNewestPerson();
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    deleteNewestTeam();
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertEquals(5, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo(); // deleteNewestTeam
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.undo(); // edit person
    undoRedoHandler.undo(); // edit team
    undoRedoHandler.undo(); // new team
    undoRedoHandler.undo(); // new person
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
    undoRedoHandler.redo(); // new person
    undoRedoHandler.redo(); // new team
    assertTrue(mainApp.getTeams().get(0).getTeamMembers().isEmpty());

    undoRedoHandler.redo(); // edit team
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(3, undoRedoHandler.getUndoStack().size());
    assertEquals(2, undoRedoHandler.getRedoStack().size());

    Person personInList = mainApp.getPeople().get(0);
    Team teamInList = mainApp.getTeams().get(0);
    assertEquals(personInList, teamInList.getTeamMembers().get(0));
    assertEquals(teamInList, personInList.getTeam());

    undoRedoHandler.redo(); // edit person
    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, mainApp.getTeams().get(0).getTeamMembers().size());
    assertEquals(4, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Person editedPersonInList = mainApp.getPeople().get(0);
    Team editedTeamInList = mainApp.getTeams().get(0);
    assertEquals(editedPersonInList, editedTeamInList.getTeamMembers().get(0));
    assertEquals(editedTeamInList, editedPersonInList.getTeam());

    undoRedoHandler.redo(); // deleteNewestTeam
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(mainApp.getPeople().isEmpty());
    assertEquals(5, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

}
