package seng302.group5.controller.dialogControllers;

import org.mockito.cglib.core.Local;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Date;
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
import seng302.group5.model.Status;
import seng302.group5.model.Story;
import seng302.group5.model.Task;

/**
 * Created by Craig Barnard on 10/09/2015.
 */
public class BurndownController {

  @FXML private ComboBox<Backlog> backlogCombo;
  @FXML private ComboBox<Sprint> sprintCombo;
  @FXML private LineChart<LocalDate, Integer> burndownChart;
  @FXML private VBox holder;


  private Main mainApp;
  private Stage stage;

  private ObservableList<Sprint> availableSprints;
  private ObservableList<Effort> allEffort;
  private ObservableList<Backlog> availableBacklogs;
  private  ObservableList<Task> doneTasks;

  private Sprint sprint;
  private Integer time;

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
    allEffort = FXCollections.observableArrayList();
    doneTasks = FXCollections.observableArrayList();
    availableBacklogs.addAll(this.mainApp.getBacklogs());
    ArrayList<Task> tasks = new ArrayList<Task>();
    backlogCombo.getSelectionModel().clearSelection();
    sprintCombo.getSelectionModel().clearSelection();
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
          allEffort.clear();
          tasks.clear();

          if (sprintCombo.getSelectionModel().getSelectedItem() != null) {
            sprint = sprintCombo.getValue();
            tasks.addAll(sprint.getTasks());
            if (newSprint != null) {
              burndownChart.setTitle(sprintCombo.getSelectionModel().getSelectedItem().toString());
              for (Story story : newSprint.getSprintStories()) {
                if (mainApp.getStories().contains(story)) {
                  tasks.addAll(story.getTasks());
                }
              }
              XYChart.Series<LocalDate, Integer> aSeries = new XYChart.Series<LocalDate, Integer>();
              XYChart.Series<Integer, LocalDate> cSeries = new XYChart.Series<Integer, LocalDate>();
              cSeries.setName("Burn-Up");
              aSeries.setName("Burn-Down");

              time = 0;
              for (Task task : tasks) {
                time += task.getTaskEstimation();
                if (task.getEfforts() != null) {
                  allEffort.addAll(task.getEfforts());
                }
                if (task.getStatus().equals(Status.DONE)) {
                  doneTasks.add(task);
                }

                }
              burndownChart.getData().clear();
              System.out.println(burndownChart.getYAxis().autoRangingProperty().getValue());
              burndownChart.setData(getChartData(time));

          }
        }
        }
    );

  }

  private ObservableList<XYChart.Series<LocalDate, Integer>> getChartData(Integer time) {
    int diff = 0;
    int days = 0;
    double timeDiff = 0;

    ObservableList<XYChart.Series<LocalDate, Integer>> answer = FXCollections.observableArrayList();
    answer.clear();

    XYChart.Series<LocalDate, Integer> aSeries = new XYChart.Series<LocalDate, Integer>();
    XYChart.Series<LocalDate, Integer> bSeries = new XYChart.Series<LocalDate, Integer>();
    XYChart.Series<LocalDate, Integer> cSeries = new XYChart.Series<LocalDate, Integer>();
    aSeries.setName("Reference Velocity");
    bSeries.setName("Burn-Down");
    cSeries.setName("Burn-Up");
    LocalDate date = sprint.getSprintStart();
    LocalDate date1 = sprint.getSprintEnd();
    LocalDate date2 = sprint.getSprintStart();

    if (date.getYear() == date1.getYear()) {
      days = date1.getDayOfYear() - date.getDayOfYear();
    } else {
      days = (365-date.getDayOfYear()) + date1.getDayOfYear();
    }
    Double i = time+ 0.0;
    if (days > 0) {
      timeDiff = (time+0.0) / days;
    }
    else if (days < 0) {
      days = days* -1;
      timeDiff = (time+ 0.0) / days;
    }
    if (timeDiff != 0) {
      int burnUp = 0;
      for (Integer day = days; day >= 0; day -= 1) {
        aSeries.getData().add(new XYChart.Data(date2.toString(), i));

        i = i - timeDiff;
        for (Effort effort : allEffort) {
          if (effort.getDateTime().getDayOfYear() == date2.getDayOfYear()) {
            cSeries.getData().add(new XYChart.Data(date2.toString(), burnUp));
            burnUp += effort.getSpentEffort();
          }
        }
        date2 = date2.plusDays(1);
      }
      for (Task doneTask : doneTasks) {
        bSeries.getData().add(new XYChart.Data(doneTask.getDoneDate().toString(),
                                               time - doneTask.getTaskEstimation()));
      }
    }
    answer.addAll(aSeries, bSeries, cSeries);
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
