package seng302.group5.controller.mainAppControllers;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import seng302.group5.Main;
import seng302.group5.model.AgileHistory;
import seng302.group5.model.AgileItem;
import seng302.group5.model.Backlog;
import seng302.group5.model.Release;
import seng302.group5.model.Role;
import seng302.group5.model.Sprint;
import seng302.group5.model.Story;
import seng302.group5.model.util.Settings;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Team;
import seng302.group5.model.Skill;

/**
 * Created by Michael on 3/15/2015.
 * ListManePane fxml contains styling for both List and MainPane,
 * still requires asthetic tweaks, doesn't scale properly.
 */
public class ListMainPaneController {

  @FXML private ListView listView;
  @FXML private TextFlow displayTextFlow;
  @FXML private SplitPane splitPane;
  @FXML private AnchorPane listViewPane;
  @FXML private Label listViewLabel;
  private Main mainApp;
  private boolean isListShown = true;

  private AgileItem selectedItem;

  /**
   * Initialise the fxml, basic setup functions called.
   */
  @FXML
  private void initialize() {
    Settings.currentListType = "Projects";
    Settings.setSysDefault();
    iniActorList();
  }

  /**
   * Sets listeners to whatever is in the list.
   */
  private void iniActorList() {
    listView.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<AgileItem>() {
          @Override
          public void changed(ObservableValue<? extends AgileItem> observableValue,
                              AgileItem previous, AgileItem next) {
            if (next != null) {
              // Will place checks to update main pane here based on item type selected
              displayInfo(next);

              selectedItem = next;
            }
          }
        }
    );

