package seng302.group5.model.undoredo;

import java.util.ArrayList;

import seng302.group5.model.AgileItem;

/**
 * @author Su-Shing Chen
 */
public class UndoRedoObject {

  private Action action;
  private ArrayList<AgileItem> data;

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

  public ArrayList<AgileItem> getData() {
    return data;
  }

  public void addDatum(AgileItem datum) {
    this.data.add(datum);
  }
}
