package seng302.group5.model.util;

import java.io.File;

/**
 * Default settings for program, contains static variables for checking states of elements in program.
 * Created by Michael on 3/19/2015.
 */
public class Settings {

  public static File defaultFilepath;
  public static File currentFile;
  public static String currentListType;

  /**
   * Sets default scrumfinity filepath for open/load.
   */
  public static void setSysDefault() {
    File scrumHome;
    String directory;

    directory = System.getProperty("user.home");
    directory = directory + File.separator + "Scrumfinity";
    scrumHome = new File(directory);

//    if (!scrumHome.exists()) {
//      scrumHome.mkdir();
//    }

    defaultFilepath = scrumHome;
  }
}
