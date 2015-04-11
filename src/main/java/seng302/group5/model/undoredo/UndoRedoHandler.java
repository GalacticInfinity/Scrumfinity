package seng302.group5.model.undoredo;

import java.util.ArrayList;
import java.util.Stack;

import javafx.collections.FXCollections;
import seng302.group5.Main;
import seng302.group5.model.AgileItem;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Skill;

/**
 * Created by Michael on 3/17/2015.
 *
 * Work in progess, may scrap alltogether
 */
public class UndoRedoHandler {

  /**
   * Local enum for denoting whether the action is an undo or a redo
   */
  private enum UndoOrRedo {
    UNDO, REDO
  }

  private Main mainApp;

  private Stack<UndoRedoObject> undoStack;
  private Stack<UndoRedoObject> redoStack;

  /**
   * Constructor. Set the main app to communicate with and initialise stacks
   * @param mainApp Main app to communicate with
   */
  public UndoRedoHandler(Main mainApp) {
    this.mainApp = mainApp;
    undoStack = new Stack<>();
    redoStack = new Stack<>();
  }

  /**
   * Peek at what is on top of the undo stack
   * @return The top element of the undo stack, or null if it's empty
   */
  public UndoRedoObject peekUndoStack() {
    if (undoStack.isEmpty()) {
      return null;
    } else {
      return undoStack.peek();
    }
  }

  /**
   * Clear the undo and redo stacks
   */
  public void clearStacks() {
    undoStack.clear();
    redoStack.clear();
  }

  /**
   * Add a new action onto the undo stack
   *
   * @param undoRedoObject The action object
   */
  public void newAction(UndoRedoObject undoRedoObject) {
    undoStack.push(undoRedoObject);
    redoStack.clear();
  }

  /**
   * Generic undo function to undo the latest action
   *
   * @throws Exception Error message if data is invalid
   */
  public void undo() throws Exception {
    if (undoStack.isEmpty()) {
      System.out.println("Nothing to undo");
      return;
    }
    // Rearrange stacks
    redoStack.push(undoStack.peek());
    UndoRedoObject undoRedoObject = undoStack.pop();

    // Handle the action
    handleUndoRedoObject(undoRedoObject, UndoOrRedo.UNDO);
  }

  /**
   * Generic redo function to redo the latest undone action
   *
   * @throws Exception Error message if data is invalid
   */
  public void redo() throws Exception {
    if (redoStack.isEmpty()) {
      System.out.println("Nothing to redo");
      return;
    }
    // Rearrange stacks
    undoStack.push(redoStack.peek());
    UndoRedoObject undoRedoObject = redoStack.pop();

    // Handle the action
    handleUndoRedoObject(undoRedoObject, UndoOrRedo.REDO);
  }

