package seng302.group5.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.model.AgileHistory;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Role;
import seng302.group5.model.Skill;
import seng302.group5.model.Story;
import seng302.group5.model.Team;

/**
 * Created by @author Alex Woo
 */
public class RevertHandler {

  private Main mainApp;

  private ObservableList<Project> projectsLastSaved;
  private ObservableList<Team> teamsLastSaved;
  private ObservableList<Skill> skillsLastSaved;
  private ObservableList<Person> peopleLastSaved;
  private ObservableList<Release> releasesLastSaved;
  private ObservableList<Role> rolesLastSaved;
  private ObservableList<Story> storiesLastSaved;

  /**
   * Constructor. Set the main app to communicate with and initialise lists
   *
   * @param mainApp Main app to communicate with
   */
  public RevertHandler(Main mainApp) {
    this.mainApp = mainApp;
    this.projectsLastSaved = FXCollections.observableArrayList();
    this.teamsLastSaved = FXCollections.observableArrayList();
    this.skillsLastSaved = FXCollections.observableArrayList();
    this.peopleLastSaved = FXCollections.observableArrayList();
    this.releasesLastSaved = FXCollections.observableArrayList();
    this.rolesLastSaved = FXCollections.observableArrayList();
    this.storiesLastSaved = FXCollections.observableArrayList();
  }


  /**
   * Used when the revert button is pushed.
   * It undoes until it is in the previously saved state. IT then clears the undo and redo stacks
   */
  public void revert() {
    mainApp.getProjects().clear();
    for (Project project : projectsLastSaved){
      mainApp.getProjects().add(new Project(project));
    }

    mainApp.getTeams().clear();
    for (Team team : teamsLastSaved) {
      mainApp.getTeams().add(new Team(team));
    }

    mainApp.getPeople().clear();
    for (Person person : peopleLastSaved) {
      mainApp.getPeople().add(new Person(person));
    }

    mainApp.getSkills().clear();
    for (Skill skill : skillsLastSaved) {
      mainApp.getSkills().add(new Skill(skill));
    }

    mainApp.getReleases().clear();
    for (Release release: releasesLastSaved) {
      mainApp.getReleases().add(new Release(release));
    }

    mainApp.getRoles().clear();
    for (Role role : rolesLastSaved) {
      mainApp.getRoles().add(new Role(role));
    }

    mainApp.getStories().clear();
    for (Story story : storiesLastSaved) {
      mainApp.getStories().add(new Story(story));
    }

    // Ensure data in main refer to each other
    syncPeopleWithSkills(mainApp.getPeople(), mainApp.getSkills());
    syncTeamsWithPeople(mainApp.getTeams(), mainApp.getPeople(), mainApp.getRoles());
    syncProjectsWithTeams(mainApp.getProjects(), mainApp.getTeams());
    syncReleasesWithProjects(mainApp.getReleases(), mainApp.getProjects());
    syncRolesWithSkills(mainApp.getRoles(), mainApp.getSkills());
    syncStoriesWithPeople(mainApp.getStories(), mainApp.getPeople());

    mainApp.refreshLastSaved();
    mainApp.refreshList();
  }

  /**
   * Makes the last saved lists copy the current state list.
   * used when saving so that a revert maybe done.
   */
  public void setLastSaved() {

    projectsLastSaved.clear();
    for (Project project : mainApp.getProjects()) {
      projectsLastSaved.add(new Project(project));
    }

    teamsLastSaved.clear();
    for (Team team : mainApp.getTeams()) {
      teamsLastSaved.add(new Team(team));
    }

    peopleLastSaved.clear();
    for (Person person : mainApp.getPeople()) {
      peopleLastSaved.add(new Person(person));
    }

    skillsLastSaved.clear();
    for (Skill skill : mainApp.getSkills()) {
      skillsLastSaved.add(new Skill(skill));
    }

    releasesLastSaved.clear();
    for (Release release : mainApp.getReleases()) {
      releasesLastSaved.add(new Release(release));
    }

    rolesLastSaved.clear();
    for (Role role : mainApp.getRoles()) {
      rolesLastSaved.add(new Role(role));
    }

    storiesLastSaved.clear();
    for (Story story : mainApp.getStories()) {
      storiesLastSaved.add(new Story(story));
    }

    // Ensure data in the copies refer to each other
    syncPeopleWithSkills(peopleLastSaved, skillsLastSaved);
    syncTeamsWithPeople(teamsLastSaved, peopleLastSaved, rolesLastSaved);
    syncProjectsWithTeams(projectsLastSaved, teamsLastSaved);
    syncReleasesWithProjects(releasesLastSaved, projectsLastSaved);
    syncRolesWithSkills(rolesLastSaved, skillsLastSaved);
    syncStoriesWithPeople(storiesLastSaved, peopleLastSaved);
  }

