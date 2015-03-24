package seng302.group5.controller;

import java.util.List;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Saving;
import seng302.group5.model.util.Settings;

/**
 * Created by Michael on 3/15/2015.
 * Controller for MenuBar
 *
 * Edited by Craig on 17/03/2015.
 * added save button functionality.
 */
public class MenuBarController {

  @FXML private MenuItem showListMenuItem;
  @FXML private MenuItem showProjectsMenuItem;
  @FXML private MenuItem showPeopleMenuItem;

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
    mainApp.showProjectDialog(CreateOrEdit.CREATE);
  }

  @FXML
  protected void editProject(ActionEvent event) {
    mainApp.showProjectDialog(CreateOrEdit.EDIT);
  }

  @FXML
  protected void createSkill(ActionEvent event) {
    mainApp.showSkillCreationDialog(CreateOrEdit.CREATE);
  }

  @FXML
  protected void editSkill(ActionEvent event) {
    mainApp.showSkillCreationDialog(CreateOrEdit.EDIT);
  }

  @FXML
  protected void createPerson(ActionEvent event) {
    mainApp.showPersonDialog(CreateOrEdit.CREATE);
  }

  /**
   * Fxml import for quit button, closes application on click
   */
  @FXML
  protected void btnQuitClick(ActionEvent event){
    System.exit(0);
  }

  @FXML
  protected void btnClickSave(ActionEvent event) {
    /**
     * Save file button in File menu, opens up the file chooser to select where you would
     * like to save.
     */
    // Has no actual save functionality yet.
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Project");
    File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

    Saving.saveDataToFile(file, mainApp);


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
    if (Settings.defaultFilepath != null) {
      fileChooser.setInitialDirectory(Settings.defaultFilepath);
    }
    File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
    Saving.loadDataFromFile(file, mainApp);
    mainApp.getLMPC().refreshList();
  }


  @FXML
  protected void btnShowProjects() {
    Settings.currentListType = "Project";
    mainApp.getLMPC().refreshList();
  }

  @FXML
  protected void btnShowPeople() {
    Settings.currentListType = "People";
    mainApp.getLMPC().refreshList();
  }

  @FXML
  protected void btnShowSkills() {
    Settings.currentListType = "Skills";
    mainApp.getLMPC().refreshList();
  }

  @FXML
  protected void btnUndoClick() {
    mainApp.undo();
  }

  @FXML
  protected void btnRedoClick() {
    mainApp.redo();
  }

  public void setMainApp(Main mainApp){
    this.mainApp = mainApp;
  }


}
