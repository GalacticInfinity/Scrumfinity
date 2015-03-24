package seng302.group5.model.undoredo;

import java.util.ArrayList;

/**
 * @author Su-Shing Chen
 */
public class UndoRedoObject {

  private Action action;
  private ArrayList<String> data;

  public UndoRedoObject() {
    this.action = Action.UNDEFINED;
    this.data = new ArrayList<>();
  }

  public Action getAction() {
    return action;
  }

  public void setAction(Action action) {
    this.action = action;
  }

  public ArrayList<String> getData() {
    return data;
  }

  public void addDatum(String datum) {
    this.data.add(datum);
  }
}
