package seng302.group5.model.SavingTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Saving;
import seng302.group5.model.Skill;
import seng302.group5.model.Team;

/**
 * Created by Michael on 3/19/2015.
 */
public class SkillSavingTest {

  public File file = new File(System.getProperty("user.home".concat("//Documents")), "SkillSavingTest.xml");
  Main savedMain;
  Main loadedMain;

  @Before
  // Sets up basic test case xml.
  public void setUp(){
    savedMain = new Main();
    Skill skill1 = new Skill("Typing", "So revolutionary");
    Skill skill2 = new Skill("Riverdance", "From the old country");
    ObservableList<Skill> skills = FXCollections.observableArrayList();
    skills.add(skill1);
    skills.add(skill2);

    savedMain.addPerson(new Person("JAMES", "James", "Wildberry", skills));
    savedMain.addSkill(skill1);
    savedMain.addSkill(skill2);

    Saving.saveDataToFile(file, savedMain);
  }

  // Checks concurrency of main app skills and person skills.
  @Test
  public void testSkillSaving() {
    loadedMain = new Main();
    Saving.loadDataFromFile(file, loadedMain);

    assertTrue(loadedMain.getPeople().size() == 1);

    Set<Skill> checkSizeMain = new HashSet<>(loadedMain.getSkills());
    assertTrue(checkSizeMain.size() == 2);

    int numPersonSkills = 0;
    for (Skill skill : loadedMain.getSkills()) {
      assertTrue(loadedMain.getPeople().get(0).getSkillSet().contains(skill));
      numPersonSkills ++;
    }

    assertEquals(numPersonSkills, 2);
  }
}