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

  private Main mainApp;

  Stack<UndoRedoObject> undoStack = new Stack<>();
  Stack<UndoRedoObject> redoStack = new Stack<>();

  public void undo() throws Exception {
    if (undoStack.isEmpty()) {
      System.out.println("Nothing to undo");
      return;
    }
    redoStack.push(undoStack.peek());
    UndoRedoObject undoRedoObject = undoStack.pop();
    Action action = undoRedoObject.getAction();
    ArrayList<String> data = undoRedoObject.getData();

    switch(action) {
      case PROJECT_CREATE:
        //TODO: delete a project
        System.out.println("I am undoing a project creation");
        break;
      case PROJECT_EDIT:
        System.out.println("I am undoing a project edit");
        break;
      case PROJECT_DELETE:
        System.out.println("I am undoing a project deletion");
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

  public void redo() throws Exception {
    if (redoStack.isEmpty()) {
      System.out.println("Nothing to redo");
      return;
    }
    undoStack.push(redoStack.peek());
    UndoRedoObject undoRedoObject = redoStack.pop();
  }

  public void newAction(UndoRedoObject item) {
    undoStack.push(item);
    redoStack.clear();
  }

}
