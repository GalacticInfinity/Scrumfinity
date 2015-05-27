package seng302.group5.controller;

import sun.security.x509.AVA;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.AgileItem;
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
  private ObservableList<AgileItem> chosenAvailableItems = FXCollections.observableArrayList();
  private ObservableList<AgileItem> chosenSelectedItems = FXCollections.observableArrayList();

  /**
   * Setup the report DialogController
   *
   * @param thisStage    The stage of the dialog
   */
  public void setupController(Main mainApp, Stage thisStage) {
    this.report = new ReportWriter();
    this.mainApp = mainApp;
    this.thisStage = thisStage;
    reportLevelCombo.setItems(reportLevels);
    String os = System.getProperty("os.name");
    selectedItemsList.setDisable(true);
    availableItemsList.setDisable(true);
    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(saveBtn);
      btnContainer.getChildren().add(saveBtn);
    }
    initialiseLists();

    reportLevelCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
      setLevel();
    });
  }

  private void initialiseLists() {
    this.reportLevels.addAll("All", "Projects", "Team", "Person", "Skill", "Release", "Story"); // Add backlogs when they are done.
    availableItemsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    selectedItemsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    availableItemsList.setItems(availableItems);
    this.reportLevelCombo.setValue("All");
    selectedItemsList.setItems(selectedItems);

  }

  private void setLevel() {
    String level = reportLevelCombo.getSelectionModel().getSelectedItem().toString();
    switch(level) {
      case ("All"):
        availableItemsList.setItems(null);
        updateLists();
        break;
      case ("Projects"):
        this.availableItems.clear();
        this.availableItems.setAll(mainApp.getProjects());
        this.availableItemsList.setItems(availableItems);
        updateLists();
        break;
      case ("Team"):
        this.availableItems.clear();
        this.availableItems.setAll(mainApp.getTeams());
        this.availableItemsList.setItems(availableItems);
        updateLists();
        break;
      case ("Person"):
        this.availableItems.clear();
        this.availableItems.setAll(mainApp.getPeople());
        this.availableItemsList.setItems(availableItems);
        updateLists();
        break;
      case ("Skill"):
        this.availableItems.clear();
        this.availableItems.setAll(mainApp.getSkills());
        this.availableItemsList.setItems(availableItems);
        updateLists();
        break;
      case ("Release"):
        this.availableItems.clear();
        this.availableItems.setAll(mainApp.getReleases());
        this.availableItemsList.setItems(availableItems);
        updateLists();
        break;
      case ("Story"):
        this.availableItems.clear();
        this.availableItems.setAll(mainApp.getStories());
        this.availableItemsList.setItems(availableItems);
        updateLists();
        break;
    }
  }

  private void updateLists(){
    if (reportLevelCombo.getSelectionModel().getSelectedItem().toString().equals("All")) {
      selectedItemsList.setDisable(true);
      availableItemsList.setDisable(true);
    } else {
      selectedItemsList.setDisable(false);
      availableItemsList.setDisable(false);
    }

    for (AgileItem item: selectedItems){
      if (availableItems.contains(item)) {
        availableItems.remove(item);
      }
    }
  }

  public void addBtnClick(ActionEvent actionEvent) throws  Exception{
    try {
    chosenAvailableItems.setAll(availableItemsList.getSelectionModel().getSelectedItems());
      if (!chosenAvailableItems.equals(null)) {
        selectedItems.addAll(availableItemsList.getSelectionModel().getSelectedItems());
        updateLists();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    }

  public void removeBtnClick(ActionEvent actionEvent) throws Exception {
    try {
      chosenSelectedItems.setAll(selectedItemsList.getSelectionModel().getSelectedItems());
      if (!chosenSelectedItems.equals(null)) {
        selectedItems.removeAll(selectedItemsList.getSelectionModel().getSelectedItems());
        Object temp = reportLevelCombo.getSelectionModel().getSelectedItem();
        reportLevelCombo.setValue("All");
        reportLevelCombo.setValue(temp);
        updateLists();
      }
    } catch (Exception e) {
    }
  }

  public void cancelBtnClick(ActionEvent actionEvent) {
    thisStage.close();
  }

  public void saveBtnClick(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Report");
    File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

    if (file != null) {
      ReportWriter report = new ReportWriter();
      report.writeReport(mainApp, file);


    }
  }


}


