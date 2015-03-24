package seng302.group5.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Skills;

/**
 * @author liang Ma
 */
public class SkillsDialogController {

  @FXML private TextField skillName;
  @FXML private TextArea skillDescription;
  @FXML private Button skillCreation;

  private Main mainApp;
  private Stage thisStage;
  private Skills skill;
  private CreateOrEdit createOrEdit;

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
      for (Skills aSkill : mainApp.getSkills()) {
        if (aSkill.getSkillName().equals(inputSkillName)) {
          throw new Exception("Skill name is not unique.");
        }
      }
      return inputSkillName;
    }
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
      skill = new Skills(nameOfSkill, skillDescription.getText());
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

  public void setSkill(Skills skill) {
    this.skill = skill;
  }
}

