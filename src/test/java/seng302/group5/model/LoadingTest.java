package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;

/**
 * Testing loading functionality
 * Created by Michael on 4/23/2015.
 */
public class LoadingTest {
  NewSaving saving;
  NewLoading loading;
  Main savedMain;
  Main loadedMain;
  Project project1;
  Project project2;
  Project project3;
  Person person1;
  Person person2;
  Person person3;
  Skill skill1;
  Skill skill2;
  Skill skill3;
  ObservableList<Skill> skillSet1;
  ObservableList<Skill> skillSet2;
  ObservableList<Skill> skillSet3;
  Team team1;
  Team team2;
  Team team3;
  ObservableList<Person> memberList1;
  ObservableList<Person> memberList2;
  Release release1;
  Release release2;
  Release release3;

  @Before
  public void setUp() {
    savedMain = new Main();
    loadedMain = new Main();

  }

  public void createVanillaProjects() {
    project1 = new Project();
    project1.setProjectID("Project1");
    project1.setProjectName("No Project Description");
    savedMain.addProject(project1);
    project2 = new Project();
    project2.setProjectID("Project2");
    project2.setProjectName("Has Description");
    project2.setProjectDescription("Proj Descroption");
    savedMain.addProject(project2);
    project3 = new Project();
    project3.setProjectID("Project3");
    project3.setProjectName("Back to no description");
    savedMain.addProject(project3);
  }

  public void createVanillaPeople() {
    person1 = new Person();
    person1.setPersonID("Person1");
    person1.setFirstName("Only first name");
    savedMain.addPerson(person1);
    person2 = new Person();
    person2.setPersonID("Person2");
    person2.setFirstName("Both first");
    person2.setLastName("And last");
    savedMain.addPerson(person2);
    person3 = new Person();
    person3.setPersonID("Person3");
    person3.setLastName("Only last name");
    savedMain.addPerson(person3);
  }

  public void createSkillsWithDependency() {
    skill1 = new Skill();
    skill1.setSkillName("Skill1");
    skill1.setSkillDescription("Skill description 1");
    savedMain.addSkill(skill1);
    skill2 = new Skill();
    skill2.setSkillName("Skill2");
    savedMain.addSkill(skill2);
    skill3 = new Skill();
    skill3.setSkillName("Skill3");
    skill3.setSkillDescription("Skill description 3");
    savedMain.addSkill(skill3);

    createVanillaPeople();
    skillSet1 = FXCollections.observableArrayList();
    skillSet1.add(skill2);
    person1.setSkillSet(skillSet1);
    skillSet2 = FXCollections.observableArrayList();
    skillSet2.addAll(skill1, skill3);
    person2.setSkillSet(skillSet2);
    skillSet3 = FXCollections.observableArrayList();
    skillSet3.addAll(skill3, skill2, skill1);
    person3.setSkillSet(skillSet3);
  }

  public void createTeamWithDependency() {
    createVanillaPeople();
    team1 = new Team();
    team1.setTeamID("Team1");
    team1.setTeamDescription("Description Team1");
    memberList1 = FXCollections.observableArrayList();
    memberList1.addAll(person1);
    team1.setTeamMembers(memberList1);
    savedMain.addTeam(team1);
    team2 = new Team();
    team2.setTeamID("Team2");
    memberList2 = FXCollections.observableArrayList();
    memberList2.addAll(person3, person2);
    team2.setTeamMembers(memberList2);
    savedMain.addTeam(team2);
    team3 = new Team();
    team3.setTeamID("Team3");
    team3.setTeamDescription("Description Team2");
    savedMain.addTeam(team3);
  }

  public void createReleaseWithDependency() {
    createVanillaProjects();
    release1 = new Release();
    release1.setReleaseName("Release1");
    release1.setReleaseDescription("Description Release1");
    release1.setReleaseNotes("Notes Release1");
    release1.setProjectRelease(project3);
    release1.setReleaseDate(LocalDate.of(1765, 10, 27));
    savedMain.addRelease(release1);
    release2 = new Release();
    release2.setReleaseName("Release2");
    release2.setReleaseDescription("Description Release2");
    release2.setReleaseNotes("Notes Release2");
    release2.setProjectRelease(project1);
    release2.setReleaseDate(LocalDate.of(3602, 01, 05));
    savedMain.addRelease(release2);
    release3 = new Release();
    release3.setReleaseName("Release3");
    release3.setReleaseDescription("Description Release3");
    release3.setReleaseNotes("Notes Release3");
    release3.setProjectRelease(project1);
    release3.setReleaseDate(LocalDate.of(1765, 10, 27));
    savedMain.addRelease(release3);
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

    assertEquals(loadedMain.getPeople().get(0).getPersonID(), "Person1");
    assertEquals(loadedMain.getPeople().get(0).getFirstName(), "Only first name");
    assertEquals(loadedMain.getPeople().get(1).getPersonID(), "Person2");
    assertEquals(loadedMain.getPeople().get(1).getFirstName(), "Both first");
    assertEquals(loadedMain.getPeople().get(1).getLastName(), "And last");
    assertEquals(loadedMain.getPeople().get(2).getPersonID(), "Person3");
    assertEquals(loadedMain.getPeople().get(2).getLastName(), "Only last name");

    file.delete();
  }

