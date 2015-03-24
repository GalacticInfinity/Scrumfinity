package seng302.group5.model.undoredo;

import java.util.ArrayList;
import java.util.Stack;

import seng302.group5.Main;
import seng302.group5.model.Project;

/**
 * Created by Michael on 3/17/2015.
 *
 * Work in progess, may scrap alltogether
 */
public class UndoRedoHandler {
  private enum UndoOrRedo { UNDO, REDO }

  private Main mainApp;

  Stack<UndoRedoObject> undoStack = new Stack<>();
  Stack<UndoRedoObject> redoStack = new Stack<>();

  /**
   * Set the main application object the handler will communicate with
   *
   * @param mainApp The main app
   */
  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
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
    redoStack.push(undoStack.peek());
    UndoRedoObject undoRedoObject = undoStack.pop();

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
    undoStack.push(redoStack.peek());
    UndoRedoObject undoRedoObject = redoStack.pop();

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
        //TODO: delete a project
        System.out.println(String.format("I am %sing a project creation", undoOrRedoStr)) ; // temp
        break;

      case PROJECT_EDIT:
        System.out.println(String.format("I am %sing a project edit", undoOrRedoStr));      // temp
        handleProjectEdit(undoRedoObject, undoOrRedo);
        break;

      case PROJECT_DELETE:
        System.out.println(String.format("I am %sing a project deletion", undoOrRedoStr));  // temp
        // Some possible delete code for future
//        if (data.size() < 3) {
//          throw new Exception("Can't recreate project - Less than 3 variables");
//        }
//        Project project = new Project(data.get(0), data.get(1), data.get(2));
//        mainApp.addProject(project);
        break;

      case UNDEFINED:
        throw new Exception("Unreadable UndoRedoObject");
    }
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

    ArrayList<String> data = undoRedoObject.getData();

    if (data.size() < 6) {
      throw new Exception("Can't undo/redo project edit - Less than 6 variables");
    }
    String currentProjectID;
    if (undoOrRedo == UndoOrRedo.UNDO) {
      currentProjectID = data.get(3);
    } else {
      currentProjectID = data.get(0);
    }
    Project projectToEdit = null;
    for (Project project : mainApp.getProjects()) {
      if (project.getProjectID().equals(currentProjectID)) {
        projectToEdit = project;
        break;
      }
    }
    if (projectToEdit == null) {
      throw new Exception("Can't undo/redo project edit - Can't find the edited project");
    }

    String newProjectID;
    String newProjectName;
    String newProjectDescription;
    if (undoOrRedo == UndoOrRedo.UNDO) {
      newProjectID = data.get(0);
      newProjectName = data.get(1);
      newProjectDescription = data.get(2);
    } else {
      newProjectID = data.get(3);
      newProjectName = data.get(4);
      newProjectDescription = data.get(5);
    }

    projectToEdit.setProjectID(newProjectID);
    projectToEdit.setProjectName(newProjectName);
    projectToEdit.setProjectDescription(newProjectDescription);
    mainApp.refreshList();
  }

}
