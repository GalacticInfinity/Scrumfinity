package seng302.group5.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import seng302.group5.Main;

/**
 * Created by Michael on 3/15/2015.
 * Controller for MenuBar
 */
public class MenuBarController {

  @FXML private MenuItem showListMenuItem;
  private Main mainApp;

  /**
   * Tells ListMainPaneController to hide/show the item list based on current state
   * (stored in ListMainPaneController)
   */
  @FXML
  protected void showHideList(ActionEvent event) {
    mainApp.getLMPC().showHideList();
  }

  /**
   * Fxml import for quit button, closes application on click
   */
  @FXML
  protected void btnQuitClick(ActionEvent event){
    System.exit(0);
  }

  public void setMainApp(Main mainApp){
    this.mainApp = mainApp;
  }

}