  /**
   * Creates proper object reference between people and skills.
   *
   * @param people Reference Person objects to link together
   * @param skills Reference Skill objects to link together
   */
  private void syncPeopleWithSkills(List<Person> people, List<Skill> skills) {
    Map<String, Skill> skillMap = new HashMap<>();

    for (Skill mainSkill : skills) {
      skillMap.put(mainSkill.getLabel(), mainSkill);
    }

    // For every available person
    for (Person person : people) {
      List<Skill> skillArray = new ArrayList<>();
      // For every skill in that person
      for (Skill personSkill : person.getSkillSet()) {
        // For every skill in main app
        skillArray.add(skillMap.get(personSkill.getLabel()));
      }
      person.getSkillSet().setAll(skillArray);
    }
  }

  /**
   * Creates concurrency between people in main app and people in teams.
   *
   * @param teams Reference Team objects to link together
   * @param people Reference Person objects to link together
   * @param roles Reference Role objects to link together
   */
  private void syncTeamsWithPeople(List<Team> teams, List<Person> people, List<Role> roles) {
    Map<String, Person> personMap = new HashMap<>();
    Map<String, Role> roleMap = new HashMap<>();

    for (Person mainPerson : people) {
      personMap.put(mainPerson.getLabel(), mainPerson);
    }
    for (Role mainRole : roles) {
      roleMap.put(mainRole.getLabel(), mainRole);
    }

    // For every available team
    for (Team team : teams) {
      List<Person> personArray = new ArrayList<>();
      Map<Person, Role> personRoleMap = new HashMap<>();
      // For every person in that team
      for (Person teamPerson : team.getTeamMembers()) {
        // For every person that is in Main App
        Person mainPerson = personMap.get(teamPerson.getLabel());
        Role mainRole = null;
        if (team.getMembersRole().get(teamPerson) != null) {
          mainRole = roleMap.get(team.getMembersRole().get(teamPerson).getLabel());
        }
        personArray.add(mainPerson);
        personRoleMap.put(mainPerson, mainRole);
      }
      team.getTeamMembers().clear();
      team.getMembersRole().clear();
      for (Person person : personArray) {
        person.assignToTeam(team);
        team.getTeamMembers().add(person);
        team.getMembersRole().put(person, personRoleMap.get(person));
      }
    }
  }

  /**
   * Creates proper object reference between projects and skills.
   *
   * @param projects Reference Project objects to link together
   * @param teams Reference Team objects to link together
   */
  private void syncProjectsWithTeams(List<Project> projects, List<Team> teams) {
    Map<String, Team> teamMap = new HashMap<>();

    for (Team mainTeam : teams) {
      teamMap.put(mainTeam.getLabel(), mainTeam);
    }

    // For every available project
    for (Project project : projects) {
      List<AgileHistory> agileHistoryArray = new ArrayList<>();
      // For every team in that project
      for (AgileHistory projectAH : project.getAllocatedTeams()) {
        // For every team that is in Main App
        Team projectTeam = (Team) projectAH.getAgileItem();
        Team mainTeam = teamMap.get(projectTeam.getLabel());

        agileHistoryArray.add(new AgileHistory(mainTeam,
                                               projectAH.getStartDate(),
                                               projectAH.getEndDate()));

      }
      project.getAllocatedTeams().setAll(agileHistoryArray);
      // TODO: TALK TO TEAM ABOUT currentProject in Team object.
    }
  }

  /**
   * Creates proper object reference between releases and projects.
   *
   * @param releases Reference Release objects to link together
   * @param projects Reference Project objects to link together
   */
  private void syncReleasesWithProjects(List<Release> releases, List<Project> projects) {
    Map<String, Project> projectMap = new HashMap<>();

    for (Project mainProject : projects) {
      projectMap.put(mainProject.getLabel(), mainProject);
    }

    // For every available release
    for (Release release : releases) {
      Project mainProject = projectMap.get(release.getProjectRelease().getLabel());
      release.setProjectRelease(mainProject);
    }
  }

  /**
   * Creates proper object reference between roles and required skills.
   *
   * @param roles Reference Role objects to link together
   * @param skills Reference Skill objects to link together
   */
  private void syncRolesWithSkills(List<Role> roles, List<Skill> skills) {
    Map<String, Skill> skillMap = new HashMap<>();

    for (Skill mainSkill : skills) {
      skillMap.put(mainSkill.getLabel(), mainSkill);
    }

    // Update required skill in existing role objects
    for (Role mainRole : roles) {
      if (mainRole.getRequiredSkill() != null) {
        Skill mainSkill = skillMap.get(mainRole.getRequiredSkill().getLabel());
        mainRole.setRequiredSkill(mainSkill);
      }
    }
  }

  /**
   * Creates the proper reference to the Person object (Creator) related to the story.
   *
   * @param stories Reference story objects to link to the person
   * @param people Reference person object to link to the story
   */
  private void syncStoriesWithPeople (List<Story> stories, List<Person> people) {
    Map<String, Person> personMap = new HashMap<>();

    for (Person mainPerson : people) {
      personMap.put(mainPerson.getLabel(), mainPerson);
    }

    for (Story mainStory : stories) {
      if (mainStory.getCreator() != null) {
        Person mainPerson = personMap.get(mainStory.getCreator().getLabel());
        mainStory.setCreator(mainPerson);
      }
    }
  }

}