package seng302.group5.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import seng302.group5.Main;
import seng302.group5.model.AgileItem;
import seng302.group5.model.util.Settings;

/**
 * Created by Michael on 3/15/2015.
 * ListManePane fxml contains styling for both List and MainPane, still
 * requires asthetic tweaks, doesn't scale properly.
 */
public class ListMainPaneController {

  @FXML private ListView listView;
  @FXML private TextArea sampleTextArea;
  private Main mainApp;
  private boolean isListHidden = true;

  public ListMainPaneController() {};
  private AgileItem selectedItem;

  /**
   * Initialise the fxml, basic setup functions called.
   */
  @FXML
  private void initialize() {
    Settings.currentListType = "Project";
    iniActorList();
    showHideList();
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
              sampleTextArea.clear();
              sampleTextArea.appendText(next.toString());
              selectedItem = next;
            }
          }
        }
    );
  }

  /**
   * Shows/Hides items in list.
   */
  public void showHideList() {
    if(!isListHidden){
      checkListType();
      isListHidden = true;
    } else {
      ObservableList<AgileItem> clear = FXCollections.observableArrayList();
      listView.setItems(clear);
      isListHidden = false;
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
    if (selectedItem != null) {
      sampleTextArea.appendText(selectedItem.toString());
    }
  }

  /**
   * Checks list type currently set as viewed list by user.
   * TODO testing
   */
  public void checkListType(){
    String listType = Settings.currentListType;
    switch (listType) {
      case "Project":
        listView.setItems(mainApp.getProjects());
        break;
      case "People":
        listView.setItems(mainApp.getPeople());
        break;
      case "Skills":
        listView.setItems(mainApp.getSkills());
        break;
    }
  }

  public Object getSelectedProject() {
    return selectedItem;
  }

  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }
}
