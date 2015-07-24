package seng302.group5.controller.dialogControllers;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

/**
 * A controller for the sprint dialog.
 * TODO: expand once it works
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

  //todo jdoc
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
      initialiseLists(CreateOrEdit.CREATE, sprint);

      sprintTeamCombo.setDisable(true);
      sprintReleaseCombo.setDisable(true);

    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Sprint");
      btnConfirm.setText("Save");
      btnConfirm.setDisable(true);
      initialiseLists(CreateOrEdit.EDIT, sprint);

      sprintGoalField.setText(sprint.getLabel());
      sprintNameField.setText(sprint.getSprintFullName());
      sprintDescriptionField.setText(sprint.getSprintDescription());
      sprintBacklogCombo.getSelectionModel().select(sprint.getSprintBacklog());
      // todo project label field
      sprintTeamCombo.getSelectionModel().select(sprint.getSprintTeam());
      sprintReleaseCombo.getSelectionModel().select(sprint.getSprintRelease());
      // todo date fields
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

    // todo change listeners
  }

  //todo jdoc
  private void initialiseLists(CreateOrEdit createOrEdit, Sprint sprint) {
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
  }

  /**
   * Adds the selected story from the list of available stories in the selected product backlog
   * to the allocated stories for the sprint.
   *
   * @param event Action event
   */
  @FXML
  protected void btnAddStoryClick(ActionEvent event) {
    Story selectedStory = availableStoriesList.getSelectionModel().getSelectedItem();
    if (selectedStory != null) {
      allocatedStories.add(selectedStory);
      refreshLists();
      allocatedStoriesList.getSelectionModel().select(selectedStory);
    }
  }

  /**
   * Remove the selected story from the list of allocated stories in the sprint and put it back
   * in the list of available stories.
   *
   * @param event Action event
   */
  @FXML
  protected void btnRemoveStoryClick(ActionEvent event) {
    Story selectedStory = allocatedStoriesList.getSelectionModel().getSelectedItem();
    if (selectedStory != null) {
      allocatedStories.remove(selectedStory);
      refreshLists();
      availableStoriesList.getSelectionModel().select(selectedStory);
    }
  }

  /**
   * Refresh the lists such that they maintain the original priority order specified in the
   * backlog. Call whenever the story allocation changes.
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

  // todo jdoc
  @FXML
  protected void btnConfirmClick(ActionEvent event) {

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
}
