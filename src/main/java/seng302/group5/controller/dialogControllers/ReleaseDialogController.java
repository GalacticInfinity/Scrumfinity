package seng302.group5.controller.dialogControllers;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Release;
import seng302.group5.model.Project;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;
import seng302.group5.model.util.Settings;

/**
 * Created by Craig Barnard on 7/04/2015.
 * Release Dialog Controller, manages the usage of Dialogs involved in the creating and editing of releases.
 */
public class ReleaseDialogController {

  @FXML private TextField releaseLabelField;
  @FXML private TextArea releaseDescriptionField;
  @FXML private DatePicker releaseDateField;
  @FXML private TextArea releaseNotesField;

  @FXML private Button btnConfirm;
  @FXML private ComboBox<Project> projectComboBox;
  @FXML private HBox btnContainer;

  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Release release = new Release();
  private Release lastRelease;

  private ObservableList<Project> availableProjects = FXCollections.observableArrayList();

  /**
   * Handles when the Create button is pushed by finalising all changes made or creating the new
   * Release object and adding it after checking for errors.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCreateRelease(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    int noErrors = 0;

    String releaseId = releaseLabelField.getText().trim();
    String releaseDescription = releaseDescriptionField.getText().trim();
    LocalDate releaseDate = releaseDateField.getValue();
    String releaseNotes = releaseNotesField.getText().trim();
    Project selectedProject = projectComboBox.getValue();

    Project releaseProject = new Project();

    try {
      releaseId = parseReleaseLabel(releaseLabelField.getText());
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    try {
      releaseDescription = parseReleaseDescription(releaseDescription);
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    try {
      releaseDate = parseReleaseDate(releaseDate);
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    try {
      releaseProject = parseReleaseProject(selectedProject);
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    try {
      releaseNotes = parseReleaseNotes(releaseNotes);
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    // Display all errors if they exist
    if (noErrors > 0) {
      String title = String.format("%d Invalid Field", noErrors);
      if (noErrors > 1) {
        title += "s";  // plural
      }
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(title);
      alert.setHeaderText(null);
      noErrors += 1;
      alert.getDialogPane().setPrefHeight(60 + 30 * noErrors);
      alert.setContentText(errors.toString());
      alert.showAndWait();
    } else {
      if (createOrEdit == CreateOrEdit.CREATE) {
        release = new Release(releaseId, releaseDescription, releaseNotes, releaseDate, releaseProject);
        mainApp.addRelease(release);
        if (Settings.correctList(release)) {
          mainApp.refreshList(release);
        }
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        release.setLabel(releaseId);
        release.setReleaseDescription(releaseDescription);
        release.setReleaseDate(releaseDate);
        release.setReleaseNotes(releaseNotes);
        release.setProjectRelease(releaseProject);

        releaseDateField.setValue(release.getReleaseDate());
        projectComboBox.setValue(release.getProjectRelease());
        mainApp.refreshList(release);
      }
      UndoRedoObject undoRedoObject = generateUndoRedoObject();
      mainApp.newAction(undoRedoObject);

      thisStage.close();
    }
  }

  /**
   * Generate an UndoRedoObject to place in the stack
   *
   * @return the UndoRedoObject to store
   */
  private UndoRedoObject generateUndoRedoObject() {
    UndoRedoObject undoRedoObject = new UndoRedoObject();

    if (createOrEdit == CreateOrEdit.CREATE) {
      undoRedoObject.setAction(Action.RELEASE_CREATE);
    } else {
      undoRedoObject.setAction(Action.RELEASE_EDIT);
      undoRedoObject.addDatum(lastRelease);
    }

    // Store a copy of project to edit in stack to avoid reference problems
    undoRedoObject.setAgileItem(release);
    Release releaseToStore = new Release(release);
    undoRedoObject.addDatum(releaseToStore);

    return undoRedoObject;
  }

