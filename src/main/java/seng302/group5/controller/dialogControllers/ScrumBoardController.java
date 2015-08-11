package seng302.group5.controller.dialogControllers;

import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.model.Backlog;
import seng302.group5.model.Sprint;
import seng302.group5.model.Story;

/**
 * The controller class for the scrum board dialog. Tasks can be viewed from this dialog by
 * selecting the backlog->sprint->story accordingly. Also tasks are sorted by their status in four
 * different lists: not started, in progress, verify and done.
 *
 * Version 1: status are to be changed by double click editing.
 *
 * @author liangma
 */
public class ScrumBoardController {
  @FXML private ComboBox<Backlog> backlogCombo;
  @FXML private ComboBox<Sprint> sprintCombo;
  @FXML private ComboBox<Story> storyCombo;

  private Main mainApp;
  private Stage thisStage;

  private Backlog backlog;
  private Sprint sprint;
  private Story story;

  private ObservableList<Backlog> availableBacklogs;
  private ObservableList<Sprint> availableSprints;
  private ObservableList<Story> availableStories;

  /**
   * This function sets up the scrum board dialog controller.
   * @param mainApp     The main application object
   * @param thisStage   The stage of the dialog
   */
  public void setupController(Main mainApp, Stage thisStage) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

    thisStage.setTitle("Scrum Board Support");
    thisStage.setResizable(false);
    initialiseLists();
    sprintCombo.setDisable(true);
    storyCombo.setDisable(true);
  }

  /**
   * Initialises the models lists and populates these with values from the main application,
   * such as available backlogs, sprints and stories. These values
   * are then populated into their respective GUI elements. The backlog combo box has a listener
   * to update other GUI elements which depend on the backlog.
   */
  private void initialiseLists() {
    availableBacklogs = FXCollections.observableArrayList();
    availableSprints = FXCollections.observableArrayList();
    availableStories = FXCollections.observableArrayList();

    availableBacklogs.setAll(mainApp.getBacklogs());

    backlogCombo.setVisibleRowCount(5);
    sprintCombo.setVisibleRowCount(5);
    storyCombo.setVisibleRowCount(5);
    backlogCombo.setItems(availableBacklogs);
    sprintCombo.setItems(availableSprints);
    storyCombo.setItems(availableStories);

    backlogCombo.getSelectionModel().selectedItemProperty().addListener(
      (observable, oldBacklog, newBacklog) -> {

        sprintCombo.setDisable(false);

        // get backlog's sprints
        availableSprints.addAll(mainApp.getSprints().stream()
                                    .filter(sprint -> sprint.getSprintBacklog().equals(backlog))
                                    .collect(Collectors.toList()));
        sprintCombo.setItems(availableSprints);
      }
    );

    sprintCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldSprint, newSprint) -> {

          storyCombo.setDisable(false);

          availableStories.removeAll();
          availableStories.addAll(newSprint.getSprintStories());

          storyCombo.setItems(availableStories);
        }
    );
  }
}
