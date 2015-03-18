package seng302.group5.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seng302.group5.Main;

/**
 * @author liang Ma
 */
public class SkillsDialogController {

  @FXML private TextField skillName;
  @FXML private TextArea skillDescription;

  private Main mainApp;
  private Stage thisStage;

  /**
   * Handles when the create button is pushed
   */
  @FXML
  protected void SkillCreation(ActionEvent e) {

    mainApp.addSkill(skillName.getText(), skillDescription.getText());
    thisStage.close();
  }

  @FXML
  protected void CancelCreation(ActionEvent e) {
    thisStage.close();
  }

  public void setMainApp(Main mainApp){
    this.mainApp = mainApp;
  }

  public void setStage(Stage stage) {
    this.thisStage = stage;
  }
}

