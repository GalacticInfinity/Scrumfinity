package seng302.group5.controller.dialogControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Sprint;
import seng302.group5.model.Status;
import seng302.group5.model.Story;
import seng302.group5.model.Task;
import seng302.group5.model.Team;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.CompositeUndoRedo;
import seng302.group5.model.undoredo.UndoRedo;
import seng302.group5.model.undoredo.UndoRedoObject;

/**
 * Controller class for a single storyPane for the accordion in the scrum board.
 */
public class StoryItemController {

  @FXML private VBox notStartedList;
  @FXML private VBox inProgressList;
  @FXML private VBox verifyList;
  @FXML private VBox doneList;

  @FXML private AnchorPane storyAnchor;
  @FXML private TitledPane storyPane;

  @FXML private ImageView SBImage;
  @FXML private Rectangle doneBar;
  @FXML private Rectangle inProgBar;
  @FXML private Rectangle notStartedBar;

  private Main mainApp;
  private Story story;
  private Sprint sprint;
  private Accordion accordion;

  private Image inprogress;
  private Image complete;
  private Image notStarted;
  private ImageView dinoGif;

  private ObservableList<Task> notStartedTasks;
  private ObservableList<Task> inProgressTasks;
  private ObservableList<Task> verifyTasks;
  private ObservableList<Task> doneTasks;

  /**
   * This function sets up the story item controller.
   * @param story       The story being displayed in this.
   */
  public void setupController(Story story, Main mainApp, Accordion accordion, Sprint sprint) {
    this.story = story;
    this.mainApp = mainApp;
    this.accordion = accordion;
    this.sprint = sprint;
    storyPane.setText(story.getLabel());

    //Set up the dino gif and place it on the accordion
    inprogress = new Image("runningDino.gif");
    complete = new Image("victoryDino.gif");
    notStarted = new Image("progressDino1.png");
    dinoGif = new ImageView();

    dinoGif.setFitHeight(28);
    dinoGif.setFitWidth(28);

    updateDino();

    updateProgBar();

    setupLists();
  }

  public void updateDino() {
    if (this.story.percentComplete() == 0.0 && this.story.percentInProg() == 0.0) {
      dinoGif.setImage(notStarted);
      SBImage.setImage(notStarted);
    }else if (this.story.percentComplete() == 1.0) {
      dinoGif.setImage(complete);
      SBImage.setImage(complete);
    } else {
      dinoGif.setImage(inprogress);
      SBImage.setImage(inprogress);

    }
    storyPane.setGraphic(dinoGif);
  }


  public void updateProgBar() {
    int totalSpace = 180;
    float doneSpace = totalSpace*story.percentComplete();
    float inProgSpace = totalSpace*story.percentInProg();
    float notStartedSpace = totalSpace*(1 - (story.percentComplete() + story.percentInProg()));

    doneBar.setWidth(doneSpace);
    inProgBar.setWidth(inProgSpace);
    notStartedBar.setWidth(notStartedSpace);
  }

