package seng302.group5.controller;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import seng302.group5.Main;
import seng302.group5.model.NewLoading;
import seng302.group5.model.util.Settings;

/**
 * Created by Michael on 5/8/2015.
 *
 * Controller which handles the login screen. Exists on a seperate Scene, and shows the
 * mainApp scene when user enters the program
 */


public class LoginController {

  @FXML private TextField organizationName;
  private Main mainApp;

  @FXML
  protected void btnNewLogin(ActionEvent event) {
    String setName = organizationName.getText().trim();
    if (setName != null && !setName.isEmpty()) {
      mainApp.setMainTitle(setName);
      mainApp.setMainScene();
    }
  }

  @FXML
  protected void btnLoginLoad(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Project");
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV files (*.xml)", "*.xml");
    fileChooser.getExtensionFilters().add(filter);
    if (Settings.defaultFilepath != null) {
      fileChooser.setInitialDirectory(Settings.defaultFilepath);
    }
    try {
      File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
      if (file != null) {
        Settings.currentFile = file;
        mainApp.resetAll();
        NewLoading load = new NewLoading(mainApp);
        load.loadFile(file);

        mainApp.getLMPC().refreshList();
        if (!Settings.organizationName.isEmpty()) {
          mainApp.setMainTitle("Scrumfinity - " + Settings.organizationName);
          mainApp.toggleName();
        }
        mainApp.setLastSaved(); //for revert
        mainApp.setMainScene();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }
}
