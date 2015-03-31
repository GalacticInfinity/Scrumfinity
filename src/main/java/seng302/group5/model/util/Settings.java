package seng302.group5.model.util;

import java.io.File;

/**
 * Created by Michael on 3/19/2015.
 */
public class Settings {

  public static File defaultFilepath;
  public static String currentListType;

  public static void setSysDefault() {
    File scrumHome;
    String dirz;

    dirz = System.getProperty("user.home");
    dirz = dirz + File.separator + "Scrumfinity";
    scrumHome = new File(dirz);

    if (!scrumHome.exists()) {
      scrumHome.mkdir();
    };

    defaultFilepath = scrumHome;
  }
}
