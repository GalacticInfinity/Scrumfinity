package seng302.group5.controller.mainAppControllers;

import java.io.File;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.dialogControllers.DependantsDialogController;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.AgileItem;
import seng302.group5.model.util.Loading;
import seng302.group5.model.util.Saving;
import seng302.group5.model.undoredo.UndoRedoHandler;
import seng302.group5.model.util.Settings;

/**
 * Created by Michael on 3/15/2015. Controller for MenuBar
 *
 * Edited by Craig on 17/03/2015. added save button functionality.
 */
public class MenuBarController {

  @FXML private Menu editMenu;

  @FXML private MenuItem directAddMenuItem;
  @FXML private MenuItem openMenuItem;
  @FXML private MenuItem saveMenuItem;
  @FXML private MenuItem undoMenuItem;
  @FXML private MenuItem redoMenuItem;
  @FXML private MenuItem deleteMenuItem;
  @FXML private MenuItem editMenuItem;
  @FXML private MenuItem dependantsMenuItem;

  @FXML private CheckMenuItem showListMenuItem;
  @FXML private CheckMenuItem showProjectsMenuItem;
  @FXML private CheckMenuItem showPeopleMenuItem;
  @FXML private CheckMenuItem showTeamsMenuItem;
  @FXML private CheckMenuItem showSkillsMenuItem;
  @FXML private CheckMenuItem showReleasesMenuItem;
  @FXML private CheckMenuItem showStoriesMenuItem;
  @FXML private CheckMenuItem showBacklogsMenuItem;
  @FXML private CheckMenuItem showSprintsMenuItem;

  private Main mainApp;

