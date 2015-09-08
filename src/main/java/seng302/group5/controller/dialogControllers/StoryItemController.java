package seng302.group5.controller.dialogControllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
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

  private Main mainApp;
  private Stage stage;

  private ObservableList<Task> notStartedTasks;
  private ObservableList<Task> inProgressTasks;
  private ObservableList<Task> verifyTasks;
  private ObservableList<Task> doneTasks;

  /**
   * This function sets up the story item controller.
   * @param mainApp     The main application object
   * @param stage       The stage the application is in.
   */
  public void setupController(Main mainApp, Stage stage) {
    this.mainApp = mainApp;
    this.stage = stage;
  }

}
