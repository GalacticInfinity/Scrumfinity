package seng302.group5.controller.dialogControllers;

import java.io.File;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Backlog;
import seng302.group5.model.Sprint;
import seng302.group5.model.Status;
import seng302.group5.model.Story;
import seng302.group5.model.Task;
import seng302.group5.model.undoredo.UndoRedo;


/**
 * The controller class for the scrum board dialog. Tasks can be viewed from this dialog by
 * selecting the backlog->sprint->story accordingly. Also tasks are sorted by their status in four
 * different lists: not started, in progress, verify and done.
 *
 * Version 1: status are to be changed by double click editing.
 *
 * @author liangma
 */
public class ScrumBoardController {
  @FXML private ComboBox<Backlog> backlogCombo;
  @FXML private ComboBox<Sprint> sprintCombo;
  @FXML private ComboBox<Story> storyCombo;
  @FXML private ListView<Task> notStartedList;
  @FXML private ListView<Task> inProgressList;
  @FXML private ListView<Task> verifyList;
  @FXML private ListView<Task> doneList;

  private Main mainApp;
  private Stage stage;

  private ObservableList<Sprint> availableSprints;
  private ObservableList<Story> availableStories;
  private ObservableList<Task> notStartedTasks;
  private ObservableList<Task> inProgressTasks;
  private ObservableList<Task> verifyTasks;
  private ObservableList<Task> doneTasks;

  private Story nonStory;

  /**
   * This function sets up the scrum board dialog controller.
   * @param mainApp     The main application object
   * @param stage       The stage the application is in.
   */
  public void setupController(Main mainApp, Stage stage) {
    this.mainApp = mainApp;
    this.stage = stage;

    initialiseLists();
    sprintCombo.setDisable(true);
    storyCombo.setDisable(true);

    setupListView();
  }

