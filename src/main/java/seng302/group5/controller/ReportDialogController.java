package seng302.group5.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.AgileHistory;
import seng302.group5.model.AgileItem;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Team;
import seng302.group5.model.util.ReportWriter;

/**
 * Created by Craig barnard on 26/05/2015.
 * Class that controls the report dialog which is used to create parameterised reports.
 */
public class ReportDialogController {

  @FXML private ComboBox reportLevelCombo;
  @FXML private ListView availableItemsList;
  @FXML private ListView selectedItemsList;
  @FXML private Button addBtn;
  @FXML private Button removeBtn;
  @FXML private Button saveBtn;
  @FXML private Button cancelBtn;
  @FXML private HBox btnContainer;

  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private ObservableList<AgileItem> selectedItems = FXCollections.observableArrayList();
  private ObservableList<AgileItem> availableItems = FXCollections.observableArrayList();
  private ObservableList<String> reportLevels = FXCollections.observableArrayList();
  private ReportWriter report;

  /**
   * Setup the report DialogController
   *
   * @param mainApp      The main application object
   * @param thisStage    The stage of the dialog
   */
  public void setupController(Main mainApp,
                              Stage thisStage) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(saveBtn);
      btnContainer.getChildren().add(saveBtn);
    }
    initialiseLists();
  }

  private void initialiseLists() {
    reportLevels.addAll("All", "Projects", "Team", "Person", "Skill", "Release", "Story"); // Add backlogs when they are done.
    reportLevelCombo.setItems(reportLevels);
    report.setLists();
    availableItemsList.setItems(report.getProjectItems());

    }
  }

