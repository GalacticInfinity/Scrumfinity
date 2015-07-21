package seng302.group5.controller.dialogControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.model.Story;

/**
 * Created by Michael and Shingy on 7/22/2015.
 */
public class DependantsDialogController {

  private Main mainApp;
  private Stage thisStage;
  private Story story;

  private ObservableList<Story> availableStories;
  private ObservableList<Story> dependantStories;

  @FXML
  private Button btnAddStory;
  @FXML
  private ListView<Story> availableStoriesList;
  @FXML
  private ListView<Story> dependantStoriesList;
  @FXML
  private Button btnRemoveStory;

  public void setupController(Main mainApp, Stage thisStage, Story story) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;
    this.story = story;

    //Populate up in here
    availableStories = FXCollections.observableArrayList();
    dependantStories = FXCollections.observableArrayList();
    availableStories.setAll(mainApp.getStories());
    if (story != null) {
      dependantStories.addAll(story.getDependencies());
      availableStories.remove(story);
      availableStories.removeAll(dependantStories);
    }

    availableStoriesList.setItems(availableStories);
    dependantStoriesList.setItems(dependantStories);
  }

  @FXML
  void btnAddStoryClick(ActionEvent event) {
    //Cyclin dependency checks
  }

  @FXML
  void btnRemoveStoryClick(ActionEvent event) {
    // Remove the bugger
  }
}