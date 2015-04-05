package seng302.group5.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import javafx.collections.ObservableList;
import seng302.group5.Main;

/**
 * Created by Michael on 3/19/2015.
 */
public class SavingTest {

  public Main main;
  public File filez;
  public File newFile = new File(System.getProperty("user.home".concat("//Documents")), "Test1.xml");

  @Before
  public void setUp(){
    main = new Main();
    ObservableList<Skill> skillSet = null;
    skillSet.add(new Skill("C"));
    main.getPeople().add(new Person("msr51", "Mike", "Roman", skillSet));
    main.getProjects().addAll(
        new Project("xyz01", "supah proj", "This is the best thing ever"),
        new Project("xyz01", "supah proj", "This is the best thing ever"),
        new Project("xyz01", "supah proj", "This is the best thing ever"));
  }

  @Test
  public void testSavingCreate(){
    Saving.saveDataToFile(newFile, main);
    assertTrue(newFile.exists());
  }

  @Test
  public void testLoadingContents(){
    Main orig = main;
    main.getProjects().clear();
    main.getPeople().clear();
    Saving.loadDataFromFile(newFile, main);
    for (Person i: main.getPeople()) {
      assertTrue(orig.getPeople().contains(i));
    }
    for (Project i: main.getProjects()) {
      assertTrue(orig.getProjects().contains(i));
    }
  }
}
