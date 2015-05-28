package seng302.group5.controller;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

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
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Backlog;
import seng302.group5.model.Person;
import seng302.group5.model.Role;
import seng302.group5.model.Skill;
import seng302.group5.model.Story;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;
import seng302.group5.model.util.Settings;

/**
 * @author Liang Ma Backlog Dialog Controller, manages the usage of Dialogs involved in the creating
 *         and editing of backlog and maintain stories within the backlog.
 */
public class BacklogDialogController {

  private Main mainApp;  //Testing if Jenkins is working.
  private Stage thisStage;

  private CreateOrEdit createOrEdit;

  private Backlog backlog;
  private Backlog lastBacklog;

  private boolean comboListenerFlag;
  //private Estimate lastSelectedEstimate

  private ObservableList<Story> availableStories;
  private ObservableList<Story> allocatedStories;
  private ObservableList<Story> originalStories;
  private ObservableList<Person> productOwners;

  @FXML private TextField backlogLabelField;
  @FXML private TextField backlogNameField;
  @FXML private TextArea backlogDescriptionField;
  @FXML private ComboBox<Person> backlogProductOwnerCombo;
  @FXML private HBox btnContainer;
  @FXML private Button btnConfirm;
  @FXML private ListView<Story> allocatedStoriesList;
  @FXML private ListView<Story> availableStoriesList;

  /**
   * Setup the backlog dialog controller
   *
   * @param mainApp      The main application object
   * @param thisStage    The stage of the dialog
   * @param createOrEdit If dialog is for creating or editing a Team
   * @param backlog      The backlog object if editing, null otherwise
   */
  public void setupController(Main mainApp, Stage thisStage, CreateOrEdit createOrEdit,
                              Backlog backlog) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnConfirm);
      btnContainer.getChildren().add(btnConfirm);
    }

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Backlog");
      btnConfirm.setText("Create");

      initialiseLists(CreateOrEdit.CREATE, backlog);
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Team");
      btnConfirm.setText("Save");
      btnConfirm.setDisable(true);
      initialiseLists(CreateOrEdit.EDIT, backlog);
      backlogLabelField.setText(backlog.getLabel());
      backlogNameField.setText(backlog.getBacklogName());
      backlogDescriptionField.setText(backlog.getBacklogDescription());
      backlogProductOwnerCombo.setValue(backlog.getProductOwner());
    }
    this.createOrEdit = createOrEdit;

    if (backlog != null) {
      this.backlog = backlog;
      this.lastBacklog = new Backlog(backlog);
    } else {
      this.backlog = null;
      this.lastBacklog = null;
    }

