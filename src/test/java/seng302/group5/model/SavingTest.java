package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.model.util.Settings;

/**
 * Created by Michael on 5/13/2015.
 */
public class SavingTest {

  @Test
  public void testDirCreation() {
    Settings.setSysDefault();
    if (!Settings.defaultFilepath.exists()) {
      fail();
    }
  }
}
