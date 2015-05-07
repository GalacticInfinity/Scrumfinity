package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.model.util.Settings;

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
  AgileHistory teamHistory1;
  AgileHistory teamHistory2;
  AgileHistory teamHistory3;

  @Before
  public void setUp() {
    savedMain = new Main();
    loadedMain = new Main();

  }

  public void createVanillaProjects() {
    project1 = new Project();
    project1.setLabel("Project1");
    project1.setProjectName("No Project Description");
    savedMain.addProject(project1);
    project2 = new Project();
    project2.setLabel("Project2");
    project2.setProjectName("Has Description");
    project2.setProjectDescription("Proj Descroption");
    savedMain.addProject(project2);
    project3 = new Project();
    project3.setLabel("Project3");
    project3.setProjectName("Back to no description");
    savedMain.addProject(project3);
  }

  public void createVanillaPeople() {
    person1 = new Person();
    person1.setLabel("Person1");
    person1.setFirstName("Only first name");
    savedMain.addPerson(person1);
    person2 = new Person();
    person2.setLabel("Person2");
    person2.setFirstName("Both first");
    person2.setLastName("And last");
    savedMain.addPerson(person2);
    person3 = new Person();
    person3.setLabel("Person3");
    person3.setLastName("Only last name");
    savedMain.addPerson(person3);
  }

  public void createSkillsWithDependency() {
    skill1 = new Skill();
    skill1.setLabel("Skill1");
    skill1.setSkillDescription("Skill description 1");
    savedMain.addSkill(skill1);
    skill2 = new Skill();
    skill2.setLabel("Skill2");
    savedMain.addSkill(skill2);
    skill3 = new Skill();
    skill3.setLabel("Skill3");
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
    team1.setLabel("Team1");
    team1.setTeamDescription("Description Team1");
    memberList1 = FXCollections.observableArrayList();
    memberList1.addAll(person1);
    team1.setTeamMembers(memberList1);
    savedMain.addTeam(team1);
    team2 = new Team();
    team2.setLabel("Team2");
    memberList2 = FXCollections.observableArrayList();
    memberList2.addAll(person3, person2);
    team2.setTeamMembers(memberList2);
    savedMain.addTeam(team2);
    team3 = new Team();
    team3.setLabel("Team3");
    team3.setTeamDescription("Description Team2");
    savedMain.addTeam(team3);
  }

  public void createReleaseWithDependency() {
    createVanillaProjects();
    release1 = new Release();
    release1.setLabel("Release1");
    release1.setReleaseDescription("Description Release1");
    release1.setReleaseNotes("Notes Release1");
    release1.setProjectRelease(project3);
    release1.setReleaseDate(LocalDate.of(1765, 10, 27));
    savedMain.addRelease(release1);
    release2 = new Release();
    release2.setLabel("Release2");
    release2.setReleaseDescription("Description Release2");
    release2.setReleaseNotes("Notes Release2");
    release2.setProjectRelease(project1);
    release2.setReleaseDate(LocalDate.of(3602, 1, 5));
    savedMain.addRelease(release2);
    release3 = new Release();
    release3.setLabel("Release3");
    release3.setReleaseDescription("Description Release3");
    release3.setReleaseNotes("Notes Release3");
    release3.setProjectRelease(project1);
    release3.setReleaseDate(LocalDate.of(1765, 10, 27));
    savedMain.addRelease(release3);
  }

  public void createProjectsWithDependency() {
    createVanillaPeople();
    createTeamWithDependency();
    project1 = new Project();
    project1.setLabel("Project1");
    project1.setProjectName("Name Project1");
    project1.setProjectDescription("Description Project1");
    project1.getAllocatedTeams().add(new AgileHistory(team1,
                                                      LocalDate.of(2010, Month.APRIL, 3),
                                                      LocalDate.of(2010, Month.APRIL, 3)));
    savedMain.addProject(project1);
    project2 = new Project();
    project2.setLabel("Project2");
    project2.setProjectName("Name Project2");
    project2.setProjectDescription("Description Project2");
    project2.getAllocatedTeams().add(new AgileHistory(team2,
                                                      LocalDate.of(2010, Month.APRIL, 3),
                                                      LocalDate.of(2014, Month.DECEMBER, 30)));
    savedMain.addProject(project2);
    project3 = new Project();
    project3.setLabel("Project3");
    project3.setProjectName("Name Project3");
    project3.setProjectDescription("Description Project3");
    project3.getAllocatedTeams().add(new AgileHistory(team3,
                                                      LocalDate.of(2012, Month.APRIL, 5),
                                                      null));
    savedMain.addProject(project3);
  }

  public void allocateTeams() {
    createVanillaProjects();
    createTeamWithDependency();
    teamHistory1 = new AgileHistory(team1, LocalDate.of(2000, 3, 4), LocalDate.of(2000, 3, 5));
    project1.addTeam(teamHistory1);
    teamHistory2 = new AgileHistory(team3, LocalDate.of(1860, 5, 12), LocalDate.of(1861, 2, 17));
    teamHistory3 = new AgileHistory(team3, LocalDate.of(1861, 2, 18), LocalDate.of(1861, 2, 19));
    project2.addTeam(teamHistory2);
    project2.addTeam(teamHistory3);
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

    assertEquals(loadedMain.getProjects().get(0).getLabel(), "Project1");
    assertEquals(loadedMain.getProjects().get(0).getProjectName(), "No Project Description");
    assertEquals(loadedMain.getProjects().get(1).getLabel(), "Project2");
    assertEquals(loadedMain.getProjects().get(1).getProjectName(), "Has Description");
    assertEquals(loadedMain.getProjects().get(1).getProjectDescription(), "Proj Descroption");
    assertEquals(loadedMain.getProjects().get(2).getLabel(), "Project3");
    assertEquals(loadedMain.getProjects().get(2).getProjectName(), "Back to no description");

    if (!file.delete()) {
      fail();
    }
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

    assertEquals(loadedMain.getPeople().get(0).getLabel(), "Person1");
    assertEquals(loadedMain.getPeople().get(0).getFirstName(), "Only first name");
    assertEquals(loadedMain.getPeople().get(1).getLabel(), "Person2");
    assertEquals(loadedMain.getPeople().get(1).getFirstName(), "Both first");
    assertEquals(loadedMain.getPeople().get(1).getLastName(), "And last");
    assertEquals(loadedMain.getPeople().get(2).getLabel(), "Person3");
    assertEquals(loadedMain.getPeople().get(2).getLastName(), "Only last name");

    if (!file.delete()) {
      fail();
    }
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
    assertEquals(skill1.getLabel(), "Skill1");
    assertEquals(skill1.getSkillDescription(), "Skill description 1");
    skill2 = loadedMain.getSkills().get(1);
    assertEquals(skill2.getLabel(), "Skill2");
    skill3 = loadedMain.getSkills().get(2);
    assertEquals(skill3.getLabel(), "Skill3");
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

    if (!file.delete()) {
      fail();
    }
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
    assertEquals("Team1", team1.getLabel());
    assertEquals("Description Team1", team1.getTeamDescription());
    assertEquals(person1, team1.getTeamMembers().get(0));
    team2 = loadedMain.getTeams().get(1);
    assertEquals("Team2", team2.getLabel());
    assertEquals(person3, team2.getTeamMembers().get(0));
    assertEquals(person2, team2.getTeamMembers().get(1));
    team3 = loadedMain.getTeams().get(2);
    assertEquals("Team3", team3.getLabel());
    assertEquals("Description Team2", team3.getTeamDescription());

    if (!file.delete()) {
      fail();
    }
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
    assertEquals("Release1", release1.getLabel());
    assertEquals("Description Release1", release1.getReleaseDescription());
    assertEquals("Notes Release1", release1.getReleaseNotes());
    assertEquals(project3, release1.getProjectRelease());
    assertEquals(LocalDate.of(1765, 10, 27).toString(),
                 release1.getReleaseDate().toString());
    release2 = loadedMain.getReleases().get(1);
    assertEquals("Release2", release2.getLabel());
    assertEquals("Description Release2", release2.getReleaseDescription());
    assertEquals("Notes Release2", release2.getReleaseNotes());
    assertEquals(project1, release2.getProjectRelease());
    assertEquals(LocalDate.of(3602, 1, 5).toString(),
                 release2.getReleaseDate().toString());
    release3 = loadedMain.getReleases().get(2);
    assertEquals("Release3", release3.getLabel());
    assertEquals("Description Release3", release3.getReleaseDescription());
    assertEquals("Notes Release3", release3.getReleaseNotes());
    assertEquals(project1, release3.getProjectRelease());
    assertEquals(LocalDate.of(1765, 10, 27).toString(),
                 release3.getReleaseDate().toString());

    if (!file.delete()) {
      fail();
    }
  }

  @Test
  public void dependantProjectTest() {
    createProjectsWithDependency();
    saving = new NewSaving(savedMain);
    File file = new File(System.getProperty("user.dir")
                         + File.separator
                         + "DependantProjectSave.xml");
    saving.saveData(file);

    loading = new NewLoading(loadedMain);
    loading.loadFile(file);

    team1 = loadedMain.getTeams().get(0);
    team2 = loadedMain.getTeams().get(1);
    team3 = loadedMain.getTeams().get(2);

    project1 = loadedMain.getProjects().get(0);
    assertEquals("Project1", project1.getLabel());
    assertEquals("Name Project1", project1.getProjectName());
    assertEquals("Description Project1", project1.getProjectDescription());
    assertEquals(team1, project1.getAllocatedTeams().get(0).getAgileItem());
    assertEquals(LocalDate.of(2010, Month.APRIL, 3), project1.getAllocatedTeams().get(0).getStartDate());
    assertEquals(LocalDate.of(2010, Month.APRIL, 3), project1.getAllocatedTeams().get(0).getEndDate());

    project2 = loadedMain.getProjects().get(1);
    assertEquals("Project2", project2.getLabel());
    assertEquals("Name Project2", project2.getProjectName());
    assertEquals("Description Project2", project2.getProjectDescription());
    assertEquals(team2, project2.getAllocatedTeams().get(0).getAgileItem());
    assertEquals(LocalDate.of(2010, Month.APRIL, 3), project2.getAllocatedTeams().get(0).getStartDate());
    assertEquals(LocalDate.of(2014, Month.DECEMBER, 30), project2.getAllocatedTeams().get(0).getEndDate());

    project3 = loadedMain.getProjects().get(2);
    assertEquals("Project3", project3.getLabel());
    assertEquals("Name Project3", project3.getProjectName());
    assertEquals("Description Project3", project3.getProjectDescription());
    assertEquals(team3, project3.getAllocatedTeams().get(0).getAgileItem());
    assertEquals(LocalDate.of(2012, Month.APRIL, 5), project3.getAllocatedTeams().get(0).getStartDate());
    assertNull(project3.getAllocatedTeams().get(0).getEndDate());

    if (!file.delete()) {
      fail();
    }
  }

  @Test
  public void testTeamAllocation() {
    allocateTeams();
    saving = new NewSaving(savedMain);
    File file = new File(System.getProperty("user.dir")
                         + File.separator
                         + "DependantReleaseSave.xml");
    saving.saveData(file);

    loading = new NewLoading(loadedMain);
    loading.loadFile(file);

    team1 = loadedMain.getTeams().get(0);
    team3 = loadedMain.getTeams().get(2);

    project1 = loadedMain.getProjects().get(0);
    assertEquals(team1, project1.getAllocatedTeams().get(0).getAgileItem());
    assertEquals(LocalDate.of(2000, 3, 4).toString(),
                 project1.getAllocatedTeams().get(0).getStartDate().toString());
    assertEquals(LocalDate.of(2000, 3, 5).toString(),
                 project1.getAllocatedTeams().get(0).getEndDate().toString());
    project2 = loadedMain.getProjects().get(1);
    assertEquals(team3, project2.getAllocatedTeams().get(0).getAgileItem());
    assertEquals(LocalDate.of(1860, 5, 12).toString(),
                 project2.getAllocatedTeams().get(0).getStartDate().toString());
    assertEquals(LocalDate.of(1861, 2, 17).toString(),
                 project2.getAllocatedTeams().get(0).getEndDate().toString());

    assertEquals(team3, project2.getAllocatedTeams().get(1).getAgileItem());
    assertEquals(LocalDate.of(1861, 2, 18).toString(),
                 project2.getAllocatedTeams().get(1).getStartDate().toString());
    assertEquals(LocalDate.of(1861, 2, 19).toString(),
                 project2.getAllocatedTeams().get(1).getEndDate().toString());

    if (!file.delete()) {
      fail();
    }
  }

  @Test
  public void testingBlankHeader() {
    Settings.organizationName = "";
    saving = new NewSaving(savedMain);
    File file = new File(System.getProperty("user.dir")
                         + File.separator
                         + "BlankHeader.xml");
    saving.saveData(file);

    loading = new NewLoading(loadedMain);
    loading.loadFile(file);

    assertEquals("", Settings.organizationName);

    if (!file.delete()) {
      fail();
    }
  }

  @Test
  public void testingFilledHeader() {
    String expectedName = "test";
    Settings.organizationName = expectedName;
    saving = new NewSaving(savedMain);
    File file = new File(System.getProperty("user.dir")
                         + File.separator
                         + "FilledHeader.xml");
    saving.saveData(file);

    Settings.organizationName = "";
    loading = new NewLoading(loadedMain);
    loading.loadFile(file);

    assertEquals(expectedName, Settings.organizationName);

    if (!file.delete()) {
      fail();
    }
  }
}
