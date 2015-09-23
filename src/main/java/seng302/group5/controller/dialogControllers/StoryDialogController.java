package seng302.group5.controller.dialogControllers;

import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.controller.enums.DialogMode;
import seng302.group5.model.AgileController;
import seng302.group5.model.Status;
import seng302.group5.model.Backlog;
import seng302.group5.model.Person;
import seng302.group5.model.Sprint;
import seng302.group5.model.Story;
import seng302.group5.model.Task;
import seng302.group5.model.Team;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.CompositeUndoRedo;
import seng302.group5.model.undoredo.UndoRedo;
import seng302.group5.model.undoredo.UndoRedoObject;
import seng302.group5.model.util.Settings;

/**
 * Controller for Story creation and editing.
 *
 * Created by Zander on 5/05/2015.
 */
public class StoryDialogController implements AgileController {

  @FXML private TextField storyLabelField;
  @FXML private TextField storyNameField;
  @FXML private TextArea storyDescriptionField;
  @FXML private ComboBox<Person> storyCreatorList;
  @FXML private ListView<String> listAC;
  @FXML private Label backlogContainer; // Dirty container but works
  @FXML private ComboBox<Backlog> backlogCombo;
  @FXML private CheckBox readyCheckbox;
  @FXML private Button addAC;
  @FXML private Button removeAC;
  @FXML private Button upAC;
  @FXML private Button downAC;
  @FXML private Button btnCreateStory;
  @FXML private Button addTask;
  @FXML private Button removeTask;
  @FXML private HBox btnContainer;
  @FXML private Label shownEstimate;
  @FXML private TextField impedimentsTextField;
  @FXML private ComboBox<String> statusCombo;
  @FXML private ListView<Task> taskList;
  @FXML private Button btnNewBacklog;
  @FXML private Button btnEditBacklog;
  @FXML private Button btnNewCreator;
  @FXML private Button btnEditCreator;

  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private DialogMode dialogMode;
  private Story story;
  private Story lastStory;
  private Backlog lastBacklog;
  private Team team;

  private ObservableList<Person> availablePeople = FXCollections.observableArrayList();
  private ObservableList<String> acceptanceCriteria = FXCollections.observableArrayList();
  private ObservableList<Backlog> backlogs = FXCollections.observableArrayList();
  private ObservableList<String> statuses = FXCollections.observableArrayList();
  private ObservableList<Task> tasks = FXCollections.observableArrayList();
  private List<Task> originalTasks = new ArrayList<>();

  private CompositeUndoRedo tasksUndoRedo;  // Contains all the task changes within the dialog

