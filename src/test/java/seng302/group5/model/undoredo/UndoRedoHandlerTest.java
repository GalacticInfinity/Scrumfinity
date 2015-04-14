package seng302.group5.model.undoredo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.controller.ListMainPaneController;
import seng302.group5.model.AgileItem;
import seng302.group5.model.Person;
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

  private Person person;
  private Skill skill;
  private Team team;

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
    undoRedoObject.addDatum(new Person(person));

    undoRedoHandler.newAction(undoRedoObject);
  }

  private void editNewestPerson() {
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

    Person newPerson = new Person(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_EDIT);
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

    mainApp.addPerson(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_CREATE);
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
    undoRedoObject.addDatum(new Skill(skill));
    for (Person skillUser : skillUsers) {
      // Add data so users can get the skill back after undo
      undoRedoObject.addDatum(new Person(skillUser));
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
    undoRedoObject.addDatum(lastTeam);
    undoRedoObject.addDatum(newTeam);

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
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testPersonDeleteUndo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    deleteNewestPerson();
    assertEquals(0, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
  }

  @Test
  public void testPersonDeleteRedo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    deleteNewestPerson();
    assertEquals(0, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

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

    editNewestPerson();
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

    newPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Person createdPerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(personID, createdPerson.getPersonID());
    assertEquals(firstName, createdPerson.getFirstName());
    assertEquals(lastName, createdPerson.getLastName());
    assertTrue(createdPerson.getSkillSet().isEmpty());  // no skills yet

    editNewestPerson();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    Person editedPerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(newPersonID, editedPerson.getPersonID());
    assertEquals(newFirstName, editedPerson.getFirstName());
    assertEquals(newLastName, editedPerson.getLastName());
    assertEquals(newSkillSet, editedPerson.getSkillSet());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    Person undonePerson = mainApp.getPeople().get(mainApp.getPeople().size() - 1);
    assertEquals(personID, undonePerson.getPersonID());
    assertEquals(firstName, undonePerson.getFirstName());
    assertEquals(lastName, undonePerson.getLastName());
    assertTrue(undonePerson.getSkillSet().isEmpty());  // no skills now

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getPeople().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
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
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertTrue(mainApp.getSkills().isEmpty());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());
  }

  @Test
  public void testSkillDeleteUndo() throws Exception {
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    deleteNewestSkill();
    assertEquals(0, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
  }

  @Test
  public void testSkillDeleteRedo() throws Exception {
    assertTrue(mainApp.getSkills().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    newSkill();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    deleteNewestSkill();
    assertEquals(0, mainApp.getSkills().size());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    undoRedoHandler.undo();
    assertEquals(1, mainApp.getSkills().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

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
    //testing creating and redoing an empty team
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());

    newTeam();

    assertEquals(1, mainApp.getTeams().size());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());

    undoRedoHandler.undo();

    assertTrue(mainApp.getTeams().isEmpty());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

    undoRedoHandler.redo();
    assertEquals(1, mainApp.getTeams().size());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());
  }

  @Test
  public void testTeamDeleteUndo() throws Exception {
    //testing deleting and undoing an empty team
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());

    newTeam();

    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

    deleteNewestTeam();

    assertTrue(mainApp.getTeams().isEmpty());
    assertEquals(2, undoRedoHandler.getUndoStack().size());

    undoRedoHandler.undo();

    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());

  }

  @Test
  public void testTeamDeleteRedo() throws Exception {
    //testing deleting and redoing an empty team
    assertTrue(mainApp.getTeams().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().isEmpty());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());

    newTeam();

    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());

    deleteNewestTeam();

    assertTrue(mainApp.getTeams().isEmpty());
    assertEquals(2, undoRedoHandler.getUndoStack().size());
    assertTrue(undoRedoHandler.getRedoStack().isEmpty());

    undoRedoHandler.undo();

    assertEquals(1, mainApp.getTeams().size());
    assertEquals(1, undoRedoHandler.getUndoStack().size());
    assertEquals(1, undoRedoHandler.getRedoStack().size());

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
    editNewestPerson();

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
    undoRedoHandler.redo(); // new person
    undoRedoHandler.redo(); // new team

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
