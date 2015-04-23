package seng302.group5.controller;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.AgileHistory;
import seng302.group5.model.Project;
import seng302.group5.model.Team;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;

/**
 * Created by @author Alex Woo
 */
public class ProjectDialogController {

  @FXML private TextField projectIDField;
  @FXML private TextField projectNameField;
  @FXML private TextArea projectDescriptionField;
  @FXML private ListView availableTeamsList;
  @FXML private ListView allocatedTeamsList;
  @FXML private DatePicker teamStartDate;
  @FXML private DatePicker teamEndDate;
  @FXML private Button btnConfirm;
  @FXML private Button btnAddTeam;
  @FXML private Button btnRemoveTeam;


  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Project project = new Project();
  private Project lastProject;
  private ObservableList<AgileHistory> allocatedTeams = FXCollections.observableArrayList();
  private ObservableList<Team> availableTeams =  FXCollections.observableArrayList();
  private AgileHistory projectHistory = new AgileHistory();



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
    teamStartDate.setValue(LocalDate.now());
    teamEndDate.setValue(LocalDate.now());

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Project");
      btnConfirm.setText("Create");
      initialiseLists(CreateOrEdit.CREATE, project);
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Project");
      btnConfirm.setText("Save");
      initialiseLists(CreateOrEdit.EDIT, project);
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

