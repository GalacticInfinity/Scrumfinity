package seng302.group5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng302.group5.controller.ListMainPaneController;
import seng302.group5.controller.MenuBarController;
import seng302.group5.controller.ReleaseDialogController;
import seng302.group5.controller.PersonDialogController;
import seng302.group5.controller.ProjectDialogController;
import seng302.group5.controller.TeamDialogController;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.controller.SkillsDialogController;
import seng302.group5.model.AgileItem;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Role;
import seng302.group5.model.Skill;
import seng302.group5.model.Person;
import seng302.group5.model.Team;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoHandler;
import seng302.group5.model.undoredo.UndoRedoObject;

/**
 * Main class to run the application
 */
public class Main extends Application {

  private Stage primaryStage;
  private BorderPane rootLayout;

  private ListMainPaneController LMPC;
  private MenuBarController MBC;

  private ObservableList<Project> projects = FXCollections.observableArrayList();
  private ObservableList<Team> teams = FXCollections.observableArrayList();
  private ObservableList<Skill> skills = FXCollections.observableArrayList();
  private ObservableList<Person> people = FXCollections.observableArrayList();
  private ObservableList<Release> releases = FXCollections.observableArrayList();
  private ObservableList<Role> roles = FXCollections.observableArrayList();

  private UndoRedoHandler undoRedoHandler = new UndoRedoHandler(this);

  private UndoRedoObject lastSavedObject = null;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Scrumfinity");
    // Constructs the application
    initRootLayout();
    showMenuBar();
    showListMainPane();

    //Set predetermined roles and skills
    Skill smSkill = new Skill("Scrum Master", "Trained to be a Scrum Master");
    Skill poSkill = new Skill("Product Owner", "Trained to be a Product Owner");
    addSkill(smSkill);
    addSkill(poSkill);

    Role smRole = new Role("SM", "Scrum Master", smSkill, 1);
    Role poRole = new Role("PO", "Product Owner", poSkill, 1);
    Role devRole = new Role("DEV", "Developer");
    addRole(poRole);
    addRole(smRole);
    addRole(devRole);

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
      controller.checkListType();   // Load objects into list view
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

