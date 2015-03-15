package seng302.group5;

import java.io.IOException;
import java.util.GregorianCalendar;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng302.group5.controller.ListMainPaneController;
import seng302.group5.controller.MenuBarController;
import seng302.group5.model.Actor;

/**
 * Main class to run the application
 */
public class Main extends Application {

  private Stage primaryStage;
  private BorderPane rootLayout;
  // Quick test list
  private ObservableList<Actor> testGroup = FXCollections.observableArrayList(
      new Actor("bill", "likes potatoes", new GregorianCalendar(1994,5,7)),
      new Actor("bob", "likes potatoes n stuff", new GregorianCalendar(1994,5,7)),
      new Actor("Left Shark", "random text", new GregorianCalendar(1994,5,7)),
      new Actor("bobbeh", "specific text", new GregorianCalendar(1994,5,7)),
      new Actor("shellington", "lorem ipsum", new GregorianCalendar(1994,5,7)),
      new Actor("aquadude", "cant think of else", new GregorianCalendar(1994,5,7))
  );
  private ListMainPaneController LMPC;
  private MenuBarController MBC;

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

  public ObservableList<Actor> getTestGroup() {
    return  testGroup;
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

  public static void main(String[] args) {
    launch(args);
  }
}
