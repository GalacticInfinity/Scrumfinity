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
    Project project = new Project();

    for (Project item : projects) {
      project = item;
    }

    //try {
    //  releaseId = parseReleaseID(releaseIDField.getText()); ToDo ParseReleaseID method
   // }
   // catch (Exception e) {
    //  noErrors++;
    //  errors.append(String.format("\n\t%s", e.getMessage()));
  //}

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
        release = new Release(releaseId, releaseDescription, releaseNotes, releaseDate, project);
        mainApp.addRelease(release);
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        release.setReleaseName(releaseId);
        release.setReleaseDescription(releaseDescription);
        release.setReleaseDate(releaseDate);
        release.setReleaseNotes(releaseNotes);
        release.setProjectRelease(project);

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
      thisStage.setTitle("Edit Person");
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