  /**
   * Initialise the fxml, basic setup functions called.
   */
  @FXML
  private void initialize() {
    // Set up the keyboard shortcuts
    directAddMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
    openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
    saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
    undoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
    redoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
    deleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
    editMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

    // Alt + digit to change list view
    showProjectsMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.ALT_DOWN));
    showTeamsMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.ALT_DOWN));
    showPeopleMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.ALT_DOWN));
    showSkillsMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.ALT_DOWN));
    showReleasesMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.ALT_DOWN));
    showStoriesMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.ALT_DOWN));
    showBacklogsMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT7, KeyCombination.ALT_DOWN));
    showSprintsMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT8, KeyCombination.ALT_DOWN));

    // Ticks the project menu item
    showProjectsMenuItem.setSelected(true);
    showListMenuItem.setSelected(true);

    // Disable the undo/redo buttons on launch
    undoMenuItem.setDisable(true);
    redoMenuItem.setDisable(true);
  }

  /**
   * Tells ListMainPaneController to hide/show the item list based on current state (stored in
   * ListMainPaneController)
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void showHideList(ActionEvent event) {
    mainApp.getLMPC().showHideList(showListMenuItem);
  }

  /**
   * Creates the dialog for when new project is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createProject(ActionEvent event) {
    mainApp.showProjectDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates the dialog for when new Release is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createRelease(ActionEvent event) {
    mainApp.showReleaseDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates the dialog for when the user tries to edit a item in the list. has alert popups when
   * unable to.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void editItem(ActionEvent event) {
    String listType = Settings.currentListType;
    switch (listType) {
      case "Projects":
        mainApp.showProjectDialog(CreateOrEdit.EDIT);
        break;
      case "People":
        mainApp.showPersonDialog(CreateOrEdit.EDIT);
        break;
      case "Skills":
        mainApp.showSkillDialog(CreateOrEdit.EDIT);
        break;
      case "Teams":
        mainApp.showTeamDialog(CreateOrEdit.EDIT);
        break;
      case "Releases":
        mainApp.showReleaseDialog(CreateOrEdit.EDIT);
        break;
      case "Stories":
        mainApp.showStoryDialog(CreateOrEdit.EDIT);
        break;
      case "Backlogs":
        mainApp.showBacklogDialog(CreateOrEdit.EDIT);
        break;
      case "Sprints":
        mainApp.showSprintDialog(CreateOrEdit.EDIT);
        break;
    }
  }

  /**
   * Creates the dialog for when new skill is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createSkill(ActionEvent event) {
    mainApp.showSkillDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates the dialog for when new Person is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createPerson(ActionEvent event) {
    mainApp.showPersonDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates the dialog for when new team is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createTeam(ActionEvent event) {
    mainApp.showTeamDialog(CreateOrEdit.CREATE);
  }

  /**
   * Create a dialog for creation depending on what is currently being displayed in the list
   *
   * @param event Event generated by event listener
   */
  @FXML
  protected void btnClickDirectAdd(ActionEvent event) {
    String listType = Settings.currentListType;
    switch (listType) {
      case "Projects":
        mainApp.showProjectDialog(CreateOrEdit.CREATE);
        break;
      case "People":
        mainApp.showPersonDialog(CreateOrEdit.CREATE);
        break;
      case "Skills":
        mainApp.showSkillDialog(CreateOrEdit.CREATE);
        break;
      case "Teams":
        mainApp.showTeamDialog(CreateOrEdit.CREATE);
        break;
      case "Releases":
        mainApp.showReleaseDialog(CreateOrEdit.CREATE);
        break;
      case "Stories":
        mainApp.showStoryDialog(CreateOrEdit.CREATE);
        break;
      case "Backlogs":
        mainApp.showBacklogDialog(CreateOrEdit.CREATE);
        break;
      case "Sprints":
        mainApp.showSprintDialog(CreateOrEdit.CREATE);
        break;
    }
  }

  /**
   * Creates a dialog for when a new Story is clicked.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createStory(ActionEvent event) {
    mainApp.showStoryDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates a dialog for when a new backlog is clicked.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createBacklog(ActionEvent event) {
    mainApp.showBacklogDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates a dialog for when a new sprint is clicked.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createSprint(ActionEvent event) {
    mainApp.showSprintDialog(CreateOrEdit.CREATE);
  }

  /**
   * Fxml import for quit button, closes application on click
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnQuitClick(ActionEvent event) {
    mainApp.exitScrumfinity();
  }


  /**
   * Save button that attempts to save in the current open file, if no file is open, it opens the
   * file chooser dialog to allow the user to select where they wish to save the file.
   * Checks if Settings.defaultFilepath is set, if so opens in that directory, if not, uses system
   * default.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnClickSave(ActionEvent event) {
    if (Settings.currentFile != null) {
      try {
        Saving save = new Saving(mainApp);
        save.saveData(Settings.currentFile);
        mainApp.refreshLastSaved();
        mainApp.setLastSaved(); //for revert
      } catch (Exception a) {
//        System.out.println("Current File does not exist");
      }
    } else {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Project");
      FileChooser.ExtensionFilter
          filter =
          new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
      fileChooser.getExtensionFilters().add(filter);
      if (Settings.defaultFilepath != null) {
        fileChooser.setInitialDirectory(Settings.defaultFilepath);
      }
      try {
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
          Settings.currentFile = file;
          Saving save = new Saving(mainApp);
          save.saveData(file);

          // Refresh the last saved action
          mainApp.refreshLastSaved();
          mainApp.setLastSaved(); //for revert
        }
      } catch (Exception e) {
//        System.out.println("No filename specified");
      }
    }
  }


  /**
   * Save as file button in File menu, opens up the file chooser to select where you would like to
   * save. If Settings.currentFile is set, opens in the file's folder. If not, checks if
   * Settings.defaultFilepath is set, if so opens in that directory, if not, uses system
   * default.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnClickSaveAs(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Project");
    FileChooser.ExtensionFilter
        filter =
        new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
    fileChooser.getExtensionFilters().add(filter);
    if (Settings.currentFile != null) {
      fileChooser.setInitialDirectory(Settings.currentFile.getParentFile());
    } else if (Settings.defaultFilepath != null){
      fileChooser.setInitialDirectory(Settings.defaultFilepath);
    }
    try {
      File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

      if (file != null) {
        Settings.currentFile = file;
        Saving save = new Saving(mainApp);
        save.saveData(file);

        // Refresh the last saved action
        mainApp.refreshLastSaved();
        mainApp.setLastSaved(); //for revert
      }
    } catch (Exception e) {
//      System.out.println("No filename specified");
    }
  }


  /**
   * Handles the event of the REVERT button being pushed.
   * @param event Event generated by event listener
   */
  @FXML
  protected void btnRevert(ActionEvent event) {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Are you sure?!");
    alert.setHeaderText(null);

    String message = "This cannot be undone! Are you sure you want to do this?\nWe suggest doing a \"Save as..\" first!";
    alert.setResizable(true);
    alert.getDialogPane().setPrefHeight(150);
    alert.setContentText(message);

    ButtonType buttonTypeSaveAs = new ButtonType("Save as then revert");
    ButtonType buttonTypeConfirm = new ButtonType("Revert", ButtonBar.ButtonData.OK_DONE);
    ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    alert.getButtonTypes().setAll(buttonTypeSaveAs, buttonTypeConfirm, buttonTypeCancel);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == buttonTypeSaveAs) {
      // ... user chose "Save as then revert"
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Project");
      if (Settings.defaultFilepath != null) {
        fileChooser.setInitialDirectory(Settings.defaultFilepath);
      }
      try {
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
          Saving save = new Saving(mainApp);
          save.saveData(file);
          mainApp.revert();
        } else {
          Alert alert2 = new Alert(Alert.AlertType.ERROR);
          alert2.setTitle("No file selected");
          alert2.setHeaderText(null);
          alert2.setContentText("No file was selected for saving. Revert cancelled.");
          alert2.showAndWait();
        }
      } catch (Exception e) {
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("No file selected");
        alert2.setHeaderText(null);
        alert2.setContentText("No file was selected for saving. Revert cancelled.");
        alert2.showAndWait();
      }
    } else if (result.get() == buttonTypeConfirm) {
      // ... user chose "Revert"
      mainApp.revert();
    }
  }

  /**
   * Open file button in File menu, opens up the file chooser to select which file you wish to
   * open. If Settings.currentFile is set, opens in the file's folder. If not, checks if
   * Settings.defaultFilepath is set, if so opens in that directory, if not, uses system
   * default.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnClickOpen(ActionEvent event) {
    Optional<ButtonType> result = null;
    ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
    ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    if (!mainApp.checkSaved()) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Unsaved changes");
      alert.setHeaderText(null);
      alert.setContentText("There are unsaved changes. Do you wish to save "
                           + "before loading a new file?");

      alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

      result = alert.showAndWait();
    }

    if (result == null || result.get() == buttonTypeYes || result.get() == buttonTypeNo) {

      if (result != null && result.get() == buttonTypeYes) {
        // save first before continuing
        btnClickSave(null);
      }

      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Project");
      FileChooser.ExtensionFilter
          filter =
          new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
      fileChooser.getExtensionFilters().add(filter);
      if (Settings.currentFile != null) {
        fileChooser.setInitialDirectory(Settings.currentFile.getParentFile());
      } else if (Settings.defaultFilepath != null) {
        fileChooser.setInitialDirectory(Settings.defaultFilepath);
      }
      try {
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
          Settings.currentFile = file;
          mainApp.getLMPC().getScrumBoardController().hardReset();
          mainApp.resetAll();
          Loading load = new Loading(mainApp);
          load.loadFile(file);

          mainApp.getLMPC().refreshList(null);
          mainApp.getLMPC().getScrumBoardController().refreshComboBoxes();
          mainApp.getLMPC().getBurndownController().refreshComboBoxes();
          if (!(Settings.organizationName.equals(""))) {
            mainApp.setMainTitle("Scrumfinity - " + Settings.organizationName);
          } else {
            mainApp.setMainTitle("Scrumfinity");
          }
          mainApp.toggleName();
          showListMenuItem.setSelected(true);
          deselectList(Settings.currentListType);
          mainApp.setLastSaved(); //for revert
          mainApp.checkUndoRedoItems();
        }
      } catch (Exception e) {
//      System.out.println("No file selected");
      }
    }
  }

  /**
   * Handles menu item Revert, initiates report creation process.
   */
  @FXML
  protected void btnClickReport() {
    mainApp.showReportDialog(CreateOrEdit.CREATE);
/*    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Report");
    File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

    if (file != null) {
      ReportWriter report = new ReportWriter();
      report.writeReport(mainApp, file);
    }*/
  }

  /**
   * Handles menu item Org. Name, lets user assign organization name through dialog.
   */
  @FXML
  protected void btnClickOrganization() {
    // Basic setup
    Dialog<String> orgDialog = new Dialog<>();
    orgDialog.setTitle("Organisation Name");
    orgDialog.setHeaderText("Enter organisation name");
    orgDialog.setGraphic(null);

    ButtonType acceptButtonType = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
    orgDialog.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);

    TextField orgField = new TextField();
    if (!Settings.organizationName.isEmpty()) {
      orgField.setText(Settings.organizationName);
    } else {
      orgField.setPromptText("Default");
    }

    Node acceptButton = orgDialog.getDialogPane().lookupButton(acceptButtonType);
    acceptButton.setDisable(true);

    // Deactivates button if no changes.
    orgField.textProperty().addListener((observable, oldValue, newValue) -> {
      acceptButton.setDisable(newValue.equals(Settings.organizationName) || newValue.equals("__undefined__"));
    });

    orgDialog.getDialogPane().setContent(orgField);
    Platform.runLater(orgField::requestFocus);

    orgDialog.setResultConverter(dialogButton -> {
      if (dialogButton == acceptButtonType) {
        return orgField.getText();
      }
      return null;
    });

    Optional<String> result = orgDialog.showAndWait();

    result.ifPresent(newField -> {
      Settings.organizationName = result.get().trim();
      if (!Settings.organizationName.equals("")) {
        mainApp.setMainTitle("Scrumfinity - " + Settings.organizationName);
      } else {
        mainApp.setMainTitle("Scrumfinity");
      }
      mainApp.toggleName();
    });
  }

  /**
   * Populates left side with projects.
   */
  @FXML
  protected void btnShowProjects() {
    Settings.currentListType = "Projects";
    deselectList("Projects");
    showProjectsMenuItem.setSelected(true);
    mainApp.getLMPC().refreshList(null);
  }

  /**
   * Populates left side with people.
   */
  @FXML
  protected void btnShowPeople() {
    Settings.currentListType = "People";
    deselectList("People");
    showPeopleMenuItem.setSelected(true);
    mainApp.getLMPC().refreshList(null);
  }

  /**
   * Populates left side with skills.
   */
  @FXML
  protected void btnShowSkills() {
    Settings.currentListType = "Skills";
    deselectList("Skills");
    showSkillsMenuItem.setSelected(true);
    mainApp.getLMPC().refreshList(null);
  }

  /**
   * Populates left side with teams.
   */
  @FXML
  protected void btnShowTeams() {
    Settings.currentListType = "Teams";
    deselectList("Teams");
    mainApp.getLMPC().refreshList(null);
  }

  /**
   * Populates left side with Releases.
   */
  @FXML
  protected void btnShowReleases() {
    Settings.currentListType = "Releases";
    deselectList("Releases");
    mainApp.getLMPC().refreshList(null);
  }

  /**
   * Populates left side with Stories
   */
  @FXML
  protected void btnShowStories() {
    Settings.currentListType = "Stories";
    deselectList("Stories");
    mainApp.getLMPC().refreshList(null);
  }

  /**
   * Populates left side with Backlogs.
   */
  @FXML
  protected void btnShowBacklogs() {
    Settings.currentListType = "Backlogs";
    deselectList("Backlogs");
    mainApp.getLMPC().refreshList(null);
  }

  /**
   * populates left side with Sprints.
   */
  @FXML
  protected void btnShowSprints() {
    Settings.currentListType = "Sprints";
    deselectList("Sprints");
    mainApp.getLMPC().refreshList(null);
  }

  /**
   * Launch the dialog for editing dependencies of stories in tha main application
   *
   * @param event Event generated by event listener
   */
  @FXML
  protected void launchDependantsDialog(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/DependantsDialog.fxml"));
      Pane dependantsLayout = loader.load();

      DependantsDialogController controller = loader.getController();

      Scene dependantsDialogScene = new Scene(dependantsLayout);
      Stage dependantsDialogStage = new Stage();

      controller.setupController(mainApp, dependantsDialogStage, null);

      dependantsDialogStage.initModality(Modality.APPLICATION_MODAL);
      dependantsDialogStage.initOwner(mainApp.getPrimaryStage());
      dependantsDialogStage.setScene(dependantsDialogScene);
      dependantsDialogStage.setTitle("Story Dependencies");
      dependantsDialogStage.setResizable(false);
      dependantsDialogStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Using a list type value, display that type on the displayed list.
   *
   * @param listType Type of list to display.
   */
  public void showListType(String listType) {
    Settings.currentListType = listType;
    deselectList(listType);
    mainApp.getLMPC().refreshList(null);
  }

  /**
   * Used to deselect all lists except for the list you want to be showed (selectedList) Show/hide
   * lists uses this to show no lists.
   *
   * @param selectedList The currently displayed list
   */
  protected void deselectList(String selectedList) {
    showProjectsMenuItem.setSelected(false);
    showPeopleMenuItem.setSelected(false);
    showTeamsMenuItem.setSelected(false);
    showSkillsMenuItem.setSelected(false);
    showReleasesMenuItem.setSelected(false);
    showStoriesMenuItem.setSelected(false);
    showBacklogsMenuItem.setSelected(false);
    showSprintsMenuItem.setSelected(false);
    if (!selectedList.equals("")) {
      showListMenuItem.setSelected(true);
      switch (selectedList) {
        case "Projects":
          showProjectsMenuItem.setSelected(true);
          break;
        case "People":
          showPeopleMenuItem.setSelected(true);
          break;
        case "Skills":
          showSkillsMenuItem.setSelected(true);
          break;
        case "Teams":
          showTeamsMenuItem.setSelected(true);
          break;
        case "Releases":
          showReleasesMenuItem.setSelected(true);
          break;
        case "Stories":
          showStoriesMenuItem.setSelected(true);
          break;
        case "Backlogs":
          showBacklogsMenuItem.setSelected(true);
          break;
        case "Sprints":
          showSprintsMenuItem.setSelected(true);
          break;
      }
    }
  }

  /**
   * Deletes the currently selected item in the list. If it is able to.
   */
  @FXML
  protected void btnDelete() {
    AgileItem selectedItem = mainApp.getLMPC().getSelected();
    if (selectedItem == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Cannot delete");
      alert.setHeaderText(null);
      alert.setContentText("Deleting failed - No item selected");
      alert.showAndWait();
    } else if (mainApp.getNonRemovable().contains(selectedItem)) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Cannot delete");
      alert.setHeaderText(null);
      alert.setContentText(String.format("The item %s cannot be deleted", selectedItem));
      alert.showAndWait();
    } else {
      mainApp.delete(selectedItem);
      mainApp.refreshList(null);
    }
  }

  /**
   * Gets rid of the last change.
   */
  @FXML
  protected void btnUndoClick() {
    mainApp.undo();
  }

  /**
   * Does again the last action that was undone.
   */
  @FXML
  protected void btnRedoClick() {
    mainApp.redo();
  }

  /**
   * sets the main app to the param
   *
   * @param mainApp The main application object
   */
  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }

  /**
   * Refresh the undo and redo menu items based on the state of the undo/redo handler
   *
   * @param undoRedoHandler the undo/redo handler to check
   */
  public void checkUndoRedoMenuItems(UndoRedoHandler undoRedoHandler) {
    // undo menu item
    if (undoRedoHandler.peekUndoStack() == null) {
      undoMenuItem.setDisable(true);
      undoMenuItem.setText("Undo");
    } else {
      undoMenuItem.setDisable(false);
      undoMenuItem.setText("Undo " + undoRedoHandler.peekUndoStack().getActionString());
    }

    // redo menu item
    if (undoRedoHandler.peekRedoStack() == null) {
      redoMenuItem.setDisable(true);
      redoMenuItem.setText("Redo");
    } else {
      redoMenuItem.setDisable(false);
      redoMenuItem.setText("Redo " + undoRedoHandler.peekRedoStack().getActionString());
    }

    // workaround to refresh edit menu size
    editMenu.setVisible(false);
    editMenu.setVisible(true);
  }
}
