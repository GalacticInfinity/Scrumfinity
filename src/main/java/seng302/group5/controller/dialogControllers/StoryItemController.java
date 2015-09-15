package seng302.group5.controller.dialogControllers;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seng302.group5.model.Story;
import seng302.group5.model.Task;

/**
 * Controller class for a single storyPane for the accordion in the scrum board.
 */
public class StoryItemController {

  @FXML private ListView<Task> notStartedList;
  @FXML private ListView<Task> inProgressList;
  @FXML private ListView<Task> verifyList;
  @FXML private ListView<Task> doneList;

  @FXML private AnchorPane storyAnchor;
  @FXML private TitledPane storyPane;

  @FXML private ImageView SBImage;
  @FXML private Rectangle doneBar;
  @FXML private Rectangle inProgBar;
  @FXML private Rectangle notStartedBar;

  private Story story;

  private Image inprogress;
  private Image complete;
  private ImageView dinoGif;

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

    //Set up the dino gif and place it on the accordion
    inprogress = new Image("runningDino.gif");
    complete = new Image("victoryDino.gif");
    dinoGif = new ImageView();

    dinoGif.setFitHeight(28);
    dinoGif.setFitWidth(28);

    if (this.story.percentComplete() == 1.0) {
      dinoGif.setImage(complete);
      SBImage.setImage(complete);

    } else {
      dinoGif.setImage(inprogress);
      SBImage.setImage(inprogress);

    }
    storyPane.setGraphic(dinoGif);

    progBar();

    setupLists();
  }


  public void progBar() {

    int totalSpace = 180;
    float doneSpace = totalSpace*story.percentComplete();
    float inProgSpace = totalSpace*story.percentInProg();
    float notStartedSpace = totalSpace*(1 - (story.percentComplete() + story.percentInProg()));

    doneBar.setWidth(doneSpace);
    inProgBar.setWidth(inProgSpace);
    notStartedBar.setWidth(notStartedSpace);

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
    notStartedList.setItems(notStartedTasks);
    inProgressList.setItems(inProgressTasks);
    verifyList.setItems(verifyTasks);
    doneList.setItems(doneTasks);
  }

  public Story getStory() {
    return story;
  }

}
