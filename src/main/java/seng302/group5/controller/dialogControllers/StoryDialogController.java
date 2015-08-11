package seng302.group5.controller.dialogControllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import seng302.group5.controller.enums.Status;
import seng302.group5.model.Backlog;
import seng302.group5.model.Person;
import seng302.group5.model.Sprint;
import seng302.group5.model.Story;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;
import seng302.group5.model.util.Settings;

/**
 * Controller for Story creation and editing.
 *
 * Created by Zander on 5/05/2015.
 */
public class StoryDialogController {

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
  @FXML private HBox btnContainer;
  @FXML private Label shownEstimate;
  @FXML private ComboBox<String> statusCombo;

  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Story story;
  private Story lastStory;
  private Backlog lastBacklog;

  private ObservableList<Person> availablePeople = FXCollections.observableArrayList();
  private ObservableList<String> acceptanceCriteria = FXCollections.observableArrayList();
  private ObservableList<Backlog> backlogs = FXCollections.observableArrayList();
  private ObservableList<String> statuses = FXCollections.observableArrayList();

  Map<String, Status> statusStringMap;

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

    statusStringMap = new HashMap<>();
    statusStringMap.put("Done", Status.DONE);
    statusStringMap.put("Verify", Status.VERIFY);
    statusStringMap.put("In Progress", Status.IN_PROGRESS);
    statusStringMap.put("Not Started", Status.NOT_STARTED);

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnCreateStory);
      btnContainer.getChildren().add(btnCreateStory);
    }

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Story");
      btnCreateStory.setText("Create");

      initialiseLists();
      readyCheckbox.setDisable(true);
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Story");
      btnCreateStory.setText("Save");

      storyLabelField.setText(story.getLabel());
      storyNameField.setText(story.getStoryName());
      storyDescriptionField.setText(story.getDescription());
      storyCreatorList.setValue(story.getCreator());
      acceptanceCriteria.setAll(story.getAcceptanceCriteria());

      for (Map.Entry<String, Status> entry : statusStringMap.entrySet())
      {
        if (story.getStatus() == entry.getValue()){
          statusCombo.setValue(entry.getKey());
        }
      }

      initialiseLists();
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
    } else {
      this.story = null;
      this.lastStory = null;
      this.lastBacklog = null;
    }

    btnCreateStory.setDefaultButton(true);
    thisStage.setResizable(false);

    storyLabelField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
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

    backlogCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          //For disabling the button
          if(createOrEdit == CreateOrEdit.EDIT) {
            checkButtonDisabled();
          }
        }
    );
  }

  /**
   * Checks if there are any changed fields and disables or enables the button accordingly
   */
  private void checkButtonDisabled() {
    if (storyDescriptionField.getText().equals(story.getDescription()) &&
        storyLabelField.getText().equals(story.getLabel()) &&
        storyNameField.getText().equals(story.getStoryName()) &&
        listAC.getItems().equals(story.getAcceptanceCriteria()) &&
        readyCheckbox.isSelected() == story.getStoryState() &&
        statusCombo.getValue().equals(story.getStatusString()) &&
        (backlogCombo.getValue() == null || backlogCombo.getValue().equals(lastBacklog))) {
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
  private UndoRedoObject generateUndoRedoObject() {
    UndoRedoObject undoRedoObject = new UndoRedoObject();

    if (createOrEdit == CreateOrEdit.CREATE) {
      undoRedoObject.setAction(Action.STORY_CREATE);
    } else {
      undoRedoObject.setAction(Action.STORY_EDIT);
      undoRedoObject.addDatum(lastStory);
    }

    // Store a copy of story to edit in stack to avoid reference problems
    undoRedoObject.setAgileItem(story);
    Story storyToStore = new Story(story);
    undoRedoObject.addDatum(storyToStore);

    return undoRedoObject;
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
    Person creator = storyCreatorList.getValue();
    Backlog backlog = backlogCombo.getValue();
    Status status = statusStringMap.get(statusCombo.getValue());

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
        story = new Story(label, storyName, storyDescription, creator, acceptanceCriteria, status);
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
        story.setCreator(creator);
        story.setAcceptanceCriteria(acceptanceCriteria);
        story.setStoryState(readyCheckbox.isSelected());
        if (lastBacklog == null && backlog != null) {
          backlog.addStory(story);
        }
        story.setStatus(status);

        if (Settings.correctList(story)) {
          mainApp.refreshList(story);
        }
      }
      UndoRedoObject undoRedoObject = generateUndoRedoObject();
      if (backlog != null && createOrEdit == CreateOrEdit.CREATE) {
        undoRedoObject.addDatum(backlog);
      } else {
        undoRedoObject.addDatum(null);
      }
      mainApp.newAction(undoRedoObject);

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
    if (Settings.correctList(story) && createOrEdit == CreateOrEdit.EDIT) {
      mainApp.refreshList(story);
    }
    thisStage.close();
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
    }
  }

  /**
   * Opens the ACDialog dialog in order to create a new acceptance criteria.
   *
   * @param createOrEdit Whether its being created or editted
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

      for (String status : statusStringMap.keySet()) {
        this.statuses.add(status);
      }
      this.statusCombo.setItems(statuses); //This is the only way i can think to do this with an
                                          // Enum. I know how shit this is :C i am so sad

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
   * Sets the custom behaviour for the acceptance criteria listview.
   */
  private void setupListView() {
    //Sets the cell being populated with custom settings defined in the ListViewCell class.
    this.listAC.setCellFactory(listView -> new ListViewCell());
  }

  /**
   * Handles the behaviour of the readiness checkbox on click.
   */
  @FXML
  private void readinessCheckboxClick() {
    checkButtonDisabled();
  }

  /**
   * Checks if the story meets the criteria to be marked as ready:
   * Has acceptance criteria.
   * Is in a backlog.
   * Has a size estimate.
   *
   * @return Whether the story meets readiness criteria as boolean.
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
}