  /**
   * Initialises the models lists and populates these with values from the main application,
   * such as available backlogs, sprints and stories. These values
   * are then populated into their respective GUI elements. The backlog combo box has a listener
   * to update other GUI elements which depend on the backlog.
   */
  private void initialiseLists() {
    availableSprints = FXCollections.observableArrayList();
    availableStories = FXCollections.observableArrayList();
    notStartedTasks = FXCollections.observableArrayList();
    inProgressTasks = FXCollections.observableArrayList();
    verifyTasks = FXCollections.observableArrayList();
    doneTasks = FXCollections.observableArrayList();
    Story nonStory = new Story();
    nonStory.setLabel("Non-story Tasks");
    availableStories.add(nonStory);

    backlogCombo.setVisibleRowCount(5);
    sprintCombo.setVisibleRowCount(5);
    storyCombo.setVisibleRowCount(5);
    backlogCombo.setItems(mainApp.getBacklogs());

    backlogCombo.getSelectionModel().selectedItemProperty().addListener(
      (observable, oldBacklog, newBacklog) -> {
        if (newBacklog != null) {
          sprintCombo.setDisable(false);

          // get backlog's sprints
          availableSprints.setAll(mainApp.getSprints().stream()
                                      .filter(
                                          sprint -> sprint.getSprintBacklog().equals(newBacklog))
                                      .collect(Collectors.toList()));
          sprintCombo.setItems(availableSprints);
        }
      }
    );

    sprintCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldSprint, newSprint) -> {
          if (newSprint != null) {
            storyCombo.setDisable(false);
            availableStories.setAll(newSprint.getSprintStories());
            availableStories.add(0, nonStory);
            storyCombo.setItems(availableStories);
            storyCombo.setValue(nonStory);
            refreshLists();
          }
        }
    );

    storyCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldStory, newStory) -> {
          if (oldStory != null && newStory != null) {
            refreshLists();
          }
        }
    );
  }

  /**
   * Sets the custom behaviour for all four tasks ListView.
   */
  private void setupListView() {
    //Sets the cell being populated with custom settings defined in the ListViewCell class.
    this.notStartedList.setCellFactory(listView -> new ListCell(notStartedList));
    this.inProgressList.setCellFactory(listView -> new ListCell(inProgressList));
    this.verifyList.setCellFactory(listView -> new ListCell(verifyList));
    this.doneList.setCellFactory(listView -> new ListCell(doneList));
  }

  /**
   * Allows us to override a ListViewCell - a single cell in the ListView.
   */
  private class ListCell extends TextFieldListCell<Task> {
    private String state;

    public ListCell(ListView<Task> taskListView) {
      super();

      // double click for editing
      this.setOnMouseClicked(click -> {
        if (click.getClickCount() == 2 &&
            click.getButton() == MouseButton.PRIMARY &&
            !isEmpty()) {
          Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
          mainApp.showTaskDialog(storyCombo.getValue(), selectedTask,
                                 sprintCombo.getValue().getSprintTeam(),
                                 CreateOrEdit.EDIT, stage);
          refreshLists();
        }
      });

      taskListView.setOnDragDetected(event -> {
        if (taskListView.getSelectionModel().getSelectedItem() != null) {
          state = "";

          Dragboard dragBoard = taskListView.startDragAndDrop(TransferMode.MOVE);

          ClipboardContent content = new ClipboardContent();
          File dragFileImage = new File("src/main/resources/DragCursor.png");
          Image dragImage = new Image(dragFileImage.toURI().toString());
          dragBoard.setDragView(dragImage);

          content.putString(taskListView.getSelectionModel().getSelectedItem().getLabel());

          dragBoard.setContent(content);

          notStartedList.setOnDragOver(hover -> state = "notstarted");
          inProgressList.setOnDragOver(hover -> state = "progress");
          verifyList.setOnDragOver(hover -> state = "verify");
          doneList.setOnDragOver(hover -> state = "done");
        }
      });
      //TODO Add undo Redo for drag and dropping tasks
      taskListView.setOnDragDone(
                                  event -> {
                                    if (taskListView.getSelectionModel().getSelectedItem() != null) {
                                      if (state.equals("notstarted")) {
                                        taskListView.getSelectionModel()
                                            .getSelectedItem()
                                            .setStatus(Status.NOT_STARTED);

                } else if (Objects.equals(state, "progress")) {
                  taskListView.getSelectionModel()
                      .getSelectedItem()
                      .setStatus(Status.IN_PROGRESS);
                } else if (Objects.equals(state, "verify")) {
                  taskListView.getSelectionModel()
                      .getSelectedItem()
                      .setStatus(Status.VERIFY);
                } else if (Objects.equals(state, "done")) {
                                        taskListView.getSelectionModel()
                                            .getSelectedItem()
                                            .setStatus(Status.DONE);
                                      }
                refreshLists();
              }
            });
      refreshLists();
          }
        }




  /**
   * Refreshes the four list views when any of the tasks within the story is updated.
   */
  public void refreshLists() {
    nonStory = new Story();
    nonStory.setLabel("Non-story Tasks");

    notStartedTasks.clear();
    inProgressTasks.clear();
    verifyTasks.clear();
    doneTasks.clear();

    if (backlogCombo.getSelectionModel().getSelectedItem() != null &&
        sprintCombo.getSelectionModel().getSelectedItem() != null) {

      if (storyCombo.getValue().getLabel().equals(nonStory.getLabel())) {
        sprintCombo.getValue().getTasks().forEach(this::sortTaskToLists);
      } else if (!storyCombo.getValue().getTasks().isEmpty()) {
        storyCombo.getValue().getTasks().forEach(this::sortTaskToLists);
      } else {
        Task newTask = new Task();
        notStartedTasks.add(newTask);
        inProgressTasks.add(newTask);
        verifyTasks.add(newTask);
        doneTasks.add(newTask);
        notStartedTasks.clear();
        inProgressTasks.clear();
        verifyTasks.clear();
        doneTasks.clear();
      }
    }
    notStartedList.setItems(notStartedTasks);
    inProgressList.setItems(inProgressTasks);
    verifyList.setItems(verifyTasks);
    doneList.setItems(doneTasks);

  }

  /**
   * Sorting tasks to the correct list according to their current status.
   * @param task the task that's within the story or sprint
   */
  private void sortTaskToLists(Task task) {
    if (task.getStatus().equals(Status.NOT_STARTED)) {
      notStartedTasks.add(task);
    } else if (task.getStatus().equals(Status.IN_PROGRESS)) {
      inProgressTasks.add(task);
    } else if (task.getStatus().equals(Status.VERIFY)) {
      verifyTasks.add(task);
    } else if (task.getStatus().equals(Status.DONE)) {
      doneTasks.add(task);
    }
  }

  /**
   * On loading the scrum board needs to be completely reset. This functions does that, clears all
   * selections.
   */
  public void clearSelections() {
    storyCombo.getSelectionModel().clearSelection();
    storyCombo.setDisable(true);
    sprintCombo.getSelectionModel().clearSelection();
    sprintCombo.setDisable(true);
    backlogCombo.getSelectionModel().clearSelection();
    availableSprints.clear();
    availableStories.clear();
    notStartedTasks.clear();
    inProgressTasks.clear();
    verifyTasks.clear();
    doneTasks.clear();
  }


  /**
   * A button which when clicked can add a task to either the selected story, or if the "nonStory"
   * of sprint tasks, can add into there as well. Also adds to undo/redo stack so creationg is undoable
   * @param event Button click
   */
  @FXML
  protected void addNewTask(ActionEvent event) {
    Story story = storyCombo.getSelectionModel().getSelectedItem();
    if (storyCombo.getSelectionModel().getSelectedItem() != null) {
      Sprint sprint = sprintCombo.getSelectionModel().getSelectedItem();
      UndoRedo undoRedoCreate;
      if (story == nonStory) {
        undoRedoCreate = mainApp.showTaskDialog(sprint, null, sprint.getSprintTeam(), CreateOrEdit.CREATE, stage);
      } else {
        undoRedoCreate = mainApp.showTaskDialog(story, null, sprint.getSprintTeam(), CreateOrEdit.CREATE, stage);
      }
      if (undoRedoCreate != null) {
        mainApp.newAction(undoRedoCreate);
      }
    }
  }

}
