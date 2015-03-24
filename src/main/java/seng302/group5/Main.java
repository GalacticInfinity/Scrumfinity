package seng302.group5;

import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng302.group5.controller.ListMainPaneController;
import seng302.group5.controller.MenuBarController;
import seng302.group5.controller.PersonDialogController;
import seng302.group5.controller.ProjectDialogController;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.controller.SkillsDialogController;
import seng302.group5.model.Project;
import seng302.group5.model.Skills;
import seng302.group5.model.Person;

/**
 * Main class to run the application
 */
public class Main extends Application {

  private Stage primaryStage;
  private BorderPane rootLayout;

  private ListMainPaneController LMPC;
  private MenuBarController MBC;

  private ObservableList<Project> projects = FXCollections.observableArrayList();
  private ObservableList<Skills> skills = FXCollections.observableArrayList();
  private ObservableList<Person> people = FXCollections.observableArrayList();

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Scrumfinity");
    // Constructs the application
    initRootLayout();
    showMenuBar();
    showListMainPane();
  }


  /**
   * Initializes the root layout.
   */
  public void initRootLayout() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/Main.fxml"));
      rootLayout = (BorderPane) loader.load();

      Scene scene = new Scene(rootLayout);
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Shows the menu bar inside root layout
   */
  public void showMenuBar() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MenuBarController.class.getResource("/MenuBar.fxml"));
      MenuBar menuBar = (MenuBar) loader.load();

      MenuBarController controller = loader.getController();
      controller.setMainApp(this);
      MBC = controller;

     rootLayout.setTop(menuBar);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Shows the ListMainPane
   */
  public void showListMainPane() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(ListMainPaneController.class.getResource("/ListMainPaneController.fxml"));
      SplitPane splitPane = (SplitPane) loader.load();

      ListMainPaneController controller = loader.getController();
      controller.setMainApp(this);
      LMPC = controller;

      rootLayout.setCenter(splitPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showProjectDialog(CreateOrEdit createOrEdit) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/ProjectDialog.fxml"));
      VBox projectDialogLayout = (VBox) loader.load();

      ProjectDialogController controller = loader.getController();
      Scene projectDialogScene = new Scene(projectDialogLayout);
      Stage projectDialogStage = new Stage();

      controller.setMainApp(this);
      controller.setStage(projectDialogStage);
      if (createOrEdit == CreateOrEdit.EDIT) {
        Project project = (Project) LMPC.getSelectedProject();
        if (project == null) {
          System.err.println("No project selected");
          return;
        }
        controller.setProject(project);
      }
      controller.setCreateOrEdit(createOrEdit);

      projectDialogStage.initModality(Modality.APPLICATION_MODAL);
      projectDialogStage.initOwner(primaryStage);
      projectDialogStage.setScene(projectDialogScene);
      projectDialogStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showPersonDialogCreation() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/PersonDialog.fxml"));
      VBox personDialogLayout = (VBox) loader.load();

      PersonDialogController controller = loader.getController();
      controller.setMainApp(this);

      Scene personDialogScene = new Scene(personDialogLayout);
      Stage personDialogStage = new Stage();
      controller.setStage(personDialogStage);

      personDialogStage.initModality(Modality.APPLICATION_MODAL);
      personDialogStage.initOwner(primaryStage);
      personDialogStage.setScene(personDialogScene);
      personDialogStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showSkillCreationDialog() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/SkillsDialog.fxml"));
      VBox SkillsDialogLayout = (VBox) loader.load();

      SkillsDialogController controller = loader.getController();
      controller.setMainApp(this);

      Scene skillDialogScene = new Scene(SkillsDialogLayout);
      Stage skillDialogStage = new Stage();
      controller.setStage(skillDialogStage);

      skillDialogStage.initModality(Modality.APPLICATION_MODAL);
      skillDialogStage.initOwner(primaryStage);
      skillDialogStage.setScene(skillDialogScene);
      skillDialogStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Stage getPrimaryStage(){
    return primaryStage;
  }

  public ListMainPaneController getLMPC() {
    return LMPC;
  }

  public MenuBarController getMBC() {
    return MBC;
  }

  public ObservableList<Project> getProjects() {
    return projects;
  }

  public ObservableList<Person> getPeople() {
    return people;
  }

  public void addProject(Project project) {
    projects.add(project);
  }

  public void addPerson(Person person) {
    people.add(person);
  }

  public void updateProjectList() {
    LMPC.refreshList();
  }

  public void addSkill(String skillName, String skillDescription) {
    skills.add(new Skills(skillName, skillDescription));
  }
  public static void main(String[] args) {
    launch(args);
  }
}
