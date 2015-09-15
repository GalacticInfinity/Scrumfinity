package seng302.group5.controller.dialogControllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Person;
import seng302.group5.model.Status;
import seng302.group5.model.Task;
import seng302.group5.model.Taskable;
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
  @FXML private TextField impedimentsField;
  @FXML private ComboBox<String> statusComboBox;
  @FXML private ListView<Person> availablePeopleList;
  @FXML private ListView<PersonEffort> allocatedPeopleList;
  @FXML private Button btnAddPerson;
  @FXML private Button btnRemovePerson;
  @FXML private HBox btnContainer;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Taskable taskable;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Task task;
  private Task lastTask;
  private ObservableList<Person> availablePeople;
  private ObservableList<PersonEffort> allocatedPeople;
  private ObservableList<PersonEffort> originalPeople;

  private UndoRedoObject undoRedoObject;

  /**
   * Sets up the controller on start up. Is called currently from sprints, stories and scrum board
   * but can be called from many places. The Taskable instance is used instead of Main compared to
   * other dialogs because Tasks cannot exist on their own, i.e. taskable.addTask() rather than
   * mainApp.addStory().
   *
   * @param taskable The collection which will contain the task
   * @param team The team of the sprint which will contain the task
   * @param thisStage This is the window that will be displayed
   * @param createOrEdit This is an ENUM object to determine if creating or editing
   * @param task The object that will be edited (null if creating)
   */
  public void setupController(Taskable taskable, Team team,
                              Stage thisStage, CreateOrEdit createOrEdit, Task task) {
    this.taskable = taskable;
    this.thisStage = thisStage;

    if (task != null) {
      this.task = task;
      this.lastTask = new Task(task);
    } else {
      this.task = new Task();  // different because efforts
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
      impedimentsField.setText(task.getImpediments());
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

    impedimentsField.textProperty().addListener((observable, oldValue, newValue) -> {
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

    undoRedoObject = null;
  }

  /**
   * Checks if there are any changed fields and disables or enables the button accordingly
   */
  private void checkButtonDisabled() {
    if (labelField.getText().equals(task.getLabel()) &&
        descriptionField.getText().equals(task.getTaskDescription()) &&
        TimeFormat.parseMinutes(estimateField.getText()) == task.getTaskEstimation() &&
        impedimentsField.getText().equals(task.getImpediments()) &&
        Status.getStatusEnum(statusComboBox.getValue()).equals(task.getStatus()) &&
        allocatedPeopleList.getItems().equals(originalPeople)) {
      btnConfirm.setDisable(true);
    } else {
      btnConfirm.setDisable(false);
    }
  }

  /**
   * Initialises the models lists including available people, allocated people with their logged
   * effort, available statuses, and sets the custom list cell of the allocated people list view.
   *
   * @param team The team for which team is initialized for.
   */
  private void initialiseLists(Team team) {
    availablePeople = FXCollections.observableArrayList();
    allocatedPeople = FXCollections.observableArrayList();
    originalPeople = FXCollections.observableArrayList();

    if (team != null) {
      availablePeople.addAll(team.getTeamMembers());
    }
    if (task != null && createOrEdit == CreateOrEdit.EDIT) {
      for (Person person : task.getTaskPeople()) {
        int effort = task.getSpentEffort().get(person);
        PersonEffort personEffort = new PersonEffort(person, TimeFormat.parseTime(effort));
        allocatedPeople.add(personEffort);
        availablePeople.remove(person);
        originalPeople.add(new PersonEffort(person, TimeFormat.parseTime(effort)));
      }
    }

    availablePeopleList.setItems(availablePeople.sorted(Comparator.<Person>naturalOrder()));
    allocatedPeopleList.setItems(allocatedPeople.sorted(Comparator.<PersonEffort>naturalOrder()));

    ObservableList<String> availableStatuses = FXCollections.observableArrayList();
    for (Status status : Status.values()) {
      availableStatuses.add(Status.getStatusString(status));
    }
    statusComboBox.setItems(availableStatuses);
    statusComboBox.getSelectionModel().select(0);

    allocatedPeopleList.setCellFactory(listView -> new PersonEffortCell());
  }

  /**
   * Allocate a team member to the task.
   *
   * @param event Action event.
   */
  @FXML
  protected void btnAddPersonClick(ActionEvent event) {
    Person selectedPerson = availablePeopleList.getSelectionModel().getSelectedItem();
    if (selectedPerson != null) {
      PersonEffort personEffort = new PersonEffort(selectedPerson, "0m");
      allocatedPeople.add(personEffort);
      availablePeople.remove(selectedPerson);

      allocatedPeopleList.getSelectionModel().select(personEffort);
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
    PersonEffort selectedPersonEffort = allocatedPeopleList.getSelectionModel().getSelectedItem();
    if (selectedPersonEffort != null) {
      availablePeople.add(selectedPersonEffort.getPerson());
      allocatedPeople.remove(selectedPersonEffort);

      availablePeopleList.getSelectionModel().select(selectedPersonEffort.getPerson());
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    }
  }

  /**
   * Generate an UndoRedoObject to represent a task edit action and store it globally. This is so
   * a cancel in a dialog higher in the hierarchy can undo the changes made to the task.
   */
  private void generateUndoRedoObject() {
    if (createOrEdit == CreateOrEdit.CREATE) {
      undoRedoObject = new UndoRedoObject();
      undoRedoObject.setAction(Action.TASK_CREATE);
      undoRedoObject.addDatum(new Task(task));
      undoRedoObject.addDatum(taskable);

      // Store a copy of task to edit in object to avoid reference problems
      undoRedoObject.setAgileItem(task);

    } else if (createOrEdit == CreateOrEdit.EDIT) {
      undoRedoObject = new UndoRedoObject();
      undoRedoObject.setAction(Action.TASK_EDIT);
      undoRedoObject.addDatum(lastTask);

      // Store a copy of task to edit in object to avoid reference problems
      undoRedoObject.setAgileItem(task);
      Task taskToStore = new Task(task);
      undoRedoObject.addDatum(taskToStore);
    }
  }

  /**
   * Handles the action of clicking the confirm button. It parses the values that were input
   * into the dialog fields and updates or creates the task it is looking at. It creates
   * the UndoRedoObject for the changes made but does not force it onto the stack in
   * UndoRedoHandler. It is up to the object that is using the dialog to use it by accessing
   * it using the getter.
   *
   * @param event Action event
   */
  @FXML
  protected void btnConfirmClick(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    int noErrors = 0;

    String taskLabel = "";
    String taskDescription = descriptionField.getText().trim();
    int taskEstimateMinutes;
    String taskImpediments = impedimentsField.getText().trim();
    Status taskStatus = Status.getStatusEnum(statusComboBox.getValue());

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

    Map<Person, Integer> peoplesEffort = new IdentityHashMap<>();
    for (PersonEffort personEffort : allocatedPeopleList.getItems()) {
      Person person = personEffort.getPerson();
      int effort = TimeFormat.parseMinutes(personEffort.getEffortStr());
      if (effort < 0) {
        noErrors++;
        errors.append(String.format("Invalid effort time format for %s (e.g. 1h30m).\n", person));
        break;
      }
      peoplesEffort.put(person, effort);
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
      alert.getDialogPane().setPrefHeight(60 + 30 * noErrors);
      alert.setContentText(errors.toString());
      alert.showAndWait();
    } else {
      List<Person> allocatedPeopleSorted = new ArrayList<>();
      for (PersonEffort personEffort : allocatedPeopleList.getItems()) {
        allocatedPeopleSorted.add(personEffort.getPerson());
      }
      if (createOrEdit == CreateOrEdit.CREATE) {
        task = new Task(taskLabel, taskDescription, taskEstimateMinutes, taskStatus,
                        allocatedPeopleSorted);
        task.setImpediments(taskImpediments);
        task.updateSpentEffort(peoplesEffort);
        taskable.addTask(task);
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        task.setLabel(taskLabel);
        task.setTaskDescription(taskDescription);
        task.setTaskEstimation(taskEstimateMinutes);
        task.setImpediments(taskImpediments);
        task.setStatus(taskStatus);
        task.removeAllTaskPeople();
        task.addAllTaskPeople(allocatedPeopleSorted);
        task.updateSpentEffort(peoplesEffort);
      }
      generateUndoRedoObject();
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
      if (lastTask == null) {
        lastTaskLabel = "";
      } else {
        lastTaskLabel = lastTask.getLabel();
      }
      for (Task task : taskable.getTasks()) {
        String taskLabel = task.getLabel();
        if (taskLabel.equalsIgnoreCase(inputTaskLabel) &&
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
  public UndoRedoObject getUndoRedoObject() {
    return undoRedoObject;
  }

  /**
   * Shows the dialog used for logging effort against a task.
   * @param createOrEdit Whether the dialog is for creating or editing effort.
   */
  public void showEffortDialog(CreateOrEdit createOrEdit) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/LoggingEffortDialog.fxml"));
      VBox effortDialogLayout = loader.load();

      EffortDialogController controller = loader.getController();
      Scene effortDialogScene = new Scene(effortDialogLayout);
      Stage effortDialogStage = new Stage();

      List<Person> allocated = new ArrayList<>();
      for (PersonEffort personEffort : allocatedPeople) {
        allocated.add(personEffort.getPerson());
      }
      controller.setupController(task, allocated, effortDialogStage, createOrEdit, null);

      effortDialogStage.initModality(Modality.APPLICATION_MODAL);
      effortDialogStage.initOwner(thisStage);
      effortDialogStage.setScene(effortDialogScene);
      effortDialogStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Calls the method to open the effort dialog to log some new effort.
   * @param event The event generated by the listener.
   */
  @FXML
  private void btnAddEffort(ActionEvent event) {
    showEffortDialog(CreateOrEdit.CREATE);
  }

  /**
   * Class for containing a person and their effort against the task.
   */
  private class PersonEffort implements Comparable<PersonEffort> {

    private Person person;
    private String effortStr;

    /**
     * Constructor for PersonEffort object
     *
     * @param person Person to store
     * @param effortStr Effort string to store
     */
    public PersonEffort(Person person, String effortStr) {
      this.person = person;
      this.effortStr = effortStr;
    }

    /**
     * gets the person object from this object
     *
     * @return Person object
     */
    public Person getPerson() {
      return person;
    }

    /**
     * gets the effort string from this object
     *
     * @return Effort string
     */
    public String getEffortStr() {
      return effortStr;
    }

    /**
     * Set the effort string to this object
     *
     * @param effortStr Effort string
     */
    public void setEffortStr(String effortStr) {
      this.effortStr = effortStr;
    }

    @Override
    public String toString() {
      return person.toString() + " " + effortStr;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      PersonEffort that = (PersonEffort) o;

      if (!person.equals(that.person)) {
        return false;
      }
      return TimeFormat.parseMinutes(effortStr) == (TimeFormat.parseMinutes(that.effortStr));

    }

    @Override
    public int hashCode() {
      int result = person.hashCode();
      result = 31 * result + Integer.valueOf(TimeFormat.parseMinutes(effortStr)).hashCode();
      return result;
    }

    @Override
    public int compareTo(PersonEffort o) {
      return person.getLabel().compareToIgnoreCase(o.getPerson().getLabel());
    }
  }

  /**
   * List cell to combine a person with their logged effort.
   */
  private class PersonEffortCell extends TextFieldListCell<PersonEffort> {

    private TextField effortField;
    private Label cellText;
    private double labelWidth;
    private GridPane pane;

    public PersonEffortCell() {
      super();

      cellText = new Label();
      effortField = new TextField("0m");
      effortField.setMaxWidth(55);
      effortField.textProperty().addListener(observable -> {
        getItem().setEffortStr(effortField.getText());
        if (createOrEdit == CreateOrEdit.EDIT) {
          checkButtonDisabled();
        }
      });
      labelWidth = allocatedPeopleList.getLayoutBounds().getWidth() - 76;
      pane = new GridPane();
      pane.getColumnConstraints().add(new ColumnConstraints(labelWidth));
      pane.setHgap(5);
      pane.add(cellText, 0, 0);
      pane.add(effortField, 1, 0);
    }

    /**
     * Sets the overridden parameters for the PersonEffortCell when the cell is updated.
     * @param personEffort The Person being added to the cell with their effort.
     * @param empty Whether or not string is empty as a boolean flag.
     */
    @Override
    public void updateItem(PersonEffort personEffort, boolean empty) {
      super.updateItem(personEffort, empty);

      if (empty) {
        cellText.setText(null);
        setText(null);
        setGraphic(null);
      } else {
        cellText.setText(personEffort.getPerson().toString());
        effortField.setText(personEffort.getEffortStr());
        setText(null);
        setGraphic(pane);
      }
    }
  }
}
