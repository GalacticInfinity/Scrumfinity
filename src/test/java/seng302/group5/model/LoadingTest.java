package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

import seng302.group5.Main;

/**
 * Created by Michael on 4/23/2015.
 */
public class LoadingTest {
  NewSaving saving;
  NewLoading loading;
  Main savedMain;
  Main loadedMain;
  Project project;
  Person person;



  @Before
  public void setUp() {
    savedMain = new Main();
    loadedMain = new Main();

  }

  public void createVanillaProjects() {
    project = new Project();
    project.setProjectID("Project1");
    project.setProjectName("No Project Description");
    savedMain.addProject(project);
    project = new Project();
    project.setProjectID("Project2");
    project.setProjectName("Has Description");
    project.setProjectDescription("Proj Descroption");
    savedMain.addProject(project);
    project = new Project();
    project.setProjectID("Project3");
    project.setProjectName("Back to no description");
    savedMain.addProject(project);
  }

  public void createVanillaPeople() {
    person = new Person();
    person.setPersonID("Person1");
    person.setFirstName("Only first name");
    savedMain.addPerson(person);
    person = new Person();
    person.setPersonID("Person2");
    person.setFirstName("Both first");
    person.setLastName("And last");
    savedMain.addPerson(person);
    person = new Person();
    person.setPersonID("Person3");
    person.setLastName("Only last name");
    savedMain.addPerson(person);
  }

  @Test
  public void vanillaProjectLoadTest() {
    createVanillaProjects();
    saving = new NewSaving(savedMain);
    File file = new File(System.getProperty("user.dir")
                         + File.separator
                         + "VanillaProjectSave.xml");
    saving.saveData(file);

    loading = new NewLoading(loadedMain);
    loading.loadFile(file);

    assertEquals(loadedMain.getProjects().get(0).getProjectID(), "Project1");
    assertEquals(loadedMain.getProjects().get(0).getProjectName(), "No Project Description");
    assertEquals(loadedMain.getProjects().get(1).getProjectID(), "Project2");
    assertEquals(loadedMain.getProjects().get(1).getProjectName(), "Has Description");
    assertEquals(loadedMain.getProjects().get(1).getProjectDescription(), "Proj Descroption");
    assertEquals(loadedMain.getProjects().get(2).getProjectID(), "Project3");
    assertEquals(loadedMain.getProjects().get(2).getProjectName(), "Back to no description");

    file.delete();
  }

  @Test
  public void vanillaPeopleTest() {
    createVanillaPeople();
    saving = new NewSaving(savedMain);
    File file = new File(System.getProperty("user.dir")
                         + File.separator
                         + "VanillaPersonSave.xml");
    saving.saveData(file);

    loading = new NewLoading(loadedMain);
    loading.loadFile(file);

    person = new Person();
    assertEquals(loadedMain.getPeople().get(0).getPersonID(), "Person1");
    assertEquals(loadedMain.getPeople().get(0).getFirstName(), "Only first name");
    assertEquals(loadedMain.getPeople().get(1).getPersonID(), "Person2");
    assertEquals(loadedMain.getPeople().get(1).getFirstName(), "Both first");
    assertEquals(loadedMain.getPeople().get(1).getLastName(), "And last");
    assertEquals(loadedMain.getPeople().get(2).getPersonID(), "Person3");
    assertEquals(loadedMain.getPeople().get(2).getLastName(), "Only last name");

    file.delete();
  }
}
