package seng302.group5.controller;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.AgileItem;
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
  @FXML private MenuItem showTeamsMenuItem;

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
  protected void editItem(ActionEvent event) {
    String listType = Settings.currentListType;
    switch (listType) {
      case "Project":
        mainApp.showProjectDialog(CreateOrEdit.EDIT);
        break;
      case "People":
        mainApp.showPersonDialog(CreateOrEdit.EDIT);
        break;
      case "Skills":
        mainApp.showSkillCreationDialog(CreateOrEdit.EDIT);
        break;
    }
  }

  @FXML
  protected void createSkill(ActionEvent event) {
    mainApp.showSkillCreationDialog(CreateOrEdit.CREATE);
  }

  @FXML
  protected void createPerson(ActionEvent event) {
    mainApp.showPersonDialog(CreateOrEdit.CREATE);
  }

  @FXML
  protected void createTeam(ActionEvent event) {
    mainApp.showTeamDialog(CreateOrEdit.CREATE);
  }

  @FXML
  protected void editTeam(ActionEvent event) {
    mainApp.showTeamDialog(CreateOrEdit.EDIT);
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
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Project");
    if (Settings.defaultFilepath != null) {
      fileChooser.setInitialDirectory(Settings.defaultFilepath);
    }
    try {
      File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
      Saving.saveDataToFile(file, mainApp);
    } catch (Exception e) {
      System.out.println("No filename specified");
  }
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
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV files (*.xml)", "*.xml");
    fileChooser.getExtensionFilters().add(filter);
    if (Settings.defaultFilepath != null) {
      fileChooser.setInitialDirectory(Settings.defaultFilepath);
    }
    try {
      File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
      Saving.loadDataFromFile(file, mainApp);
      mainApp.getLMPC().refreshList();
    } catch (Exception e) {
      System.out.println("No file selected");
    }
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
  protected void btnShowTeams() {
    Settings.currentListType = "Team";
    mainApp.getLMPC().refreshList();
  }

  @FXML
  protected void btnDelete(){
    if (mainApp.getLMPC().getSelected() == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Cannot delete");
      alert.setHeaderText(null);
      alert.setContentText("Deleting failed - No item selected");
      alert.showAndWait();
    } else {
      mainApp.delete(mainApp.getLMPC().getSelected());
      mainApp.refreshList();
    }
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
