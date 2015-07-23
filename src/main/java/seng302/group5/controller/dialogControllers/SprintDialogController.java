package seng302.group5.controller.dialogControllers;

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
 * Created by Michael on 7/23/2015.
 */
public class SprintDialogController {

  @FXML private HBox btnContainer;
  @FXML private ComboBox<Backlog> sprintBacklogCombo;
  @FXML private Label sprintProjectField;
  @FXML private TextField sprintGoalField;
  @FXML private TextField sprintNameField;
  @FXML private Button btnRemoveStory;
  @FXML private TextArea sprintDescriptionField;
  @FXML private DatePicker sprintStartDate;
  @FXML private ListView<Story> allocatedStoriesList;
  @FXML private Button btnConfirm;
  @FXML private DatePicker sprintEndDate;
  @FXML private ComboBox<Team> sprintTeamCombo;
  @FXML private Button btnCancel;
  @FXML private ComboBox<Release> sprintReleaseCombo;

  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Sprint sprint;

  public void setupController(Main mainApp, Stage thisStage, CreateOrEdit createOrEdit,
                              Sprint sprint) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;
  }

  @FXML
  private Button btnAddStory;

  @FXML
  private ListView<?> availableStoriesList;

  @FXML
  void btnAddStoryClick(ActionEvent event) {

  }

  @FXML
  void btnRemoveStoryClick(ActionEvent event) {

  }

  @FXML
  void btnConfirmClick(ActionEvent event) {

  }

  @FXML
  void btnCancelClick(ActionEvent event) {

  }
}
