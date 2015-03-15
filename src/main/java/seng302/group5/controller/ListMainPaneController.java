package seng302.group5.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import seng302.group5.Main;
import seng302.group5.model.Actor;

/**
 * Created by Michael on 3/15/2015.
 * ListManePane fxml contains styling for both List and MainPane, still
 * requires asthetic tweaks, doesn't scale properly
 */
public class ListMainPaneController {

  @FXML private ListView listView;
  @FXML private TextArea sampleTextArea;
  private Main mainApp;
  private boolean isListHidden = false;

  public ListMainPaneController() {};

  /**
   * Initialise the fxml, basic setup functions called.
   */
  @FXML
  private void initialize() {
    iniActorList();
  }

  /**
   * Sets listeners to whatever is in the list. TODO create item interface
   */
  private void iniActorList() {
    listView.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<Actor>() {
          @Override
          public void changed(ObservableValue<? extends Actor> observableValue,
                              Actor previous, Actor next) {
            if (next != null) {
              // Will place checks to update main pane here based on item type selected
              sampleTextArea.clear();
              sampleTextArea.appendText(next.getActorDescription());
            }
          }
        }
    );
  }

  /**
   * Shows/Hides items in list. TODO check what type list is currently showing
   */
  public void showHideList() {
    if(!isListHidden){
      // This part will need extending
      listView.setItems(mainApp.getTestGroup());
      isListHidden = true;
    } else {
      ObservableList<Actor> clear = FXCollections.observableArrayList();
      listView.setItems(clear);
      isListHidden = false;
    }
  }

  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }
}
