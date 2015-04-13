package seng302.group5.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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

  @FXML private MenuItem openMenuItem;
  @FXML private MenuItem saveMenuItem;
  @FXML private MenuItem undoMenuItem;
  @FXML private MenuItem redoMenuItem;
  @FXML private MenuItem deleteMenuItem;

  @FXML private CheckMenuItem showListMenuItem;
  @FXML private CheckMenuItem showProjectsMenuItem;
  @FXML private CheckMenuItem showPeopleMenuItem;
  @FXML private CheckMenuItem showTeamsMenuItem;
  @FXML private CheckMenuItem showSkillsMenuItem;
  @FXML private CheckMenuItem showReleasesMenuItem;

  private Main mainApp;

  /**
   * Initialise the fxml, basic setup functions called.
   */
  @FXML
  private void initialize() {
    // Set up the keyboard shortcuts
    openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
    saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
    undoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
    redoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
    deleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));

    // Ticks the project menu item
    showProjectsMenuItem.setSelected(true);
    showListMenuItem.setSelected(true);
  }

  /**
   * Tells ListMainPaneController to hide/show the item list based on current state
   * (stored in ListMainPaneController)
   */
  @FXML
  protected void showHideList(ActionEvent event) {
    mainApp.getLMPC().showHideList(showListMenuItem);
  }

  @FXML
  protected void createProject(ActionEvent event) {
    mainApp.showProjectDialog(CreateOrEdit.CREATE);
  }

  @FXML
  protected void createRelease(ActionEvent event) {
    mainApp.showReleaseDialog(CreateOrEdit.CREATE);
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
      case "Team":
        mainApp.showTeamDialog(CreateOrEdit.EDIT);
        break;
      case "Release":
        // TODO add this when the dialog done
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
    /** Save button that attempts to save in the current open file, if no file is open, it
     * opens the file chooser dialog to allow the user to select where they wish to save the file.
     */
    if (Settings.currentFile != null) {
      try {
        Saving.saveDataToFile(Settings.currentFile, mainApp);
        mainApp.refreshLastSaved();
      } catch (Exception a) {
        System.out.println("Current File does not exist");
      }
    }
    else {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Project");
      if (Settings.defaultFilepath != null) {
        fileChooser.setInitialDirectory(Settings.defaultFilepath);
      }
      try {
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
          Settings.currentFile = file;
          Saving.saveDataToFile(file, mainApp);

          // Refresh the last saved action
          mainApp.refreshLastSaved();
        }
      } catch (Exception e) {
        System.out.println("No filename specified");
      }
    }
  }
  @FXML
  protected void btnClickSaveAs(ActionEvent event) {
    /**
     * Save as file button in File menu, opens up the file chooser to select where you would
     * like to save.
     */
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Project");
    if (Settings.defaultFilepath != null) {
      fileChooser.setInitialDirectory(Settings.defaultFilepath);
    }
    try {
      File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

      if (file != null) {
        Settings.currentFile = file;
        Saving.saveDataToFile(file, mainApp);

        // Refresh the last saved action
        mainApp.refreshLastSaved();
      }
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
      if (file != null) {
        Settings.currentFile = file;
        Saving.loadDataFromFile(file, mainApp);
        mainApp.getLMPC().refreshList();
        showListMenuItem.setSelected(true);
        deselectList(Settings.currentListType);
      }
    } catch (Exception e) {
      System.out.println("No file selected");
    }
  }


  /**
   * Populates left side with projects
   */
  @FXML
  protected void btnShowProjects() {
    Settings.currentListType = "Project";
    deselectList("Project");
    showProjectsMenuItem.setSelected(true);
    mainApp.getLMPC().refreshList();
  }

  /**
   * Populates left side with people.
   */
  @FXML
  protected void btnShowPeople() {
    Settings.currentListType = "People";
    deselectList("People");
    showPeopleMenuItem.setSelected(true);
    mainApp.getLMPC().refreshList();
  }

  /**
   * Populates left side with skills
   */
  @FXML
  protected void btnShowSkills() {
    Settings.currentListType = "Skills";
    deselectList("Skill");
    showSkillsMenuItem.setSelected(true);
    mainApp.getLMPC().refreshList();
  }

  /**
   * Populates left side with teams
   */
  @FXML
  protected void btnShowTeams() {
    Settings.currentListType = "Team";
    deselectList("Team");
    mainApp.getLMPC().refreshList();
  }

  @FXML
  protected void btnShowReleases() {
    Settings.currentListType = "Release";
    deselectList("Release");
    mainApp.getLMPC().refreshList();
  }


  /**
   *
   * @param selectedList
   */
  protected void deselectList(String selectedList) {
    showProjectsMenuItem.setSelected(false);
    showPeopleMenuItem.setSelected(false);
    showTeamsMenuItem.setSelected(false);
    showSkillsMenuItem.setSelected(false);
    showReleasesMenuItem.setSelected(false);
    if (!(selectedList == "")) {
      switch (selectedList) {
        case "Project":
          showProjectsMenuItem.setSelected(true);
          break;
        case "People":
          showPeopleMenuItem.setSelected(true);
          break;
        case "Skill":
          showSkillsMenuItem.setSelected(true);
          break;
        case "Team":
          showTeamsMenuItem.setSelected(true);
          break;
        case "Release":
          showReleasesMenuItem.setSelected(true);
          break;
      }
    }
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
