package seng302.group5.controller.dialogControllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.model.Backlog;
import seng302.group5.model.Sprint;
import seng302.group5.model.Story;

/**
 * Scrum board controller completely redone. Now using accordions to make things prettier.
 * First controller create which contains a list of other controller objects (a controller for
 * each story to be displayed in the accordion view).
 */
public class ScrumBoardController {

  @FXML private ComboBox<Sprint> sprintCombo;
  @FXML private ComboBox<Backlog> backlogCombo;
  @FXML private VBox storiesBox;
  @FXML private ScrollPane scrollPane;

  private Main mainApp;
  private Stage stage;
  private Story fakeStory;

  private ObservableList<Sprint> availableSprints;
  private ObservableList<Story> availableStories;
  private List<StoryItemController> storyPanes;
  private List<String> openedTabs;

  @FXML
  private void initialize() {
  }

  /**
   * This function sets up the scrum board dialog controller.
   * @param mainApp     The main application object
   * @param stage       The stage the application is in.
   */
  public void setupController(Main mainApp, Stage stage) {
    this.mainApp = mainApp;
    this.stage = stage;
    storyPanes = new ArrayList<>();
    openedTabs = new ArrayList<>();
    initialiseLists();
  }

  /**
   * Initializes the controller and sets the listeners to the combo boxes to update and
   * reload controllers as necessary. Changing backlog combo refreshes sprint combo. Changing
   * sprint combo refreshes list of controllers.
   */
  private void initialiseLists() {
    availableSprints = FXCollections.observableArrayList();
    availableStories = FXCollections.observableArrayList();

    backlogCombo.getSelectionModel().clearSelection();
    backlogCombo.setItems(mainApp.getBacklogs());

    backlogCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldBacklog, newBacklog) -> {
          if (newBacklog != null) {
            sprintCombo.setDisable(false);
            // get backlog's sprints
            availableSprints.setAll(mainApp.getSprints().stream()
                                        .filter(
                                            sprint -> sprint.getSprintBacklog().equals(newBacklog))
                                        .collect(Collectors.toList()));
            sprintCombo.setItems(null);
            sprintCombo.setItems(availableSprints);
            sprintCombo.setValue(null);
          }
        }
    );

    // Resizes the scrumboards width when the main stage is resized.
    stage.widthProperty().addListener((observable, oldValue, newValue) -> {
      storiesBox.setMinWidth(stage.getWidth() - 200);
    });

    sprintCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldSprint, newSprint) -> {
          if (newSprint != null) {
            availableStories.clear();
            storiesBox.getChildren().setAll(FXCollections.observableArrayList());
            storyPanes.clear();
            // Place the fake story in
            fakeStory = new Story();
            fakeStory.setLabel("Non-story Tasks");
            fakeStory.addAllTasks(newSprint.getTasks());
            availableStories.add(fakeStory);
            // ***************************************************************
            for (Story story : newSprint.getSprintStories()) {
              if (mainApp.getStories().contains(story)) {
                availableStories.add(story);
              }
            }
            for (Story story : availableStories) {
              StoryItemController paneController = createStoryPane(story, storiesBox);
              if (paneController != null) {
                storyPanes.add(paneController);
              }
              if (openedTabs.contains(paneController.getPaneName())) {
                paneController.expandTab();
              }
            }
          }
        }
    );
  }

  @FXML
  void addNewTask(ActionEvent event) {

  }

  @FXML
  void deleteTask(ActionEvent event) {

  }

  /**
   * TODO write all the stuff it does
   * @param story Story object which holds the data
   * @return The created controller
   */
  private StoryItemController createStoryPane(Story story, VBox storiesBox) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(StoryItemController.class.getResource("/StoryItem.fxml"));
      TitledPane accordionPane = loader.load();

      StoryItemController controller = loader.getController();
      Accordion storyAccordion = new Accordion();
      storyAccordion.getPanes().add(accordionPane);
      storiesBox.getChildren().add(storyAccordion);
      controller.setupController(story, mainApp, storyAccordion, sprintCombo.getValue());
      return controller;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Resets everything about the scrum board, everything cleared and disabled apart from
   * Backlog combo box.
   * TODO allocate extra memory and a few checks to reduce run time significantly
   */
  public void refreshComboBoxes() {
    Backlog backlog = backlogCombo.getValue();
    Sprint sprint = sprintCombo.getValue();
    if (backlog != null &&
        sprint != null) {
      backlogCombo.getSelectionModel().clearSelection();
      sprintCombo.getSelectionModel().clearSelection();

      // The pane stuff
      openedTabs = new ArrayList<>();
      for (StoryItemController titledPane : storyPanes) {
        if (titledPane.checkIfOpened()) {
          openedTabs.add(titledPane.getPaneName());
        }
      }

      storiesBox.getChildren().setAll(FXCollections.observableArrayList());
      storyPanes.clear();
      //initialiseLists();

      sprintCombo.setDisable(true);

      if (mainApp.getBacklogs().contains(backlog)) {
        backlogCombo.setValue(backlog);

        if (availableSprints.contains(sprint)) {
          sprintCombo.setValue(sprint);
          //todo make stories in sprints be removed properly at some point.
        } else {
          sprintCombo.setValue(null);
        }
      } else {
        backlogCombo.setValue(null);
        sprintCombo.setValue(null);
      }
    }
  }

  /**
   * Refreshes the selections of the combo boxes
   */
  public void hardReset() {
    sprintCombo.getSelectionModel().clearSelection();
    sprintCombo.getItems().clear();
    sprintCombo.setDisable(true);
    availableSprints.clear();
    backlogCombo.getSelectionModel().clearSelection();
    backlogCombo.setItems(mainApp.getBacklogs());
    storiesBox.getChildren().setAll(FXCollections.observableArrayList());
    availableStories.clear();
    storyPanes.clear();
  }

  /**
   * Does not refresh the scrum board, just refreshes all the lists of the story items
   */
  public void refreshTaskLists() {
    for (StoryItemController controller : storyPanes) {
      controller.setupLists();
    }
  }
}