  /**
   * Setup the Story dialog controller.
   *
   * @param mainApp The main application object.
   * @param thisStage The stage of the dialog.
   * @param createOrEdit Whether the dialog is for creating or editing a story.
   * @param story The story object if editing. Null otherwise.
   */
  public void setupController(Main mainApp, Stage thisStage, CreateOrEdit createOrEdit, Story story) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnCreateStory);
      btnContainer.getChildren().add(btnCreateStory);
    }

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Story");
      btnCreateStory.setText("Create");
      statusCombo.getSelectionModel().select(Status.getStatusString(Status.NOT_STARTED));

      initialiseLists();
      readyCheckbox.setDisable(true);
      btnEditBacklog.setDisable(true);
      btnEditCreator.setDisable(true);
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Story");
      btnCreateStory.setText("Save");

      storyLabelField.setText(story.getLabel());
      storyNameField.setText(story.getStoryName());
      storyDescriptionField.setText(story.getDescription());
      storyCreatorList.setValue(story.getCreator());
      impedimentsTextField.setText(story.getImpediments());
      acceptanceCriteria.setAll(story.getAcceptanceCriteria());
      tasks.setAll(story.getTasks());
      originalTasks.addAll(story.getTasks());

      initialiseLists();

      statusCombo.getSelectionModel().select(Status.getStatusString(story.getStatus()));

      storyCreatorList.setDisable(true);
      btnCreateStory.setDisable(true);

      if (checkReadinessCriteria(story)) {
        readyCheckbox.setDisable(false);
        readyCheckbox.setSelected(story.getStoryState());
      } else {
        readyCheckbox.setDisable(true);
      }

      for (Sprint sprint : mainApp.getSprints()) {
        if (sprint.getSprintStories().contains(story)) {
          readyCheckbox.setDisable(true);
        }
      }
    }
    this.createOrEdit = createOrEdit;

    if (story != null) {
      this.story = story;
      this.lastStory = new Story(story);
      this.lastBacklog = null;    // Stays null if not found
      this.team = null;

      Backlog back = new Backlog();
      if (Settings.correctList(back)) {
        backlogCombo.setDisable(true);
      }
      for (Backlog backlog : mainApp.getBacklogs()) {
        if (backlog.getStories().contains(story)) {
          this.lastBacklog = backlog;
          this.backlogCombo.setValue(backlog);
          Tooltip tooltip = new Tooltip(
              "Backlog already assigned. Please edit in backlog dialogs " +
              "to avoid problems with priority and estimates.");
          this.backlogContainer.setTooltip(tooltip);
          this.backlogCombo.setDisable(true);
          if (backlog.getEstimate().getEstimateNames().get(backlog.getSizes().get(story))!=null) {
            this.shownEstimate.setText(backlog.getEstimate().getEstimateNames() //This gets the estimate name to be shown on the story dialog
                                           .get(backlog.getSizes().get(story)));
          }
          break;
        }
      }
      if (backlogCombo.getValue() != null) {
        btnEditBacklog.setDisable(false);
      } else {
        btnEditBacklog.setDisable(true);
      }
      for (Sprint sprint : mainApp.getSprints()) {
        if (sprint.getSprintStories().contains(story)) {
          this.team = sprint.getSprintTeam();
          break;
        }
      }
    } else {
      this.story = new Story(); // different because tasks
      this.lastStory = null;
      this.lastBacklog = null;
    }

    this.tasksUndoRedo = new CompositeUndoRedo("Edit Multiple Tasks");

    btnCreateStory.setDefaultButton(true);
    thisStage.setResizable(false);

    thisStage.setOnCloseRequest(event -> {
      mainApp.popControllerStack();
    });

    storyLabelField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
      // Handle TextField text changes.
      if (newValue.trim().length() > 20) {
        storyLabelField.setStyle("-fx-text-inner-color: red;");
      } else {
        storyLabelField.setStyle("-fx-text-inner-color: black;");
      }
    });

    statusCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (createOrEdit == CreateOrEdit.EDIT) {
            checkButtonDisabled();
          }
        });

    storyNameField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    storyDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    listAC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    taskList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                                                                        -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    impedimentsTextField.textProperty().addListener((observable, oldValues, newVlaue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    backlogCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          //For disabling the button
          if(createOrEdit == CreateOrEdit.EDIT) {
            checkButtonDisabled();
          }
          if (newValue != null) {
            if (!newValue.getLabel().equals("No Backlog")) {
              btnEditBacklog.setDisable(false);
            } else {
              btnEditBacklog.setDisable(true);
            }
          }
        }
    );

    storyCreatorList.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue != null) {
            btnEditCreator.setDisable(false);
          } else {
            btnEditCreator.setDisable(true);
          }
        }
    );
  }

  /**
   * Checks if there are any changed fields and disables or enables the button accordingly
   */
  private void checkButtonDisabled() {
    //Needs to check for null which occurs on creation of new task
    if (taskList.getItems() != null) {
      if (taskList.getItems().equals(story.getTasks())) {
        btnCreateStory.setDisable(true);
      }
    }
    if (storyDescriptionField.getText().equals(story.getDescription()) &&
        storyLabelField.getText().equals(story.getLabel()) &&
        storyNameField.getText().equals(story.getStoryName()) &&
        impedimentsTextField.getText().equals(story.getImpediments()) &&
        listAC.getItems().equals(story.getAcceptanceCriteria()) &&
        readyCheckbox.isSelected() == story.getStoryState() &&
        statusCombo.getValue().equals(story.getStatusString()) &&
        (backlogCombo.getValue() == null || backlogCombo.getValue().equals(lastBacklog)) &&
        tasks.equals(originalTasks) &&
        tasksUndoRedo.getUndoRedos().isEmpty()) {
      btnCreateStory.setDisable(true);
    } else {
      btnCreateStory.setDisable(false);
    }
  }

  /**
   * Generate an UndoRedoObject to place in the stack.
   *
   * @return The UndoRedoObject to store.
   */
  private UndoRedo generateUndoRedoObject() {
    Action action;
    UndoRedoObject storyChanges = new UndoRedoObject();

    if (createOrEdit == CreateOrEdit.CREATE) {
      action = Action.STORY_CREATE;
      storyChanges.setAction(action);
    } else {
      action = Action.STORY_EDIT;
      storyChanges.setAction(action);
      storyChanges.addDatum(lastStory);
    }

    // Store a copy of story to edit in stack to avoid reference problems
    storyChanges.setAgileItem(story);
    Story storyToStore = new Story(story);
    storyChanges.addDatum(storyToStore);

    // Create composite undo/redo with original action string to handle story and task changes
    CompositeUndoRedo storyAndTaskChanges = new CompositeUndoRedo(Action.getActionString(action));
    storyAndTaskChanges.addUndoRedo(storyChanges);
    for (UndoRedo taskChange : tasksUndoRedo.getUndoRedos()) {
      // only include edits to avoid doubling tasks
      UndoRedo actualTaskChange;
      if (taskChange instanceof CompositeUndoRedo) {
        // if it's composite then the actual task edit is the first item of the composite
        actualTaskChange = ((CompositeUndoRedo) taskChange).getUndoRedos().get(0);
      } else {
        actualTaskChange = taskChange;
      }
      if (actualTaskChange.getAction().equals(Action.TASK_EDIT)) {
        storyAndTaskChanges.addUndoRedo(taskChange);
      }
    }

    return storyAndTaskChanges;
  }

  /**
   * Creates a new Story from the textfield data on click of 'Create' button.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCreateStoryClick(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    int noErrors = 0;

    String label = "";
    String storyName = storyNameField.getText().trim();
    String storyDescription = storyDescriptionField.getText().trim();
    String impediments = impedimentsTextField.getText().trim();
    Person creator = storyCreatorList.getValue();
    Backlog backlog = backlogCombo.getValue();
    Status status = Status.getStatusEnum(statusCombo.getValue());

    try {
      label = parseStoryLabel(storyLabelField.getText());
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }

    try {
      creator = parseCreatorList(storyCreatorList.getValue());
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
      alert.setContentText(errors.toString());
      alert.showAndWait();
    } else {
      if (createOrEdit == CreateOrEdit.CREATE) {
        story.setLabel(label);
        story.setStoryName(storyName);
        story.setDescription(storyDescription);
        story.setCreator(creator);
        story.setAcceptanceCriteria(acceptanceCriteria);
        story.setStatus(status);
        story.setImpediments(impediments);
        // tasks are already in story
        mainApp.addStory(story);
        if (backlog != null) {
          backlog.addStory(story);
        }
        if (Settings.correctList(backlog)) {
          mainApp.refreshList(backlog);
        }
        if (Settings.correctList(story)) {
          mainApp.refreshList(story);
        }
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        story.setLabel(label);
        story.setStoryName(storyName);
        story.setDescription(storyDescription);
        story.setImpediments(impediments);
        story.setCreator(creator);
        story.setAcceptanceCriteria(acceptanceCriteria);
        story.setStoryState(readyCheckbox.isSelected());
        //Refreshes task list so that order changes are preserved
        story.removeAllTasks();
        story.addAllTasks(tasks);
        if (lastBacklog == null && backlog != null) {
          backlog.addStory(story);
        }
        story.setStatus(status);

        if (Settings.correctList(story)) {
          mainApp.refreshList(story);
        }
      }
      UndoRedo undoRedoObject = generateUndoRedoObject();
      if (backlog != null && createOrEdit == CreateOrEdit.CREATE) {
        undoRedoObject.addDatum(backlog);
      } else {
        undoRedoObject.addDatum(null);
      }
      mainApp.newAction(undoRedoObject);
      mainApp.popControllerStack();
      thisStage.close();
    }
  }

  /**
   * Closes the Story dialog box on click of 'Cancel' button.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    Alert alert = null;
    if ((createOrEdit == CreateOrEdit.CREATE && !tasks.isEmpty()) ||
        (createOrEdit == CreateOrEdit.EDIT && (!tasks.equals(story.getTasks()) ||
                                               !tasksUndoRedo.getUndoRedos().isEmpty()))) {

      alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Changes have been made to the story's tasks");
      alert.setHeaderText(null);
      String message = "By cancelling this dialog you will lose all changes you have made "
                       + "to tasks since this story dialog was opened. Are you sure you wish "
                       + "to continue?";
      alert.getDialogPane().setPrefHeight(120);
      alert.setContentText(message);
      //checks response
      alert.showAndWait();
    }
    if (alert == null || alert.getResult() == ButtonType.OK) {
      if (Settings.correctList(story) && createOrEdit == CreateOrEdit.EDIT) {
        mainApp.refreshList(story);
      }
      // undo all editing of existing tasks made within this dialog
      mainApp.quickUndo(tasksUndoRedo);
      mainApp.popControllerStack();
      thisStage.close();
    }
  }

  /**
   * Calls the showACDialog method to open the ACDialog dialog.
   *
   * @param event Event generated by the event listener.
   */
  @FXML
  private void btnAddAC(ActionEvent event) {
    showACDialog(CreateOrEdit.CREATE);
  }

  @FXML
  /**
   * Removes the selected acceptence criteria item from the ListView and the ACList.
   *
   * @param event Event generated by the event listener.
   */
  private void btnRemoveAC(ActionEvent event) {
    if (checkStoryEstimated(story)) {
      acceptanceCriteria.remove(listAC.getSelectionModel().getSelectedItem());
      listAC.setItems(acceptanceCriteria);
    }
    else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Acceptance Criteria Required!");
      alert.setHeaderText(null);
      alert.setContentText("This story is currently assigned an estimate in "
                           + "a backlog, which requires at least one Acceptance Criterion"
                           + " to be assigned. Please ensure there is at least one "
                           + "Acceptance Criterion!");
      alert.setResizable(true);
      alert.getDialogPane().setPrefSize(400, 150);
      alert.showAndWait();
    }
  }

  @FXML
  /**
   * Checks whether or not the story is assigned to a backlog and given an estimate
   * so that, removing an ac from a story does not break the requirement of having
   * Acceptance criteria for estimations.
   */
  private Boolean checkStoryEstimated(Story story) {
    for (Backlog backlog : mainApp.getBacklogs()) {
      for (Map.Entry<Story, Integer> entry : backlog.getSizes().entrySet()) {
        if (entry.getKey() == story && entry.getValue() != 0) {
          if (listAC.getItems().size() == 1) {
            return false;
          } else
            return true;
        }
      }
    }
    return true;

  }

  /**
   * Moves the selected task up one position in the ListView.
   * @param event Event generated by the listener.
   */
  @FXML
  private void btnUpTask(ActionEvent event) {
    if (taskList.getSelectionModel().getSelectedItem() != null) {
      Object selectedItem = taskList.getSelectionModel().getSelectedItem();
      int itemIndex = tasks.indexOf(selectedItem);
      if (itemIndex != 0) {
        Task temp = tasks.get(itemIndex - 1);
        tasks.set(itemIndex - 1, (Task) selectedItem);
        tasks.set(itemIndex, temp);
        taskList.getSelectionModel().select(itemIndex - 1);
      }
    taskList.scrollTo(itemIndex - 8);
    }
  }

  /**
   * Moves the selected task down one position in the ListView.
   * @param event Event generated by the listener.
   */
  @FXML
  private void btnDownTask(ActionEvent event) {
    if (taskList.getSelectionModel().getSelectedItem() != null) {
      Object selectedItem = taskList.getSelectionModel().getSelectedItem();
      int itemIndex = tasks.indexOf(selectedItem);
      if (itemIndex != tasks.size() - 1) {
        Task temp = tasks.get(itemIndex + 1);
        tasks.set(itemIndex + 1, (Task) selectedItem);
        tasks.set(itemIndex, temp);
        taskList.getSelectionModel().select(itemIndex + 1);
      }
      taskList.scrollTo(itemIndex - 6);
    }
  }

  /**
   * Moves the selected acceptance criteria up one position in the ListView.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  private void btnUpAC(ActionEvent event) {
    if(listAC.getSelectionModel().getSelectedItem() != null) {
      Object selectedItem = listAC.getSelectionModel().getSelectedItem();
      int itemIndex = acceptanceCriteria.indexOf(selectedItem);
      if (itemIndex != 0) {
        String temp = acceptanceCriteria.get(itemIndex - 1);
        acceptanceCriteria.set(itemIndex - 1, (String) selectedItem);
        acceptanceCriteria.set(itemIndex, temp);
        listAC.getSelectionModel().select(itemIndex - 1);
      }
      listAC.scrollTo(itemIndex - 4);
    }
  }

  /**
   * Moves the selected acceptance criteria down one position in the ListView.
   *
   * @param event Event generated by the event listener.
   */
  @FXML
  private void btnDownAC(ActionEvent event) {
    if(listAC.getSelectionModel().getSelectedItem() != null) {
      Object selectedItem = listAC.getSelectionModel().getSelectedItem();
      int itemIndex = acceptanceCriteria.indexOf(selectedItem);
      if (itemIndex != acceptanceCriteria.size() - 1) {
        String temp = acceptanceCriteria.get(itemIndex + 1);
        acceptanceCriteria.set(itemIndex + 1, (String) selectedItem);
        acceptanceCriteria.set(itemIndex, temp);
        listAC.getSelectionModel().select(itemIndex + 1);
      }
      listAC.scrollTo(itemIndex - 2);
    }
  }

  /**
   * Opens the ACDialog dialog in order to create a new acceptance criteria.
   *
   * @param createOrEdit Whether its being created or edited
   */
  public void showACDialog(CreateOrEdit createOrEdit) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/ACDialog.fxml"));
      VBox ACDialogLayout = loader.load();

      ACDialogController controller = loader.getController();
      Scene ACDialogScene = new Scene(ACDialogLayout);
      Stage ACDialogStage = new Stage();

      String ac = null;
      if (createOrEdit == CreateOrEdit.EDIT) {
        ac = (String) listAC.getSelectionModel().getSelectedItem();
        if (ac == null) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText(null);
          alert.setContentText("No acceptance criterion selected.");
          alert.showAndWait();
          return;
        }
      }

      controller.setupController(this, ACDialogStage, createOrEdit, ac);

      ACDialogStage.initModality(Modality.APPLICATION_MODAL);
      ACDialogStage.initOwner(thisStage);
      ACDialogStage.setScene(ACDialogScene);
      ACDialogStage.show();


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks that the Story label entry box contains valid input.
   *
   * @param inputStoryLabel Story label from entry field.
   * @return Story label if label is valid.
   * @throws Exception Any invalid input.
   */
  private String parseStoryLabel(String inputStoryLabel) throws Exception {
    inputStoryLabel = inputStoryLabel.trim();

    if (inputStoryLabel.isEmpty()) {
      throw new Exception("Story Label is empty.");
    } else {
      String lastStoryLabel;
      if (lastStory == null) {
        lastStoryLabel = "";
      } else {
        lastStoryLabel = lastStory.getLabel();
      }
      for (Story storyInList : mainApp.getStories()) {
        String storyLabel = storyInList.getLabel();
        if (storyLabel.equalsIgnoreCase(inputStoryLabel) &&
            !storyLabel.equalsIgnoreCase(lastStoryLabel)) {
          throw new Exception("Story Label is not unique.");
        }
      }
    }
    return inputStoryLabel;
  }

  /**
   * Checks that the Creator combobox contains a valid creator.
   *
   * @param inputPerson The Person selected in the combobox.
   * @return The inputPerson if the Person is valid.
   * @throws Exception If the inputPerson is invalid.
   */
  private Person parseCreatorList(Person inputPerson) throws Exception {
    if (inputPerson == null) {
      throw new Exception("No creator has been selected for story.");
    }

    return inputPerson;
  }

  /**
   * Adds an acceptance criteria to the list of acceptance criteria.
   *
   * @param newAC Acceptance criteria in String form.
   */
  public void appendAcceptanceCriteria(String newAC) {
    this.acceptanceCriteria.add(newAC);
    listAC.setItems(acceptanceCriteria);
    btnCreateStory.setDisable(false); //gotta ungrey it when a change is made
  }

  public void changeAcceptanceCriteria(String oldAC, String newAC) {
    int index = this.acceptanceCriteria.indexOf(oldAC);
    this.acceptanceCriteria.set(index, newAC);

    listAC.setItems(acceptanceCriteria);
    btnCreateStory.setDisable(false); //gotta ungrey it when a change is made
  }

  /**
   * Checks if a given string already exists as part of the Story.
   *
   * @param newAC The acceptance criteria to check.
   * @return The acceptance criteria if it's valid.
   * @throws Exception The error if a duplicate exists.
   */
  public String checkForDuplicateAC(String newAC) throws Exception {
    if(acceptanceCriteria.contains(newAC)) {
      throw new Exception("This story already has this acceptance criterion.");
    } else {
      return newAC;
    }
  }

  public void setCheckboxState(boolean state) {
    readyCheckbox.setDisable(state);
  }

  /**
   * Initalises the Creator assignment list.
   */
  private void initialiseLists() {
    try {
      for (Person person : mainApp.getPeople()) {
        availablePeople.add(person);
      }
      this.backlogs.addAll(mainApp.getBacklogs());

      for (Status status : Status.values()) {
        String statusStr = Status.getStatusString(status);
        this.statuses.add(statusStr);
      }
      this.statusCombo.setItems(statuses); //This is the only way i can think to do this with an
                                          // Enum. I know how shit this is :C i am so sad
      this.taskList.setItems(tasks);

      this.statusCombo.setValue("Not Started"); // For setting this to not started as its a reasonable assumption also fixes hashcode bug

      this.storyCreatorList.setVisibleRowCount(5);
      this.storyCreatorList.setItems(availablePeople);

      this.listAC.setItems(acceptanceCriteria);

      this.backlogCombo.setItems(backlogs);

      setupListView();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets the custom behaviour for the acceptance criteria and task ListViews.
   */
  private void setupListView() {
    //Sets the cell being populated with custom settings defined in the ListViewCell class.
    this.listAC.setCellFactory(listView -> new ListViewCell());
    this.taskList.setCellFactory(listView -> new TaskListCell());
  }

  /**
   * Handles the behaviour of the readiness checkbox on click.
   */
  @FXML
  private void readinessCheckboxClick() {
    checkButtonDisabled();
  }


  /**
   *
   * Checks if the story meets the criteria to be marked as ready:
   * Has acceptance criteria.
   * Is in a backlog.
   * Has a size estimate.
   *
   * @param story Story which readiness is checked.
   * @return sWhether the story meets readiness criteria as boolean.
   */
  private boolean checkReadinessCriteria(Story story) {
    for (Backlog backlog : mainApp.getBacklogs()) {   //Search each backlog
      if (backlog.getStories().contains(story)) {     //If backlog contains story
        if (backlog.getSizes().get(story) > 0) {      //If story is estimated (and therefore has AC)
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Open the dialog for task dialog creation. The created task will be added to the tasks list,
   * not the story model.
   */
  @FXML
  private void addTask() {
    UndoRedo taskCreate = mainApp.showTaskDialog(story, null, team, CreateOrEdit.CREATE, thisStage);
    if (taskCreate != null) {
      tasksUndoRedo.addUndoRedo(taskCreate);
      tasks.add(story.getTasks().get(story.getTasks().size() - 1));
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    }
  }

  /**
   * Remove the selected task from the list.
   */
  @FXML
  private void removeTask() {
    Task selectedTask = taskList.getSelectionModel().getSelectedItem();
    if (selectedTask != null) {
      UndoRedo taskDelete = new UndoRedoObject();
      taskDelete.setAction(Action.TASK_DELETE);
      taskDelete.addDatum(new Task(selectedTask));
      taskDelete.addDatum(story);

      // Store a copy of task to edit in object to avoid reference problems
      taskDelete.setAgileItem(selectedTask);

      story.removeTask(selectedTask);
      tasksUndoRedo.addUndoRedo(taskDelete);

      tasks.remove(selectedTask);
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    }
  }

  /**
   * Allows us to override the a ListViewCell - a single cell in a ListView.
   */
  private class ListViewCell extends TextFieldListCell<String> {

    private Label cellText;
    private GridPane pane;
    private double labelWidth;

    public ListViewCell() {
      super();

      this.setOnMouseClicked(click -> {
        if (click.getClickCount() == 2 &&
            click.getButton() == MouseButton.PRIMARY &&
            !isEmpty()) {
          showACDialog(CreateOrEdit.EDIT);
        }
      });

      labelWidth = listAC.getLayoutBounds().getWidth() - 16;
      cellText = new Label();
      pane = new GridPane();
      pane.getColumnConstraints().add(new ColumnConstraints(labelWidth));
      pane.setHgap(5);
      pane.add(cellText, 0, 0);
      //pane.add(editButton, 1, 0);
    }

    /**
     * Sets the overriden parameters for the ListViewCell when the cell is updated.
     *
     * @param string The String being added to the cell.
     * @param empty Whether or not string is empty as a boolean flag.
     */
    @Override
    public void updateItem(String string, boolean empty) {
      super.updateItem(string, empty);

      if (empty || string == null) {
        cellText.setText(null);
        setGraphic(null);
      } else {
        //Checks if string is longer than pixel limit.
        //If it is, trims it until it fits and adds '...'.
        //Also removes new lines.
        //Does not touch original String stored in list.
        string = string.replace("\n", " ");
        Text text = new Text(string);
        double width = text.getLayoutBounds().getWidth();

        if (width > labelWidth) {
          while (width > labelWidth) {
            string = string.substring(0, string.length() - 1);
            text = new Text(string);
            width = text.getLayoutBounds().getWidth();
          }
          cellText.setText(string + "...");
          setText(null);
        } else {
          cellText.setText(string);
          setText(null);
        }
        setGraphic(pane);
      }
    }
  }

  /**
   * Allow double clicking for editing a Task.
   */
  private class TaskListCell extends TextFieldListCell<Task> {

    public TaskListCell() {
      super();
      this.setOnMouseClicked(click -> {
        if (click.getClickCount() == 2 &&
            click.getButton() == MouseButton.PRIMARY &&
            !isEmpty()) {
          UndoRedo taskEdit =
                     mainApp.showTaskDialog(story, getItem(), team, CreateOrEdit.EDIT, thisStage);
          if (taskEdit != null) {
            tasksUndoRedo.addUndoRedo(taskEdit);
            taskList.setItems(null);
            taskList.setItems(tasks);
            taskList.getSelectionModel().select(getItem());
            if (createOrEdit == CreateOrEdit.EDIT) {
              checkButtonDisabled();
            }
          }
        }
      });
    }
  }

  /**
   * A button which when clicked can edit the selected backlog in the backlog combo box.
   * Also adds to undo/redo stack so the edit is undoable.
   * @param event Button click
   */
  @FXML
  protected void editBacklog(ActionEvent event) {
    List<Backlog> tempBacklogList = new ArrayList<>(backlogs);
    Backlog selectedBacklog = backlogCombo.getSelectionModel().getSelectedItem();
    if (selectedBacklog != null) {
      mainApp.showBacklogDialogNested(selectedBacklog, thisStage);
      backlogs.setAll(tempBacklogList);
      backlogCombo.getSelectionModel().select(selectedBacklog);
    }
  }

  /**
   * A button which when clicked can add a backlog to the system.
   * Also adds to undo/redo stack so creation is undoable.
   * @param event Button click
   */
  @FXML
  protected void addNewBacklog(ActionEvent event) {
    List<Backlog> tempBacklogList = new ArrayList<>(backlogs);
    mainApp.showBacklogDialog(CreateOrEdit.CREATE);
    if (!backlogCombo.isDisabled()) {
      List<Backlog> tempNewBacklogList = new ArrayList<>(mainApp.getBacklogs());
      for (Backlog backlog : tempNewBacklogList) {
        if (!tempBacklogList.contains(backlog)) {
          backlogs.setAll(tempNewBacklogList);
          backlogCombo.getSelectionModel().select(backlog);
          break;
        }
      }
    }
  }

  /**
   * A button which when clicked can edit the selected backlog in the backlog combo box.
   * Also adds to undo/redo stack so the edit is undoable.
   * @param event Button click
   */
  @FXML
  protected void editCreator(ActionEvent event) {
    List<Person> tempCreatorList = new ArrayList<>(availablePeople);
    Person selectedPerson = storyCreatorList.getSelectionModel().getSelectedItem();
    if (selectedPerson != null) {
      mainApp.showPersonDialogWithinTeam(selectedPerson, thisStage);
      availablePeople.setAll(tempCreatorList);
      storyCreatorList.getSelectionModel().select(selectedPerson);
    }
  }

  /**
   * A button which when clicked can add a backlog to the system.
   * Also adds to undo/redo stack so creation is undoable.
   * @param event Button click
   */
  @FXML
  protected void addNewCreator(ActionEvent event) {
    List<Person> tempCreatorList = new ArrayList<>(availablePeople);
    mainApp.showPersonDialog(CreateOrEdit.CREATE);
    if (!storyCreatorList.isDisabled()) {
      List<Person> tempNewCreatorList = new ArrayList<>(mainApp.getPeople());
      for (Person creator : tempNewCreatorList) {
        if (!tempCreatorList.contains(creator)) {
          availablePeople.setAll(tempNewCreatorList);
          storyCreatorList.getSelectionModel().select(creator);
          break;
        }
      }
    }
  }

  /**
   * Returns the label of the backlog if a backlog is being edited.
   *
   * @return The label of the backlog as a string.
   */
  public String getLabel() {
    if (createOrEdit == CreateOrEdit.EDIT) {
      return story.getLabel();
    }
    return "";
  }
}
