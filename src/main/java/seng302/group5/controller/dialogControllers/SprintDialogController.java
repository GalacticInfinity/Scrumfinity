package seng302.group5.controller.dialogControllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Backlog;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Sprint;
import seng302.group5.model.Story;
import seng302.group5.model.Team;
import seng302.group5.model.util.Settings;

/**
 * A controller for the sprint dialog which allows the creating or editing of sprints.
 * Note that all fields except the full name and description fields and the sprint can have no
 * stories assigned.
 *
 * Created by Michael Roman and Su-Shing Chen on 24/7/2015.
 */
public class SprintDialogController {

  @FXML private TextField sprintGoalField;
  @FXML private TextField sprintNameField;
  @FXML private TextArea sprintDescriptionField;
  @FXML private ComboBox<Backlog> sprintBacklogCombo;
  @FXML private Label sprintProjectLabel;
  @FXML private ComboBox<Team> sprintTeamCombo;
  @FXML private ComboBox<Release> sprintReleaseCombo;
  @FXML private DatePicker sprintStartDate;
  @FXML private DatePicker sprintEndDate;
  @FXML private ListView<Story> availableStoriesList;
  @FXML private ListView<Story> allocatedStoriesList;
  @FXML private Button btnAddStory;
  @FXML private Button btnRemoveStory;
  @FXML private HBox btnContainer;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Main mainApp;
  private Stage thisStage;

  private CreateOrEdit createOrEdit;

  private Sprint sprint;
  private Sprint lastSprint;
  private Project project;

  private ObservableList<Story> availableStories;
  private ObservableList<Story> allocatedStoriesPrioritised;
  private ObservableList<Backlog> backlogs;
  private ObservableList<Team> teams;
  private ObservableList<Release> releases;

  private Map<Backlog, Project> projectMap;
  private Set<Story> allocatedStories;   // use to maintain priority order

