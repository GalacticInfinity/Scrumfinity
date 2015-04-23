package seng302.group5.controller;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Person;
import seng302.group5.model.Role;
import seng302.group5.model.Team;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;

/**
 * Created by Zander on 24/03/2015.
 */
public class TeamDialogController {

  private Main mainApp;
  private Stage thisStage;

  private Team team;
  private Team lastTeam;

  private CreateOrEdit createOrEdit;

  private ObservableList<Person> availableMembers = FXCollections.observableArrayList();
  private ObservableList<PersonRole> selectedMembers = FXCollections.observableArrayList();
  private ArrayList<Person> membersToRemove = new ArrayList<>();

  @FXML private TextField teamIDField;
  @FXML private ListView<PersonRole> teamMembersList;
  @FXML private ComboBox<Person> teamMemberAddCombo;
  @FXML private ComboBox<Role> teamMemberRoleCombo;
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
    this.createOrEdit = createOrEdit;

    if (team != null) {
      this.team = team;
      this.lastTeam = new Team(team);
    } else {
      this.team = null;
      this.lastTeam = null;
    }

    teamDescriptionField.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        btnConfirm.fire();
      }
    });
    btnConfirm.setDefaultButton(true);
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
          Role role = team.getMembersRole().get(person);
          selectedMembers.add(new PersonRole(person, role));
        }
      }

      this.teamMemberAddCombo.setVisibleRowCount(5);
      this.teamMemberAddCombo.setItems(availableMembers);
      this.teamMembersList.setItems(selectedMembers);

      this.teamMemberRoleCombo.setPromptText("Person's Role");
      this.teamMemberRoleCombo.setItems(mainApp.getRoles());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void btnAddMemberClick(ActionEvent event) {
    try {
      Person selectedPerson = teamMemberAddCombo.getSelectionModel().getSelectedItem();
      Role selectedRole = teamMemberRoleCombo.getSelectionModel().getSelectedItem();

      int roleTally = 0;
      for (PersonRole personRole : selectedMembers) {
        if (personRole.getRole() == selectedRole) {
          roleTally++;
        }
      }

      if (selectedRole != null && roleTally >= selectedRole.getMemberLimit()) {
        // TODO dialog
        System.out.println(String.format("Max number of %s already assigned", selectedRole));
      } else if (selectedPerson != null) {

        if (selectedRole != null && selectedRole.getRequiredSkill() != null &&
            !selectedPerson.getSkillSet().contains(selectedRole.getRequiredSkill())) {

          System.out.println(String.format("%s does not have the required skill of %s",
                                           selectedPerson, selectedRole.getRequiredSkill()));
        } else {
          this.selectedMembers.add(new PersonRole(selectedPerson, selectedRole));
          this.availableMembers.remove(selectedPerson);
          this.membersToRemove.remove(selectedPerson);
          this.teamMemberAddCombo.setItems(availableMembers);
        }
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  @FXML
  protected void btnRemoveMemberClick(ActionEvent event) {
    try {
      PersonRole selectedPersonRole = teamMembersList.getSelectionModel().getSelectedItem();

      if (selectedPersonRole != null) {
        Person selectedPerson = selectedPersonRole.getPerson();
        this.availableMembers.add(selectedPerson);
        this.selectedMembers.remove(selectedPersonRole);
        this.membersToRemove.add(selectedPerson);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Generate an UndoRedoObject to place in the stack
   * @return the UndoRedoObject to store
   */
  private UndoRedoObject generateUndoRedoObject() {
    UndoRedoObject undoRedoObject = new UndoRedoObject();

    if (createOrEdit == CreateOrEdit.CREATE) {
      undoRedoObject.setAction(Action.TEAM_CREATE);
    } else {
      undoRedoObject.setAction(Action.TEAM_EDIT);
      undoRedoObject.addDatum(lastTeam);
    }

    // Store a copy of skill to edit in stack to avoid reference problems
    undoRedoObject.setAgileItem(team);
    Team teamToStore = new Team(team);
    undoRedoObject.addDatum(teamToStore);

    return undoRedoObject;
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
        team = new Team(teamID, teamDescription);
        for (PersonRole personRole : selectedMembers) {
          team.addTeamMember(personRole.getPerson(), personRole.getRole());
          personRole.getPerson().assignToTeam(team);
        }
        mainApp.addTeam(team);

      } else if (createOrEdit == CreateOrEdit.EDIT) {

        team.setTeamID(teamID);
        team.setTeamDescription(teamDescription);
        team.getTeamMembers().clear();
        team.getMembersRole().clear();
        for (PersonRole personRole : selectedMembers) {
          team.addTeamMember(personRole.getPerson(), personRole.getRole());
          personRole.getPerson().assignToTeam(team);
        }
        for (Person memberToRemove : membersToRemove) {
          memberToRemove.removeFromTeam();
        }
        mainApp.refreshList();
      }

      UndoRedoObject undoRedoObject = generateUndoRedoObject();
      mainApp.newAction(undoRedoObject);
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
      String lastTeamID;
      if (lastTeam == null) {
        lastTeamID = "";
      } else {
        lastTeamID = lastTeam.getTeamID();
      }
      for (Team team : mainApp.getTeams()) {
        String teamID = team.getTeamID();
        if (team.getTeamID().equals(inputTeamID) && !teamID.equals(lastTeamID)) {
          throw new Exception("Team ID is not unique.");
        }
      }
      return inputTeamID;
    }
  }

  /**
   * Inner class for storing/displaying allocated team members with their respective roles
   */
  private class PersonRole {

    private Person person;
    private Role role;

    public PersonRole(Person person, Role role) {
      this.person = person;
      this.role = role;
    }

    public Person getPerson() {
      return person;
    }

    public Role getRole() {
      return role;
    }

    @Override
    public String toString() {
      if (role != null) {
        return person.toString() + " - Role: " + role.toString();
      } else {
        return person.toString();
      }
    }

  }
}
