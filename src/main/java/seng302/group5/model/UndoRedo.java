package seng302.group5.model;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Michael on 3/17/2015.
 */
public class UndoRedo {

  Deque<AgileItem> undoStack = new ArrayDeque<AgileItem>();
  Deque<AgileItem> redoStack = new ArrayDeque<AgileItem>();

  public AgileItem undo() {
    redoStack.addFirst(undoStack.peekFirst());
    return undoStack.removeFirst();
  }

  public AgileItem redo() {
    undoStack.addFirst(redoStack.peekFirst());


    return redoStack.removeFirst();
  }

  public void newAction(AgileItem item) {
    undoStack.addFirst(item);
  }

}
