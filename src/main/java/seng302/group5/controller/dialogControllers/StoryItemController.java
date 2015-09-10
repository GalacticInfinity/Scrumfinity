package seng302.group5.controller.dialogControllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.model.Story;
import seng302.group5.model.Task;

/**
 * Controller class for a single storyPane for the accordion in the scrum board.
 */
public class StoryItemController {

  @FXML private ListView<Story> inProgressList;
  @FXML private ListView<Story> doneList;
  @FXML private ListView<Story> notStartedList;
  @FXML private ListView<Story> verifyList;
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
  }

}
