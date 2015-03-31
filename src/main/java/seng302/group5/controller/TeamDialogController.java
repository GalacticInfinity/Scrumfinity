package seng302.group5.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Person;
import seng302.group5.model.Team;

/**
 * Created by Zander on 24/03/2015.
 */
public class TeamDialogController {

  private Main mainApp;
  private Stage thisStage;

  private ObservableList<Person> availableMembers = FXCollections.observableArrayList();
  private ObservableList<Person> selectedMembers = FXCollections.observableArrayList();

  @FXML private TextField teamIDField;
  @FXML private ListView teamMembersList;
  @FXML private ComboBox teamMemberAddCombo;
  @FXML private TextArea teamDescriptionField;
  @FXML private Button btnConfirm;

  public void setupController(Main mainApp, Stage thisStage, CreateOrEdit createOrEdit, Team team) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Team");
      btnConfirm.setText("Create");
      initialiseLists(CreateOrEdit.CREATE, team);
    }
    else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Team");
      btnConfirm.setText("Save");

      teamIDField.setText(team.getTeamID());
      initialiseLists(CreateOrEdit.EDIT, team);
      teamDescriptionField.setText(team.getTeamDescription());
    }
  }

  /**
   * Populates the people selection lists for assigning
   * people to the team.
   */
  private void initialiseLists(CreateOrEdit createOrEdit, Team team) {
    try {
      if (createOrEdit == CreateOrEdit.CREATE) {
        for (Person person : mainApp.getPeople()) {
          if (person.isInTeam() == false) {
            availableMembers.add(person);
          }
        }
      }
      else if (createOrEdit == CreateOrEdit.EDIT) {
        for (Person person : mainApp.getPeople()) {
          if (person.isInTeam() == false) {
            availableMembers.add(person);
          }
        }
        for (Person person : team.getTeamMembers()) {
          selectedMembers.add(person);
        }
      }

      this.teamMemberAddCombo.setVisibleRowCount(5);
      this.teamMemberAddCombo.setPromptText("Available People");

      this.teamMemberAddCombo.setItems(availableMembers);
      this.teamMembersList.setItems(selectedMembers);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void btnAddMemberClick(ActionEvent event) {
    try {
      Person selectedPerson = (Person) teamMemberAddCombo.getSelectionModel().getSelectedItem();

      if (selectedPerson != null) {
        this.selectedMembers.add(selectedPerson);
        this.availableMembers.remove(selectedPerson);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void btnRemoveMemberClick(ActionEvent event) {
    try {
      Person selectedPerson = (Person) teamMembersList.getSelectionModel().getSelectedItem();

      if (selectedPerson != null) {
        this.availableMembers.add(selectedPerson);
        this.selectedMembers.remove(selectedPerson);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void btnConfirmClick(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    errors.append("Invalid Fields:");
    int noErrors = 0;

    String teamID = "";
    String teamDescription = teamDescriptionField.getText().trim();

    try {
      teamID = parseTeamID(teamIDField.getText());
    }
    catch (Exception e) {
      noErrors++;
      errors.append(String.format("\n\t%s", e.getMessage()));
    }

    // Display all errors if they exist
    if (noErrors > 0) {
      String title;
      if (noErrors == 1) {
        title = String.format("%d Invalid Field", noErrors);
      }
      else {
        title = String.format("%d Invalid Fields", noErrors);
      }
      // TODO: Dialogs for errors
      System.out.println(String.format("%s\n%s", title, errors.toString()));
    }
    else {
      Team team = new Team(teamID, selectedMembers, teamDescription);
      for (Person person : selectedMembers) {
        person.assignToTeam(team);
      }
      mainApp.addTeam(team);
      thisStage.close();
    }
  }

  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

  /**
   * Checks if teamID field contains valid input.
   *
   * @param inputTeamID String teamID.
   * @return teamID if teamID is valid.
   * @throws Exception If teamID is not valid.
   */
  private String parseTeamID(String inputTeamID) throws Exception {
    inputTeamID = inputTeamID.trim();

    if (inputTeamID.isEmpty()) {
      throw new Exception("Team ID is empty.");
    }
    else if (inputTeamID.length() > 8) {
      throw new Exception("Team ID is more than 8 characters long");
    }
    else {
      for (Team team : mainApp.getTeams()) {
        if (team.getTeamID().equals(inputTeamID)) {
          throw new Exception("Team ID is not unique.");
        }
      }
      return inputTeamID;
    }
  }
}
