package seng302.group5.model.SavingTests;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.model.Person;
import seng302.group5.model.Saving;
import seng302.group5.model.Skill;
import seng302.group5.model.Team;

import static org.junit.Assert.assertTrue;

/**
 * Created by Michael on 4/13/2015.
 */
public class TeamSavingTest {

  public File file = new File(System.getProperty("user.home".concat("//Documents")), "TeamSavingTest.xml");
  Main savedMain;
  Main loadedMain;

  @Before
  // Sets up basic test case xml.
  public void setUp(){
    savedMain = new Main();
    Person p1 = new Person("JAMES", "James", "Wildbretty", null);
    Person p2 = new Person("MALCOM", "Malcom", "Renolds", null);
    Person p3 = new Person("JAYNE", "Jane", "Pass", null);
    Person p4 = new Person("BOB", "Bob", "Bobilton", null);
    savedMain.getPeople().addAll(p1, p2, p3, p4);

    ObservableList<Person> persons1 = FXCollections.observableArrayList(p1, p4);
    ObservableList<Person> persons2 = FXCollections.observableArrayList(p2, p3);
    Team t1 = new Team("Firefly", persons1, "Couldnt help myself");
    Team t2 = new Team("Misc", persons2, "Lets go with this for now");
    savedMain.getTeams().addAll(t1, t2);

    Saving.saveDataToFile(file, savedMain);
  }

  @Test
  public void testTeamLoadConcurrency() {
    loadedMain = new Main();
    Saving.loadDataFromFile(file, loadedMain);

    Set<Person> checkPersonDuplicates = new HashSet<>(loadedMain.getPeople());
    assertTrue(checkPersonDuplicates.size() == 4);

    Set<Team> checkTeamDuplicates = new HashSet<>(loadedMain.getTeams());
    assertTrue(checkTeamDuplicates.size() == 2);

    for (Person person : loadedMain.getPeople()) {
      Boolean contains1 = loadedMain.getTeams().get(0).getTeamMembers().contains(person);
      Boolean contains2 = loadedMain.getTeams().get(1).getTeamMembers().contains(person);
      assertTrue(contains1 ^ contains2);
    }
  }

}