    projectDescriptionField.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        btnConfirm.fire();
      }
    });
    btnConfirm.setDefaultButton(true);
  }

  /** Initialise the contents of the lists according to whether the user is creating a new
   * project or editing an existing one.
   * @param createOrEdit identifiy whether the user is creating or editing a project.
   * @param project The project that is being created or edited.
   */
  private void initialiseLists(CreateOrEdit createOrEdit, Project project) {
    try {
      if (createOrEdit == CreateOrEdit.CREATE) {
        availableTeams.addAll(
            mainApp.getTeams().stream().filter(team -> team.getCurrentProject() == null)
                .collect(Collectors.toList()));
      }
      else if (createOrEdit == CreateOrEdit.EDIT) {

        allocatedTeams.addAll(project.getTeam().stream().collect(Collectors.toList()));
        availableTeams.addAll(mainApp.getTeams().stream().collect(Collectors.toList()));
      }

      this.availableTeamsList.setItems(availableTeams);
      this.allocatedTeamsList.setItems(allocatedTeams);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
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
      for (Project projectInList : mainApp.getProjects()) {
        String projectID = projectInList.getProjectID();
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
   * Parses the selected dates and checks that all inputs are valid, that the selected team
   * is not currently assigned during the chosen dates to any projects.
   *
   * @param startDate The selected start date for the selected team.
   * @param endDate The selected end date for the selected team.
   * @param team The selected team that is going to be assigned if dates are valid.
   * @throws Exception with message describing why the input is invalid.
   */
  private void parseProjectDates(LocalDate startDate, LocalDate endDate, Team team) throws Exception {
    if (startDate.isAfter(endDate)) {
      throw new Exception("Start date must be before End Date");
    }
    else if (team == null) {
      throw new Exception("Please select a team to assign.");
    }
    else {
      for (Project project1 : mainApp.getProjects()) {
        for (AgileHistory team1 : project1.getTeam()) {
          if (Objects.equals(team.toString(), team1.getAgileItem().toString()) &&
              !project.getProjectID().equals(project1.getProjectID())) {
            if (team1.getStartDate().isEqual(startDate) || team1.getEndDate().isEqual(endDate)) {
              throw new Exception("The selected team is already assigned during selected dates.");
            }
            if (startDate.isAfter(team1.getStartDate()) && startDate.isBefore(team1.getEndDate())) {
              throw new Exception("The selected team is already assigned during selected dates.");
            } else if (endDate.isAfter(team1.getStartDate()) && endDate.isBefore(team1.getEndDate())) {
              throw new Exception("The selected team is already assigned during selected dates.");
            }
            else if (startDate.isBefore(team1.getStartDate()) && endDate.isAfter(team1.getStartDate())) {
              throw new Exception("The selected team is already assigned during selected dates.");
            }
            else if (startDate.isEqual(team1.getEndDate())) {
              throw new Exception("The selected team is already assigned during selected dates.");
            }
            else if (endDate.isEqual(team1.getStartDate())) {
              throw new Exception("The selected team is already assigned during selected dates.");
            }
          }
        }
      }
      for (AgileHistory team2 : allocatedTeams) {
        if (Objects.equals(team.toString(), team2.getAgileItem().toString())) {
          if (team2.getStartDate().isEqual(startDate) || team2.getEndDate().isEqual(endDate)) {
            throw new Exception("The selected team is already assigned during selected dates.");
          }
          if (startDate.isAfter(team2.getStartDate()) && startDate.isBefore(team2.getEndDate())) {
            throw new Exception("The selected team is already assigned during selected dates.");
          } else if (endDate.isAfter(team2.getStartDate()) && endDate.isBefore(team2.getEndDate())) {
            throw new Exception("The selected team is already assigned during selected dates.");
          }
          else if (startDate.isBefore(team2.getStartDate()) && endDate.isAfter(team2.getStartDate())) {
            throw new Exception("The selected team is already assigned during selected dates.");
          }
          else if (startDate.isEqual(team2.getEndDate())) {
            throw new Exception("The selected team is already assigned during selected dates.");
          }
          else if (endDate.isEqual(team2.getStartDate())) {
            throw new Exception("The selected team is already assigned during selected dates.");
          }
        }
      }
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
    undoRedoObject.setAgileItem(project);
    Project projectToStore = new Project(project);
    undoRedoObject.addDatum(projectToStore);

    return undoRedoObject;
  }





  /**
   * Handles when the create button is pushed
   */

  @FXML
  protected void btnAddTeam() {
    try {
      Team selectedTeam = (Team) availableTeamsList.getSelectionModel().getSelectedItem();
      parseProjectDates(teamStartDate.getValue(), teamEndDate.getValue(), selectedTeam);
      if (selectedTeam != null) {
        AgileHistory temp = new AgileHistory();
        temp.setAgileItem(selectedTeam);
        temp.setStartDate(teamStartDate.getValue());
        temp.setEndDate(teamEndDate.getValue());
        this.allocatedTeams.add(temp);

        projectHistory.setAgileItem(selectedTeam);
        projectHistory.setStartDate(teamStartDate.getValue());
        projectHistory.setEndDate(teamEndDate.getValue());
      }
      } catch (Exception e1) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Dates");
        alert.setHeaderText(null);
        alert.setContentText(e1.getMessage());
        alert.showAndWait();
        mainApp.refreshList();
    }
  }

  @FXML
  protected void btnRemoveTeam() {
    try {
      AgileHistory selectedAgileHistory = (AgileHistory) allocatedTeamsList.getSelectionModel().getSelectedItem();
      Team selectedTeam = (Team) selectedAgileHistory.getAgileItem();

      if (selectedTeam != null) {
        AgileHistory temp = new AgileHistory();
        for (AgileHistory t : allocatedTeams) {
          if (Objects.equals(t.getAgileItem().toString(), selectedTeam.toString())) {
            temp = t;
            break;
          }
        }
        this.allocatedTeams.remove(temp);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void btnConfirmClick() {
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
        for (AgileHistory team : this.allocatedTeams) {
          project.addTeam(team);
        }
        mainApp.addProject(project);
      } else {
        if (createOrEdit == CreateOrEdit.EDIT) {
          project.setProjectID(projectID);
          project.setProjectName(projectName);
          project.setProjectDescription(projectDescription);
          project.getTeam().clear();
          for (AgileHistory team : this.allocatedTeams) {
            project.addTeam(team);
          }

          mainApp.refreshList();
        }
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
  protected void btnCancelClick() {
    thisStage.close();
  }

}
