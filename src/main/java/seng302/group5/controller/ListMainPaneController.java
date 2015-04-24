package seng302.group5.controller;

import java.time.format.DateTimeFormatter;

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
import seng302.group5.model.util.Settings;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Team;
import seng302.group5.model.Skill;


/**
 * Created by Michael on 3/15/2015.
 * ListManePane fxml contains styling for both List and MainPane, still
 * requires asthetic tweaks, doesn't scale properly.
 */
public class ListMainPaneController {

  @FXML private ListView listView;
  @FXML private TextArea sampleTextArea;
  @FXML private SplitPane splitPane;
  @FXML private AnchorPane listViewPane;
  @FXML private Label listViewLabel;
  private Main mainApp;
  private boolean isListShown = true;

  public ListMainPaneController() {}
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
   */
  public void showHideList(CheckMenuItem item) {
    if(!isListShown){
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
   * TODO Testing
   */
  public void refreshList() {
    listView.setItems(null);
    checkListType();
    listView.getSelectionModel().clearSelection();
    sampleTextArea.clear();
    selectedItem = null;
  }

  /**
   * Checks list type currently set as viewed list by user.
   * TODO testing
   */
  public void checkListType(){
    String listType = Settings.currentListType;
    switch (listType) {
      case "Projects":
        isListShown = true;
        listView.setItems(mainApp.getProjects());
        break;
      case "People":
        isListShown = true;
        listView.setItems(mainApp.getPeople());
        break;
      case "Skills":
        isListShown = true;
        listView.setItems(mainApp.getSkills());
        break;
      case "Teams":
        isListShown = true;
        listView.setItems(mainApp.getTeams());
        break;
      case "Releases":
        isListShown = true;
        listView.setItems(mainApp.getReleases());
        break;
    }
    // Set the list label
    listViewLabel.setText(listType);
    // Show the pane containing the list if not already
    if (splitPane.getItems().size() < 2) {
      splitPane.getItems().add(0, listViewPane);
    }
  }

  public AgileItem getSelected(){
    String listType = Settings.currentListType;
    switch (listType) {
      case "Projects":
        if (!isListShown || listType != "Projects") {
          return null;
        }
        return selectedItem;
      case "People":
        if (!isListShown || listType != "People") {
          return null;
        }
        return selectedItem;
      case "Skills":
        if (!isListShown || listType != "Skills") {
          return null;
        }
        return selectedItem;
      case "Teams":
        if (!isListShown || listType != "Teams") {
          return null;
        }
        return selectedItem;
      case "Releases":
        if (!isListShown || listType != "Releases") {
          return null;
        }
        return  selectedItem;
    }
    return null;
  }

  public String getCurrentListType() {
    return Settings.currentListType;
  }

  /**
   * Takes the current selected item (next) as an AgileItem and then casts it to the correct object
   * based on what the current selected list is.
   * @param next
   */
  public void displayInfo(AgileItem next) {
    if (Settings.currentListType == "People") {
      sampleTextArea.clear();
      Person person = (Person) next;
      sampleTextArea.appendText("Person Information \nPerson ID: ");
      sampleTextArea.appendText(person.getPersonID());
      sampleTextArea.appendText("\nFirst Name: ");
      sampleTextArea.appendText(person.getFirstName());
      sampleTextArea.appendText("\nLast Name: ");
      sampleTextArea.appendText(person.getLastName());
      sampleTextArea.appendText("\nTeam: ");
      if (person.isInTeam()) {
        sampleTextArea.appendText(person.getTeamID());
      } else {
        sampleTextArea.appendText("Not assigned.");
      }
      sampleTextArea.appendText("\nSkills: ");
      StringBuilder listOfSkills = new StringBuilder();
      for (Skill skill : person.getSkillSet()) {
        listOfSkills.append(skill.getSkillName());
        listOfSkills.append(", ");
      }
      if (listOfSkills.length() == 0) {
        sampleTextArea.appendText("No known skills. Please add skills to this person.");
      } else {
        sampleTextArea.appendText(listOfSkills.substring(0, listOfSkills.length() - 2));
      }
    } else if (Settings.currentListType == "Projects") {
      sampleTextArea.clear();
      Project project = (Project) next;
      sampleTextArea.appendText("Project Information \nProject ID: ");
      sampleTextArea.appendText(project.getProjectID());
      sampleTextArea.appendText("\nProject Name: ");
      sampleTextArea.appendText(project.getProjectName());
      sampleTextArea.appendText("\nProject Description: \n");
      sampleTextArea.appendText(project.getProjectDescription());
      sampleTextArea.appendText("\nAssigned Teams: \n");
      for (AgileHistory team : project.getTeam()) {
        sampleTextArea.appendText("" + team.toString() + "\n");
        //sampleTextArea.appendText(": Start Date: " + team.getStartDate().toString() + " End Date: ");
        //sampleTextArea.appendText(team.getEndDate().toString() + "\n");
        }


    } else if (Settings.currentListType == "Skills") {
      sampleTextArea.clear();
      Skill skill = (Skill) next;
      sampleTextArea.appendText("Skill Information \nSkill Name: ");
      sampleTextArea.appendText(skill.getSkillName().toString());
      sampleTextArea.appendText("\nSkill Description: ");
      sampleTextArea.appendText(skill.getSkillDescription());

    } else if (Settings.currentListType == "Teams") {
      sampleTextArea.clear();
      Team team = (Team) next; //Casts next as Team object
      sampleTextArea.appendText("Team Information \nTeam ID: ");
      sampleTextArea.appendText(team.getTeamID());
      sampleTextArea.appendText("\nTeam Description: ");
      sampleTextArea.appendText(team.getTeamDescription());
      sampleTextArea.appendText("\nTeam Members: \n");
      for (Person member : team.getTeamMembers()) {
        sampleTextArea.appendText(member.getFirstName());
        sampleTextArea.appendText(" - ");
        sampleTextArea.appendText(member.getPersonID());
        sampleTextArea.appendText(" Role: ");
        Role role = team.getMembersRole().get(member);
        if (role != null) {
          sampleTextArea.appendText(role.toString());
        } else {
          sampleTextArea.appendText("Not assigned to a role yet.");
        }
        sampleTextArea.appendText("\n");
      }
    } else if (Settings.currentListType == "Releases") {
      sampleTextArea.clear();
      Release release = (Release) next;
      sampleTextArea.appendText("Release Information \nRelease Name: ");
      sampleTextArea.appendText(release.getReleaseName());
      sampleTextArea.appendText("\nRelease Description: ");
      sampleTextArea.appendText(release.getReleaseDescription());
      sampleTextArea.appendText("\nRelease Date: ");
      sampleTextArea.appendText(release.getReleaseDate().format(
          DateTimeFormatter.ofPattern("dd/MM/yyyy")));
      sampleTextArea.appendText("\nRelease Notes: ");
      sampleTextArea.appendText(release.getReleaseNotes());
      sampleTextArea.appendText("\nProject: ");
      sampleTextArea.appendText(release.getProjectRelease().toString());
    }
  }

  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }
}
