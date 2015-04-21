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
  @FXML private TextField releaseDescriptionField;
  @FXML private DatePicker releaseDateField;
  @FXML private TextField releaseNotesField;

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
      releaseDescription = parseReleaseDescription(releaseDescription);
      releaseDate = parseReleaseDate(releaseDate);
      releaseNotes = parseReleaseNotes(releaseNotes);
      releaseProject = parseReleaseProject(selectedProject);
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

      thisStage.close();
    }
  }

  public void setupController(Main mainApp,
                              Stage thisStage,
                              CreateOrEdit createOrEdit,
                              Release release) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

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
      this.lastRelease = new Release();
    } else {
      this.release = null;
      this.lastRelease = null;
    }

    btnConfirm.setDefaultButton(true);
  }

  private void initialiseLists(CreateOrEdit createOrEdit, Release release) {
    try {

      // loop for adding the specific project to release.
      for (Project item : mainApp.getProjects()) {
        if(!selectedProject.contains(item)) {
          availableProjects.add(item);
        }
      }

      this.projectList.setVisibleRowCount(5);
      this.projectList.setPromptText("Available Projects");


      this.projectList.setItems(availableProjects);

      this.projectLists.setItems(selectedProject);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void btnRemoveSkill(ActionEvent event) {
    try {
      Project selectedItem = (Project) projectLists.getSelectionModel().getSelectedItem();

      if (selectedProject != null) {
        this.availableProjects.add(selectedItem);
        this.selectedProject.remove(selectedItem);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void btnAddProject(ActionEvent event) {
    try {
      Project selectedProject = (Project) projectList.getSelectionModel().getSelectedItem();

      if (selectedProject != null) {
        this.selectedProject.add(selectedProject);
        this.availableProjects.remove(availableProjects);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

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
          throw new Exception("Person ID is not unique");
        }
      }
    }
    return inputReleaseID;
  }


  private String parseReleaseDescription(String inputReleaseDescription) throws Exception {

    if (inputReleaseDescription.isEmpty()) {
      throw new Exception("Release Description is empty.");
    }
    return inputReleaseDescription;
  }

  private String parseReleaseNotes(String inputReleaseNotes) throws Exception {

    if (inputReleaseNotes.isEmpty()) {
      throw new Exception("Release Notes is empty.");
    }
    return inputReleaseNotes;
  }

  private LocalDate parseReleaseDate(LocalDate inputReleaseDate) throws Exception {

    if (inputReleaseDate == null) {
      throw new Exception("Release Date is empty.");
    }
    return inputReleaseDate;
  }

  private Project parseReleaseProject(ObservableList inputReleaseProject) throws Exception {

    if (inputReleaseProject.isEmpty()) {
      throw new Exception("Assign a project to this release.");
    }
    return (Project) inputReleaseProject.get(0);
  }

  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

  public void setMainApp(Main mainApp){
    this.mainApp = mainApp;
  }

  public void setStage(Stage stage) {
    this.thisStage = stage;
  }

}
