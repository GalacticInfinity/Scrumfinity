package seng302.group5.controller;

import java.util.List;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import seng302.group5.Main;
import seng302.group5.model.Project;

/**
 * Created by Michael on 3/15/2015.
 * Controller for MenuBar
 *
 * Edited by Craig on 17/03/2015.
 * added save button functionality.
 */
public class MenuBarController {

  @FXML private MenuItem showListMenuItem;

  private Main mainApp;

  /**
   * Tells ListMainPaneController to hide/show the item list based on current state
   * (stored in ListMainPaneController)
   */
  @FXML
  protected void showHideList(ActionEvent event) {
    mainApp.getLMPC().showHideList();
  }

  @FXML
  protected void createProject(ActionEvent event) {
    mainApp.showProjectDialogCreation();
  }

  @FXML
  protected void createSkill(ActionEvent event) {
    mainApp.showSkillCreationDialog();
  }

  /**
   * Fxml import for quit button, closes application on click
   */
  @FXML
  protected void btnQuitClick(ActionEvent event){
    System.exit(0);
  }

  @FXML
  protected void btnClickSave(ActionEvent event)
  {

    /**
     * Save file button in File menu, opens up the file chooser to select where you would
     * like to save.
     */

    // Has no actual save functionality yet.
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Project");
    File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

  }

  @FXML
  protected void btnClickOpen(ActionEvent event) {

    /**
     * Open file button in File menu, opens up the file chooser to select which file you
     * wish to open.
     */

    //No open functionality at the moment.
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Project");
    File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

  }


  public void setMainApp(Main mainApp){
    this.mainApp = mainApp;
  }


}
