package seng302.group5.controller.dialogControllers;

import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
  @FXML private Accordion scrumBoardAccordion;
  @FXML private Button btnDeleteTask;
  @FXML private Button btnNewTask;

  private Main mainApp;
  private Stage stage;

  private ObservableList<Sprint> availableSprints;
  private ObservableList<Story> availableStories;

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
  }

  /**
   * Initializes the controller and sets the listeners to the combo boxes to update and
   * reload controllers as necessary. Changing backlog combo refreshes sprint combo. Changing
   * sprint combo refreshes list of controllers.
   *
   * TODO look over at end
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

    sprintCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldSprint, newSprint) -> {
          if (newSprint != null) {
            availableStories.clear();
            for (Story story : newSprint.getSprintStories()) {
              if (mainApp.getStories().contains(story)) {
                availableStories.add(story);
              }
            }
            for (Story story : availableStories) {
              //TODO Make load controllers
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

  private StoryItemController createStory(Story story) {
    return new StoryItemController();
  }

}
