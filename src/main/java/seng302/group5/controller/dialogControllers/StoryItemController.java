package seng302.group5.controller.dialogControllers;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import seng302.group5.model.Story;
import seng302.group5.model.Task;

/**
 * Controller class for a single storyPane for the accordion in the scrum board.
 */
public class StoryItemController {

  @FXML private VBox notStartedList;
  @FXML private VBox inProgressList;
  @FXML private VBox verifyList;
  @FXML private VBox doneList;

  @FXML private AnchorPane storyAnchor;
  @FXML private TitledPane storyPane;

  private Story story;

  private ObservableList<Task> notStartedTasks;
  private ObservableList<Task> inProgressTasks;
  private ObservableList<Task> verifyTasks;
  private ObservableList<Task> doneTasks;

  /**
   * This function sets up the story item controller.
   * @param story       The story being displayed in this.
   */
  public void setupController(Story story) {
    this.story = story;
    storyPane.setText(story.getLabel());
    storyPane.setGraphic(new Rectangle(10, 10));
    setupLists();
  }

  /**
   * Sets the tasks from story into their appropriate lists.
   */
  public void setupLists() {
    notStartedTasks = FXCollections.observableArrayList();
    inProgressTasks = FXCollections.observableArrayList();
    verifyTasks = FXCollections.observableArrayList();
    doneTasks = FXCollections.observableArrayList();
    for (Task task : story.getTasks()) {
      switch (task.getStatus()) {
        case NOT_STARTED:
          notStartedTasks.add(task);
          addWithDragging(notStartedList, new Label(task.getLabel()));
          // TODO this line specifically
          break;
        case IN_PROGRESS:
          inProgressTasks.add(task);
          addWithDragging(inProgressList, new Label(task.getLabel()));
          break;
        case VERIFY:
          verifyTasks.add(task);
          addWithDragging(verifyList, new Label(task.getLabel()));
          break;
        case DONE:
          doneTasks.add(task);
          addWithDragging(doneList, new Label(task.getLabel()));
          break;
      }
    }

    // in case user drops node in blank space in root:
    notStartedList.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        addToBottom(notStartedList, (Node) event.getGestureSource());
      }
    });

    inProgressList.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        addToBottom(inProgressList, (Node) event.getGestureSource());
      }
    });

    verifyList.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        addToBottom(verifyList, (Node) event.getGestureSource());
      }
    });

    doneList.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        addToBottom(doneList, (Node) event.getGestureSource());
      }
    });
  }

  /**
   * Adds the label to a VBox, and assigns mouse event listeners for drag and drop functionality
   * between the four VBoxes (notStartedList, inProgressList, verifyList, doneList).
   * Overrides drag detected, mouse drag detected, drag exited, drag released.
   * @param root VBox root.
   * @param label Label to be added
   */
  private void addWithDragging(VBox root, Label label) {
    label.setOnDragDetected(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        label.startFullDrag();
      }
    });

    // next two handlers just an idea how to show the drop target visually:
    label.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        label.setStyle("-fx-background-color: #ffffa0;");
      }
    });
    label.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        label.setStyle("");
      }
    });

    label.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        // Mouse is dropped;
        label.setStyle("");
        int indexOfDropTarget = -1;
        Label sourceLbl = (Label) event.getGestureSource();
        VBox newRoot = null;
        // Removes dragged label from root, saves index of dragged to label;
        if (notStartedList.getChildren().contains(sourceLbl)) {
          notStartedList.getChildren().remove(sourceLbl);
          indexOfDropTarget = notStartedList.getChildren().indexOf(sourceLbl);
        } else if (inProgressList.getChildren().contains(sourceLbl)) {
          inProgressList.getChildren().remove(sourceLbl);
          indexOfDropTarget = inProgressList.getChildren().indexOf(sourceLbl);
        } else if (verifyList.getChildren().contains(sourceLbl)) {
          verifyList.getChildren().remove(sourceLbl);
          indexOfDropTarget = verifyList.getChildren().indexOf(sourceLbl);
        } else if (doneList.getChildren().contains(sourceLbl)) {
          doneList.getChildren().remove(sourceLbl);
          indexOfDropTarget = doneList.getChildren().indexOf(sourceLbl);
        }
        int indexOfDrag = root.getChildren().indexOf(label);
        // Adds label to root with new listeners
        addWithDragging(root, sourceLbl);
        // Label is now in vbox at bottom
        // Root=correct Vbox, iODT=index of highlighted node, Lbl=label to be moved up
        bringUpNode(root, indexOfDrag, sourceLbl);
        event.consume();
      }
    });
    root.getChildren().add(label);
  }

  /**
   * If already exists in root, removes the node then inserts it at the given index.
   * All parameters must be instantiated.
   * @param root Where to insert into
   * @param indexOfDropTarget Index to be inserted at
   * @param label Node to be inserted
   */
  private void bringUpNode(VBox root, int indexOfDropTarget, Node label) {
    if (root.getChildren().contains(label)) {
      root.getChildren().remove(label);
    }
    root.getChildren().add(indexOfDropTarget, label);
  }

  /**
   * Adds the node to the bottom of the root, and removes it from it's original parent.
   * @param root A javafx container with children
   * @param node Node to be appended
   */
  private void addToBottom(VBox root, Node node) {
    //1:Remove from orig location
    Label draggedLabel = (Label) node;
    VBox oldVBox = (VBox) draggedLabel.getParent();
    oldVBox.getChildren().remove(draggedLabel);
    //2:Add to the new list
    addWithDragging(root, draggedLabel);
  }

  public Story getStory() {
    return story;
  }

}
