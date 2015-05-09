package seng302.group5.controller;

import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Person;
import seng302.group5.model.Skill;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;

/**
 * The controller for the person dialog when creating a new person or editing an existing one.
 *
 * Created by Zander on 18/03/2015.
 */
public class PersonDialogController {

  @FXML private TextField personLabelField;
  @FXML private TextField personFirstNameField;
  @FXML private TextField personLastNameField;
  @FXML private Button btnCreatePerson;
  @FXML private Button btnCancel;
  @FXML private ListView<Skill> skillsList;
  @FXML private ListView<Skill> personSkillList;
  @FXML private HBox btnContainer;

  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Person person;
  private Person lastPerson;

  private ObservableList<Skill> availableSkills = FXCollections.observableArrayList();
  private ObservableList<Skill> selectedSkills = FXCollections.observableArrayList();


  /**
   * Setup the person dialog controller
   *
   * @param mainApp      The main application object
   * @param thisStage    The stage of the dialog
   * @param createOrEdit If dialog is for creating or editing a person
   * @param person       The person object if editing, null otherwise
   */
  public void setupController(Main mainApp,
                              Stage thisStage,
                              CreateOrEdit createOrEdit,
                              Person person) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      Button confirmBtn = (Button) btnContainer.getChildren().get(1);
      btnContainer.getChildren().remove(1);
      btnContainer.getChildren().add(confirmBtn);
    }

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Person");
      btnCreatePerson.setText("Create");

      initialiseLists();
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Person");
      btnCreatePerson.setText("Save");

      personLabelField.setText(person.getLabel());
      personFirstNameField.setText(person.getFirstName());
      personLastNameField.setText(person.getLastName());
      selectedSkills = FXCollections.observableArrayList(person.getSkillSet());
      personSkillList.setItems(selectedSkills);
      initialiseLists();
      btnCreatePerson.setDisable(true);
    }
    this.createOrEdit = createOrEdit;

    if (person != null) {
      this.person = person;
      this.lastPerson = new Person(person);
    } else {
      this.person = null;
      this.lastPerson = null;
    }

    btnCreatePerson.setDefaultButton(true);

    // Handle TextField text changes.
    personLabelField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
      if (newValue.trim().length() > 20) {
        personLabelField.setStyle("-fx-text-inner-color: red;");
      } else {
        personLabelField.setStyle("-fx-text-inner-color: black;");
      }
    });

    personFirstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    personLastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });

    skillsList.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          //For disabling the button
          if (createOrEdit == CreateOrEdit.EDIT) {
            checkButtonDisabled();
          }
        });

    personSkillList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      //For disabling the button
      if(createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }
    });
  }

  /**
   * checks if there are any changed fields and disables or enables the button accordingly
   */
  private void checkButtonDisabled() {
    if (personLabelField.getText().equals(person.getLabel()) &&
        personFirstNameField.getText().equals(person.getFirstName()) &&
        personLastNameField.getText().equals(person.getLastName()) &&
        personSkillList.getItems().equals(person.getSkillSet())) {
      btnCreatePerson.setDisable(true);
    } else {
      btnCreatePerson.setDisable(false);
    }
  }

  /**
   * Generate an UndoRedoObject to place in the stack
   *
   * @return the UndoRedoObject to store
   */
  private UndoRedoObject generateUndoRedoObject() {
    UndoRedoObject undoRedoObject = new UndoRedoObject();

    if (createOrEdit == CreateOrEdit.CREATE) {
      undoRedoObject.setAction(Action.PERSON_CREATE);
    } else {
      undoRedoObject.setAction(Action.PERSON_EDIT);
      undoRedoObject.addDatum(lastPerson);
    }

    // Store a copy of person to edit in stack to avoid reference problems
    undoRedoObject.setAgileItem(person);
    Person personToStore = new Person(person);
    undoRedoObject.addDatum(personToStore);

    return undoRedoObject;
  }

  /**
   * Creates a new Person from the textfield data on click of 'Create' button.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCreatePersonClick(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    int noErrors = 0;

    String personLabel = "";
    String personFirstName = personFirstNameField.getText().trim();
    String personLastName = personLastNameField.getText().trim();
    ObservableList<Skill> personSkillSet =
        FXCollections.observableArrayList(personSkillList.getItems());

    try {
      personLabel = parsePersonLabel(personLabelField.getText());
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
        person = new Person(personLabel, personFirstName, personLastName, personSkillSet);
        mainApp.addPerson(person);
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        person.setLabel(personLabel);
        person.setFirstName(personFirstName);
        person.setLastName(personLastName);
        person.setSkillSet(personSkillSet);
        mainApp.refreshList();
      }

      UndoRedoObject undoRedoObject = generateUndoRedoObject();
      mainApp.newAction(undoRedoObject);

      thisStage.close();
    }
  }

  /**
   * Closes the CreatePerson dialog box in click of 'Cancel' button.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

  /**
   * Checks that the Person label entry box contains valid input.
   *
   * @param inputPersonLabel Person label from entry field.
   * @return Person label if label is valid.
   * @throws Exception Any invalid input.
   */
  private String parsePersonLabel(String inputPersonLabel) throws Exception {
    inputPersonLabel = inputPersonLabel.trim();

    if (inputPersonLabel.isEmpty()) {
      throw new Exception("Person Label is empty.");
    } else {
      String lastPersonLabel;
      if (lastPerson == null) {
        lastPersonLabel = "";
      } else {
        lastPersonLabel = lastPerson.getLabel();
      }
      for (Person personInList : mainApp.getPeople()) {
        String personLabel = personInList.getLabel();
        if (personLabel.equalsIgnoreCase(inputPersonLabel) &&
            !personLabel.equalsIgnoreCase(lastPersonLabel)) {
          throw new Exception("Person Label is not unique.");
        }
      }
    }
    return inputPersonLabel;
  }

  /**
   * Populates a list of available skills for assigning them to people
   */
  private void initialiseLists() {
    try {

      // loop for adding the skills that you can assign to someone.
      for (Skill item : mainApp.getSkills()) {
        if (!selectedSkills.contains(item)) {
          availableSkills.add(item);
        }
      }

      this.skillsList.setItems(availableSkills.sorted(Comparator.<Skill>naturalOrder()));
      this.personSkillList.setItems(selectedSkills.sorted(Comparator.<Skill>naturalOrder()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds in a skill to the person once add is click and a skill is selected.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnAddSkillClick(ActionEvent event) {
    try {
      Skill selectedSkill = skillsList.getSelectionModel().getSelectedItem();
      if (selectedSkill != null) {
        this.selectedSkills.add(selectedSkill);
        this.availableSkills.remove(selectedSkill);

        this.skillsList.getSelectionModel().clearSelection();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * removes the selected skill from the person.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnRemoveSkillClick(ActionEvent event) {
    try {
      Skill selectedSkill = personSkillList.getSelectionModel().getSelectedItem();

      if (selectedSkill != null) {
        this.availableSkills.add(selectedSkill);
        this.selectedSkills.remove(selectedSkill);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