  /**
   * Sets up the controller on start up.
   * If editing it fills the fields with the values in that sprint object
   * Otherwise leaves fields empty.
   * Adds listeners to all fields to enable checking of changes when editing so that the save
   * button can be greyed out.
   *
   * @param mainApp The main class of the program. For checking the list of all existing sprints.
   * @param thisStage This is the window that will be displayed.
   * @param createOrEdit This is an ENUM object to determine if creating or editing.
   * @param sprint The object that will edited or created (made into a valid sprint).
   */
  public void setupController(Main mainApp, Stage thisStage, CreateOrEdit createOrEdit,
                              Sprint sprint) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnConfirm);
      btnContainer.getChildren().add(btnConfirm);
    }

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Sprint");
      btnConfirm.setText("Create");
      initialiseLists();

      sprintTeamCombo.setDisable(true);
      sprintReleaseCombo.setDisable(true);

    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Sprint");
      btnConfirm.setText("Save");
      btnConfirm.setDisable(true);
      initialiseLists();

      sprintGoalField.setText(sprint.getLabel());
      sprintNameField.setText(sprint.getSprintFullName());
      sprintDescriptionField.setText(sprint.getSprintDescription());
      sprintBacklogCombo.getSelectionModel().select(sprint.getSprintBacklog()); // Updates Project
      sprintTeamCombo.getSelectionModel().select(sprint.getSprintTeam());
      sprintReleaseCombo.getSelectionModel().select(sprint.getSprintRelease());
      sprintStartDate.setValue(sprint.getSprintStart());
      sprintEndDate.setValue(sprint.getSprintEnd());
      allocatedStories.addAll(sprint.getSprintStories());
      refreshLists();
    }
    this.createOrEdit = createOrEdit;

    if (sprint != null) {
      this.sprint = sprint;
      this.lastSprint = new Sprint(sprint);
    } else {
      this.sprint = null;
      this.lastSprint = null;
    }

    btnConfirm.setDefaultButton(true);
    thisStage.setResizable(false);

    sprintGoalField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    sprintNameField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });
    sprintDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });
    sprintBacklogCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });
    sprintTeamCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });
    sprintReleaseCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });
    sprintStartDate.valueProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });
    sprintEndDate.valueProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });
  }

  /**
   * Check if any of the fields has been changed. If nothing changed,
   * then confirm button is disabled.
   */
  private void checkButtonDisabled() {
    if (sprintGoalField.getText().equals(sprint.getLabel()) &&
        sprintNameField.getText().equals(sprint.getSprintFullName()) &&
        sprintDescriptionField.getText().equals(sprint.getSprintDescription()) &&
        sprintBacklogCombo.getValue().equals(sprint.getSprintBacklog()) &&
        (sprintTeamCombo.getValue() == null ||
         sprintTeamCombo.getValue().equals(sprint.getSprintTeam())) &&
        (sprintReleaseCombo.getValue() == null ||
         sprintReleaseCombo.getValue().equals(sprint.getSprintRelease())) &&
        sprintStartDate.getValue().equals(sprint.getSprintStart()) &&
        sprintEndDate.getValue().equals(sprint.getSprintEnd()) &&
        allocatedStoriesPrioritised.equals(sprint.getSprintStories())) {
      btnConfirm.setDisable(true);
    } else {
      btnConfirm.setDisable(false);
    }
  }

  /**
   * Initialises the models lists and populates these with values from the main application,
   * such as available stories, allocated stories, backlogs, teams and releases. These values
   * are then populated into their respective GUI elements. The backlog combo box has a listener
   * to update other GUI elements which depend on the backlog.
   * Populates a list of available stories for assigning them to sprint
   */
  private void initialiseLists() {
    availableStories = FXCollections.observableArrayList();
    allocatedStoriesPrioritised = FXCollections.observableArrayList();
    backlogs = FXCollections.observableArrayList();
    teams = FXCollections.observableArrayList();
    releases = FXCollections.observableArrayList();

    // set up map from backlog to project
    projectMap = new IdentityHashMap<>();
    for (Project project : mainApp.getProjects()) {
      Backlog projectBacklog = project.getBacklog();
      if (projectBacklog != null) {
        projectMap.put(projectBacklog, project);
      }
    }

    allocatedStories = new TreeSet<>();

    backlogs.setAll(mainApp.getBacklogs());

    sprintBacklogCombo.setVisibleRowCount(5);
    sprintTeamCombo.setVisibleRowCount(5);
    sprintReleaseCombo.setVisibleRowCount(5);

    sprintBacklogCombo.setItems(backlogs);
    sprintTeamCombo.setItems(teams);
    sprintReleaseCombo.setItems(releases);

    availableStoriesList.setItems(availableStories);
    allocatedStoriesList.setItems(allocatedStoriesPrioritised);

    sprintBacklogCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldBacklog, newBacklog) -> {
          // if the backlog is assigned to a project
          if (projectMap.containsKey(newBacklog)) {
            project = projectMap.get(newBacklog);
            sprintProjectLabel.setText(project.toString());

            sprintTeamCombo.setDisable(false);
            sprintReleaseCombo.setDisable(false);

            // get project's current teams
            teams.setAll(project.getCurrentlyAllocatedTeams());

            // get project's releases
            releases.clear();
            for (Release release : mainApp.getReleases()) {
              if (release.getProjectRelease().equals(project)) {
                releases.add(release);
              }
            }

            // reset interface
            sprintTeamCombo.getSelectionModel().select(null);
            sprintReleaseCombo.getSelectionModel().select(null);
            allocatedStories.clear();
            refreshLists();
          } else {
            // todo dialog for confirming change
            project = null;
            sprintProjectLabel.setText("No Project Found");
            sprintTeamCombo.setValue(null);
            sprintReleaseCombo.setValue(null);
            sprintTeamCombo.setDisable(true);
            sprintReleaseCombo.setDisable(true);
            availableStories.clear();
            allocatedStoriesPrioritised.clear();
            allocatedStories.clear();
          }
        });
    setupListView();
  }

  /**
   * Adds the selected story from the list of available stories in the selected product backlog to
   * the allocated stories for the sprint.
   *
   * @param event Action event
   */
  @FXML
  protected void btnAddSprintClick(ActionEvent event) {
    Story selectedStory = availableStoriesList.getSelectionModel().getSelectedItem();
    if (selectedStory != null) {
      allocatedStories.add(selectedStory);
      refreshLists();
      allocatedStoriesList.getSelectionModel().select(selectedStory);
      if (!availableStories.isEmpty()) {
        availableStoriesList.getSelectionModel().select(0);
      }
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    }
  }

  /**
   * Remove the selected story from the list of allocated stories in the sprint and put it back in
   * the list of available stories.
   *
   * @param event Action event
   */
  @FXML
  protected void btnRemoveSprintClick(ActionEvent event) {
    Story selectedStory = allocatedStoriesList.getSelectionModel().getSelectedItem();
    if (selectedStory != null) {
      allocatedStories.remove(selectedStory);
      refreshLists();
      availableStoriesList.getSelectionModel().select(selectedStory);
      if (!allocatedStoriesPrioritised.isEmpty()) {
        allocatedStoriesList.getSelectionModel().select(0);
      }
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    }
  }

  /**
   * Refresh the lists such that they maintain the original priority order specified in the backlog.
   * Call whenever the story allocation changes.
   */
  private void refreshLists() {
    Backlog selectedBacklog = sprintBacklogCombo.getSelectionModel().getSelectedItem();

    availableStories.clear();
    allocatedStoriesPrioritised.clear();

    for (Story story : selectedBacklog.getStories()) {
      // add story to either available or allocated stories in priority order
      if (allocatedStories.contains(story)) {
        allocatedStoriesPrioritised.add(story);
      } else {
        availableStories.add(story);
      }
    }
  }

  /**
   * Handles the event of clicking the save button in this dialog.
   * Checks for errors with what was input into the fields and displays alerts if errors are found
   * Otherwise creates or updates sprint depending on if your creating or editing.
   * Then creates the undo/redo object.
   *
   * @param event This is the event of the save button being clicked
   */
  @FXML
  protected void btnConfirmClick(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    int noErrors = 0;

    String sprintGoal = "";
    String sprintName = sprintNameField.getText().trim();
    String sprintDescription = sprintDescriptionField.getText().trim();
    Backlog backlog = sprintBacklogCombo.getValue();
    Team team = sprintTeamCombo.getValue();
    Release release = sprintReleaseCombo.getValue();
    LocalDate startDate = null;
    LocalDate endDate = null;

    try {
      sprintGoal = parseSprintGoal(sprintGoalField.getText());
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    if (backlog == null) {
      noErrors++;
      errors.append(String.format("%s\n", "No backlog selected"));
    } else if (project == null) {
      noErrors++;
      errors.append(String.format("%s\n", "Selected backlog is not assigned to a project"));
    }

    if (team == null) {
      noErrors++;
      errors.append(String.format("%s\n", "No team selected"));
    }

    if (release == null) {
      noErrors++;
      errors.append(String.format("%s\n", "No release selected"));
    }

    try {
      startDate = parseStartDate(sprintStartDate.getValue(), release);
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    try {
      endDate = parseEndDate(sprintEndDate.getValue(), sprintStartDate.getValue(), release);
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
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
      noErrors += 1;
      alert.getDialogPane().setPrefHeight(60 + 30 * noErrors);
      alert.setContentText(errors.toString());
      alert.showAndWait();
    } else {
      if (createOrEdit == CreateOrEdit.CREATE) {
        sprint = new Sprint(sprintGoal, sprintName, sprintDescription, backlog, project, team,
                            release, startDate, endDate, allocatedStoriesPrioritised);
        mainApp.addSprint(sprint);
        if (Settings.correctList(sprint)) {
          mainApp.refreshList(sprint);
        }
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        sprint.setSprintGoal(sprintGoal);
        sprint.setSprintFullName(sprintName);
        sprint.setSprintDescription(sprintDescription);
        sprint.setSprintBacklog(backlog);
        sprint.setSprintProject(project);
        sprint.setSprintTeam(team);
        sprint.setSprintRelease(release);
        sprint.setSprintStart(startDate);
        sprint.setSprintEnd(endDate);
        sprint.removeAllStories();
        sprint.addAllStories(allocatedStoriesPrioritised);

        mainApp.refreshList(sprint);
      }
      // todo undo/redo

      thisStage.close();
    }
  }

  /**
   * Discards all changes made from within the dialog and exits the dialog.
   *
   * @param event Action event
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

  /**
   * Checks if sprint goal field contains valid input.
   *
   * @param inputSprintGoal String sprint label or sprint goal.
   * @return sprint label/goal if sprint label/goal is valid.
   * @throws Exception If sprint label/goal is not valid.
   */
  private String parseSprintGoal(String inputSprintGoal) throws Exception {
    inputSprintGoal = inputSprintGoal.trim();

    if (inputSprintGoal.isEmpty()) {
      throw new Exception("Sprint Goal is empty.");
    } else {
      String lastSprintGoal;
      if (lastSprint == null) {
        lastSprintGoal = "";
      } else {
        lastSprintGoal = lastSprint.getLabel();
      }
      for (Sprint sprint : mainApp.getSprints()) {
        String sprintGoal = sprint.getLabel();
        if (sprint.getLabel().equalsIgnoreCase(inputSprintGoal) &&
            !sprintGoal.equalsIgnoreCase(lastSprintGoal)) {
          throw new Exception("Sprint Goal is not unique.");
        }
      }
      return inputSprintGoal;
    }
  }

  /**
   * Verify that the start date is valid in that it is not null and it is before the release date.
   *
   * @param startDate Start date of sprint.
   * @param release Release of sprint used to get date.
   * @return Start date of sprint if it is valid.
   * @throws Exception if sprint start date is not valid.
   */
  private LocalDate parseStartDate(LocalDate startDate, Release release) throws Exception {
    if (startDate == null) {
      throw new Exception("No start date selected");
    } else if (release != null && startDate.isAfter(release.getReleaseDate())) {
      String dateFormat = "dd/MM/yyy";
      String releaseDate = release.getReleaseDate().format(DateTimeFormatter.ofPattern(dateFormat));
      throw new Exception("Start date must be before release date - " + releaseDate);
    }
    return startDate;
  }

  /**
   * Verify that the end date is valid in that it is not null, it is after the start date, and
   * it is before the release date.
   *
   * @param endDate End date of sprint
   * @param startDate Start date of sprint
   * @param release Release of sprint used to get date.
   * @return End date of sprint if it is valid.
   * @throws Exception if sprint end date is not valid.
   */
  private LocalDate parseEndDate(LocalDate endDate, LocalDate startDate, Release release)
      throws Exception {
    if (endDate == null) {
      throw new Exception("No end date selected");
    } else {
      boolean afterReleaseDate = release != null && endDate.isAfter(release.getReleaseDate());
      boolean beforeStartDate = endDate.isBefore(startDate);
      if (afterReleaseDate && beforeStartDate) {
        throw new Exception("End date must be before release date and after start date");
      } else if (afterReleaseDate) {
        String dateFormat = "dd/MM/yyy";
        String releaseDate = release.getReleaseDate().format(DateTimeFormatter.ofPattern(dateFormat));
        throw new Exception("End date must be before release date - " + releaseDate);
      } else if (beforeStartDate) {
        throw new Exception("End date must be after start date");
      }
    }
    return endDate;
  }
  /**
   * Sets the custom behaviour for the stories ListView.
   */
  private void setupListView() {
    //Sets the cell being populated with custom settings defined in the ListViewCell class.
    this.availableStoriesList.setCellFactory(listView -> new AvailableStoriesListViewCell());
    this.allocatedStoriesList.setCellFactory(listView -> new SprintStoriesListViewCell());
  }
  /**
   * Allows us to override a ListViewCell - a single cell in a ListView.
   */
  private class AvailableStoriesListViewCell extends TextFieldListCell<Story> {

    public AvailableStoriesListViewCell() {
      super();

      // double click for editing
      this.setOnMouseClicked(click -> {
        if (click.getClickCount() == 2 &&
            click.getButton() == MouseButton.PRIMARY &&
            !isEmpty()) {
          Story selectedStory = availableStoriesList.getSelectionModel().getSelectedItem();
          mainApp.showStoryDialogWithinSprint(selectedStory, thisStage);
        }
      });
    }
  }

  /**
   * Allows us to override the a ListViewCell - a single cell in a ListView.
   */
  private class SprintStoriesListViewCell extends TextFieldListCell<Story> {

    public SprintStoriesListViewCell() {
      super();

      // double click for editing
      this.setOnMouseClicked(click -> {
        if (click.getClickCount() == 2 &&
            click.getButton() == MouseButton.PRIMARY &&
            !isEmpty()) {
          Story selectedStory = allocatedStoriesList.getSelectionModel().getSelectedItem();
          mainApp.showStoryDialogWithinSprint(selectedStory, thisStage);
        }
      });
    }
  }
}
