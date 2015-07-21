package seng302.group5.controller.dialogControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * Created by Michael and Shingy on 7/22/2015.
 */
public class DependantsDialogController {

  @FXML private Button btnAddStory;
  @FXML private ListView<?> dependantStories;
  @FXML private ListView<?> AllStoriesList;
  @FXML private Button btnRemoveStory;

  @FXML
  void btnAddStoryClick(ActionEvent event) {
    //Cyclin dependency checks
  }

  @FXML
  void btnRemoveStoryClick(ActionEvent event) {
    // Remove the bugger
  }
}
