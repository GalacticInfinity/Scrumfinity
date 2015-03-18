package seng302.group5.model;

import org.junit.Test;

import java.io.File;
import java.util.List;

import seng302.group5.Main;

/**
 * Created by Michael on 3/19/2015.
 */
public class SavingTest {

  @Test
  public void testSaving(){
    Main main = new Main();
    main.getPeople().add(new Person("msr51", "Mike", "Roman"));
    main.getProjects().add(new Project("xyz01", "supah proj", "This is the best thing ever"));
    File newFile = new File(System.getProperty("user.home"), "Test1.csv");
    Saving.saveDataToFile(newFile, main);
  }
}
