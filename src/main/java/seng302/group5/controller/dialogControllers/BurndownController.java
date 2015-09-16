package seng302.group5.controller.dialogControllers;

import org.mockito.cglib.core.Local;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAmount;
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
  @FXML private LineChart<LocalDate, Integer> burndownChart;
  @FXML private VBox holder;


  private Main mainApp;
  private Stage stage;

  private ObservableList<Sprint> availableSprints;
  private ObservableList<Backlog> availableBacklogs;

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

          if (sprintCombo.getSelectionModel().getSelectedItem() != null) {
            sprint = sprintCombo.getValue();
            tasks.addAll(sprint.getTasks());
            if (newSprint != null) {
              burndownChart.setTitle(sprintCombo.getSelectionModel().getSelectedItem().toString());
              for (Story story : newSprint.getSprintStories()) {
                if (mainApp.getStories().contains(story)) {
                  tasks.addAll(story.getTasks());
                  //TODO set the sprints burndown chart here
                }
              }
              XYChart.Series<LocalDate, Integer> aSeries = new XYChart.Series<LocalDate, Integer>();
              XYChart.Series<Integer, LocalDate> cSeries = new XYChart.Series<Integer, LocalDate>();
              cSeries.setName("Burn-Up");
              aSeries.setName("Burn-Down");

              time = 0;
              for (Task task : tasks) {
                time += task.getTaskEstimation();
                for (Effort efforts : task.getEfforts()) {

                }
            }
              burndownChart.setData(getChartData(time));

          }
        }
        }
    );

  }

  private ObservableList<XYChart.Series<LocalDate, Integer>> getChartData(Integer time) {
    double aValue = 1.56;
    double cValue = 1.06;

    ObservableList<XYChart.Series<LocalDate, Integer>> answer = FXCollections.observableArrayList();

    XYChart.Series<LocalDate, Integer> aSeries = new XYChart.Series<LocalDate, Integer>();
    //XYChart.Series<Integer, String> cSeries = new XYChart.Series<Integer, String>();
    aSeries.setName("Reference Velocity");
    //cSeries.setName("Burn-Up");
    LocalDate date = sprint.getSprintStart();
    LocalDate date1 = sprint.getSprintStart();
    int diff = 0;
    while (date1.compareTo(sprint.getSprintEnd()) == -1) {
      System.out.println(date1 + "  " + sprint.getSprintEnd());
      date1 = date1.plusDays(1);
      diff += 1;
    }
    System.out.println(diff);
    int timeDiff = time/diff;
    System.out.println(timeDiff);
    for (Integer i = time; i >= 0; i -= timeDiff) {
      aSeries.getData().add(new XYChart.Data(date.toString(), i));
      date = date.plusDays(1);
    //  cSeries.getData().add(new XYChart.Data(Integer.toString(i), cValue));
    //  cValue = cValue + Math.random() - .2;
    }
    //answer.add(aSeries);
    answer.add(aSeries);
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
