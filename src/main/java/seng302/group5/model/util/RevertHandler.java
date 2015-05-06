package seng302.group5.model.util;

import java.util.ArrayList;
import java.util.HashMap;
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

  /**
   * Constructor. Set the main app to communicate with and initialise lists
   *
   * @param mainApp Main app to communicate with
   */
  public RevertHandler(Main mainApp) {
    this.mainApp = mainApp;
    rolesLastSaved = FXCollections.observableArrayList();
    releasesLastSaved = FXCollections.observableArrayList();
    peopleLastSaved = FXCollections.observableArrayList();
    skillsLastSaved = FXCollections.observableArrayList();
    teamsLastSaved = FXCollections.observableArrayList();
    projectsLastSaved = FXCollections.observableArrayList();
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

    syncPeopleWithSkills();
    syncTeamsWithPeople();
    syncProjectsWithTeams();
    syncReleasesWithProjects();
    syncRolesWithSkills();

    mainApp.refreshLastSaved();
    mainApp.refreshList();
  }

  /**
   * makes the last saved lists copy the current state list.
   * used when saving so that a revert maybe done.
   */
  public void setLastSaved() {
    Map<String, Project> projectMap = new HashMap<>();
    Map<String, Team> teamMap = new HashMap<>();
    Map<String, Person> personMap = new HashMap<>();
    Map<String, Skill> skillMap = new HashMap<>();
    // nothing refers to releases

    for (Project mainProject : mainApp.getProjects()) {
      projectMap.put(mainProject.getLabel(), mainProject);
    }
    for (Team mainTeam : mainApp.getTeams()) {
      teamMap.put(mainTeam.getLabel(), mainTeam);
    }
    for (Person mainPerson : mainApp.getPeople()) {
      personMap.put(mainPerson.getLabel(), mainPerson);
    }
    for (Skill mainSkill : mainApp.getSkills()) {
      skillMap.put(mainSkill.getLabel(), mainSkill);
    }

    projectsLastSaved.clear();
    for (Project project : mainApp.getProjects()) {
      //TODO AH shit
      projectsLastSaved.add(new Project(project));
    }

    teamsLastSaved.clear();
    for (Team team : mainApp.getTeams()) {
      Team deepTeamClone = new Team(team);
      deepTeamClone.getTeamMembers().clear();
      for (Person teamMember : team.getTeamMembers()) {
        Person personClone = new Person(personMap.get(teamMember.getLabel()));
        deepTeamClone.getTeamMembers().add(personClone);
      }

      teamsLastSaved.add(deepTeamClone);
    }

    peopleLastSaved.clear();
    for (Person person : mainApp.getPeople()) {
      Person deepPersonClone = new Person(person);
      deepPersonClone.getSkillSet().clear();
      for (Skill personSkill : person.getSkillSet()) {
        Skill skillClone = new Skill(skillMap.get(personSkill.getLabel()));
        deepPersonClone.getSkillSet().add(skillClone);
      }
      peopleLastSaved.add(deepPersonClone);
    }

    skillsLastSaved.clear();
    for (Skill skill : mainApp.getSkills()) {
      skillsLastSaved.add(new Skill(skill));
    }

    releasesLastSaved.clear();
    for (Release release : mainApp.getReleases()) {
      Release deepReleaseClone = new Release(release);
      Project projectClone = new Project(projectMap.get(release.getProjectRelease().getLabel()));
      deepReleaseClone.setProjectRelease(projectClone);
      releasesLastSaved.add(deepReleaseClone);
    }

    rolesLastSaved.clear();
    for (Role role : mainApp.getRoles()) {
      Role deepRoleClone = new Role(role);
      if (role.getRequiredSkill() != null) {
        Skill skillClone = new Skill(role.getRequiredSkill());
        deepRoleClone.setRequiredSkill(skillClone);
      }
      rolesLastSaved.add(deepRoleClone);
    }

  }

  /**
   * Creates proper object reference between people and skills.
   */
  private void syncPeopleWithSkills() {
    Map<String, Skill> skillMap = new HashMap<>();

    for (Skill mainSkill : mainApp.getSkills()) {
      skillMap.put(mainSkill.getLabel(), mainSkill);
    }

    // For every available person
    for (Person person : mainApp.getPeople()) {
      ArrayList<Skill> skillArray = new ArrayList<>();
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
   */
  private void syncTeamsWithPeople() {
    Map<String, Person> personMap = new HashMap<>();
    Map<String, Role> roleMap = new HashMap<>();

    for (Person mainPerson : mainApp.getPeople()) {
      personMap.put(mainPerson.getLabel(), mainPerson);
    }
    for (Role mainRole : mainApp.getRoles()) {
      roleMap.put(mainRole.getLabel(), mainRole);
    }

    // For every available team
    for (Team team : mainApp.getTeams()) {
      ArrayList<Person> personArray = new ArrayList<>();
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
   * Creates concurrency between people in main app and people in teams.
   */
  private void syncProjectsWithTeams() {
    Map<String, Team> teamMap = new HashMap<>();

    for (Team mainTeam : mainApp.getTeams()) {
      teamMap.put(mainTeam.getLabel(), mainTeam);
    }

    // For every available project
    for (Project project : mainApp.getProjects()) {
      ArrayList<AgileHistory> agileHistoryArray = new ArrayList<>();
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

  private void syncReleasesWithProjects() {
    Map<String, Project> projectMap = new HashMap<>();

    for (Project mainProject : mainApp.getProjects()) {
      projectMap.put(mainProject.getLabel(), mainProject);
    }

    // For every available release
    for (Release release : mainApp.getReleases()) {
      Project mainProject = projectMap.get(release.getProjectRelease().getLabel());
      release.setProjectRelease(mainProject);
    }
  }

  private void syncRolesWithSkills() {
    Map<String, Skill> skillMap = new HashMap<>();

    for (Skill mainSkill : mainApp.getSkills()) {
      skillMap.put(mainSkill.getLabel(), mainSkill);
    }

    // Update required skill in existing role objects
    for (Role mainRole : mainApp.getRoles()) {
      if (mainRole.getRequiredSkill() != null) {
        Skill mainSkill = skillMap.get(mainRole.getRequiredSkill().getLabel());
        mainRole.setRequiredSkill(mainSkill);
      }
    }
  }

}
