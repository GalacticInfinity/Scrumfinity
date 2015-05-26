package seng302.group5.controller;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Backlog;
import seng302.group5.model.Story;

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

  @FXML private TextField backlogLabelField;
  @FXML private TextArea backlogDescriptionField;
  @FXML private HBox btnContainer;
  @FXML private Button btnCreateBacklog;
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
      btnContainer.getChildren().remove(btnCreateBacklog);
      btnContainer.getChildren().add(btnCreateBacklog);
    }

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Team");
      btnCreateBacklog.setText("Create");

      initialiseLists(CreateOrEdit.CREATE, backlog);
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Team");
      btnCreateBacklog.setText("Save");
      btnCreateBacklog.setDisable(true);
      backlogLabelField.setText(backlog.getLabel());
      initialiseLists(CreateOrEdit.EDIT, backlog);
      backlogDescriptionField.setText(backlog.getBacklogDescription());
    }
    this.createOrEdit = createOrEdit;

    if (backlog != null) {
      this.backlog = backlog;
      this.lastBacklog = new Backlog(backlog);
    } else {
      this.backlog = null;
      this.lastBacklog = null;
    }

    comboListenerFlag = false;  // if true, assign the selected role in combo box
    //lastSelectedEstimate = new Estimate();

    btnCreateBacklog.setDefaultButton(true);
    thisStage.setResizable(false);

    // Handle TextField text changes.
    backlogLabelField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        //checkButtonDisabled();
      }
      if (newValue.trim().length() > 20) {
        backlogLabelField.setStyle("-fx-text-inner-color: red;");
      } else {
        backlogLabelField.setStyle("-fx-text-inner-color: black;");
      }
    });

    backlogDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if (createOrEdit == CreateOrEdit.EDIT) {
        //checkButtonDisabled();
      }
    });
  }

  /**
   * Check if any of the fields has been changed. If nothing changed, then confirm button is disabled.
   */
//  private void checkButtonDisabled() {  TODO
//    if (backlogLabelField.getText().equals(backlog.getLabel()) &&
//        backlogDescriptionField.getText().equals(backlog.getBacklogDescription()) &&
//        allocatedStoriesList.getItems().toString().equals(originalMembers.toString())){
//      btnConfirm.setDisable(true);
//    } else {
//      btnConfirm.setDisable(false);
//    }
//  }

  /**
   * Populates the people selection lists for assigning people to the team.
   *
   * @param createOrEdit the enum object to decide if it is creating or editing
   * @param backlog      the Team that is being created or edited
   */
  private void initialiseLists(CreateOrEdit createOrEdit, Backlog backlog) {
    availableStories = FXCollections.observableArrayList();
    allocatedStories = FXCollections.observableArrayList();

    try {
      Set<Story> storiesInUse = new HashSet<>();
      for (Backlog mainBacklog : mainApp.getBacklogs()) {
        storiesInUse.addAll(mainBacklog.getStories());
      }
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
//          originalMembers.add(new PersonRole(person, role));
        }
//        originalMembers = originalMembers.sorted(Comparator.<PersonRole>naturalOrder());
      }
      this.availableStoriesList
          .setItems(availableStories.sorted(Comparator.<Story>naturalOrder()));
      this.allocatedStoriesList.setItems(allocatedStories.sorted(Comparator.<Story>naturalOrder()));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}