  /**
   * Sets up the dialog controller
   *
   * @param mainApp      The main application object
   * @param thisStage    The stage of the dialog
   * @param createOrEdit If dialog is for creating or editing a project
   * @param release      the Release object if editing other wise null
   */
  public void setupController(Main mainApp,
                              Stage thisStage,
                              CreateOrEdit createOrEdit,
                              Release release) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;
    releaseDateField.setValue(LocalDate.now());

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnConfirm);
      btnContainer.getChildren().add(btnConfirm);
    }

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Release");
      btnConfirm.setText("Create");

      initialiseLists();
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Release");
      btnConfirm.setText("Save");
      btnConfirm.setDisable(true);
      releaseLabelField.setText(release.getLabel());
      releaseDescriptionField.setText(release.getReleaseDescription());
      releaseNotesField.setText(release.getReleaseNotes());
      projectComboBox.setValue(release.getProjectRelease());
      releaseDateField.setValue(release.getReleaseDate());

      initialiseLists();
    }
    this.createOrEdit = createOrEdit;

    if (release != null) {
      this.release = release;
      this.lastRelease = new Release(release);
    } else {
      this.release = null;
      this.lastRelease = null;
    }

    btnConfirm.setDefaultButton(true);
    thisStage.setResizable(false);

    // Handle TextField text changes.
    releaseLabelField.textProperty().addListener((observable, oldValue, newValue) -> {
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
      if (newValue.trim().length() > 20) {
        releaseLabelField.setStyle("-fx-text-inner-color: red;");
      } else {
        releaseLabelField.setStyle("-fx-text-inner-color: black;");
      }
    });

    releaseDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    releaseNotesField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    projectComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    releaseDateField.valueProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });




  }


  /**
   * checks if there are any changed fields and disables or enables the button accordingly
   */
  private void checkButtonDisabled() {
    if (releaseLabelField.getText().equals(release.getLabel()) &&
        releaseDescriptionField.getText().equals(release.getReleaseDescription()) &&
        releaseNotesField.getText().equals(release.getReleaseNotes()) &&
        projectComboBox.getSelectionModel().getSelectedItem().equals(release.getProjectRelease())
        && releaseDateField.getValue().toString().equals(release.getReleaseDate().toString())) {
      btnConfirm.setDisable(true);
    } else {
      btnConfirm.setDisable(false);
    }
  }

  /**
   * Initialise the contents of the lists according to whether the user is creating a new Release or
   * editing an existing one.
   */
  private void initialiseLists() {
    try {
      // loop for adding the specific project to release.
      availableProjects.addAll(mainApp.getProjects());

      this.projectComboBox.setVisibleRowCount(5);
      this.projectComboBox.setItems(availableProjects);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Takes the inputReleaseLabel and checks to see if it is allowed to be used as an label
   *
   * @param inputReleaseLabel the label that the user wants for this release
   * @return String returns this only if its an allowed label
   * @throws Exception Throws an exception if the label is not allowed
   */
  private String parseReleaseLabel(String inputReleaseLabel) throws Exception {
    inputReleaseLabel = inputReleaseLabel.trim();

    if (inputReleaseLabel.isEmpty()) {
      throw new Exception("Release Label is empty.");
    } else {
      String lastReleaseLabel;
      if (lastRelease == null) {
        lastReleaseLabel = "";
      } else {
        lastReleaseLabel = lastRelease.getLabel();
      }
      for (Release releaseInList : mainApp.getReleases()) {
        String releaseLabel = releaseInList.getLabel();
        if (releaseLabel.equalsIgnoreCase(inputReleaseLabel) &&
            !releaseLabel.equalsIgnoreCase(lastReleaseLabel)) {
          throw new Exception("Release Label is not unique");
        }
      }
    }
    return inputReleaseLabel;
  }

  /**
   * @param inputReleaseDescription The description that the user wants for this release
   * @return String returns the description if its allowed
   * @throws Exception Throws this if the description is empty
   */
  private String parseReleaseDescription(String inputReleaseDescription) throws Exception {

    if (inputReleaseDescription.isEmpty()) {
      throw new Exception("Release Description is empty.");
    }
    return inputReleaseDescription;
  }

  /**
   * @param inputReleaseNotes The release notes that the user wants
   * @return String returns the notes if it is allowed
   * @throws Exception throws notes is empty
   */
  private String parseReleaseNotes(String inputReleaseNotes) throws Exception {

    if (inputReleaseNotes.isEmpty()) {
      throw new Exception("Release Notes is empty.");
    }
    return inputReleaseNotes;
  }

  /**
   * @param inputReleaseDate the release date the user wants
   * @return String returns if date is allowed
   * @throws Exception throws if date is empty
   */
  private LocalDate parseReleaseDate(LocalDate inputReleaseDate) throws Exception {

    if (inputReleaseDate == null) {
      throw new Exception("Release Date is empty.");
    }
    return inputReleaseDate;
  }

  /**
   * @param inputReleaseProject the project that the user wants the release to be assigned too
   * @return Project object if it is allowed
   * @throws Exception throws if no project is assigned to this release
   */
  private Project parseReleaseProject(Project inputReleaseProject) throws Exception {

    if (inputReleaseProject == null) {
      throw new Exception("No project has been selected for this release.");
    }
    return inputReleaseProject;
  }

  /**
   * Handles when the cancel button is clicked by not applying any changes and closing dialog
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

}