    listView.setCellFactory(listView -> new ListViewCell());
  }

  /**
   * Shows/Hides items in list.
   *
   * @param item a CheckMenuItem is used to select the current item or deselect it depending on if
   *             you're hiding or showing the list
   */
  public void showHideList(CheckMenuItem item) {
    if (!isListShown) {
      checkListType();
      item.setSelected(true);
      mainApp.getMBC().deselectList(Settings.currentListType);
    } else {
      ObservableList<AgileItem> clear = FXCollections.observableArrayList();
      listView.setItems(clear);
      isListShown = false;
      item.setSelected(false);
      mainApp.getMBC().deselectList("");
      // Hide the pane containing the list
      splitPane.getItems().remove(listViewPane);
    }
  }


  /**
   * Refreshes listView and text area text for when edits occur.
   *
   * @param agileItem agile item being editted/created.
   */
  public void refreshList(AgileItem agileItem) {
    listView.setItems(null);
    checkListType();
    selectedItem = agileItem;
    if (agileItem != null) {
      listView.getSelectionModel().select(agileItem);
      displayInfo(agileItem);
    } else {
      listView.getSelectionModel().clearSelection();
      displayTextFlow.getChildren().clear();
    }
  }

  /**
   * Checks list type currently set as viewed list by user.
   */
  public void checkListType() {
    String listType = Settings.currentListType;
    switch (listType) {
      case "Projects":
        isListShown = true;
        listView.setItems(mainApp.getProjects().sorted(Comparator.<Project>naturalOrder()));
        break;
      case "People":
        isListShown = true;
        listView.setItems(mainApp.getPeople().sorted(Comparator.<Person>naturalOrder()));
        break;
      case "Skills":
        isListShown = true;
        listView.setItems(mainApp.getSkills().sorted(Comparator.<Skill>naturalOrder()));
        break;
      case "Teams":
        isListShown = true;
        listView.setItems(mainApp.getTeams().sorted(Comparator.<Team>naturalOrder()));
        break;
      case "Releases":
        isListShown = true;
        listView.setItems(mainApp.getReleases().sorted(Comparator.<Release>naturalOrder()));
        break;
      case "Stories":
        isListShown = true;
        listView.setItems(mainApp.getStories().sorted(Comparator.<Story>naturalOrder()));
        break;
      case "Backlogs":
        isListShown = true;
        listView.setItems(mainApp.getBacklogs().sorted(Comparator.<Backlog>naturalOrder()));
        break;
      case "Sprints":
        isListShown = true;
        listView.setItems(mainApp.getSprints().sorted(Comparator.<Sprint>naturalOrder()));
        break;
    }
    // Set the list label
    listViewLabel.setText(listType);
    // Show the pane containing the list if not already
    if (splitPane.getItems().size() < 2) {
      splitPane.getItems().add(0, listViewPane);
    }
  }

  /**
   * Gets the currently selected item in the listview.
   *
   * @return AgileItem which is the item selected in the list.
   */
  public AgileItem getSelected() {
    String listType = Settings.currentListType;
    switch (listType) {
      case "Projects":
        if (!isListShown || !listType.equals("Projects")) {
          return null;
        }
        return selectedItem;
      case "People":
        if (!isListShown || !listType.equals("People")) {
          return null;
        }
        return selectedItem;
      case "Skills":
        if (!isListShown || !listType.equals("Skills")) {
          return null;
        }
        return selectedItem;
      case "Teams":
        if (!isListShown || !listType.equals("Teams")) {
          return null;
        }
        return selectedItem;
      case "Releases":
        if (!isListShown || !listType.equals("Releases")) {
          return null;
        }
        return selectedItem;
      case "Stories":
        if (!isListShown || !listType.equals("Stories")) {
          return null;
        }
        return selectedItem;
      case "Backlogs":
        if (!isListShown || !listType.equals("Backlogs")) {
          return null;
        }
        return selectedItem;
      case "Sprints":
        if (!isListShown || !listType.equals("Sprints")) {
          return null;
        }
        return selectedItem;
    }
    return null;
  }

  /**
   * Gets the current list type being displayed
   *
   * @return String, a string of the current list type.
   */
  public String getCurrentListType() {
    return Settings.currentListType;
  }

  /**
   * Takes the current selected item (next) as an AgileItem and then casts it to the correct object
   * based on what the current selected list is.
   *
   * @param next Next selected item
   */
  public void displayInfo(AgileItem next) {
    displayTextFlow.getChildren().clear();
    switch (Settings.currentListType) {
      case "People":
        displayTextFlow.getChildren().clear();
        Person person = (Person) next;
        displayPeopleTextArea(person);
        break;
      case "Projects":
        displayTextFlow.getChildren().clear();
        Project project = (Project) next;
        displayProjectTextArea(project);
        break;
      case "Skills":
        displayTextFlow.getChildren().clear();
        Skill skill = (Skill) next;
        displaySkillTextArea(skill);
        break;
      case "Teams":
        displayTextFlow.getChildren().clear();
        Team team = (Team) next;
        displayTeamTextArea(team);
        break;
      case "Releases":
        displayTextFlow.getChildren().clear();
        Release release = (Release) next;
        displayReleaseTextArea(release);
        break;
      case "Stories":
        displayTextFlow.getChildren().clear();
        Story story = (Story) next;
        displayStoryTextArea(story);
        break;
      case "Backlogs":
        displayTextFlow.getChildren().clear();
        Backlog backlog = (Backlog) next;
        displayBacklogTextArea(backlog);
        break;
      case "Sprints":
        displayTextFlow.getChildren().clear();
        Sprint sprint = (Sprint) next;
        displaySprintTextArea(sprint);
        break;
    }
  }

  /**
   * Method that takes the selected skill and displays its information in the main display pane.
   * @param skill the skills who's information will be displayed.
   */
  private void displaySkillTextArea(Skill skill) {
    Text text1 = new Text("Skill Information\n");
    text1.setFill(Color.BLACK);
    text1.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));

    Text text2 = new Text("Skill Label: ");
    text2.setFill(Color.BLACK);
    text2.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text text3 = new Text(skill.getLabel());
    text3.setFill(Color.BLACK);
    text3.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text text4 = new Text("\nSkill Description: \n");
    text4.setFill(Color.BLACK);
    text4.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text text5 = new Text();
    if (skill.getSkillDescription().isEmpty()) {
      text5 = new Text("N/A");
      text5.setFill(Color.BLACK);
      text5.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    } else {
      text5 = new Text(skill.getSkillDescription());
      text5.setFill(Color.BLACK);
      text5.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    }

    displayTextFlow.getChildren().addAll(text1, text2, text3, text4, text5);
  }

  /**
   * method that takes the selected person and displays there information in the main pane.
   * @param person the person who's information will be displayed.
   */
  private void displayPeopleTextArea(Person person) {
    Text text1 = new Text("Person Information\n");
    text1.setFill(Color.BLACK);
    text1.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));

    Text text2 = new Text("Person Label: ");
    text2.setFill(Color.BLACK);
    text2.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text text3 = new Text(person.getLabel());
    text3.setFill(Color.BLACK);
    text3.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text text4 = new Text("\nFirst Name: ");
    text4.setFill(Color.BLACK);
    text4.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text text5 = new Text(person.getFirstName());
    text5.setFill(Color.BLACK);
    text5.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text text6 = new Text("\nLast Name: ");
    text6.setFill(Color.BLACK);
    text6.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text text7 = new Text(person.getLastName());
    text7.setFill(Color.BLACK);
    text7.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text text8 = new Text("\nTeam: ");
    text8.setFill(Color.BLACK);
    text8.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text text9 = new Text();
    if (person.isInTeam()) {
      text9 = new Text(person.getTeam().toString());
      text9.setFill(Color.BLACK);
      text9.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    } else {
      text9 = new Text("Not Assigned.");
      text9.setFill(Color.BLACK);
      text9.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    }
    Text text10 = new Text("\nSkills: ");
    text10.setFill(Color.BLACK);
    text10.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));


    StringBuilder listOfSkills = new StringBuilder();
    for (Skill skill : person.getSkillSet().sorted(Comparator.<Skill>naturalOrder())) {
      listOfSkills.append(skill.getLabel());
      listOfSkills.append(", ");
    }
    Text text11 = new Text();
    if (listOfSkills.length() == 0) {
      text11 = new Text("No skills, please assign skills.");
      text11.setFill(Color.BLACK);
      text11.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    } else {
      text11 = new Text(listOfSkills.substring(0, listOfSkills.length() - 2));
      text11.setFill(Color.BLACK);
      text11.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    }
    displayTextFlow.getChildren().addAll(text1, text2, text3, text4, text5, text6,
                                         text7, text8, text9, text10, text11);
  }

  /**
   * Method that takes the selected project and displays its information in the main pane.
   * @param project the project who's information will be displayed.
   */
  private void displayProjectTextArea(Project project) {
    Text text1 = new Text("Project Information\n");
    text1.setFill(Color.BLACK);
    text1.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));

    Text text2 = new Text("Project Label: ");
    text2.setFill(Color.BLACK);
    text2.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text text3 = new Text(project.getLabel());
    text3.setFill(Color.BLACK);
    text3.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text text4 = new Text("\nProject Name: ");
    text4.setFill(Color.BLACK);
    text4.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text text5 = new Text(project.getProjectName());
    text5.setFill(Color.BLACK);
    text5.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text text6 = new Text("\nProject Description: \n");
    text6.setFill(Color.BLACK);
    text6.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text text7 = new Text();
    if (project.getProjectDescription().isEmpty()) {
      text7 = new Text("N/A");
      text7.setFill(Color.BLACK);
      text7.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    } else {
      text7 = new Text(project.getProjectDescription());
      text7.setFill(Color.BLACK);
      text7.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    }


    Text text8 = new Text("\nAssigned Teams: \n");
    text8.setFill(Color.BLACK);
    text8.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));

    StringBuilder listOfTeams = new StringBuilder();
    for (AgileHistory team : project.getAllocatedTeams().sorted(
        Comparator.<AgileHistory>naturalOrder())) {
        listOfTeams.append("" + team.toString() + " \n");
    }
    Text text9 = new Text();
    if (listOfTeams.length() == 0) {
      text9 = new Text("No Teams assigned, please assign Teams.");
      text9.setFill(Color.BLACK);
      text9.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    } else {
      text9 = new Text(listOfTeams.substring(0, listOfTeams.length() - 2));
      text9.setFill(Color.BLACK);
      text9.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    }

    Text text10 = new Text("\nBacklog: ");
    text10.setFill(Color.BLACK);
    text10.setFont(Font.font("Helvetica",FontWeight.BOLD, FontPosture.ITALIC, 15));
    Text text11;
    if (project.getBacklog()==null) {
      text11 = new Text("N/A");
      text11.setFill(Color.BLACK);
      text11.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    } else {
      text11 = new Text(project.getBacklog().getLabel());
      text11.setFill(Color.BLACK);
      text11.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    }
    displayTextFlow.getChildren().addAll(text1, text2, text3, text4, text5, text6,
                                         text7, text8, text9, text10,text11);
  }


  /**
   * Sets the main app to the param
   *
   * @param mainApp The main application object
   */
  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }

  /**
   * Displays the information about a given team in the text pane.
   *
   * @param team The team to display in the text pane.
   */
  private void displayTeamTextArea(Team team) {
    Text textHeader = new Text("Team Information");
    textHeader.setFill(Color.rgb(1, 0, 1));
    textHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));

    Text textLabelHeader = new Text("\nTeam Label: ");
    textLabelHeader.setFill(Color.rgb(1, 0, 1));
    textLabelHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textLabelBody = new Text(team.getLabel());
    textLabelBody.setFill(Color.rgb(1, 0, 1));
    textLabelBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textDescriptionHeader = new Text("\nTeam Description:\n");
    textDescriptionHeader.setFill(Color.rgb(1, 0, 1));
    textDescriptionHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textDescriptionBody;
    if (team.getTeamDescription().length() != 0) {
      textDescriptionBody = new Text(team.getTeamDescription());
    } else {
      textDescriptionBody = new Text("N/A");
    }
    textDescriptionBody.setFill(Color.rgb(1, 0, 1));
    textDescriptionBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textMembersHeader = new Text("\nTeam Members: ");
    textMembersHeader.setFill(Color.rgb(1, 0, 1));
    textMembersHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    displayTextFlow.getChildren().addAll(textHeader, textLabelHeader, textLabelBody,
                                         textDescriptionHeader, textDescriptionBody,
                                         textMembersHeader);

    Text textMembersBody;
    Text textMemberRole;
    for (Person member : team.getTeamMembers().sorted(Comparator.<Person>naturalOrder())) {
      if (member.getFirstName().isEmpty() && member.getLastName().isEmpty()) {
        textMembersBody = new Text("\n" + member.getLabel() + " - Role: ");
      } else {
        textMembersBody = new Text("\n" + member.getLabel() + " - " + member.getFirstName() + " " +
                                   member.getLastName() + " - Role: ");

      }
      Role role = team.getMembersRole().get(member);
        if (role != null) {
          textMemberRole = new Text(role.toString());
        } else {
          textMemberRole = new Text("Not assigned to a role yet.");
        }
      textMembersBody.setFill(Color.rgb(1, 0, 1));
      textMembersBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
      textMemberRole.setFill(Color.rgb(1, 0, 1));
      textMemberRole.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

      displayTextFlow.getChildren().addAll(textMembersBody, textMemberRole);
    }
  }

  /**
   * Displays the information about a given release in the text pane.
   *
   * @param release The release to display information about.
   */
  private void displayReleaseTextArea(Release release) {
    Text textHeader = new Text("Release Information");
    textHeader.setFill(Color.rgb(1, 0, 1));
    textHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));

    Text textLabelHeader = new Text("\nRelease Label: ");
    textLabelHeader.setFill(Color.rgb(1, 0, 1));
    textLabelHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textLabelBody = new Text(release.getLabel());
    textLabelBody.setFill(Color.rgb(1, 0, 1));
    textLabelBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textDescriptionHeader = new Text("\nRelease Description:\n");
    textDescriptionHeader.setFill(Color.rgb(1, 0, 1));
    textDescriptionHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textDescriptionBody;
    if (release.getReleaseDescription().length() != 0) {
      textDescriptionBody = new Text(release.getReleaseDescription());
    } else {
      textDescriptionBody = new Text("N/A");
    }
    textDescriptionBody.setFill(Color.rgb(1, 0, 1));
    textDescriptionBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textDateHeader = new Text("\nRelease Date: ");
    textDateHeader.setFill(Color.rgb(1, 0, 1));
    textDateHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    LocalDate date = release.getReleaseDate();
    String dateString;
    String format = "dd/MM/yyyy";
    dateString = date.format(DateTimeFormatter.ofPattern(format));

    Text textDateBody = new Text(dateString);
    textDateBody.setFill(Color.rgb(1, 0, 1));
    textDateBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textNotesHeader = new Text("\nRelease Notes:\n");
    textNotesHeader.setFill(Color.rgb(1, 0, 1));
    textNotesHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textNotesBody;
    if (release.getReleaseDescription().length() != 0) {
      textNotesBody = new Text(release.getReleaseNotes());
    } else {
      textNotesBody = new Text("N/A");
    }
    textNotesBody.setFill(Color.rgb(1, 0, 1));
    textNotesBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textProjectHeader = new Text("\nProject: ");
    textProjectHeader.setFill(Color.rgb(1, 0, 1));
    textProjectHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textProjectBody = new Text(release.getProjectRelease().toString());
    textProjectBody.setFill(Color.rgb(1, 0, 1));
    textProjectBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    displayTextFlow.getChildren().addAll(textHeader, textLabelHeader, textLabelBody,
                                         textDescriptionHeader, textDescriptionBody, textDateHeader,
                                         textDateBody, textNotesHeader, textNotesBody,
                                         textProjectHeader, textProjectBody);
  }

  /**
   * Displays the information about a given story in the text pane.
   *
   * @param story The story to display information about.
   */
  private void displayStoryTextArea(Story story) {
    Text textHeader = new Text("Story Information");
    textHeader.setFill(Color.rgb(1, 0, 1));
    textHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));

    Text textLabelHeader = new Text("\nStory Label: ");
    textLabelHeader.setFill(Color.rgb(1, 0, 1));
    textLabelHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textLabelBody = new Text(story.getLabel());
    textLabelBody.setFill(Color.rgb(1, 0, 1));
    textLabelBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textNameHeader = new Text("\nStory Name: ");
    textNameHeader.setFill(Color.rgb(1, 0, 1));
    textNameHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textNameBody = new Text(story.getStoryName());
    textNameBody.setFill(Color.rgb(1, 0, 1));
    textNameBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textDescriptionHeader = new Text("\nStory Description:\n");
    textDescriptionHeader.setFill(Color.rgb(1, 0, 1));
    textDescriptionHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textDescriptionBody;
    if (story.getDescription().length() != 0) {
      textDescriptionBody = new Text(story.getDescription());
    } else {
      textDescriptionBody = new Text("N/A");
    }
    textDescriptionBody.setFill(Color.rgb(1, 0, 1));
    textDescriptionBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textCreatorHeader = new Text("\nStory Creator: ");
    textCreatorHeader.setFill(Color.rgb(1, 0, 1));
    textCreatorHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textCreatorBody = new Text(story.getCreator().toString());
    textCreatorBody.setFill(Color.rgb(1, 0, 1));
    textCreatorBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textReadinessHeader = new Text("\nReadiness: ");
    textReadinessHeader.setFill(Color.rgb(1, 0, 1));
    textReadinessHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textReadinessBody = new Text();
    textReadinessBody.setFill(Color.rgb(1, 0, 1));
    textReadinessBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    if (story.getStoryState()) {
      textReadinessBody.setText("Ready");
    } else {
      textReadinessBody.setText("Not ready");
    }

    Text textACHeader = new Text("\nAcceptance Criteria:");
    textACHeader.setFill(Color.rgb(1, 0, 1));
    textACHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    displayTextFlow.getChildren().addAll(textHeader, textLabelHeader, textLabelBody,
                                         textNameHeader, textNameBody, textDescriptionHeader,
                                         textDescriptionBody, textCreatorHeader, textCreatorBody,
                                         textReadinessHeader, textReadinessBody, textACHeader);

    Text textACBody;
    if (story.getAcceptanceCriteria().size() != 0) {
      for (String storyAC : story.getAcceptanceCriteria()) {
        textACBody = new Text("\n•" + storyAC);
        displayTextFlow.getChildren().addAll(textACBody);
      }
    } else {
      textACBody = new Text("\nN/A");
      displayTextFlow.getChildren().addAll(textACBody);
    }
  }

  /**
   * Displays the information about a given backlog in the text pane.
   *
   * @param backlog The backlog to display information about.
   */
  private void displayBacklogTextArea(Backlog backlog) {
    Text textHeader = new Text("Backlog Information");
    textHeader.setFill(Color.rgb(1, 0, 1));
    textHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));

    Text textLabelHeader = new Text("\nBacklog Label: ");
    textLabelHeader.setFill(Color.rgb(1, 0, 1));
    textLabelHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textLabelBody = new Text(backlog.getLabel());
    textLabelBody.setFill(Color.rgb(1, 0, 1));
    textLabelBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textNameHeader = new Text("\nBacklog Name: ");
    textNameHeader.setFill(Color.rgb(1, 0, 1));
    textNameHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textNameBody = new Text(backlog.getBacklogName());
    textNameBody.setFill(Color.rgb(1, 0, 1));
    textNameBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textDescriptionHeader = new Text("\nBacklog Description:\n");
    textDescriptionHeader.setFill(Color.rgb(1, 0, 1));
    textDescriptionHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textDescriptionBody;
    if (backlog.getBacklogDescription().length() != 0) {
      textDescriptionBody = new Text(backlog.getBacklogDescription());
    } else {
      textDescriptionBody = new Text("N/A");
    }
    textDescriptionBody.setFill(Color.rgb(1, 0, 1));
    textDescriptionBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textPoHeader = new Text("\nBacklog Product Owner: ");
    textPoHeader.setFill(Color.rgb(1, 0, 1));
    textPoHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    Text textPoBody = new Text(backlog.getProductOwner().toString());
    textPoBody.setFill(Color.rgb(1, 0, 1));
    textPoBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textEsHeader = new Text("\nBacklog Estimation Scale: ");
    textEsHeader.setFill(Color.rgb(1, 0, 1));
    textEsHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));


    Text textEsBody;
    if (backlog.getEstimate() == null) {
      textEsBody = new Text("Null");
    }else {
      textEsBody = new Text(backlog.getEstimate().toString());
    }
    textEsBody.setFill(Color.rgb(1, 0, 1));
    textEsBody.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));

    Text textStoriesHeader = new Text("\nStories:");
    textStoriesHeader.setFill(Color.rgb(1, 0, 1));
    textStoriesHeader.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15));

    String prioritisedOrder = "Prioritised Order";
    String alphabeticalOrder = "Alphabetical Order";

    Hyperlink sortToggle = new Hyperlink(prioritisedOrder);

    displayTextFlow.getChildren().addAll(textHeader, textLabelHeader, textLabelBody,
                                         textNameHeader, textNameBody, textDescriptionHeader,
                                         textDescriptionBody, textPoHeader, textPoBody,
                                         textEsHeader, textEsBody,
                                         textStoriesHeader, sortToggle);

    List<Text> storiesText = new ArrayList<>();

    Text textStoriesBody;
    if (!backlog.getStories().isEmpty()) {

      for (Story story : backlog.getStories()) {
        int index = backlog.getSizes().get(story);
        textStoriesBody = new Text("\n• " + story + " - " +
                                   backlog.getEstimate().getEstimateNames().get(index));
        storiesText.add(textStoriesBody);
        displayTextFlow.getChildren().add(textStoriesBody);
      }
    } else {
      textStoriesBody = new Text("\nN/A");
      storiesText.add(textStoriesBody);
      displayTextFlow.getChildren().add(textStoriesBody);
    }

    sortToggle.setOnAction(event -> {
      displayTextFlow.getChildren().removeAll(storiesText);
      storiesText.clear();
      if (sortToggle.getText().equals(prioritisedOrder)) {
        // Change to alphabetical order
        Text tempTextStoriesBody;
        if (!backlog.getStories().isEmpty()) {
          SortedList<Story> sortedStories = new SortedList<>(
              FXCollections.observableArrayList(backlog.getStories()),
              Comparator.<Story>naturalOrder());
          Map<Story, Integer> sorted = new IdentityHashMap<>(backlog.getSizes());
          for (Map.Entry<Story, Integer> entry : sorted.entrySet()) {
            tempTextStoriesBody = new Text("\n• " + entry.getKey() + " - " +
            backlog.getEstimate().getEstimateNames().get(entry.getValue()));
            storiesText.add(tempTextStoriesBody);
            displayTextFlow.getChildren().add(tempTextStoriesBody);
          }
        } else {
          tempTextStoriesBody = new Text("\nN/A");
          storiesText.add(tempTextStoriesBody);
          displayTextFlow.getChildren().add(tempTextStoriesBody);
        }
        // Change the mode
        sortToggle.setText(alphabeticalOrder);
      } else {
        // Change to prioritised order
        Text tempTextStoriesBody;
        if (!backlog.getStories().isEmpty()) {
          for (Story story : backlog.getStories()) {
            int index = backlog.getSizes().get(story);
            tempTextStoriesBody = new Text("\n• " + story + " - " +
                                           backlog.getEstimate().getEstimateNames().get(index));
            storiesText.add(tempTextStoriesBody);
            displayTextFlow.getChildren().add(tempTextStoriesBody);
          }
        } else {
          tempTextStoriesBody = new Text("\nN/A");
          storiesText.add(tempTextStoriesBody);
          displayTextFlow.getChildren().add(tempTextStoriesBody);
        }
        // Change the mode
        sortToggle.setText(prioritisedOrder);
      }
    });
  }

  /**
   * Displays the information about a given sprint in the text pane.
   *
   * @param sprint The sprint to display information about.
   */
  private void displaySprintTextArea(Sprint sprint) {
    Text textHeader = new Text("Backlog Information");
    //TODO Do it
  }

  /**
   * Create a dialog for creation depending on what is currently being displayed in the list
   *
   * @param event Event generated by event listener
   */
  @FXML
  protected void btnClickDirectAdd(ActionEvent event) {
    // Functionality is implemented in MenuBarController. Simply call that method.
    mainApp.getMBC().btnClickDirectAdd(event);
  }

  /**
   * Allows us to override the a ListViewCell - a single cell in a ListView.
   */
  private class ListViewCell extends TextFieldListCell<Object> {

    public ListViewCell() {
      super();

      // double click for editing
      this.setOnMouseClicked(click -> {
        if (click.getClickCount() == 2 &&
            click.getButton() == MouseButton.PRIMARY &&
            !isEmpty()) {
          mainApp.getMBC().editItem(null);
        }
      });
    }
  }
}