  /**
   * Sets the tasks from story into their appropriate lists.
   */
  public void setupLists() {
    notStartedTasks = FXCollections.observableArrayList();
    inProgressTasks = FXCollections.observableArrayList();
    verifyTasks = FXCollections.observableArrayList();
    doneTasks = FXCollections.observableArrayList();
    notStartedList.getChildren().clear();
    inProgressList.getChildren().clear();
    verifyList.getChildren().clear();
    doneList.getChildren().clear();
    for (Task task : story.getTasks()) {
      switch (task.getStatus()) {
        case NOT_STARTED:
          notStartedTasks.add(task);
          addWithDragging(notStartedList, new Label(task.getLabel()));
          // TODO this line specifically
          break;
        case IN_PROGRESS:
          inProgressTasks.add(task);
          addWithDragging(inProgressList, new Label(task.getLabel()));
          break;
        case VERIFY:
          verifyTasks.add(task);
          addWithDragging(verifyList, new Label(task.getLabel()));
          break;
        case DONE:
          doneTasks.add(task);
          addWithDragging(doneList, new Label(task.getLabel()));
          break;
      }
    }

    // in case user drops node in blank space in root:
    notStartedList.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        if (checkVBox(event)) {
          addToBottom(notStartedList, (Node) event.getGestureSource());
          generateUndoRedoObject(event, Status.NOT_STARTED);
          updateDino();
          updateProgBar();
        }
      }
    });

    inProgressList.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        if (checkVBox(event)) {
          addToBottom(inProgressList, (Node) event.getGestureSource());
          generateUndoRedoObject(event, Status.IN_PROGRESS);
          updateDino();
          updateProgBar();

        }
      }
    });

    verifyList.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        if (checkVBox(event)) {
          addToBottom(verifyList, (Node) event.getGestureSource());
          generateUndoRedoObject(event, Status.VERIFY);
          updateDino();
          updateProgBar();
        }
      }
    });

    doneList.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        if (checkVBox(event)) {
          addToBottom(doneList, (Node) event.getGestureSource());
          generateUndoRedoObject(event, Status.DONE);
          updateDino();
          updateProgBar();
        }
      }
    });
  }

  /**
   * Adds the label to a VBox, and assigns mouse event listeners for drag and drop functionality
   * between the four VBoxes (notStartedList, inProgressList, verifyList, doneList).
   * Overrides drag detected, mouse drag detected, drag exited, drag released.
   * @param root VBox root.
   * @param label Label to be added
   */
  private void addWithDragging(VBox root, Label label) {
    label.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
          Label taskLabel = (Label) event.getSource();
          String taskString = taskLabel.getText();
          Task newTask = null;
          for (Task task : story.getTasks()) {
            if (taskString.equals(task.getLabel())) {
              newTask = task;
              break;
            }
          }
          if (newTask != null) {
            Team team = null;
            for (Sprint sprint : mainApp.getSprints()) {
              if (sprint.getSprintStories().contains(story)) {
                team = sprint.getSprintTeam();
              }
            }
            UndoRedo taskEdit = mainApp.showTaskDialog(story, newTask, team,
                                                       CreateOrEdit.EDIT, mainApp.getStage());
            if (taskEdit != null) {
              mainApp.newAction(taskEdit);
            }
            mainApp.getLMPC().getScrumBoardController().refreshTaskLists();
          }
        }
      }
    });

    label.setOnDragDetected(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        label.startFullDrag();
      }
    });

    // next two handlers just an idea how to show the drop target visually:
    label.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        if (checkSource(event) && !checkSameLabel(event)) {
          label.setStyle("-fx-background-color: #ffffa0;");
        }
      }
    });
    label.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        if (checkSource(event) && !checkSameLabel(event)) {
          label.setStyle("");
        }
      }
    });

    label.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
      @Override
      public void handle(MouseDragEvent event) {
        if (checkSource(event) && !checkSameLabel(event)) {
          // Mouse is dropped;
          label.setStyle("");
          int indexOfDropTarget = -1;
          Label sourceLbl = (Label) event.getGestureSource();
          VBox newRoot = null;
          // Removes dragged label from root, saves index of dragged to label;
          if (notStartedList.getChildren().contains(sourceLbl)) {
            notStartedList.getChildren().remove(sourceLbl);
            indexOfDropTarget = notStartedList.getChildren().indexOf(sourceLbl);
            generateUndoRedoObject(event, Status.NOT_STARTED);
          } else if (inProgressList.getChildren().contains(sourceLbl)) {
            inProgressList.getChildren().remove(sourceLbl);
            indexOfDropTarget = inProgressList.getChildren().indexOf(sourceLbl);
            generateUndoRedoObject(event, Status.IN_PROGRESS);
          } else if (verifyList.getChildren().contains(sourceLbl)) {
            verifyList.getChildren().remove(sourceLbl);
            indexOfDropTarget = verifyList.getChildren().indexOf(sourceLbl);
            generateUndoRedoObject(event, Status.VERIFY);
          } else if (doneList.getChildren().contains(sourceLbl)) {
            doneList.getChildren().remove(sourceLbl);
            indexOfDropTarget = doneList.getChildren().indexOf(sourceLbl);
            generateUndoRedoObject(event, Status.DONE);
          }
          int indexOfDrag = root.getChildren().indexOf(label);
          // Adds label to root with new listeners
          addWithDragging(root, sourceLbl);
          // Label is now in vbox at bottom
          // Root=correct Vbox, iODT=index of highlighted node, Lbl=label to be moved up
          bringUpNode(root, indexOfDrag, sourceLbl);
          event.consume();
        }
      }
    });
      root.getChildren().add(label);
  }

  /**
   * If already exists in root, removes the node then inserts it at the given index.
   * All parameters must be instantiated.
   * @param root Where to insert into
   * @param indexOfDropTarget Index to be inserted at
   * @param label Node to be inserted
   */
  private void bringUpNode(VBox root, int indexOfDropTarget, Node label) {
    if (root.getChildren().contains(label)) {
      root.getChildren().remove(label);
    }
    root.getChildren().add(indexOfDropTarget, label);
  }

  /**
   * Adds the node to the bottom of the root, and removes it from it's original parent.
   * @param root A javafx container with children
   * @param node Node to be appended
   */
  private void addToBottom(VBox root, Node node) {
    //1:Remove from orig location
    Label draggedLabel = (Label) node;
    VBox oldVBox = (VBox) draggedLabel.getParent();
    oldVBox.getChildren().remove(draggedLabel);
    //2:Add to the new list
    addWithDragging(root, draggedLabel);
  }

  /**
   * Checks to make sure event source and final location of mouse drag event for a label
   * have the same parent's parent. This make sure it belongs to the same set of four lists
   * (stops dragging label between stories).
   * @param event The mouse drag event
   * @return same? true : false
   */
  private boolean checkSource(MouseDragEvent event) {
    Label sourceLabel = (Label) event.getGestureSource();
    Label destinationLabel = (Label) event.getSource();
    // checks to see if both nodes contained within the higher level hbox
    if (sourceLabel.getParent().getParent().getParent().equals(
        destinationLabel.getParent().getParent().getParent())) {
      return true;
    }
    return false;
  }

  /**
   * Checks to make sure destination VBox belongs to the same parent as the label's VBox parent
   * @param event The mouse drag event
   * @return same? true : false
   */
  private boolean checkVBox(MouseDragEvent event) {
    Label sourceLabel = (Label) event.getGestureSource();
    VBox destinationVBox = (VBox) event.getSource();
    // checks to see if both nodes contained within the higher level hbox
    if (sourceLabel.getParent().getParent().getParent().equals(
        destinationVBox.getParent().getParent())) {
      return true;
    }
    return false;
  }

  /**
   * A check to see if mouse drag event source and gesture source are the same label.
   * @param event The mouse drag event
   * @return same? true : false
   */
  private boolean checkSameLabel(MouseDragEvent event) {
    Label sourceLabel = (Label) event.getGestureSource();
    Label destinationLabel = (Label) event.getSource();
    if (sourceLabel == destinationLabel) {
      return true;
    }
    return false;
  }

  /**
   * Generates the undo/redo object for drag/dropping an item between lists.
   * @param event Event of drop
   * @param status Status that the task is changed to.
   */
  private void generateUndoRedoObject(MouseDragEvent event, Status status) {
    // Step 1:Gesture source will always be label, item dragged.
    Label taskLabel = (Label) event.getGestureSource();
    String taskString = taskLabel.getText();
    String newPosString = null;
    if (event.getSource() instanceof Label) {
      Label taskNewPos = (Label) event.getSource();
      newPosString = taskNewPos.getText();
    } else if (event.getSource() instanceof VBox) {
      // For when dropped into empty space
      VBox droppedBox = (VBox) event.getSource();
      Label taskNewPos = (Label) droppedBox.getChildren().get(droppedBox.getChildren().size() - 1);
      newPosString = taskNewPos.getText();
    }
    //Step2: Create new and last task(old pre-drag, new after)
    Task lastTask = null;
    Task newTask = null;
    int newPosition = -1;
    //Step3: Clone last Task, assign task to newTask
    for (Task task : story.getTasks()) {
      if (taskString.equals(task.getLabel())) {
        lastTask = new Task(task);
        newTask = task;
      }
      if (task.getLabel().equals(newPosString)) {
        newPosition = story.getTasks().indexOf(task);
      }
    }
    // Adjust for dragged in bottom of VBox
    if (!(event.getSource() instanceof VBox)) {
      newPosition -= 1;
    }

    if (newTask != null && newPosition != -1) {
      //Step4: set newTask's status, to wherever it was dragged to.

      // If fake story, create sprint undo/redo, else story.
      UndoRedo containerUndoRedoObject = new UndoRedoObject();
      if (story.getLabel().equals("Non-story Tasks")) {
        System.out.println(sprint.getLabel());
        Sprint before = new Sprint(sprint);
        sprint.removeTask(newTask);
        sprint.addTask(newPosition, newTask);
        story.removeTask(newTask);
        story.addTask(newPosition, newTask);
        newTask.setStatus(status);
        Sprint after = new Sprint(sprint);

        containerUndoRedoObject.setAgileItem(sprint);
        containerUndoRedoObject.setAction(Action.SPRINT_EDIT);
        containerUndoRedoObject.addDatum(before);
        containerUndoRedoObject.addDatum(after);

      } else {
        Story before = new Story(story);
        story.removeTask(newTask);
        story.addTask(newPosition, newTask);
        newTask.setStatus(status);
        Story after = new Story(story);

        //Step5: Create Story undo/redo Object
        containerUndoRedoObject.setAgileItem(story);
        containerUndoRedoObject.setAction(Action.STORY_EDIT);
        containerUndoRedoObject.addDatum(before);
        containerUndoRedoObject.addDatum(after);
      }

      UndoRedoObject taskUndoRedoObject = new UndoRedoObject();
      taskUndoRedoObject.setAgileItem(newTask);
      taskUndoRedoObject.setAction(Action.TASK_EDIT);
      taskUndoRedoObject.addDatum(lastTask);
      taskUndoRedoObject.addDatum(new Task(newTask));

      CompositeUndoRedo compUndoRedoObject = new CompositeUndoRedo("Undo Task Move");
      compUndoRedoObject.addUndoRedo(containerUndoRedoObject);
      compUndoRedoObject.addUndoRedo(taskUndoRedoObject);

      mainApp.newAction(compUndoRedoObject);
    }
  }

  public String getPaneName() {
    return storyPane.getText();
  }

  public boolean checkIfOpened() {
    return storyPane.isExpanded();
  }

  public void expandTab() {
    accordion.setExpandedPane(storyPane);
  }

  public Story getStory() {
    return story;
  }

}
