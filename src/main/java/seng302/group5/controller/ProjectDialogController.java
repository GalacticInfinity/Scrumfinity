package seng302.group5.controller;

import org.mockito.internal.matchers.Null;

import java.time.LocalDate;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.AgileHistory;
import seng302.group5.model.AgileItem;
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


  private LocalDate defaultDate;
  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Project project = new Project();
  private Project lastProject;
  private ObservableList<AgileHistory> allocatedTeams = FXCollections.observableArrayList();
  private ObservableList<Team> teamsName = FXCollections.observableArrayList();
  private ObservableList<Team> availableTeams =  FXCollections.observableArrayList();
  private Team selectedTeam;
  private AgileHistory projectHistory = new AgileHistory();
  private LocalDate startDate;
  private LocalDate endDate;



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
      //teamStartDate.setValue(selectedTeam.getStartDate());

      //teamEndDate.setValue(selectedTeam.getEndDate());

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

  private void initialiseLists(CreateOrEdit createOrEdit, Project project) {
    try {
      if (createOrEdit == CreateOrEdit.CREATE) {
        for (Team team : mainApp.getTeams()) {
          if (team.getCurrentProject() == null) {
            availableTeams.add(team);
          }
        }
      }
      else if (createOrEdit == CreateOrEdit.EDIT) {

        for (AgileHistory team : project.getTeam()) {
          teamsName.add((Team) team.getAgileItem());


        }
        for (Team team : mainApp.getTeams()) {
          availableTeams.add(team);
        }
      }



      this.availableTeamsList.setItems(availableTeams);
      this.allocatedTeamsList.setItems(teamsName);
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
   * @param startDate
   * @param endDate
   * @param team
   * @throws Exception with message describing why the input is invalid.
   */
  private void parseProjectDates(LocalDate startDate, LocalDate endDate, Team team) throws Exception {
    if (startDate.isAfter(endDate)) {
      throw new Exception("Start date must be before End Date");
    }
    else {
      for (Project project1 : mainApp.getProjects()) {
        for (AgileHistory team1 : project1.getTeam()) {
          if (team.toString() == team1.getAgileItem().toString()) {
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
          }
        }
      }
      for (AgileHistory team2 : allocatedTeams) {
        if (team.toString() == team2.getAgileItem().toString()) {
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
  protected void btnAddTeam(ActionEvent event) {
    try {
      Team selectedTeam = (Team) availableTeamsList.getSelectionModel().getSelectedItem();
      parseProjectDates(teamStartDate.getValue(), teamEndDate.getValue(), selectedTeam);
      if (selectedTeam != null) {
        AgileHistory temp = new AgileHistory();
        temp.setAgileItem(selectedTeam);
        temp.setStartDate(teamStartDate.getValue());
        temp.setEndDate(teamEndDate.getValue());
        this.teamsName.add(selectedTeam);
        this.allocatedTeams.add(temp);
        //this.availableTeams.remove(selectedTeam);
        //add parse date function.

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
        return;
    }
  }

  @FXML
  protected void btnRemoveTeam(ActionEvent event) {
    try {
      Team selectedTeam = (Team) allocatedTeamsList.getSelectionModel().getSelectedItem();

      if (selectedTeam != null) {
        this.teamsName.remove(selectedTeam);
        this.availableTeams.add(selectedTeam);
        for (AgileHistory t : allocatedTeams) {
          if (t.getAgileItem().toString() == selectedTeam.toString()) {
            this.allocatedTeams.remove(t);
          }
        }

        //project.removeTeam(selectedTeam);
        //Add function to remove the team from the project on the team object level
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

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
            System.out.println(team.getAgileItem().toString());
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
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

}
