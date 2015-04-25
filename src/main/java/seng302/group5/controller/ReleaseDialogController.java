package seng302.group5.controller;

import java.time.LocalDate;

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
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Release;
import seng302.group5.model.Project;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;

/**
 * Created by Craig Barnard on 7/04/2015.
 * Release Dialog Controller, manages the usage of Dialogs involved in the creating and editing
 * of releases.
 */
public class ReleaseDialogController {

  @FXML private TextField releaseIDField;
  @FXML private TextArea releaseDescriptionField;
  @FXML private DatePicker releaseDateField;
  @FXML private TextArea releaseNotesField;

  @FXML private Button btnConfirm;
  @FXML private Button btnAddProject;
  @FXML private Button btnRemoveProject;
  @FXML private Button btnCancel;
  @FXML private ComboBox projectList;
  @FXML private ListView projectLists;

  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Release release = new Release();
  private Release lastRelease;

  private ObservableList<Project> availableProjects = FXCollections.observableArrayList();
  private ObservableList<Project> selectedProject = FXCollections.observableArrayList();

  /**
   * Handles when the Create button is pushed by finalising all changes made or creating the new
   * Release object and adding it after checking for errors.
   * @param event
   */
  @FXML
  protected void btnCreateRelease(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    errors.append("Invalid Fields:");
    int noErrors = 0;


    String releaseId = releaseIDField.getText().trim();
    String releaseDescription = releaseDescriptionField.getText().trim();
    LocalDate releaseDate = releaseDateField.getValue();
    String releaseNotes = releaseNotesField.getText().trim();
    ObservableList<Project> projects = projectLists.getItems();
    Project releaseProject = new Project();

    for (Project item : projects) {
      releaseProject = item;
    }

    try {
      releaseId = parseReleaseID(releaseIDField.getText());
   }
    catch (Exception e) {
      noErrors++;
      errors.append(String.format("\n\t%s", e.getMessage()));
    }

    try {
      releaseDescription = parseReleaseDescription(releaseDescription);
    }
    catch (Exception e) {
      noErrors++;
      errors.append(String.format("\n\t%s", e.getMessage()));
    }
    try {
      releaseDate = parseReleaseDate(releaseDate);
    }
    catch (Exception e) {
      noErrors++;
      errors.append(String.format("\n\t%s", e.getMessage()));
    }
    try {
      releaseProject = parseReleaseProject(selectedProject);
    }
    catch (Exception e) {
      noErrors++;
      errors.append(String.format("\n\t%s", e.getMessage()));
    }
    try {
      releaseNotes = parseReleaseNotes(releaseNotes);
    }
    catch (Exception e) {
      noErrors++;
      errors.append(String.format("\n\t%s", e.getMessage()));
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
    }
    else {
      if (createOrEdit == CreateOrEdit.CREATE) {
        release = new Release(releaseId, releaseDescription, releaseNotes, releaseDate, releaseProject);
        mainApp.addRelease(release);
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        release.setReleaseName(releaseId);
        release.setReleaseDescription(releaseDescription);
        release.setReleaseDate(releaseDate);
        release.setReleaseNotes(releaseNotes);
        release.setProjectRelease(releaseProject);

        releaseDateField.setValue(release.getReleaseDate());
        projectList.setValue(release.getProjectRelease());
        mainApp.refreshList();
      }
      UndoRedoObject undoRedoObject = generateUndoRedoObject();
      mainApp.newAction(undoRedoObject);

      thisStage.close();
    }
  }

  /**
   * Generate an UndoRedoObject to place in the stack
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
   * @param mainApp The main application object
   * @param thisStage The stage of the dialog
   * @param createOrEdit If dialog is for creating or editing a project
   * @param release the Release object if editing other wise null
   */
  public void setupController(Main mainApp,
                              Stage thisStage,
                              CreateOrEdit createOrEdit,
                              Release release) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;
    releaseDateField.setValue(LocalDate.now());

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Release");
      btnConfirm.setText("Create");
      initialiseLists(CreateOrEdit.CREATE, release);
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Release");
      btnConfirm.setText("Save");

      releaseIDField.setText(release.getReleaseName());
      releaseDescriptionField.setText(release.getReleaseDescription());
      this.selectedProject.add(release.getProjectRelease());
      this.releaseDateField.setValue(release.getReleaseDate());
      releaseNotesField.setText(release.getReleaseNotes());
      initialiseLists(CreateOrEdit.EDIT, release);
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
  }

  /**Initialise the contents of the lists according to whether the user is creating a new
   * Release or editing an existing one.
   * @param createOrEdit identify whether the user is creating or editing a Release.
   * @param release the Release that is being created or edited
   */
  private void initialiseLists(CreateOrEdit createOrEdit, Release release) {
    try {

      // loop for adding the specific project to release.
      for (Project item : mainApp.getProjects()) {
        availableProjects.add(item);
      }

      this.projectList.setVisibleRowCount(5);


      this.projectList.setItems(availableProjects);

      this.projectLists.setItems(selectedProject);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles when the remove button is pushed by removing the project from the release
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnRemoveProject(ActionEvent event) {
    try {
      if (this.selectedProject != null) {
        this.selectedProject.remove(0);
        projectLists.setItems(this.selectedProject);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles adding a project to the release if a project is already added then nothing happens
   * until you remove the already added project.
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnAddProject(ActionEvent event) {
    try {
      Project selectedProject = (Project) projectList.getSelectionModel().getSelectedItem();

      if (selectedProject != null) {
        if (!this.selectedProject.isEmpty()) {
          this.selectedProject.remove(0);
        }
        this.selectedProject.add(selectedProject);
        projectLists.setItems(this.selectedProject);
        this.availableProjects.remove(availableProjects);
        this.projectList.getSelectionModel().clearSelection();
        this.projectList.setValue(null);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Takes the inputReleaseID and checks to see if it is allowed to be used as an ID
   *
   * @param inputReleaseID the ID that the user wants for this release
   * @return String returns this only if its an allowed ID
   * @throws Exception Throws an exception if the ID is not allowed
   */
  private String parseReleaseID(String inputReleaseID) throws Exception {
    inputReleaseID = inputReleaseID.trim();

    if (inputReleaseID.isEmpty()) {
      throw new Exception("Release ID is empty.");
    }
    else if (inputReleaseID.length() > 8) {
      throw new Exception("Release ID is more than 8 characters long");
    }
    else {
      String lastReleaseID;
      if (lastRelease == null) {
        lastReleaseID = "";
      } else {
        lastReleaseID = lastRelease.getReleaseName();
      }
      for (Release releaseInList : mainApp.getReleases()) {
        String releaseName = releaseInList.getReleaseName();
        if (releaseName.equals(inputReleaseID) && !releaseName.equals(lastReleaseID)) {
          throw new Exception("Release ID is not unique");
        }
      }
    }
    return inputReleaseID;
  }

  /**
   *
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
   *
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
   *
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
   *
   * @param inputReleaseProject the project that the user wants the release to be assigned too
   * @return Project object if it is allowed
   * @throws Exception throws if no project is assigned to this release
   */
  private Project parseReleaseProject(ObservableList inputReleaseProject) throws Exception {

    if (inputReleaseProject.isEmpty()) {
      throw new Exception("Assign a project to this release.");
    }
    return (Project) inputReleaseProject.get(0);
  }

  /**
   * Handles when the cancel button is clicked by not applying any changes and closing dialog
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

}
