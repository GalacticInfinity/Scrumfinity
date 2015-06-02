package seng302.group5.model.util;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.model.AgileHistory;
import seng302.group5.model.Backlog;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Skill;
import seng302.group5.model.Story;
import seng302.group5.model.Team;

/**
 * Testing of report writing.
 * Created by Craig Barnard on 29/05/2015.
 */
public class ReportWriterTest {

  private Main mainApp;
  ReportWriter report;
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
  Story story1;
  Story story2;
  Story story3;
  Backlog backlog1;
  Backlog backlog2;
  Backlog backlog3;

  @Before
  public void setUp() {
    this.mainApp = new Main();
  }

  public void createVanillaProjects() {
    project1 = new Project();
    project1.setLabel("Project1");
    project1.setProjectName("No Project Description");
    mainApp.addProject(project1);
    project2 = new Project();
    project2.setLabel("Project2");
    project2.setProjectName("Has Description");
    project2.setProjectDescription("Proj Descroption");
    mainApp.addProject(project2);
    project3 = new Project();
    project3.setLabel("Project3");
    project3.setProjectName("Back to no description");
    mainApp.addProject(project3);
  }

  public void createVanillaPeople() {
    person1 = new Person();
    person1.setLabel("Person1");
    person1.setFirstName("Only first name");
    mainApp.addPerson(person1);
    person2 = new Person();
    person2.setLabel("Person2");
    person2.setFirstName("Both first");
    person2.setLastName("And last");
    mainApp.addPerson(person2);
    person3 = new Person();
    person3.setLabel("Person3");
    person3.setLastName("Only last name");
    mainApp.addPerson(person3);
  }

  public void createSkillsWithDependency() {
    skill1 = new Skill();
    skill1.setLabel("Skill1");
    skill1.setSkillDescription("Skill description 1");
    mainApp.addSkill(skill1);
    skill2 = new Skill();
    skill2.setLabel("Skill2");
    mainApp.addSkill(skill2);
    skill3 = new Skill();
    skill3.setLabel("Skill3");
    skill3.setSkillDescription("Skill description 3");
    mainApp.addSkill(skill3);

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
    team1 = new Team();
    team1.setLabel("Team1");
    team1.setTeamDescription("Description Team1");
    memberList1 = FXCollections.observableArrayList();
    memberList1.addAll(person1);
    team1.setTeamMembers(memberList1);
    mainApp.addTeam(team1);
    team2 = new Team();
    team2.setLabel("Team2");
    memberList2 = FXCollections.observableArrayList();
    memberList2.addAll(person3, person2);
    team2.setTeamMembers(memberList2);
    mainApp.addTeam(team2);
    team3 = new Team();
    team3.setLabel("Team3");
    team3.setTeamDescription("Description Team2");
    mainApp.addTeam(team3);
  }

  public void createReleaseWithDependency() {
    release1 = new Release();
    release1.setLabel("Release1");
    release1.setReleaseDescription("Description Release1");
    release1.setReleaseNotes("Notes Release1");
    release1.setProjectRelease(project3);
    release1.setReleaseDate(LocalDate.of(1765, 10, 27));
    mainApp.addRelease(release1);
    release2 = new Release();
    release2.setLabel("Release2");
    release2.setReleaseDescription("Description Release2");
    release2.setReleaseNotes("Notes Release2");
    release2.setProjectRelease(project1);
    release2.setReleaseDate(LocalDate.of(3602, 1, 5));
    mainApp.addRelease(release2);
    release3 = new Release();
    release3.setLabel("Release3");
    release3.setReleaseDescription("Description Release3");
    release3.setReleaseNotes("Notes Release3");
    release3.setProjectRelease(project1);
    release3.setReleaseDate(LocalDate.of(1765, 10, 27));
    mainApp.addRelease(release3);
  }

  public void createProjectsWithDependency() {
    project1 = new Project();
    project1.setLabel("Project1");
    project1.setProjectName("Name Project1");
    project1.setProjectDescription("Description Project1");
    project1.getAllocatedTeams().add(new AgileHistory(team1,
                                                      LocalDate.of(2010, Month.APRIL, 3),
                                                      LocalDate.of(2010, Month.APRIL, 3)));

    mainApp.addProject(project1);
    project2 = new Project();
    project2.setLabel("Project2");
    project2.setProjectName("Name Project2");
    project2.setProjectDescription("Description Project2");
    project2.getAllocatedTeams().add(new AgileHistory(team2,
                                                      LocalDate.of(2010, Month.APRIL, 3),
                                                      LocalDate.of(2014, Month.DECEMBER, 30)));
    mainApp.addProject(project2);
    project3 = new Project();
    project3.setLabel("Project3");
    project3.setProjectName("Name Project3");
    project3.setProjectDescription("Description Project3");
    project3.getAllocatedTeams().add(new AgileHistory(team3,
                                                      LocalDate.of(2012, Month.APRIL, 5),
                                                      null));
    mainApp.addProject(project3);
  }

  public void createStories() {
    story1 = new Story("Story1", "Starter Story", "Huehuehuehue", person1, null); //TODO lol its a null
    mainApp.addStory(story1);

    story2 = new Story();
    story2.setLabel("Story2");
    story2.setStoryName("Moar Story");
    story2.setCreator(person2);
    mainApp.addStory(story2);

    story3 = new Story();
    story3.setLabel("Story3");
    story3.setDescription("They story-ening is now");
    story3.setCreator(person1);
    mainApp.addStory(story3);
  }

  public void createBacklogs() {
    backlog1 = new Backlog("Backlog1", "Starter Backlog", "Huehuehuehue", person1);
    backlog1.addStory(story1);
    mainApp.addBacklog(backlog1);

    backlog2 = new Backlog("Backlog2", "Another Backlog", "Huehuehuehuehue", person2);
    backlog2.addStory(story2);
    mainApp.addBacklog(backlog2);

    backlog3 = new Backlog("Backlog3", "Another another Backlog", "DescriptionBack", person3);
    backlog3.addStory(story3);
    mainApp.addBacklog(backlog3);
  }

  public void allocateTeams() {
    teamHistory1 = new AgileHistory(team1, LocalDate.of(2000, 3, 4), LocalDate.of(2000, 3, 5));
    project1.addTeam(teamHistory1);
    teamHistory2 = new AgileHistory(team3, LocalDate.of(1860, 5, 12), LocalDate.of(1861, 2, 17));
    teamHistory3 = new AgileHistory(team3, LocalDate.of(1861, 2, 18), LocalDate.of(1861, 2, 19));
    project2.addTeam(teamHistory2);
    project2.addTeam(teamHistory3);
  }

  @Test
  public void testAllReport() {
    createVanillaPeople();
    createSkillsWithDependency();
    createStories();
    createTeamWithDependency();
    createProjectsWithDependency();
    createReleaseWithDependency();

    report = new ReportWriter();
    File file = new File(System.getProperty("user.dir")
                                     + File.separator
                                     + "ReportTestAll.xml");
    report.writeReport(mainApp, file);



  }

}
