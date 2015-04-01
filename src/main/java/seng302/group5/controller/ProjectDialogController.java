package seng302.group5.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Project;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;

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
  private Project lastProject;

  /**
   * Setup the project dialog controller
   *
   * @param mainApp - The main application object
   * @param thisStage - The stage of the dialog
   * @param createOrEdit - If dialog is for creating or editing a project
   * @param project - The project object if editing, null otherwise
   */
  public void setupController(Main mainApp,
                              Stage thisStage,
                              CreateOrEdit createOrEdit,
                              Project project) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

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

    if (project != null) {
      this.project = project;
      // Make a copy for the undo stack
      this.lastProject = new Project(project);
    } else {
      this.project = null;
      this.lastProject = null;
    }

    projectDescriptionField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          btnConfirm.fire();
        }
      }
    });
    btnConfirm.setDefaultButton(true);
  }

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
      String lastProjectID;
      if (lastProject == null) {
        lastProjectID = "";
      } else {
        lastProjectID = lastProject.getProjectID();
      }
      for (Project project : mainApp.getProjects()) {
        String projectID = project.getProjectID();
        if (projectID.equals(inputProjectID) && !projectID.equals(lastProjectID)) {
          throw new Exception("Project ID is not unique.");
        }
      }
    }
    return inputProjectID;
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
   * Generate an UndoRedoObject to place in the stack
   * @return the UndoRedoObject to store
   */
  private UndoRedoObject generateUndoRedoObject() {
    UndoRedoObject undoRedoObject = new UndoRedoObject();

    if (createOrEdit == CreateOrEdit.CREATE) {
      undoRedoObject.setAction(Action.PROJECT_CREATE);
    } else {
      undoRedoObject.setAction(Action.PROJECT_EDIT);
      undoRedoObject.addDatum(lastProject);
    }

    // Store a copy of project to edit in stack to avoid reference problems
    Project projectToStore = new Project(project);
    undoRedoObject.addDatum(projectToStore);

    return undoRedoObject;
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

      UndoRedoObject undoRedoObject = generateUndoRedoObject();
      mainApp.newAction(undoRedoObject);

      thisStage.close();
    }
  }

  /**
   * Close the dialog
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

}
