package seng302.group5.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import seng302.group5.Main;
import seng302.group5.model.util.Settings;

/**
 * Class for loading xml save files
 * Created by Michael on 4/21/2015.
 */
public class NewLoading {

  private Main main;
  private BufferedReader loadedFile;
  private double saveVersion = 0;

  public NewLoading(Main main) {
    this.main = main;
  }

  /**
   * Loads all the data from the xml file into main app
   *
   * @param file File to load from
   */
  public void loadFile(File file) {
    // Turns the file into a string
    String filename = file.toString();
    if (!filename.endsWith(".xml")) {
      filename = filename + ".xml";
      System.out.println(filename);
    }
    try {
      loadedFile = new BufferedReader(new FileReader(filename));
      loadHeader();
      loadProjects();
      loadPeople();
      loadSkills();
      loadTeams();
      loadReleases();
      syncTeamAllocation();
      loadRoles();
      syncRoles();
      if (saveVersion >= 0.2) {
        //Load stories
      }
    } catch (Exception e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Loading Error");
      alert.setHeaderText(null);
      alert.setContentText("There was a problem with loading, file is corrupted.");
      alert.showAndWait();
      e.printStackTrace();
    } finally {
      try {
        if (loadedFile != null) {
          loadedFile.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Loads the header, if no header available, assumes saveFile is version 0.1
   * @throws Exception Could not read line from file.
   */
  private void loadHeader() throws Exception {
    String headerLine;
    String progVersion;
    String orgName;
    headerLine = loadedFile.readLine();
    if (headerLine.startsWith("<scrumfinity")) {
      progVersion = headerLine.replaceAll("(?i)(.*version=\")(.+?)(\" org.*)", "$2");
      orgName = headerLine.replaceAll("(?i)(.*organization=\")(.+?)(\">)", "$2");
      Settings.organizationName = orgName;
      saveVersion = Double.parseDouble(progVersion);
    }
  }

  /**
   * Loads Projects from xml files into main app
   *
   * @throws Exception Problem with loading
   */
  private void loadProjects() throws Exception {
    String projectLine;
    String projectData;
    AgileHistory teamHistoryItem;
    Team tempTeam;
    LocalDate startDate;
    LocalDate endDate;

    // Until Project end tag
    while ((!(projectLine = loadedFile.readLine()).equals("</Projects>"))) {
      // On new Person tag
      if (projectLine.matches(".*<Project>")) {
        Project newProject = new Project();

        // Mandatory fields
        projectLine = loadedFile.readLine();
        projectData = projectLine.replaceAll("(?i)(.*<projectLabel.*?>)(.+?)(</projectLabel>)", "$2");
        newProject.setLabel(projectData);
        projectLine = loadedFile.readLine();
        projectData = projectLine.replaceAll("(?i)(.*<projectName.*?>)(.+?)(</projectName>)", "$2");
        newProject.setProjectName(projectData);

        // Non mandatory fields.
        while ((!(projectLine = loadedFile.readLine()).equals("\t</Project>"))) {
          if (projectLine.startsWith("\t\t<projectDescription>")) {
            projectData = projectLine.replaceAll("(?i)(.*<projectDescription.*?>)(.+?)(</projectDescription>)", "$2");
            newProject.setProjectDescription(projectData);
          }
          // Loads list of AgileHistory items
          if (projectLine.startsWith("\t\t<AllocatedTeams>")) {
            while (!(projectLine = loadedFile.readLine()).startsWith("\t\t</AllocatedTeams>")) {
              if (projectLine.startsWith("\t\t\t<allocatedTeam>")) {
                teamHistoryItem = new AgileHistory();
                tempTeam = new Team();

                projectLine = loadedFile.readLine();
                projectData = projectLine.replaceAll("(?i)(.*<agileTeam.*?>)(.+?)(</agileTeam>)", "$2");
                tempTeam.setLabel(projectData);
                teamHistoryItem.setAgileItem(tempTeam);
                projectLine = loadedFile.readLine();
                projectData = projectLine.replaceAll("(?i)(.*<startDate.*?>)(.+?)(</startDate>)", "$2");
                startDate = LocalDate.of(Integer.parseInt(projectData.substring(0, 4)),
                                         Integer.parseInt(projectData.substring(5, 7)),
                                         Integer.parseInt(projectData.substring(8, 10)));
                teamHistoryItem.setStartDate(startDate);
                projectLine = loadedFile.readLine();
                projectData = projectLine.replaceAll("(?i)(.*<endDate.*?>)(.+?)(</endDate>)", "$2");
                if (projectData.equals("null")) {
                  endDate = null;
                } else {
                  endDate = LocalDate.of(Integer.parseInt(projectData.substring(0, 4)),
                                         Integer.parseInt(projectData.substring(5, 7)),
                                         Integer.parseInt(projectData.substring(8, 10)));
                }
                teamHistoryItem.setEndDate(endDate);

                newProject.addTeam(teamHistoryItem);
              }
            }
          }
        }
        // Add the loaded project into main
        main.addProject(newProject);
      }
    }
  }

  /**
   * Loads People from xml files into main app
   *
   * @throws Exception Problem with loading
   */
  private void loadPeople() throws Exception {
    String personLine;
    String personData;
    Person newPerson;
    ObservableList<Skill> skills;

    // Untill People end tag
    while ((!(personLine = loadedFile.readLine()).equals("</People>"))) {
      // On new Person tag
      if (personLine.matches(".*<Person>")) {
        // Required initializers
        newPerson = new Person();
        skills = FXCollections.observableArrayList();
        Skill tempSkill;

        // Mandatory data
        personLine = loadedFile.readLine();
        personData = personLine.replaceAll("(?i)(.*<personLabel.*?>)(.+?)(</personLabel>)", "$2");
        newPerson.setLabel(personData);

        // Optional data
        while ((!(personLine = loadedFile.readLine()).equals("\t</Person>"))) {
          if (personLine.startsWith("\t\t<firstName>")) {
            personData = personLine.replaceAll("(?i)(.*<firstName.*?>)(.+?)(</firstName>)", "$2");
            newPerson.setFirstName(personData);
          }
          if (personLine.startsWith("\t\t<lastName>")) {
            personData = personLine.replaceAll("(?i)(.*<lastName.*?>)(.+?)(</lastName>)", "$2");
            newPerson.setLastName(personData);
          }
          if (personLine.startsWith("\t\t<PersonSkills>")) {
            while ((!(personLine = loadedFile.readLine()).equals("\t\t</PersonSkills>"))) {
              if (personLine.startsWith("\t\t\t<PersonSkill>")) {
                tempSkill = new Skill();
                personData = personLine.replaceAll("(?i)(.*<PersonSkill.*?>)(.+?)(</PersonSkill>)", "$2");
                tempSkill.setLabel(personData);
                skills.add(tempSkill);
              }
            }
            if (skills.size() != 0) {
              newPerson.setSkillSet(skills);
            }
          }
        }
        main.addPerson(newPerson);
      }
    }
  }

  /**
   * Loads Skills from xml files into main app and updates skill references inside people objects
   *
   * @throws Exception Problem with loading
   */
  private void loadSkills() throws Exception {
    String skillLine;
    String skillData;

    // Untill skill end tag
    while ((!(skillLine = loadedFile.readLine()).equals("</Skills>"))) {
      // For each new skill
      if (skillLine.matches(".*<Skill>")) {
        Skill newSkill = new Skill();

        // Mandatory data
        skillLine = loadedFile.readLine();
        skillData = skillLine.replaceAll("(?i)(.*<skillLabel.*?>)(.+?)(</skillLabel>)", "$2");
        newSkill.setLabel(skillData);

        // Non mandatory data
        while ((!(skillLine = loadedFile.readLine()).equals("\t</Skill>"))) {
          if (skillLine.startsWith("\t\t<skillDescription>")) {
            skillData = skillLine.replaceAll("(?i)(.*<skillDescription.*?>)(.+?)(</skillDescription>)", "$2");
            newSkill.setSkillDescription(skillData);
          }
        }
        if (newSkill.getLabel().equals("Product Owner") ||
            newSkill.getLabel().equals("Scrum Master")) {
          main.getNonRemovable().add(newSkill);
        }
        main.addSkill(newSkill);
      }
    }

    // Now sync Skills and people skills
    for (Person person : main.getPeople()) {
      ArrayList<Skill> skillArray = new ArrayList<>();
      // For every skill in that person
      for (Skill personSkill : person.getSkillSet()) {
        // For every skill in main app
        for (Skill mainSkill : main.getSkills()) {
          if (mainSkill.getLabel().equals(personSkill.getLabel())) {
            // Remove loaded skill object
            skillArray.add(mainSkill);
          }
        }
      }
      // To fix Concurrent Modification Exception
      person.getSkillSet().clear();
      for (Skill arraySkill : skillArray) {
        person.getSkillSet().add(arraySkill);
      }
    }
  }

  /**
   * Loads teams from xml files into main app, and updates references inside people objects to the
   * newly loaded teams
   *
   * @throws Exception Problem with loading
   */
  private void loadTeams() throws Exception {
    String teamLine;
    String teamData;
    Team newTeam;

    // Untill Team end tag
    while (!(teamLine = loadedFile.readLine()).startsWith("</Teams>")) {
      // For each new Team
      if (teamLine.matches(".*<Team>")) {
        // New team loading
        newTeam = new Team();

        // Mandatory fields
        teamLine = loadedFile.readLine();
        teamData = teamLine.replaceAll("(?i)(.*<teamLabel.*?>)(.+?)(</teamLabel>)", "$2");
        newTeam.setLabel(teamData);

        // Non mandatory fields
        while (!(teamLine = loadedFile.readLine()).equals("\t</Team>")) {
          if (teamLine.startsWith("\t\t<teamDescription>")) {
            teamData = teamLine.replaceAll("(?i)(.*<teamDescription.*?>)(.+?)(</teamDescription>)", "$2");
            newTeam.setTeamDescription(teamData);
          }
          // Going through teams
          if (teamLine.startsWith("\t\t<TeamPeople>")) {
            loadTeamMembers(newTeam);
          }
        }
        main.addTeam(newTeam);
      }
    }
  }

  /**
   * Loads team members for a team object.
   *
   * @param newTeam Team currently being loaded
   * @throws Exception Problem with loading
   */
  private void loadTeamMembers(Team newTeam) throws Exception {
    // Definers
    String teamLine;
    String teamData;
    Person tempPerson;
    Role tempRole;
    ObservableList<Person> people = FXCollections.observableArrayList();
    HashMap<Person, Role> roles = new HashMap<>();

    // Untill end tag team people
    while (!(teamLine = loadedFile.readLine()).equals("\t\t</TeamPeople>")) {
      // For each new person
      if (teamLine.startsWith("\t\t\t<TeamMember>")) {
        tempPerson = new Person();
        tempRole = null;
        while (!(teamLine = loadedFile.readLine()).equals("\t\t\t</TeamMember>")) {
          if (teamLine.startsWith("\t\t\t\t<teamPersonLabel>")) {
            teamData = teamLine.replaceAll("(?i)(.*<teamPersonLabel.*?>)(.+?)(</teamPersonLabel>)", "$2");
            for (Person person : main.getPeople()) {
              if (person.getLabel().equals(teamData)) {
                people.add(person);
                tempPerson = person;
                break;
              }
            }
          }
          if (teamLine.startsWith("\t\t\t\t<personRole>")) {
            tempRole = new Role();
            teamData = teamLine.replaceAll("(?i)(.*<personRole.*?>)(.+?)(</personRole>)", "$2");
            tempRole.setLabel(teamData);
          }
        }
        roles.put(tempPerson, tempRole);
      }
    }
    if (people.size() != 0) {
      newTeam.setTeamMembers(people);
      newTeam.setMembersRole(roles);
      for (Person person : people) {
        person.assignToTeam(newTeam);
      }
    }
  }

  /**
   * Loads releases from xml files into main app
   *
   * @throws Exception Problem with loading
   */
  private void loadReleases() throws Exception {
    String releaseLine;
    String releaseData;
    Release newRelease;
    LocalDate releaseDate;

    // Untill Releases end tag
    while (!(releaseLine = loadedFile.readLine()).startsWith("</Releases>")) {
      // For each new release
      if (releaseLine.matches(".*<Release>")) {
        newRelease = new Release();

        releaseLine = loadedFile.readLine();
        releaseData = releaseLine.replaceAll("(?i)(.*<releaseLabel.*?>)(.+?)(</releaseLabel>)", "$2");
        newRelease.setLabel(releaseData);
        releaseLine = loadedFile.readLine();
        releaseData = releaseLine.replaceAll(
            "(?i)(.*<releaseDescription.*?>)(.+?)(</releaseDescription>)", "$2");
        newRelease.setReleaseDescription(releaseData);
        releaseLine = loadedFile.readLine();
        releaseData = releaseLine.replaceAll("(?i)(.*<releaseNotes>)(.+?)(</releaseNotes>)", "$2");
        newRelease.setReleaseNotes(releaseData);
        releaseLine = loadedFile.readLine();
        releaseData = releaseLine.replaceAll("(?i)(.*<releaseProject>)(.+?)(</releaseProject>)",
                                             "$2");
        // Get correct project from main for concurrency
        for (Project project : main.getProjects()) {
          if (project.getLabel().equals(releaseData)) {
            newRelease.setProjectRelease(project);
          }
        }
        releaseLine = loadedFile.readLine();
        releaseData = releaseLine.replaceAll("(?i)(.*<releaseDate>)(.+?)(</releaseDate>)", "$2");
        releaseDate = LocalDate.of(Integer.parseInt(releaseData.substring(0, 4)),
                                   Integer.parseInt(releaseData.substring(5, 7)),
                                   Integer.parseInt(releaseData.substring(8, 10)));
        newRelease.setReleaseDate(releaseDate);

        main.addRelease(newRelease);
      }
    }
  }

  /**
   * Loads roles from xml files into main app
   *
   * @throws Exception Problem with loading
   */
  private void loadRoles() throws Exception {
    String roleLine;
    String roleData;
    Role newRole;

    // Untill Role end tag
    while (!(roleLine = loadedFile.readLine()).startsWith("</Roles>")) {
      // For each new role
      if (roleLine.matches(".*<Role>")) {
        newRole = new Role();

        // Mandatory fields
        roleLine = loadedFile.readLine();
        roleData = roleLine.replaceAll("(?i)(.*<roleLabel.*?>)(.+?)(</roleLabel>)", "$2");
        newRole.setLabel(roleData);
        roleLine = loadedFile.readLine();
        roleData = roleLine.replaceAll("(?i)(.*<roleName.*?>)(.+?)(</roleName>)", "$2");
        newRole.setRoleName(roleData);

        // Non Mandatory fields
        while ((!(roleLine = loadedFile.readLine()).matches(".*</Role>"))) {
          if (roleLine.startsWith("\t\t<roleSkill>")) {
            roleData = roleLine.replaceAll("(?i)(.*<roleSkill.*?>)(.+?)(</roleSkill>)", "$2");
            for (Skill skill : main.getSkills()) {
              if (skill.getLabel().equals(roleData)) {
                newRole.setRequiredSkill(skill);
              }
            }
          }
          if (roleLine.startsWith("\t\t<memberLimit>")) {
            roleData = roleLine.replaceAll("(?i)(.*<memberLimit.*?>)(.+?)(</memberLimit>)", "$2");
            newRole.setMemberLimit(Integer.parseInt(roleData));
          }
        }
        main.addRole(newRole);
      }
    }
  }

  /**
   * Syncs temporary teams inside agile history items in projects with the real team objects
   */
  private void syncTeamAllocation() {
    for (Project project : main.getProjects()) {
      // For every AgileHistory in the project
      for (AgileHistory teamHistory : project.getAllocatedTeams()) {
        // For every Team that is in Main App
        for (Team team : main.getTeams()) {
          Team historyTeam = (Team) teamHistory.getAgileItem();
          if (team.getLabel().equals(historyTeam.getLabel())) {
            teamHistory.setAgileItem(team);
          }
        }
      }
    }
  }

  /**
   * Syncs the hashmap of Person,Role in teams with roles from main app
   */
  private void syncRoles() {
    Role tempRole;
    for (Team team : main.getTeams()) {
      for (Person person : team.getTeamMembers()) {
        tempRole = team.getMembersRole().get(person);
        if (tempRole != null) {
          for (Role role : main.getRoles()) {
            if (tempRole.getLabel().equals(role.getLabel())) {
              team.getMembersRole().put(person, role);
              break;
            }
          }
        }
      }
    }
  }
}
