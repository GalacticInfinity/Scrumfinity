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
import seng302.group5.model.Skill;

/**
 * @author liang Ma
 */
public class SkillsDialogController {

  @FXML private TextField skillName;
  @FXML private TextArea skillDescription;
  @FXML private Button skillCreation;

  private Main mainApp;
  private Stage thisStage;
  private Skill skill;
  private CreateOrEdit createOrEdit;
  private String lastSkillName;

  /**
   * Setup the skill dialog controller
   *
   * @param mainApp - The main application object
   * @param thisStage - The stage of the dialog
   * @param createOrEdit - If dialog is for creating or editing a skill
   * @param skill - The skill object if editing, null otherwise
   */
  public void setupController(Main mainApp,
                              Stage thisStage,
                              CreateOrEdit createOrEdit,
                              Skill skill) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Skill");
      skillCreation.setText("Create");
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Skill");
      skillCreation.setText("Save");

      skillName.setText(skill.getSkillName());
      skillDescription.setText(skill.getSkillDescription());
    }
    this.createOrEdit = createOrEdit;

    if (skill != null) {
      this.skill = skill;
      this.lastSkillName = skill.getSkillName();
    } else {
      this.skill = null;
      this.lastSkillName = "";
    }

    skillCreation.setDefaultButton(true);
  }

  /**
   * Parse a string containing a skill name. Throws exceptions if input is not valid.
   * @param inputSkillName String of skill name
   * @return Input project name if it is valid.
   * @throws Exception Exception with message explaining why input is invalid.
   */
  private String parseSkillName(String inputSkillName) throws Exception {
    if (inputSkillName.isEmpty()) {
      throw new Exception("Skill Name is empty");
    } else if (inputSkillName.length() > 32) {
      throw new Exception("Skill Name is more than 32 characters long");
    } else {
      for (Skill aSkill : mainApp.getSkills()) {
        String aSkillName = aSkill.getSkillName();
        if (aSkillName.equals(inputSkillName) && !aSkillName.equals(lastSkillName)) {
          throw new Exception("Skill name is not unique.");
        }
      }
    }
    return inputSkillName;
  }

  /**
   * Handles when the create button is pushed
   */
  @FXML
  protected void SkillCreation(ActionEvent e) {
    String nameOfSkill = null;
    try {
      nameOfSkill = parseSkillName(skillName.getText());
    } catch (Exception e1) {
      //e1.printStackTrace();
    }
    if (createOrEdit == CreateOrEdit.CREATE) {
      skill = new Skill(nameOfSkill, skillDescription.getText());
      mainApp.addSkill(skill);
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      skill.setSkillName(nameOfSkill);
      skill.setSkillDescription(skillDescription.getText());
      mainApp.refreshList();
    }

    thisStage.close();
  }

  @FXML
  protected void CancelCreation(ActionEvent e) {
    thisStage.close();
  }

  public void setCreateOrEdit(CreateOrEdit createOrEdit) {
    this.createOrEdit = createOrEdit;
    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Skill");
      skillCreation.setText("Create");
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Skill");
      skillCreation.setText("Save");

      skillName.setText(skill.getSkillName());
      skillDescription.setText(skill.getSkillDescription());
    }
  }
  public void setMainApp(Main mainApp){
    this.mainApp = mainApp;
  }

  public void setStage(Stage stage) {
    this.thisStage = stage;
  }

  public void setSkill(Skill skill) {
    this.skill = skill;
  }

  public String checkName(String name) {
    String error = null;
    String nameOfSkill = null;
    try {
      nameOfSkill = parseSkillName(name);
    } catch (Exception e1) {
      error = e1.getMessage();
    }
    if (error != null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Input Error");
      alert.setHeaderText(null);
      alert.setContentText(error.toString());
      alert.showAndWait();
    }
    return nameOfSkill;
  }
}

