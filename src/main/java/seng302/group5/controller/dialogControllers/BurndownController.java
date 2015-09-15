package seng302.group5.controller.dialogControllers;

import org.mockito.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.model.Backlog;
import seng302.group5.model.Effort;
import seng302.group5.model.Sprint;
import seng302.group5.model.Story;
import seng302.group5.model.Task;

/**
 * Created by Craig Barnard on 10/09/2015.
 */
public class BurndownController {

  @FXML private ComboBox<Backlog> backlogCombo;
  @FXML private ComboBox<Sprint> sprintCombo;
  @FXML private LineChart<Integer, LocalDate> burndownChart;
  @FXML private VBox holder;


  private Main mainApp;
  private Stage stage;

  private ObservableList<Sprint> availableSprints;
  private ObservableList<Backlog> availableBacklogs;

  //TODO Javadoc

  /**
   * Sets up the burndown controller.
   *
   * @param mainApp the mainApp that contains all backlogs and sprints
   * @param stage the stage.
   */
  public void setupController(Main mainApp, Stage stage) {
    this.mainApp = mainApp;
    this.stage = stage;
    sprintCombo.setDisable(true);
    burndownChart.setPrefWidth(stage.getWidth());
    initialiseLists();
  }

  /**
   * initialise the lists with the available backlogs and sprints.
   */
  private void initialiseLists() {
    availableSprints = FXCollections.observableArrayList();
    availableBacklogs = FXCollections.observableArrayList();
    availableBacklogs.addAll(this.mainApp.getBacklogs());

    backlogCombo.getSelectionModel().clearSelection();
    backlogCombo.setItems(mainApp.getBacklogs());
    burndownChart.setMinWidth(stage.getWidth()-200);
    burndownChart.setMinHeight(stage.getHeight()-200);

    backlogCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldBacklog, newBacklog) -> {
          if (backlogCombo.getSelectionModel().getSelectedItem() != null) {
            sprintCombo.setDisable(false);

            // get backlog's sprints
            availableSprints.setAll(mainApp.getSprints().stream()
                                        .filter(
                                            sprint -> sprint.getSprintBacklog().equals(newBacklog))
                                        .collect(Collectors.toList()));
            sprintCombo.setItems(null);
            sprintCombo.setItems(availableSprints);
            //sprintCombo.setDisable(true);
          }
        }
    );

    // Resizes the linecharts height when the main stage is resized.
    stage.heightProperty().addListener((observable, oldValue, newValue) -> {
      burndownChart.setMinHeight(stage.getHeight() - 200);
    });

    // Resizes the linecharts width when the main stage is resized.
    stage.widthProperty().addListener((observable, oldValue, newValue) -> {
      burndownChart.setMinWidth(stage.getWidth() - 210);
    });

    sprintCombo.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldSprint, newSprint) -> {

          ArrayList<Task> tasks = new ArrayList<Task>();
          tasks.addAll(sprintCombo.getSelectionModel().getSelectedItem().getTasks());
          if (newSprint != null) {
            burndownChart.setTitle(sprintCombo.getSelectionModel().getSelectedItem().toString());
            for (Story story : newSprint.getSprintStories()) {
              if (mainApp.getStories().contains(story)) {
                tasks.addAll(story.getTasks());
                //TODO set the sprints burndown chart here
              }
            }
            XYChart.Series<Integer, LocalDate> aSeries = new XYChart.Series<Integer, LocalDate>();
            XYChart.Series<String, LocalDate> cSeries = new XYChart.Series<String, LocalDate>();
            aSeries.setName("a");
            cSeries.setName("C");
            ObservableList<XYChart.Series<Integer, LocalDate>> x;
            burndownChart.setData(getChartData());

            for (Task task : tasks) {
              for (Effort efforts : task.getEfforts()) {

              }
            }

          }
        }
    );

  }

  private ObservableList<XYChart.Series<Integer, LocalDate>> getChartData() {
    double aValue = 1.56;
    double cValue = 1.06;

    ObservableList<XYChart.Series<Integer, LocalDate>> answer = FXCollections.observableArrayList();

    XYChart.Series<Integer, LocalDate> aSeries = new XYChart.Series<Integer, LocalDate>();
    XYChart.Series<String, LocalDate> cSeries = new XYChart.Series<String, LocalDate>();
    aSeries.setName("a");
    cSeries.setName("C");

    for (Integer i = 2011; i < 2021; i++) {
      aSeries.getData().add(new XYChart.Data(Integer.toString(i), aValue));
      aValue = aValue + Math.random() - .5;
      cSeries.getData().add(new XYChart.Data(Integer.toString(i), LocalDate.now()));
      cValue = cValue + Math.random() - .5;
    }
    answer.add(aSeries);
    //answer.addAll(cSeries, aSeries);
    return answer;
  }
  /**
   * Refresh the comboboxes whenever objects are modified in the main app
   */
  public void refreshComboBoxes() {
    Backlog backlog = backlogCombo.getValue();
    backlogCombo.getSelectionModel().clearSelection();
    Sprint sprint = sprintCombo.getValue();
    sprintCombo.getSelectionModel().clearSelection();

    //initialiseLists();

    sprintCombo.setDisable(true);

    if (mainApp.getBacklogs().contains(backlog)) {
      backlogCombo.setValue(backlog);

      if(availableSprints.contains(sprint)){
        sprintCombo.setValue(sprint);
      } else {
        sprintCombo.setValue(null);
      }
    } else {
      backlogCombo.setValue(null);
      sprintCombo.setValue(null);
    }
  }



}
