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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for undo and redo handler
 */
public class UndoRedoHandlerTest {

  private String personID;
  private String firstName;
  private String lastName;
  private ObservableList<Skill> skillSet = FXCollections.observableArrayList();

  private Person person;

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
    person = new Person(personID, firstName, lastName, skillSet);

    mainApp.addPerson(person);

    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_CREATE);
    undoRedoObject.addDatum(new Person(person));

    undoRedoHandler.newAction(undoRedoObject);
  }

  @Test
  public void testPeekUndoStack() throws Exception {
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();

    UndoRedoObject peekObject = undoRedoHandler.peekUndoStack();
    ArrayList<AgileItem> data = peekObject.getData();
    Person undoPerson = (Person) data.get(0);

    assertEquals(peekObject.getAction(), Action.PERSON_CREATE);
    assertEquals(undoPerson.getPersonID(), personID);
    assertEquals(undoPerson.getFirstName(), firstName);
    assertEquals(undoPerson.getLastName(), lastName);
  }

  @Test
  public void testClearStacks() throws Exception {
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    newPerson();
    assertFalse(undoRedoHandler.getUndoStack().empty());
    assertTrue(undoRedoHandler.getRedoStack().empty());

    // Add a random person to the redo stack
    Person tempPerson = new Person(personID, firstName, lastName, skillSet);
    UndoRedoObject undoRedoObject = new UndoRedoObject();
    undoRedoObject.setAction(Action.PERSON_CREATE);
    undoRedoObject.addDatum(new Person(tempPerson));
    undoRedoHandler.getRedoStack().add(undoRedoObject);
    assertFalse(undoRedoHandler.getUndoStack().empty());
    assertFalse(undoRedoHandler.getRedoStack().empty());

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
    assertFalse(mainApp.getPeople().isEmpty());
    assertFalse(undoRedoHandler.getUndoStack().empty());

    undoRedoHandler.undo();
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());
  }

  @Test
  public void testRedo() throws Exception {
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    newPerson();
    assertFalse(mainApp.getPeople().isEmpty());
    assertFalse(undoRedoHandler.getUndoStack().empty());

    undoRedoHandler.undo();
    assertTrue(mainApp.getPeople().isEmpty());
    assertTrue(undoRedoHandler.getUndoStack().empty());

    undoRedoHandler.redo();
    assertFalse(mainApp.getPeople().isEmpty());
    assertFalse(undoRedoHandler.getUndoStack().empty());
  }
}
