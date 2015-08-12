package seng302.group5.controller.dialogControllers;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Person;
import seng302.group5.model.Status;
import seng302.group5.model.Task;
import seng302.group5.model.Team;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;
import seng302.group5.model.util.TimeFormat;

/**
 * A controller to handle the creation or editing tasks. Tasks involve a label, description,
 * estimate (hours), status, and allocated people with their logged effort.
 *
 * @author Su-Shing Chen
 */
public class TaskDialogController {

  @FXML private TextField labelField;
  @FXML private TextArea descriptionField;
  @FXML private TextField estimateField;
  @FXML private ComboBox<String> statusComboBox;
  @FXML private ListView<Person> availablePeopleList;
  @FXML private ListView<Person> allocatedPeopleList;
  @FXML private Button btnAddPerson;
  @FXML private Button btnRemovePerson;
  @FXML private HBox btnContainer;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Collection<Task> taskCollection;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Task task;
  private Task lastTask;
  private ObservableList<Person> allocatedPeople;
  private ObservableList<Person> availablePeople;

  private UndoRedoObject editUndoRedoObject;

  /**
   * Sets up the controller on start up. TODO: expand when it does more
   *
   * @param taskCollection The collection which will contain the task
   * @param team The team of the sprint which will contain the task
   * @param thisStage This is the window that will be displayed
   * @param createOrEdit This is an ENUM object to determine if creating or editing
   * @param task The object that will be edited (null if creating)
   */
  public void setupController(Collection<Task> taskCollection, Team team,
                              Stage thisStage, CreateOrEdit createOrEdit, Task task) {
    this.taskCollection = taskCollection;
    this.thisStage = thisStage;

    if (task != null) {
      this.task = task;
      this.lastTask = new Task(task);
    } else {
      this.task = null;
      this.lastTask = null;
    }

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnConfirm);
      btnContainer.getChildren().add(btnConfirm);
    }

    initialiseLists(team);

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Task");
      btnConfirm.setText("Create");

    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Task");
      btnConfirm.setText("Save");

      labelField.setText(task.getLabel());
      descriptionField.setText(task.getTaskDescription());
      estimateField.setText(TimeFormat.parseTime(task.getTaskEstimation()));
      statusComboBox.setValue(Status.getStatusString(task.getStatus()));

      btnConfirm.setDisable(true);
    }
    this.createOrEdit = createOrEdit;

    btnConfirm.setDefaultButton(true);
    thisStage.setResizable(false);

    labelField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.trim().length() > 20) {
        labelField.setStyle("-fx-text-inner-color: red;");
      } else {
        labelField.setStyle("-fx-text-inner-color: black;");
      }
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    estimateField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    statusComboBox.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (createOrEdit == CreateOrEdit.EDIT) {
            checkButtonDisabled();
          }
        }
    );
    // TODO finish
  }

  /**
   * Checks if there are any changed fields and disables or enables the button accordingly
   */
  private void checkButtonDisabled() {
    if (labelField.getText().equals(task.getLabel()) &&
        descriptionField.getText().equals(task.getTaskDescription()) &&
        TimeFormat.parseMinutes(estimateField.getText()) == task.getTaskEstimation() &&
        Status.getStatusEnum(statusComboBox.getValue()).equals(task.getStatus()) &&
        allocatedPeopleList.getItems().equals(task.getTaskPeople())) {
      btnConfirm.setDisable(true);
    } else {
      btnConfirm.setDisable(false);
    }
  }

  /**
   * Initialises the models lists todo expand when it does more
   */
  private void initialiseLists(Team team) {
    availablePeople = FXCollections.observableArrayList();
    allocatedPeople = FXCollections.observableArrayList();

    if (team != null) {
      availablePeople.addAll(team.getTeamMembers());
    }
    if (task != null) {
      for (Person person : task.getTaskPeople()) {
        allocatedPeople.add(person);
        availablePeople.remove(person);
      }
    }

    availablePeopleList.setItems(availablePeople.sorted(Comparator.<Person>naturalOrder()));
    allocatedPeopleList.setItems(allocatedPeople.sorted(Comparator.<Person>naturalOrder()));

    ObservableList<String> availableStatuses = FXCollections.observableArrayList();
    for (Status status : Status.values()) {
      availableStatuses.add(Status.getStatusString(status));
    }
    statusComboBox.setItems(availableStatuses);
    statusComboBox.getSelectionModel().select(0);
  }

  /**
   * Allocate a team member to the task.
   *
   * @param event Action event.
   */
  @FXML
  protected void btnAddPersonClick(ActionEvent event) {
    Person selectedPerson = availablePeopleList.getSelectionModel().getSelectedItem();
    // todo effort logging
    if (selectedPerson != null) {
      allocatedPeople.add(selectedPerson);
      availablePeople.remove(selectedPerson);

      allocatedPeopleList.getSelectionModel().select(selectedPerson);
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    }
  }

  /**
   * Deallocate a team member from the task.
   *
   * @param event Action event.
   */
  @FXML
  protected void btnRemovePersonClick(ActionEvent event) {
    Person selectedPerson = allocatedPeopleList.getSelectionModel().getSelectedItem();
    if (selectedPerson != null) {
      availablePeople.add(selectedPerson);
      allocatedPeople.remove(selectedPerson);

      availablePeopleList.getSelectionModel().select(selectedPerson);
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    }
  }

  /**
   * Generate an UndoRedoObject to represent a task edit action and store it globally. This is so
   * a cancel in a dialog higher in the hierarchy can undo the changes made to the task.
   */
  private void generateEditUndoRedoObject() {
    editUndoRedoObject = new UndoRedoObject();
    editUndoRedoObject.setAction(Action.TASK_EDIT);
    editUndoRedoObject.addDatum(lastTask);

    // Store a copy of task to edit in object to avoid reference problems
    editUndoRedoObject.setAgileItem(task);
    Task taskToStore = new Task(task);
    editUndoRedoObject.addDatum(taskToStore);
  }

  @FXML
  protected void btnConfirmClick(ActionEvent event) {
    // todo implement and javadoc
    // todo error parsing
    StringBuilder errors = new StringBuilder();
    int noErrors = 0;

    String taskLabel = "";
    String taskDescription = descriptionField.getText().trim();
    int taskEstimateMinutes;
    Status taskStatus = Status.getStatusEnum(statusComboBox.getValue());

    // todo more parsing
    try {
      taskLabel = parseTaskLabel(labelField.getText());
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    taskEstimateMinutes = TimeFormat.parseMinutes(estimateField.getText());
    if (taskEstimateMinutes < 0) {
      noErrors++;
      errors.append(String.format("%s\n", "Invalid estimate time format (e.g. 1h30m)."));
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
      alert.setContentText(errors.toString());
      alert.showAndWait();
    } else {
      List<Person> allocatedPeopleSorted = allocatedPeopleList.getItems();
      if (createOrEdit == CreateOrEdit.CREATE) {
        task = new Task(taskLabel, taskDescription, taskEstimateMinutes, taskStatus,
                        allocatedPeopleSorted);
        // todo set logged effort of all people
        taskCollection.add(task);
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        task.setLabel(taskLabel);
        task.setTaskDescription(taskDescription);
        task.setTaskEstimation(taskEstimateMinutes);
        task.setStatus(taskStatus);
        task.removeAllTaskPeople();
        task.addAllTaskPeople(allocatedPeopleSorted);
        // todo set logged effort of all people
        generateEditUndoRedoObject();
      }
      // todo newAction in main or return value for nested transaction?
//    UndoRedoObject undoRedoObject = generateUndoRedoObject();
//    mainApp.newAction(undoRedoObject);
      thisStage.close();
    }
  }

  /**
   * Close the dialog and remove all applied changes
   *
   * @param event Action event
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

  /**
   * Parse a task label to make sure the label is not empty and unique within the task collection
   *
   * @param inputTaskLabel Task label from entry field.
   * @return Task label if label is valid.
   * @throws Exception Any invalid input.
   */
  private String parseTaskLabel(String inputTaskLabel) throws Exception {
    inputTaskLabel = inputTaskLabel.trim();

    if (inputTaskLabel.isEmpty()) {
      throw new Exception("Task Label is empty.");
    } else {
      String lastTaskLabel;
      if (lastTask== null) {
        lastTaskLabel = "";
      } else {
        lastTaskLabel = lastTask.getLabel();
      }
      for (Task task : taskCollection) {
        String taskLabel = task.getLabel();
        if (task.getLabel().equalsIgnoreCase(inputTaskLabel) &&
            !taskLabel.equalsIgnoreCase(lastTaskLabel)) {
          throw new Exception("Task Label is not unique within the object.");
        }
      }
      return inputTaskLabel;
    }
  }

  /**
   * Get the UndoRedoObject representing the editing of the task. Use this as a return value of
   * the dialog.
   *
   * @return The UndoRedoObject representing the successful task edit.
   */
  public UndoRedoObject getEditUndoRedoObject() {
    return editUndoRedoObject;
  }
}