      Project project = null;
      if (createOrEdit == CreateOrEdit.EDIT) {
        project = (Project) LMPC.getSelected();
        if (project == null) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText(null);
          alert.setContentText("No project selected");
          alert.showAndWait();
          return;
        }
      }

      controller.setupController(this, projectDialogStage, createOrEdit, project);

      projectDialogStage.initModality(Modality.APPLICATION_MODAL);
      projectDialogStage.initOwner(primaryStage);
      projectDialogStage.setScene(projectDialogScene);
      projectDialogStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showTeamDialog(CreateOrEdit createOrEdit) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/TeamDialog.fxml"));
      VBox teamDialogLayout = (VBox) loader.load();

      TeamDialogController controller = loader.getController();

      Scene teamDialogScene = new Scene(teamDialogLayout);
      Stage teamDialogStage = new Stage();

      Team team = null;
      if (createOrEdit == CreateOrEdit.EDIT) {
        team = (Team) LMPC.getSelected();
        if (team == null) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText(null);
          alert.setContentText("No team selected");
          alert.showAndWait();
          return;
        }
      }

      controller.setupController(this, teamDialogStage, createOrEdit, team);

      teamDialogStage.initModality(Modality.APPLICATION_MODAL);
      teamDialogStage.initOwner(primaryStage);
      teamDialogStage.setScene(teamDialogScene);
      teamDialogStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showReleaseDialog(CreateOrEdit createOrEdit) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/ReleaseDialog.fxml"));
      VBox releaseDialogLayout = (VBox) loader.load();

      ReleaseDialogController controller = loader.getController();
      Scene releaseDialogScene = new Scene(releaseDialogLayout);
      Stage releaseDialogStage = new Stage();

      Release release = null;
      if (createOrEdit == CreateOrEdit.EDIT) {
        release = (Release) LMPC.getSelected();    // TODO: Fix
        if (release == null) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText(null);
          alert.setContentText("No release selected");
          alert.showAndWait();
          return;
        }
      }
      controller.setupController(this ,releaseDialogStage, createOrEdit, release);

      releaseDialogStage.initModality(Modality.APPLICATION_MODAL);
      releaseDialogStage.initOwner(primaryStage);
      releaseDialogStage.setScene(releaseDialogScene);
      releaseDialogStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void showPersonDialog(CreateOrEdit createOrEdit) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/PersonDialog.fxml"));
      VBox personDialogLayout = (VBox) loader.load();

      PersonDialogController controller = loader.getController();
      Scene personDialogScene = new Scene(personDialogLayout);
      Stage personDialogStage = new Stage();

      Person person = null;
      if (createOrEdit == CreateOrEdit.EDIT) {
        person = (Person) LMPC.getSelected();    // TODO: Fix
        if (person == null) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText(null);
          alert.setContentText("No person selected");
          alert.showAndWait();
          return;
        }
      }
      controller.setupController(this, personDialogStage, createOrEdit, person);

      personDialogStage.initModality(Modality.APPLICATION_MODAL);
      personDialogStage.initOwner(primaryStage);
      personDialogStage.setScene(personDialogScene);
      personDialogStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showSkillCreationDialog(CreateOrEdit createOrEdit) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(Main.class.getResource("/SkillsDialog.fxml"));
      VBox SkillsDialogLayout = (VBox) loader.load();

      SkillsDialogController controller = loader.getController();
      Scene skillDialogScene = new Scene(SkillsDialogLayout);
      Stage skillDialogStage = new Stage();

      Skill skill = null;
      if (createOrEdit == CreateOrEdit.EDIT) {
        skill = (Skill) LMPC.getSelected();    // TODO: Fix
        if (skill == null){
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText(null);
          alert.setContentText("No skill selected");
          alert.showAndWait();
          return;
        }
      }
      controller.setupController(this, skillDialogStage, createOrEdit, skill);

      skillDialogStage.initModality(Modality.APPLICATION_MODAL);
      skillDialogStage.initOwner(primaryStage);
      skillDialogStage.setScene(skillDialogScene);
      skillDialogStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Undo last action
   */
  public void undo() {
    try {
      undoRedoHandler.undo();
      checkSaved();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Redo last action
   */
  public void redo() {
    try {
      undoRedoHandler.redo();
      checkSaved();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a new action to the undo/redo stack
   * @param undoRedoObject Action to store
   */
  public void newAction(UndoRedoObject undoRedoObject) {
    undoRedoHandler.newAction(undoRedoObject);
    checkSaved();
  }

  /**
   * Refresh the last saved object to be the newest action on the undo stack
   */
  public void refreshLastSaved() {
    lastSavedObject = undoRedoHandler.peekUndoStack();
    checkSaved();
  }

  /**
   * Check if the newest action was the saved action and adjust the window title
   */
  private void checkSaved() {
    UndoRedoObject topObject = undoRedoHandler.peekUndoStack();
    boolean neverSaved = lastSavedObject == null && topObject == null;

    // Adjust the window title
    if (neverSaved || topObject == lastSavedObject) {
      primaryStage.setTitle("Scrumfinity");
    } else {
      primaryStage.setTitle("Scrumfinity *");
    }
  }

  /**
   * Delete a project from the list of projects
   * @param inputProject Project to delete - must be same object reference
   */
  public void deleteProject(Project inputProject) {
    projects.remove(inputProject);
  }

  /**
   * Delete a person from the list of people
   * @param inputPerson Person to delete - must be the same object reference
   */
  public void deletePerson(Person inputPerson) {
    people.remove(inputPerson);
  }

  /**
   * Delete a skill from the list of skills
   * @param inputSkill Skill to delete - must be the same object reference
   */
  public void deleteSkill(Skill inputSkill) {
    skills.remove(inputSkill);
  }

  /**
   * Delete a team from the list of teams
   * @param inputTeam Team to delete - must be the same object reference
   */
  public void deleteTeam(Team inputTeam) {
    teams.remove(inputTeam);
  }

  /**
   * Delete a release from the list of releases
   * @param inputRelease
   */
  public void deleteRelease(Release inputRelease) {
    releases.remove(inputRelease);
  }

    /**
   * Generate an UndoRedoObject to place in the stack
     *
   * @param action The action to store in the object
   * @param agileItem The item to store in the object
   * @return the UndoRedoObject to store
   */
  private UndoRedoObject generateDelUndoRedoObject(Action action, AgileItem agileItem) {
    UndoRedoObject undoRedoObject = new UndoRedoObject();

    undoRedoObject.setAction(action);

    // Store a copy of object in stack to avoid reference problems
    AgileItem itemToStore;
    switch (action) {
      case PROJECT_DELETE:
        itemToStore = new Project((Project) agileItem);
        break;
      case PERSON_DELETE:
        itemToStore = new Person((Person) agileItem);
        break;
      case SKILL_DELETE:
        itemToStore = new Skill((Skill) agileItem);
        break;
      case TEAM_DELETE:
        itemToStore = new Team((Team) agileItem);
        break;
      case RELEASE_DELETE:
        itemToStore = new Release((Release) agileItem);
        break;
      default:
        itemToStore = null;
        System.err.println("Unhandled case for generating undo/redo delete object");
    }

    undoRedoObject.setAgileItem(agileItem); // store original
    undoRedoObject.addDatum(itemToStore);   // store clone

    return undoRedoObject;
  }

  /**
   * Generic delete function which deletes an item from the appropriate list and then adds
   * the action to the undo/redo stack
   *
   * @param agileItem Item to delete
   */
  public void delete(AgileItem agileItem) {
    String listType = LMPC.getCurrentListType();
    UndoRedoObject undoRedoObject;
    switch (listType) {
      case "Project":
        Project project = (Project) agileItem;

        ArrayList<Release> projectsReleases = new ArrayList<>();

        for (Release release : releases) {
          if (release.getProjectRelease().equals(project)) {
            projectsReleases.add(release);
          }
        }
        if (!projectsReleases.isEmpty()) {
          //if so open a yes/no dialog
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Releases still exist for this project");
          alert.setHeaderText(null);
          int messageLength = 1;
          String message = String.format("Do you want to delete project '%s' and its releases:\n",
                                         project.getProjectName());
          for (Release releases: projectsReleases) {
            messageLength ++;
            message += String.format("%s\n",
                                     releases.getReleaseName());
          }
          alert.getDialogPane().setPrefHeight(60 + 30 * messageLength);
          alert.setContentText(message);
          //checks response
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK) {
            for (Release release : projectsReleases) {
              deleteRelease(release);
            }
            deleteProject(project);
          }
        } else {
          deleteProject(project);
        }
        undoRedoObject = generateDelUndoRedoObject(Action.PROJECT_DELETE, agileItem);
        for (Release release : projectsReleases) {
          undoRedoObject.addDatum(release);
        }
        newAction(undoRedoObject);
        break;
      case "People":
        Person person = (Person) agileItem;

        if (person.isInTeam()) {
          String message = String.format(
              "Do you want to delete '%s' and remove him/her from the team '%s'?",
              person.getPersonID(),
              person.getTeamID());
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Person is in team");
          alert.setHeaderText(null);
          alert.setContentText(message);
          //checks response
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK){
            //if yes then remove
            person.getTeam().getTeamMembers().remove(person);
            deletePerson(person);
          }
        } else {
          deletePerson(person);
        }
        undoRedoObject = generateDelUndoRedoObject(Action.PERSON_DELETE, agileItem);
        newAction(undoRedoObject);
        break;
      case "Skills":
        Skill skill = (Skill) agileItem;
        ArrayList<Person> skillUsers = new ArrayList<>();
        //iterate through each person
        for (Person skillPerson : people) {
          //check if they have the skill
          if (skillPerson.getSkillSet().contains(skill)) {
            skillUsers.add(skillPerson);
          }
        }
        if (!skillUsers.isEmpty()) {
          //if so open a yes/no dialog
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("People have this skill!");
          alert.setHeaderText(null);
          int messageLength = 1;
          String message = String.format("Do you want to delete skill '%s' and remove it from:\n",
                                         skill.getSkillName());
          for (Person skillUser: skillUsers) {
            messageLength ++;
            message += String.format("%s - %s %s\n",
                                     skillUser.getPersonID(),
                                     skillUser.getFirstName(),
                                     skillUser.getLastName());
          }
          alert.getDialogPane().setPrefHeight(60 + 30 * messageLength);
          alert.setContentText(message);
          //checks response
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK){
            //if yes then remove skill from all who have it
            for (Person skillUser : skillUsers) {
              skillUser.getSkillSet().remove(skill);
            }
            //after all people have this skill removed delete the skill object
            deleteSkill(skill);
          }
        } else {
          deleteSkill(skill);
        }
        undoRedoObject = generateDelUndoRedoObject(Action.SKILL_DELETE, agileItem);
        for (Person skillUser : skillUsers) {
          // Add data so users can get the skill back after undo
          undoRedoObject.addDatum(skillUser);
        }
        newAction(undoRedoObject);
        break;
      case "Team":
        Team team = (Team) agileItem;
        if (team.getTeamMembers().isEmpty()) {
          deleteTeam(team);
          undoRedoObject = generateDelUndoRedoObject(Action.TEAM_DELETE, agileItem);
          newAction(undoRedoObject);
        } else {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Team contains people");
          alert.setHeaderText(null);

          int messageLength = 1;
          String message = String.format("Are you sure you want to delete team '%s' and people:\n",
                                         team.getTeamID());
          for (Person teamMember: team.getTeamMembers()) {
            messageLength ++;
            message += String.format("%s - %s %s\n",
                                     teamMember.getPersonID(),
                                     teamMember.getFirstName(),
                                     teamMember.getLastName());
          }
          alert.getDialogPane().setPrefHeight(60 + 30*messageLength);
          alert.setContentText(message);

          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK){
            for (Person teamPerson : team.getTeamMembers()) {
              deletePerson(teamPerson);
            }
            deleteTeam(team);
            undoRedoObject = generateDelUndoRedoObject(Action.TEAM_DELETE, agileItem);
            newAction(undoRedoObject);
          }
        }
        break;
      case "Release":
        Release release = (Release) agileItem;
        deleteRelease(release);
        undoRedoObject = generateDelUndoRedoObject(Action.RELEASE_DELETE, agileItem);
        newAction(undoRedoObject);
        break;
      default:
        System.err.println("Unhandled case for deleting agile item");
        break;
    }
  }

  /**
   * Reset main to its original state
   */
  public void resetAll() {
    undoRedoHandler.clearStacks();
    lastSavedObject = null;
    projects.clear();
    teams.clear();
    people.clear();
    skills.clear();
    releases.clear();
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

  public ObservableList<Team> getTeams() {
    return teams;
  }

  public ObservableList<Person> getPeople() {
    return people;
  }

  public ObservableList<Skill> getSkills() {
    return skills;
  }

  public ObservableList<Release> getReleases() {
    return releases;
  }

  public ObservableList<Role> getRoles() {
    return roles;
  }

  public void addProject(Project project) {
    projects.add(project);
  }

  public void addPerson(Person person) {
    people.add(person);
  }

  public void addTeam(Team team) {
    teams.add(team);
  }

  public void addSkill(Skill skill) {
    skills.add(skill);
  }

  public void addRelease(Release release) {
    releases.add(release);
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public UndoRedoHandler getUndoRedoHandler() {
    // This is for tests
    return undoRedoHandler;
  }

  public void setLMPC(ListMainPaneController LMPC) {
    // This is for tests
    this.LMPC = LMPC;
  }

  public void refreshList() {
    LMPC.refreshList();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
