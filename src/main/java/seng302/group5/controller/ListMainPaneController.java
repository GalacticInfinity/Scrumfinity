package seng302.group5.controller;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import seng302.group5.Main;
import seng302.group5.model.AgileHistory;
import seng302.group5.model.AgileItem;
import seng302.group5.model.Release;
import seng302.group5.model.Role;
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
  @FXML private TextArea displayTextArea;
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
   */
  public void refreshList() {
    listView.setItems(null);
    checkListType();
    listView.getSelectionModel().clearSelection();
    displayTextArea.clear();
    selectedItem = null;
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
    displayTextArea.clear();

    switch (Settings.currentListType) {
      case "People":
        Person person = (Person) next;

        displayTextArea.appendText("Person Information \nPerson Label: ");
        displayTextArea.appendText(person.getLabel());
        displayTextArea.appendText("\nFirst Name: ");
        displayTextArea.appendText(person.getFirstName());
        displayTextArea.appendText("\nLast Name: ");
        displayTextArea.appendText(person.getLastName());
        displayTextArea.appendText("\nTeam: ");
        if (person.isInTeam()) {
          displayTextArea.appendText(person.getTeamLabel());
        } else {
          displayTextArea.appendText("Not assigned.");
        }
        displayTextArea.appendText("\nSkills: ");
        StringBuilder listOfSkills = new StringBuilder();
        for (Skill skill : person.getSkillSet()) {
          listOfSkills.append(skill.getLabel());
          listOfSkills.append(", ");
        }
        if (listOfSkills.length() == 0) {
          displayTextArea.appendText("No known skills. Please add skills to this person.");
        } else {
          displayTextArea.appendText(listOfSkills.substring(0, listOfSkills.length() - 2));
        }
        break;
      case "Projects":
        Project project = (Project) next;

        displayTextArea.appendText("Project Information \nProject Label: ");
        displayTextArea.appendText(project.getLabel());
        displayTextArea.appendText("\nProject Name: ");
        displayTextArea.appendText(project.getProjectName());
        displayTextArea.appendText("\nProject Description: \n");
        displayTextArea.appendText(project.getProjectDescription());
        displayTextArea.appendText("\nAssigned Teams: \n");
        for (AgileHistory team : project.getAllocatedTeams()) {
          displayTextArea.appendText("" + team.toString() + "\n");
        }
        break;
      case "Skills":
        Skill skill = (Skill) next;

        displayTextArea.appendText("Skill Information \nSkill Label: ");
        displayTextArea.appendText(skill.getLabel());
        displayTextArea.appendText("\nSkill Description: ");
        displayTextArea.appendText(skill.getSkillDescription());

        break;
      case "Teams":
        Team team = (Team) next; //Casts next as Team object

        displayTextArea.appendText("Team Information \nTeam Label: ");
        displayTextArea.appendText(team.getLabel());
        displayTextArea.appendText("\nTeam Description: ");
        displayTextArea.appendText(team.getTeamDescription());
        displayTextArea.appendText("\nTeam Members: \n");
        for (Person member : team.getTeamMembers()) {
          displayTextArea.appendText(member.getFirstName());
          displayTextArea.appendText(" - ");
          displayTextArea.appendText(member.getLabel());
          displayTextArea.appendText(" Role: ");
          Role role = team.getMembersRole().get(member);
          if (role != null) {
            displayTextArea.appendText(role.toString());
          } else {
            displayTextArea.appendText("Not assigned to a role yet.");
          }
          displayTextArea.appendText("\n");
        }
        break;
      case "Releases":
        Release release = (Release) next;

        displayTextArea.appendText("Release Information \nRelease Label: ");
        displayTextArea.appendText(release.getLabel());
        displayTextArea.appendText("\nRelease Description: ");
        displayTextArea.appendText(release.getReleaseDescription());
        displayTextArea.appendText("\nRelease Date: ");
        displayTextArea.appendText(release.getReleaseDate().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        displayTextArea.appendText("\nRelease Notes: ");
        displayTextArea.appendText(release.getReleaseNotes());
        displayTextArea.appendText("\nProject: ");
        displayTextArea.appendText(release.getProjectRelease().toString());
        break;
      case "Stories":
        Story story = (Story) next; //Casts next as Story object.

        displayTextArea.appendText("Story Information \nStory Label: ");
        displayTextArea.appendText(story.getLabel());
        displayTextArea.appendText("\nStory Long Name: ");
        displayTextArea.appendText(story.getLongName());
        displayTextArea.appendText("\nStory Description: ");
        displayTextArea.appendText(story.getDescription());
        displayTextArea.appendText("\nCreator: ");
        displayTextArea.appendText(story.getCreator().toString());
        break;
    }
  }

  /**
   * Sets the main app to the param
   *
   * @param mainApp The main application object
   */
  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }
}
