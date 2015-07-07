package seng302.group5.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Backlog;
import seng302.group5.model.Estimate;
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

  private Estimate estimateScale;

  private boolean comboListenerFlag;

  private ObservableList<Story> availableStories;
  private ObservableList<StoryEstimate> allocatedStories;
  private ObservableList<StoryEstimate> originalStories;
  private ObservableList<Person> productOwners;
  private ObservableList<Estimate> estimates;

  @FXML private TextField backlogLabelField;
  @FXML private TextField backlogNameField;
  @FXML private TextArea backlogDescriptionField;
  @FXML private ComboBox<Person> backlogProductOwnerCombo;
  @FXML private HBox btnContainer;
  @FXML private Button btnConfirm;
  @FXML private ListView<Story> availableStoriesList;
  @FXML private ListView<StoryEstimate> allocatedStoriesList;
  @FXML private ComboBox<Estimate> backlogScaleCombo;
  @FXML private ComboBox<String> storyEstimateCombo;

  /**
   * Setup the backlog dialog controller
   *
   * @param mainApp      The main application object
   * @param thisStage    The stage of the dialog
   * @param createOrEdit If dialog is for creating or editing a Backlog
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

      estimateScale = null;
      initialiseLists(CreateOrEdit.CREATE, backlog);
      storyEstimateCombo.setDisable(true);
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Backlog");
      btnConfirm.setText("Save");
      btnConfirm.setDisable(true);
      estimateScale = backlog.getEstimate();
      initialiseLists(CreateOrEdit.EDIT, backlog);
      backlogLabelField.setText(backlog.getLabel());
      backlogNameField.setText(backlog.getBacklogName());
      backlogDescriptionField.setText(backlog.getBacklogDescription());
      backlogProductOwnerCombo.setValue(backlog.getProductOwner());
      backlogScaleCombo.setValue(backlog.getEstimate());

      ObservableList<String> estimateNames = FXCollections.observableArrayList();
      estimateNames.setAll(backlog.getEstimate().getEstimateNames());
      storyEstimateCombo.setItems(estimateNames);
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

    comboListenerFlag = false;

    backlogScaleCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          //Check if the listener should be changing the estimate scale or not
          if (comboListenerFlag) {
            comboListenerFlag = false;
            return;
          }
          Alert alert = null;
          if (!allocatedStories.isEmpty()) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm changing estimate scale");
            alert.setHeaderText(null);
            alert.setContentText("Changing scales may result in unexpected "
                                 + "side effects to existing story estimates. "
                                 + "Are you sure you want to continue?");
            alert.showAndWait();
          }
          if (alert == null || alert.getResult().equals(ButtonType.OK)) {
            storyEstimateCombo.setDisable(false);

            estimateScale = newValue;
            ObservableList<String> estimateNames = FXCollections.observableArrayList();
            estimateNames.setAll(estimateScale.getEstimateNames());
            storyEstimateCombo.setItems(estimateNames);

            for (StoryEstimate story : allocatedStories) {
              story.refreshEstimate();
            }

            allocatedStoriesList.setItems(null);
            allocatedStoriesList.setItems(allocatedStories);

            if (createOrEdit == CreateOrEdit.EDIT) {
              checkButtonDisabled();
            }
          } else {
            //To avoid firing the listener from within itself.
            comboListenerFlag = true;
            Platform.runLater(() -> {
              backlogScaleCombo.setValue(oldValue);
            });
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
        allocatedStories.toString().equals(originalStories.toString()) &&
        backlogScaleCombo.getValue().equals(backlog.getEstimate())){
      btnConfirm.setDisable(true);
    } else {
      btnConfirm.setDisable(false);
    }
  }

  /**
   * Populates the people selection lists for assigning people to the backlog.
   *
   * @param createOrEdit the enum object to decide if it is creating or editing
   * @param backlog      the Backlog that is being created or edited
   */
  private void initialiseLists(CreateOrEdit createOrEdit, Backlog backlog) {
    availableStories = FXCollections.observableArrayList();
    allocatedStories = FXCollections.observableArrayList();
    originalStories = FXCollections.observableArrayList();
    productOwners = FXCollections.observableArrayList();
    estimates = FXCollections.observableArrayList();

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

      for (Estimate estimate : mainApp.getEstimates()) {
        estimates.add(estimate);
      }

      this.backlogScaleCombo.setVisibleRowCount(5);
      this.backlogScaleCombo.setItems(estimates);

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
          int estimateIndex = backlog.getSizes().get(story);
          allocatedStories.add(new StoryEstimate(story, estimateIndex));
          originalStories.add(new StoryEstimate(story, estimateIndex));
        }
      }
      this.availableStoriesList.setItems(availableStories.sorted(Comparator.<Story>naturalOrder()));
      this.allocatedStoriesList.setItems(allocatedStories);

      storyEstimateCombo.getSelectionModel().selectedItemProperty().addListener(
          (observable, oldEstimate, selectedEstimate) -> {
            //Handle clear selection
            if (selectedEstimate == null) {
              return;
            }
            StoryEstimate selected = allocatedStoriesList.getSelectionModel().getSelectedItem();
            if (selected == null) {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("No story selected");
              alert.setHeaderText(null);
              alert.setContentText("Please select a story to give an estimate to.");
              alert.showAndWait();
            } else {
              selected.setEstimate(storyEstimateCombo.getSelectionModel().getSelectedIndex());
              allocatedStoriesList.setItems(null);
              allocatedStoriesList.setItems(allocatedStories);
              allocatedStoriesList.getSelectionModel().select(selected);
              if (createOrEdit == CreateOrEdit.EDIT) {
                checkButtonDisabled();
              }
            }
          }
      );

      allocatedStoriesList.getSelectionModel().selectedItemProperty().addListener(
          (observable, oldStory, selectedStory) -> {
            if (selectedStory == null) {
              return;
            } else {
              int estimateIndex = selectedStory.getEstimateIndex();
              storyEstimateCombo.getSelectionModel().select(estimateIndex);
            }
          }
      );

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
      if (estimateScale == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No estimation scale selected");
        alert.setHeaderText(null);
        alert.setContentText("No estimation scale has been selected for backlog.");
        alert.showAndWait();
        return;
      }
      Story selectedStory = availableStoriesList.getSelectionModel().getSelectedItem();
      if (selectedStory != null) {
        StoryEstimate storyEstimate = new StoryEstimate(selectedStory, 0);
        this.allocatedStories.add(storyEstimate);
        this.availableStories.remove(selectedStory);

        this.allocatedStoriesList.getSelectionModel().select(storyEstimate);
        this.storyEstimateCombo.getSelectionModel().select(0);

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
      StoryEstimate storyEstimate = allocatedStoriesList.getSelectionModel().getSelectedItem();

      if (storyEstimate != null) {
        Story selectedStory = storyEstimate.getStory();
        this.availableStories.add(selectedStory);
        this.allocatedStories.remove(storyEstimate);
        this.availableStoriesList.getSelectionModel().select(selectedStory);
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
    Estimate estimate = backlogScaleCombo.getValue();

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
    if (estimate == null) {
      noErrors++;
      errors.append("No estimation scale has been selected for backlog.\n");
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
        backlog = new Backlog(backlogLabel, backlogName, backlogDescription, productOwner, estimate);
        for (StoryEstimate storyEstimate : allocatedStories) {
          backlog.addStory(storyEstimate.getStory(), storyEstimate.getEstimateIndex());
        }
        mainApp.addBacklog(backlog);
        if (Settings.correctList(backlog)) {
          mainApp.refreshList(backlog);
        }
      } else if (createOrEdit == CreateOrEdit.EDIT) {

        backlog.setLabel(backlogLabel);
        backlog.setBacklogName(backlogName);
        backlog.setBacklogDescription(backlogDescription);
        backlog.setProductOwner(productOwner);
        backlog.setEstimate(estimate);
        backlog.removeAllStories();
        for (StoryEstimate storyEstimate : allocatedStories) {
          backlog.addStory(storyEstimate.getStory(), storyEstimate.getEstimateIndex());
        }
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

  /**
   * Move the selected story up in priority.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  private void btnIncreasePriorityClick(ActionEvent event) {
    int storyIndex = allocatedStoriesList.getSelectionModel().getSelectedIndex();
    int before = storyIndex - 1;
    if (storyIndex > 0) {
      Collections.swap(allocatedStories, before, storyIndex);
      allocatedStoriesList.getSelectionModel().select(before);
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    }
  }

  /**
   * Move the selected story down in priority.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  private void btnDecreasePriorityClick(ActionEvent event) {
    int storyIndex = allocatedStoriesList.getSelectionModel().getSelectedIndex();
    int after = storyIndex + 1;
    if (storyIndex >= 0 && storyIndex < allocatedStories.size() - 1) {
      Collections.swap(allocatedStories, after, storyIndex);
      allocatedStoriesList.getSelectionModel().select(after);
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    }
  }

  /**
   * Inner class for storing/displaying allocated stories with their respective estimates
   */
  private class StoryEstimate {

    private Story story;
    private int estimateIndex;
    private String estimate;

    /**
     * Constructor for StoryEstimate object
     *
     * @param story Story to store
     * @param estimateIndex Index of estimate to store
     */
    public StoryEstimate(Story story, int estimateIndex) {
      this.story = story;
      this.estimateIndex = estimateIndex;
      this.estimate = estimateScale.getEstimateNames().get(estimateIndex);
    }

    /**
     * gets the story object from this object
     *
     * @return Story story
     */
    public Story getStory() {
      return story;
    }

    /**
     * gets the estimate index from this object
     *
     * @return int index
     */
    public int getEstimateIndex() {
      return estimateIndex;
    }

    /**
     * Set the estimate to this object
     *
     * @param estimateIndex index of estimate
     */
    public void setEstimate(int estimateIndex) {
      this.estimateIndex = estimateIndex;
      this.estimate = estimateScale.getEstimateNames().get(estimateIndex);
    }

    /**
     * Refreshes the story estimate string.
     */
    public void refreshEstimate() {
      //Truncates the estimate index to match the maximum value of the new estimate scale.
      if (estimateIndex > estimateScale.getEstimateNames().size() - 1) {
        estimateIndex = estimateScale.getEstimateNames().size() - 1;
      }
      this.estimate = estimateScale.getEstimateNames().get(estimateIndex);
    }

    @Override
    public String toString() {
      return story.toString() + "  -  " + estimate;
    }
  }
}