  @Test
  public void dependantSkillTest() {
    createSkillsWithDependency();
    saving = new NewSaving(savedMain);
    File file = new File(System.getProperty("user.dir")
                         + File.separator
                         + "DependantSkillSave.xml");
    saving.saveData(file);

    loading = new NewLoading(loadedMain);
    loading.loadFile(file);

    // Check skills loaded properly
    skill1 = loadedMain.getSkills().get(0);
    assertEquals(skill1.getSkillName(), "Skill1");
    assertEquals(skill1.getSkillDescription(), "Skill description 1");
    skill2 = loadedMain.getSkills().get(1);
    assertEquals(skill2.getSkillName(), "Skill2");
    skill3 = loadedMain.getSkills().get(2);
    assertEquals(skill3.getSkillName(), "Skill3");
    assertEquals(skill3.getSkillDescription(), "Skill description 3");


    person1 = loadedMain.getPeople().get(0);
    assertEquals(person1.getSkillSet().get(0), skill2);
    person2 = loadedMain.getPeople().get(1);
    assertEquals(person2.getSkillSet().get(0), skill1);
    assertEquals(person2.getSkillSet().get(1), skill3);
    person2 = loadedMain.getPeople().get(2);
    assertEquals(person3.getSkillSet().get(0), skill3);
    assertEquals(person3.getSkillSet().get(1), skill2);
    assertEquals(person3.getSkillSet().get(2), skill1);

    file.delete();
  }

  @Test
  public void dependantTeamTest() {
    createTeamWithDependency();
    saving = new NewSaving(savedMain);
    File file = new File(System.getProperty("user.dir")
                         + File.separator
                         + "DependantTeamSave.xml");
    saving.saveData(file);

    loading = new NewLoading(loadedMain);
    loading.loadFile(file);

    person1 = loadedMain.getPeople().get(0);
    person2 = loadedMain.getPeople().get(1);
    person3 = loadedMain.getPeople().get(2);

    team1 = loadedMain.getTeams().get(0);
    assertEquals("Team1", team1.getTeamID());
    assertEquals("Description Team1", team1.getTeamDescription());
    assertEquals(person1, team1.getTeamMembers().get(0));
    team2 = loadedMain.getTeams().get(1);
    assertEquals("Team2", team2.getTeamID());
    assertEquals(person3, team2.getTeamMembers().get(0));
    assertEquals(person2, team2.getTeamMembers().get(1));
    team3 = loadedMain.getTeams().get(2);
    assertEquals("Team3", team3.getTeamID());
    assertEquals("Description Team2", team3.getTeamDescription());

    file.delete();
  }

  @Test
  public void dependantReleaseTest() {
    createReleaseWithDependency();
    saving = new NewSaving(savedMain);
    File file = new File(System.getProperty("user.dir")
                         + File.separator
                         + "DependantReleaseSave.xml");
    saving.saveData(file);

    loading = new NewLoading(loadedMain);
    loading.loadFile(file);

    project1 = loadedMain.getProjects().get(0);
    project2 = loadedMain.getProjects().get(1);
    project3 = loadedMain.getProjects().get(2);

    release1 = loadedMain.getReleases().get(0);
    assertEquals("Release1", release1.getReleaseName());
    assertEquals("Description Release1", release1.getReleaseDescription());
    assertEquals("Notes Release1", release1.getReleaseNotes());
    assertEquals(project3, release1.getProjectRelease());
    assertEquals(LocalDate.of(1765, 10, 27).toString(),
                 release1.getReleaseDate().toString());
    release2 = loadedMain.getReleases().get(1);
    assertEquals("Release2", release2.getReleaseName());
    assertEquals("Description Release2", release2.getReleaseDescription());
    assertEquals("Notes Release2", release2.getReleaseNotes());
    assertEquals(project1, release2.getProjectRelease());
    assertEquals(LocalDate.of(3602, 01, 05).toString(),
                 release2.getReleaseDate().toString());
    assertEquals("Release3", release3.getReleaseName());
    assertEquals("Description Release3", release3.getReleaseDescription());
    assertEquals("Notes Release3", release3.getReleaseNotes());
    assertEquals(project1, release3.getProjectRelease());
    assertEquals(LocalDate.of(1765, 10, 27).toString(),
                 release3.getReleaseDate().toString());

    file.delete();
  }
}
