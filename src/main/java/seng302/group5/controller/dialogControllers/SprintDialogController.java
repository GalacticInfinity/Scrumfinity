package seng302.group5.controller.dialogControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Backlog;
import seng302.group5.model.Release;
import seng302.group5.model.Sprint;
import seng302.group5.model.Story;
import seng302.group5.model.Team;

/**
 * A controller for the sprint dialog.
 * TODO: expand once it works
 *
 * Created by Michael Roman and Su-Shing Chen on 24/7/2015.
 */
public class SprintDialogController {

  @FXML private TextField sprintGoalField;
  @FXML private TextField sprintNameField;
  @FXML private TextArea sprintDescriptionField;
  @FXML private ComboBox<Backlog> sprintBacklogCombo;
  @FXML private Label sprintProjectLabel;
  @FXML private ComboBox<Team> sprintTeamCombo;
  @FXML private ComboBox<Release> sprintReleaseCombo;
  @FXML private DatePicker sprintStartDate;
  @FXML private DatePicker sprintEndDate;
  @FXML private ListView<Story> availableStoriesList;
  @FXML private ListView<Story> allocatedStoriesList;
  @FXML private Button btnAddStory;
  @FXML private Button btnRemoveStory;
  @FXML private HBox btnContainer;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Main mainApp;
  private Stage thisStage;

  private CreateOrEdit createOrEdit;

  private Sprint sprint;
  private Sprint lastSprint;

  private ObservableList<Story> availableStories;
  private ObservableList<Story> allocatedStories;
  private ObservableList<Backlog> backlogs;
  // todo map from backlog to project?
  private ObservableList<Team> teams;
  private ObservableList<Release> releases;

  //todo jdoc
  public void setupController(Main mainApp, Stage thisStage, CreateOrEdit createOrEdit,
                              Sprint sprint) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnConfirm);
      btnContainer.getChildren().add(btnConfirm);
    }

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Sprint");
      btnConfirm.setText("Create");

      initialiseLists(CreateOrEdit.CREATE, sprint);
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Sprint");
      btnConfirm.setText("Save");
      btnConfirm.setDisable(true);
      initialiseLists(CreateOrEdit.EDIT, sprint);

      sprintGoalField.setText(sprint.getLabel());
      sprintNameField.setText(sprint.getSprintFullName());
      sprintDescriptionField.setText(sprint.getSprintDescription());
      sprintBacklogCombo.getSelectionModel().select(sprint.getSprintBacklog());
      // todo project label field
      sprintTeamCombo.getSelectionModel().select(sprint.getSprintTeam());
      sprintReleaseCombo.getSelectionModel().select(sprint.getSprintRelease());
      // todo date fields
    }
    this.createOrEdit = createOrEdit;

    if (sprint != null) {
      this.sprint = sprint;
      this.lastSprint = new Sprint(sprint);
    } else {
      this.sprint = null;
      this.lastSprint = null;
    }
    // todo change listeners
  }

  private void initialiseLists(CreateOrEdit createOrEdit, Sprint sprint) {
    availableStories = FXCollections.observableArrayList();
    allocatedStories = FXCollections.observableArrayList();
    backlogs = FXCollections.observableArrayList();
    // todo map from backlog to project?
    teams = FXCollections.observableArrayList();
    releases = FXCollections.observableArrayList();

    backlogs.setAll(mainApp.getBacklogs());
    teams.setAll(mainApp.getTeams());
    releases.setAll(mainApp.getReleases());

    sprintBacklogCombo.setVisibleRowCount(5);
    sprintBacklogCombo.setItems(backlogs);
    sprintTeamCombo.setVisibleRowCount(5);
    sprintTeamCombo.setItems(teams);
    sprintReleaseCombo.setVisibleRowCount(5);
    sprintReleaseCombo.setItems(releases);
  }

  // todo jdoc
  @FXML
  void btnAddStoryClick(ActionEvent event) {

  }

  // todo jdoc
  @FXML
  void btnRemoveStoryClick(ActionEvent event) {

  }

  // todo jdoc
  @FXML
  void btnConfirmClick(ActionEvent event) {

  }

  // todo jdoc
  @FXML
  void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }
}
