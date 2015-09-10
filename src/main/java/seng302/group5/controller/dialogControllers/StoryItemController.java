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
  @FXML private ListView<Task> inProgressList;
  @FXML private ListView<Task> verifyList;
  @FXML private ListView<Task> doneList;

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
          // TODO more testing
          addWithDragging(notStartedList, new Label(task.getLabel()));
          // TODO this line specifically
          break;
        case IN_PROGRESS:
          inProgressTasks.add(task);
          break;
        case VERIFY:
          verifyTasks.add(task);
          break;
        case DONE:
          doneTasks.add(task);
          break;
      }
    }
    inProgressList.setItems(inProgressTasks);
    verifyList.setItems(verifyTasks);
    doneList.setItems(doneTasks);

    /**
     * Testing stuff
     */
    // in case user drops node in blank space in root:
    notStartedList.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        int indexOfDraggingNode = notStartedList.getChildren().indexOf(event.getGestureSource());
        rotateNodes(notStartedList, indexOfDraggingNode, notStartedList.getChildren().size() - 1);
      }
    });
  }

  /**
   * More testing stuff
   * @param root
   * @param label
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
        label.setStyle("");
        int indexOfDraggingNode = root.getChildren().indexOf(event.getGestureSource());
        int indexOfDropTarget = root.getChildren().indexOf(label);
        rotateNodes(root, indexOfDraggingNode, indexOfDropTarget);
        event.consume();
      }
    });
    root.getChildren().add(label);
  }

  /**
   * Please dear got let this work
   * TODO everything
   * @param root
   * @param indexOfDraggingNode
   * @param indexOfDropTarget
   */
  private void rotateNodes(final VBox root, final int indexOfDraggingNode,
                           final int indexOfDropTarget) {
    if (indexOfDraggingNode >= 0 && indexOfDropTarget >= 0) {
      final Node node = root.getChildren().remove(indexOfDraggingNode);
      root.getChildren().add(indexOfDropTarget, node);
    }
  }

  public Story getStory() {
    return story;
  }

}