  /**
   * Handle a undo or redo call
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleUndoRedoObject(UndoRedoObject undoRedoObject,
                                    UndoOrRedo undoOrRedo) throws Exception {

    Action action = undoRedoObject.getAction();

    // TODO: remove temp debug printing below
    String undoOrRedoStr;
    if (undoOrRedo == UndoOrRedo.UNDO) {
      undoOrRedoStr = "undo";
    } else {
      undoOrRedoStr = "redo";
    }

    switch(action) {
      case PROJECT_CREATE:
        System.out.println(String.format("I am %sing a project creation", undoOrRedoStr)); // temp
        handleProjectCreate(undoRedoObject, undoOrRedo);
        break;

      case PROJECT_EDIT:
        System.out.println(String.format("I am %sing a project edit", undoOrRedoStr));      // temp
        handleProjectEdit(undoRedoObject, undoOrRedo);
        break;

      case PROJECT_DELETE:
        System.out.println(String.format("I am %sing a project deletion", undoOrRedoStr));   // temp
        handleProjectDelete(undoRedoObject, undoOrRedo);
        break;

      case PERSON_CREATE:
        System.out.println(String.format("I am %sing a person creation", undoOrRedoStr)); // temp
        handlePersonCreate(undoRedoObject, undoOrRedo);
        break;

      case PERSON_EDIT:
        System.out.println(String.format("I am %sing a person edit", undoOrRedoStr)); // temp
        handlePersonEdit(undoRedoObject, undoOrRedo);
        break;

      case PERSON_DELETE:
        System.out.println(String.format("I am %sing a person deletion", undoOrRedoStr)); // temp
        handlePersonDelete(undoRedoObject, undoOrRedo);
        break;

      case SKILL_CREATE:
        System.out.println(String.format("I am %sing a skill creation", undoOrRedoStr)); // temp
        handleSkillCreate(undoRedoObject, undoOrRedo);
        break;

      case SKILL_EDIT:
        System.out.println(String.format("I am %sing a skill edit", undoOrRedoStr)); // temp
        handleSkillEdit(undoRedoObject, undoOrRedo);
        break;

      case SKILL_DELETE:
        System.out.println(String.format("I am %sing a skill deletion", undoOrRedoStr));   // temp
        handleSkillDelete(undoRedoObject, undoOrRedo);
        break;

      case UNDEFINED:
        throw new Exception("Unreadable UndoRedoObject");

      default:
        throw new Exception("Undo/Redo case is not handled");
    }
  }

  /**
   * Undo or redo a project creation
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleProjectCreate(UndoRedoObject undoRedoObject,
                                   UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the project
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 1) {
      throw new Exception("Can't undo/redo project creation - No variables");
    }

    // Get the project ID which is currently in the list and the project to change
    Project project = (Project) data.get(0);
    String projectID = project.getProjectID();

    // Make the changes and refresh the list
    if (undoOrRedo == UndoOrRedo.UNDO) {
      // Find the project in the list and ensure it exists so it can be deleted
      Project projectToDelete = null;
      for (Project projectInList : mainApp.getProjects()) {
        if (projectInList.getProjectID().equals(projectID)) {
          projectToDelete = projectInList;
          break;
        }
      }
      if (projectToDelete == null) {
        throw new Exception("Can't undo project creation - Can't find the created project");
      }
      mainApp.deleteProject(projectToDelete);
    } else {
      Project projectToAdd = new Project(project);
      mainApp.addProject(projectToAdd);
    }
    mainApp.refreshList();
  }

  /**
   * Undo or redo a project edit
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleProjectEdit(UndoRedoObject undoRedoObject,
                                 UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the projects both before and after
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 2) {
      throw new Exception("Can't undo/redo project edit - Less than 2 variables");
    }

    // Get the project ID which is currently in the list and the project to edit
    Project currentProject;
    String currentProjectID;
    Project newProject;

    if (undoOrRedo == UndoOrRedo.UNDO) {
      currentProject = (Project) data.get(1);
      currentProjectID = currentProject.getProjectID();
      newProject = (Project) data.get(0);
    } else {
      currentProject = (Project) data.get(0);
      currentProjectID = currentProject.getProjectID();
      newProject = (Project) data.get(1);
    }

    // Find the project in the list and ensure it exists
    Project projectToEdit = null;
    for (Project projectInList : mainApp.getProjects()) {
      if (projectInList.getProjectID().equals(currentProjectID)) {
        projectToEdit = projectInList;
        break;
      }
    }
    if (projectToEdit == null) {
      throw new Exception("Can't undo/redo project edit - Can't find the edited project");
    }

    // Make the changes and refresh the list
    projectToEdit.setProjectID(newProject.getProjectID());
    projectToEdit.setProjectName(newProject.getProjectName());
    projectToEdit.setProjectDescription(newProject.getProjectDescription());
    mainApp.refreshList();
  }

  /**
   * Undo or redo a project deletion
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleProjectDelete(UndoRedoObject undoRedoObject,
                                   UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the project
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 1) {
      throw new Exception("Can't undo/redo project deletion - No variables");
    }

    // Get the project and ID to undo/redo deletion of
    Project project = (Project) data.get(0);
    String projectID = project.getProjectID();

    // Make the changes and refresh the list
    if (undoOrRedo == UndoOrRedo.UNDO) {
      // Create the deleted project again
      Project projectToAdd = new Project(project);
      mainApp.addProject(projectToAdd);
    } else {
      // Find the project in list and ensure it exists so it can be deleted again
      Project projectToDelete = null;
      for (Project projectInList : mainApp.getProjects()) {
        if (projectInList.getProjectID().equals(projectID)) {
          projectToDelete = projectInList;
          break;
        }
      }
      if (projectToDelete == null) {
        throw new Exception("Can't redo project deletion - Can't find the created project");
      }
      mainApp.deleteProject(projectToDelete);
    }
    mainApp.refreshList();
  }

  /**
   * Undo or redo a person creation
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handlePersonCreate(UndoRedoObject undoRedoObject,
                                  UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the person
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 1) {
      throw new Exception("Can't undo/redo person creation - No variables");
    }

    // Get the person ID which is currently in the list and the person to change
    Person person = (Person) data.get(0);
    String personID = person.getPersonID();

    // Make the changes and refresh the list
    if (undoOrRedo == UndoOrRedo.UNDO) {
      // Find the person in the list and ensure it exists so it can be deleted
      Person personToDelete = null;
      for (Person personInList : mainApp.getPeople()) {
        if (personInList.getPersonID().equals(personID)) {
          personToDelete = personInList;
          break;
        }
      }
      if (personToDelete == null) {
        throw new Exception("Can't undo person creation - Can't find the created person");
      }
      mainApp.deletePerson(personToDelete);
    } else {
      Person personToAdd = new Person(person);
      mainApp.addPerson(personToAdd);
    }
    mainApp.refreshList();
  }

  /**
   * Undo or redo a person edit
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handlePersonEdit(UndoRedoObject undoRedoObject,
                                UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the people both before and after
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 2) {
      throw new Exception("Can't undo/redo person edit - Less than 2 variables");
    }

    // Get the person ID which is currently in the list and the person to edit
    Person currentPerson;
    String currentPersonID;
    Person newPerson;

    if (undoOrRedo == UndoOrRedo.UNDO) {
      currentPerson = (Person) data.get(1);
      currentPersonID = currentPerson.getPersonID();
      newPerson = (Person) data.get(0);
    } else {
      currentPerson = (Person) data.get(0);
      currentPersonID = currentPerson.getPersonID();
      newPerson = (Person) data.get(1);
    }

    // Find the person in the list and ensure it exists
    Person personToEdit = null;
    for (Person personInList : mainApp.getPeople()) {
      if (personInList.getPersonID().equals(currentPersonID)) {
        personToEdit = personInList;
        break;
      }
    }
    if (personToEdit == null) {
      throw new Exception("Can't undo/redo person edit - Can't find the edited person");
    }

    // Make the changes and refresh the list
    personToEdit.setPersonID(newPerson.getPersonID());
    personToEdit.setFirstName(newPerson.getFirstName());
    personToEdit.setLastName(newPerson.getLastName());
    personToEdit.setSkillSet(FXCollections.observableArrayList(newPerson.getSkillSet()));
    mainApp.refreshList();
  }

  /**
   * Undo or redo a person deletion
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handlePersonDelete(UndoRedoObject undoRedoObject,
                                  UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the person
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 1) {
      throw new Exception("Can't undo/redo person deletion - No variables");
    }

    // Get the person and ID to undo/redo deletion of
    Person person = (Person) data.get(0);
    String personID = person.getPersonID();

    // Make the changes and refresh the list
    if (undoOrRedo == UndoOrRedo.UNDO) {
      // Create the deleted person again
      Person personToAdd = new Person(person);
      mainApp.addPerson(personToAdd);
    } else {
      // Find the person in list and ensure it exists so it can be deleted again
      Person personToDelete = null;
      for (Person personInList : mainApp.getPeople()) {
        if (personInList.getPersonID().equals(personID)) {
          personToDelete = personInList;
          break;
        }
      }
      if (personToDelete == null) {
        throw new Exception("Can't redo person deletion - Can't find the created person");
      }
      mainApp.deletePerson(personToDelete);
    }
    mainApp.refreshList();
  }

  /**
   * Undo or redo a skill creation
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleSkillCreate(UndoRedoObject undoRedoObject,
                                 UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the skill
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 1) {
      throw new Exception("Can't undo/redo skill creation - No variables");
    }

    // Get the skill name which is currently in the list and the skill to change
    Skill skill = (Skill) data.get(0);
    String skillName = skill.getSkillName();

    // Make the changes and refresh the list
    if (undoOrRedo == UndoOrRedo.UNDO) {
      // Find the skill in the list and ensure it exists so it can be deleted
      Skill skillToDelete = null;
      for (Skill skillInList : mainApp.getSkills()) {
        if (skillInList.getSkillName().equals(skillName)) {
          skillToDelete = skillInList;
          break;
        }
      }
      if (skillToDelete == null) {
        throw new Exception("Can't undo skill creation - Can't find the created skill");
      }
      mainApp.deleteSkill(skillToDelete);
    } else {
      Skill skillToAdd = new Skill(skill);
      mainApp.addSkill(skillToAdd);
    }
    mainApp.refreshList();
  }

  /**
   * Undo or redo a skill edit
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleSkillEdit(UndoRedoObject undoRedoObject,
                               UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the skills both before and after
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 2) {
      throw new Exception("Can't undo/redo skill edit - Less than 2 variables");
    }

    // Get the skill name which is currently in the list and the skill to edit
    Skill currentSkill;
    String currentSkillName;
    Skill newSkill;

    if (undoOrRedo == UndoOrRedo.UNDO) {
      currentSkill = (Skill) data.get(1);
      currentSkillName = currentSkill.getSkillName();
      newSkill = (Skill) data.get(0);
    } else {
      currentSkill = (Skill) data.get(0);
      currentSkillName = currentSkill.getSkillName();
      newSkill = (Skill) data.get(1);
    }

    // Find the skill in the list and ensure it exists
    Skill skillToEdit = null;
    for (Skill skillInList : mainApp.getSkills()) {
      if (skillInList.getSkillName().equals(currentSkillName)) {
        skillToEdit = skillInList;
        break;
      }
    }
    if (skillToEdit == null) {
      throw new Exception("Can't undo/redo skill edit - Can't find the edited skill");
    }

    // Make the changes and refresh the list
    skillToEdit.setSkillName(newSkill.getSkillName());
    skillToEdit.setSkillDescription(newSkill.getSkillDescription());
    mainApp.refreshList();
  }

  /**
   * Undo or redo a skill deletion
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleSkillDelete(UndoRedoObject undoRedoObject,
                                 UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the skill
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 1) {
      throw new Exception("Can't undo/redo skill deletion - No variables");
    }

    // Get the skill and name to undo/redo deletion of
    Skill skill = (Skill) data.get(0);
    String skillName = skill.getSkillName();

    // Make the changes and refresh the list
    if (undoOrRedo == UndoOrRedo.UNDO) {
      // Create the deleted skill again
      Skill skillToAdd = new Skill(skill);
      mainApp.addSkill(skillToAdd);
    } else {
      // Find the skill in list and ensure it exists so it can be deleted again
      Skill skillToDelete = null;
      for (Skill skillInList : mainApp.getSkills()) {
        if (skillInList.getSkillName().equals(skillName)) {
          skillToDelete = skillInList;
          break;
        }
      }
      if (skillToDelete == null) {
        throw new Exception("Can't redo skill deletion - Can't find the created skill");
      }
      mainApp.deleteSkill(skillToDelete);
    }
    mainApp.refreshList();
  }

  public Stack<UndoRedoObject> getUndoStack() {
    return undoStack;
  }

  public Stack<UndoRedoObject> getRedoStack() {
    return redoStack;
  }
}
