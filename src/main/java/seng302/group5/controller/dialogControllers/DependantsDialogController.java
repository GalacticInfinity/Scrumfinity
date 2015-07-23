package seng302.group5.controller.dialogControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.model.Story;

/**
 * A controller for the dependency dialog which shows the dependencies between stories in
 * the program. It can modify dependencies of any story in the main application. Intended to only
 * be used via the launchDependantsDialog function in the MenuBarController.
 *
 * Created by Michael and Shingy on 22/7/2015.
 */
public class DependantsDialogController {

  private Main mainApp;
  private Stage thisStage;
  private Story story;

  private ObservableList<Story> availableStories;
  private ObservableList<Story> dependantStories;

  @FXML private Label lblSelectedStory;
  @FXML private ListView<Story> availableStoriesList;
  @FXML private ListView<Story> dependantStoriesList;
  @FXML private Button btnAddStory;
  @FXML private Button btnRemoveStory;

  /**
   * Setup the dependency dialog controller for stories. Sets up model collections using data
   * from main.
   *
   * @param mainApp Our amazing god class.
   * @param thisStage The stage of the dialog.
   * @param story Active story to edit dependencies of, which can be null.
   */
  public void setupController(Main mainApp, Stage thisStage, Story story) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;
    this.story = story;

    // Populate up in here
    availableStories = FXCollections.observableArrayList();
    dependantStories = FXCollections.observableArrayList();
    refreshLists();

    // Set the views
    availableStoriesList.setItems(availableStories);
    dependantStoriesList.setItems(dependantStories);

    initialiseLists();
  }

  /**
   * Set up listeners to enable double click selecting of stories to make them active,
   * enable/disable buttons as required and refreshes the lists.
   */
  private void initialiseLists() {
    availableStoriesList.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getButton().equals(MouseButton.PRIMARY) &&
          mouseEvent.getClickCount() == 2 &&
          availableStoriesList.getSelectionModel().getSelectedItem() != null) {
        story = availableStoriesList.getSelectionModel().getSelectedItem();
        refreshLists();
      }
    });
    dependantStoriesList.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getButton().equals(MouseButton.PRIMARY) &&
          mouseEvent.getClickCount() == 2 &&
          dependantStoriesList.getSelectionModel().getSelectedItem() != null) {
        story = dependantStoriesList.getSelectionModel().getSelectedItem();
        refreshLists();
      }
    });
  }

  /**
   * Fills the model collections with stories from the two lists and manages what they contain
   * based on what story is selected. Enables/disables buttons as required and sets the currently
   * selected story GUI label.
   */
  private void refreshLists() {
    availableStories.setAll(mainApp.getStories());
    if (story == null) {
      btnAddStory.setDisable(true);
      btnRemoveStory.setDisable(true);
    } else {
      btnAddStory.setDisable(false);
      btnRemoveStory.setDisable(false);
      dependantStories.setAll(story.getDependencies());
      availableStories.remove(story);
      availableStories.removeAll(dependantStories);
      lblSelectedStory.setText(story.toString());
    }
    if (availableStories.isEmpty()) {
      btnAddStory.setDisable(true);
    }
    if (dependantStories.isEmpty()) {
      btnRemoveStory.setDisable(true);
    }
  }

  /**
   * Adds a selected story from the list of available stories and attempts to add it to the
   * active story's dependencies
   *
   * @param event An action event
   */
  @FXML
  protected void btnAddStoryClick(ActionEvent event) {
    Story selectedStory = availableStoriesList.getSelectionModel().getSelectedItem();
    if (selectedStory == null) {
      return;
    }
    //Cyclin dependency checks

    //if no cycle
    story.addDependency(selectedStory);
    refreshLists();
  }

  /**
   * Removes the selected story from the list of active story's dependent stories list and removes
   * the dependency from the active story's model.
   *
   * @param event An action event
   */
  @FXML
  protected void btnRemoveStoryClick(ActionEvent event) {
    Story selectedStory = dependantStoriesList.getSelectionModel().getSelectedItem();
    if (selectedStory == null) {
      return;
    }

    story.removeDependency(selectedStory);
    refreshLists();
  }
}