//    comboListenerFlag = false;  // if true, assign the selected role in combo box
    //lastSelectedEstimate = new Estimate();

    btnConfirm.setDefaultButton(true);
    thisStage.setResizable(false);

    // Handle TextField text changes.
    backlogLabelField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
      if (newValue.trim().length() > 20) {
        backlogLabelField.setStyle("-fx-text-inner-color: red;");
      } else {
        backlogLabelField.setStyle("-fx-text-inner-color: black;");
      }
    });

    backlogNameField.textProperty().addListener((observable1, oldValue1, newValue1) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    backlogDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    backlogProductOwnerCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (createOrEdit == CreateOrEdit.EDIT) {
            checkButtonDisabled();
          }
        });
  }

  /**
   * Check if any of the fields has been changed. If nothing changed, then confirm button is disabled.
   */
  private void checkButtonDisabled() {
    if (backlogLabelField.getText().equals(backlog.getLabel()) &&
        backlogNameField.getText().equals(backlog.getBacklogName()) &&
        backlogDescriptionField.getText().equals(backlog.getBacklogDescription()) &&
        backlogProductOwnerCombo.getValue().equals(backlog.getProductOwner()) &&
        allocatedStories.toString().equals(originalStories.toString())){
      btnConfirm.setDisable(true);
    } else {
      btnConfirm.setDisable(false);
    }
  }

  /**
   * Populates the people selection lists for assigning people to the team.
   *
   * @param createOrEdit the enum object to decide if it is creating or editing
   * @param backlog      the Team that is being created or edited
   */
  private void initialiseLists(CreateOrEdit createOrEdit, Backlog backlog) {
    availableStories = FXCollections.observableArrayList();
    allocatedStories = FXCollections.observableArrayList();
    originalStories = FXCollections.observableArrayList();
    productOwners = FXCollections.observableArrayList();

    try {
      Set<Story> storiesInUse = new HashSet<>();
      for (Backlog mainBacklog : mainApp.getBacklogs()) {
        storiesInUse.addAll(mainBacklog.getStories());
      }

      Skill productOwnerSkill = null;

      for (Role role : mainApp.getRoles()) {
        if (role.getLabel().equals("PO")) {
          productOwnerSkill = role.getRequiredSkill();
          break;
        }
      }

      for (Person person : mainApp.getPeople()) {
        if (person.getSkillSet().contains(productOwnerSkill)) {
          productOwners.add(person);
        }
      }

      this.backlogProductOwnerCombo.setVisibleRowCount(5);
      this.backlogProductOwnerCombo.setItems(productOwners);

      if (createOrEdit == CreateOrEdit.CREATE) {
        for (Story story : mainApp.getStories()) {
          if (!storiesInUse.contains(story)) {
            availableStories.add(story);
          }
        }
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        for (Story story : mainApp.getStories()) {
          if (!storiesInUse.contains(story)) {
            availableStories.add(story);
          }
        }
        for (Story story : backlog.getStories()) {
//          Role role = team.getMembersRole().get(person);
          allocatedStories.add(story); // TODO: Maybe inner class to combine with estimates
          originalStories.add(story);
//          originalMembers.add(new PersonRole(person, role));
        }
        originalStories = originalStories.sorted(Comparator.<Story>naturalOrder());
      }
      this.availableStoriesList.setItems(availableStories.sorted(Comparator.<Story>naturalOrder()));
      this.allocatedStoriesList.setItems(allocatedStories.sorted(Comparator.<Story>naturalOrder()));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the add button for adding a story to a backlog.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnAddStoryClick(ActionEvent event) {
    try {
      Story selectedStory = availableStoriesList.getSelectionModel().getSelectedItem();
      if (selectedStory != null) {
        this.allocatedStories.add(selectedStory);
        this.availableStories.remove(selectedStory);

        // TODO: estimates

        this.allocatedStoriesList.getSelectionModel().select(selectedStory);
        if (createOrEdit == CreateOrEdit.EDIT) {
          checkButtonDisabled();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles when the remove button is clicked for removing a story from a backlog.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnRemoveStoryClick(ActionEvent event) {
    try {
      Story selectedStory = allocatedStoriesList.getSelectionModel().getSelectedItem();

      if (selectedStory != null) {
        this.availableStories.add(selectedStory);
        this.allocatedStories.remove(selectedStory);
        if (createOrEdit == CreateOrEdit.EDIT) {
          checkButtonDisabled();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
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
      undoRedoObject.setAction(Action.BACKLOG_CREATE);
    } else {
      undoRedoObject.setAction(Action.BACKLOG_EDIT);
      undoRedoObject.addDatum(lastBacklog);
    }

    // Store a copy of backlog to edit in stack to avoid reference problems
    undoRedoObject.setAgileItem(backlog);
    Backlog backlogToStore = new Backlog(backlog);
    undoRedoObject.addDatum(backlogToStore);

    return undoRedoObject;
  }


  /**
   * Handles the confirm click by error checking the inputs and if it passes then creating and
   * adding the object
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnConfirmClick(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    int noErrors = 0;

    String backlogLabel = "";
    String backlogName = backlogNameField.getText().trim();
    String backlogDescription = backlogDescriptionField.getText().trim();
    Person productOwner = backlogProductOwnerCombo.getValue();

    try {
      backlogLabel = parseBacklogLabel(backlogLabelField.getText());
    } catch (Exception e) {
      noErrors++;
      errors.append(String.format("%s\n", e.getMessage()));
    }
    if (productOwner == null) {
      noErrors++;
      errors.append("No product owner has been selected for backlog.\n");
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
        backlog = new Backlog(backlogLabel, backlogName, backlogDescription, productOwner);
        backlog.addAllStories(allocatedStories);
        mainApp.addBacklog(backlog);
        if (Settings.correctList(backlog)) {
          mainApp.refreshList(backlog);
        }
      } else if (createOrEdit == CreateOrEdit.EDIT) {

        backlog.setLabel(backlogLabel);
        backlog.setBacklogName(backlogName);
        backlog.setBacklogDescription(backlogDescription);
        backlog.setProductOwner(productOwner);
        backlog.removeAllStories();
        backlog.addAllStories(allocatedStories);
        mainApp.refreshList(backlog);
      }
      UndoRedoObject undoRedoObject = generateUndoRedoObject();
      mainApp.newAction(undoRedoObject);
      thisStage.close();
    }
  }

  /**
   * Handles when the cancel button is clicked by not applying changes and closing dialog.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

  /**
   * Checks if backlog label field contains valid input.
   *
   * @param inputBacklogLabel String backlog label.
   * @return backlog label if backlog label is valid.
   * @throws Exception If backlog label is not valid.
   */
  private String parseBacklogLabel(String inputBacklogLabel) throws Exception {
    inputBacklogLabel = inputBacklogLabel.trim();

    if (inputBacklogLabel.isEmpty()) {
      throw new Exception("Backlog Label is empty.");
    } else {
      String lastBacklogLabel;
      if (lastBacklog == null) {
        lastBacklogLabel = "";
      } else {
        lastBacklogLabel = lastBacklog.getLabel();
      }
      for (Backlog backlog : mainApp.getBacklogs()) {
        String backlogLabel = backlog.getLabel();
        if (backlog.getLabel().equalsIgnoreCase(inputBacklogLabel) &&
            !backlogLabel.equalsIgnoreCase(lastBacklogLabel)) {
          throw new Exception("Backlog Label is not unique.");
        }
      }
      return inputBacklogLabel;
    }
  }

}
