package seng302.group5.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seng302.group5.Main;

/**
 * Created by @author Alex Woo
 */



public class ProjectDialogController {

  @FXML private TextField shortName;
  @FXML private TextField projectName;
  @FXML private TextArea projectDescription;

  private Main mainApp;
  private Stage thisStage;

  /**
   * Handles when the create button is pushed
   */
  @FXML
  protected void btnCreateProjectClick(ActionEvent event) {

    mainApp.addProject(shortName.getText(), projectName.getText(), projectDescription.getText());
    thisStage.close();
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
