package seng302.group5.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Project;

/**
 * Created by @author Alex Woo
 */
public class ProjectDialogController {

  @FXML private TextField projectIDField;
  @FXML private TextField projectNameField;
  @FXML private TextArea projectDescriptionField;
  @FXML private Button btnConfirm;

  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Project project;

  /**
   * Parse a string containing a project ID. Throws exceptions if input is not valid.
   * @param inputProjectID String of project ID
   * @return Inputted project ID if it is valid.
   * @throws Exception Exception with message explaining why input is invalid.
   */
  private String parseProjectID(String inputProjectID) throws Exception {
    inputProjectID = inputProjectID.trim();

    if (inputProjectID.isEmpty()) {
      throw new Exception("Project ID is empty");
    } else if (inputProjectID.length() > 8) {
      throw new Exception("Project ID is more than 8 characters long");
    } else {
      for (Project project : mainApp.getProjects()) {
        if (project.getProjectID().equals(inputProjectID)) {
          throw new Exception("Project ID is not unique.");
        }
      }
      return inputProjectID;
    }
  }

  /**
   * Parse a string containing a project name. Throws exceptions if input is not valid.
   * @param inputProjectName String of project name
   * @return Inputted project name if it is valid.
   * @throws Exception Exception with message explaining why input is invalid.
   */
  private String parseProjectName(String inputProjectName) throws Exception {
    inputProjectName = inputProjectName.trim();

    if (inputProjectName.isEmpty()) {
      throw new Exception("Project Name is empty");
    } else if (inputProjectName.length() > 16) {
      throw new Exception("Project Name is more than 16 characters long");
    } else {
      return inputProjectName;
    }
  }

  /**
   * Handles when the create button is pushed
   */
  @FXML
  protected void btnConfirmClick(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    int noErrors = 0;

    String projectID = "";
    String projectName = "";
    String projectDescription = projectDescriptionField.getText().trim();

    try {
      projectID = parseProjectID(projectIDField.getText());
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    try {
      projectName = parseProjectName(projectNameField.getText());
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
      alert.setContentText(errors.toString());
      alert.showAndWait();
    } else {

      if (createOrEdit == CreateOrEdit.CREATE) {
        project = new Project(projectID, projectName, projectDescription);
        mainApp.addProject(project);
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        project.setProjectID(projectID);
        project.setProjectName(projectName);
        project.setProjectDescription(projectDescription);
        mainApp.refreshList();
      }

      thisStage.close();
    }
  }

  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }

  public void setStage(Stage stage) {
    this.thisStage = stage;
  }

  public void setCreateOrEdit(CreateOrEdit createOrEdit) {
    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Project");
      btnConfirm.setText("Create");
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Project");
      btnConfirm.setText("Save");

      projectIDField.setText(project.getProjectID());
      projectNameField.setText(project.getProjectName());
      projectDescriptionField.setText(project.getProjectDescription());
    }
    this.createOrEdit = createOrEdit;
  }

  public void setProject(Project project) {
    this.project = project;
  }

}