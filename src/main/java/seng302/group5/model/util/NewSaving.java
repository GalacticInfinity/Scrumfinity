package seng302.group5.model.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import seng302.group5.Main;
import seng302.group5.model.AgileHistory;
import seng302.group5.model.Backlog;
import seng302.group5.model.Estimate;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Role;
import seng302.group5.model.Skill;
import seng302.group5.model.Story;
import seng302.group5.model.Team;

/**
 * Class which creates an xml file to save. Manually formats the document
 * Created by Michael on 4/21/2015.
 */
public class NewSaving {

  private List<Project> projects;
  private List<Person> people;
  private List<Skill> skills;
  private List<Team> teams;
  private List<Release> releases;
  private List<Role> roles;
  private List<Story> stories;
  private List<Backlog> backlogs;
  private List<Estimate> estimates;

  public NewSaving(Main main) {
    projects = main.getProjects();
    people = main.getPeople();
    skills = main.getSkills();
    teams = main.getTeams();
    releases = main.getReleases();
    roles = main.getRoles();
    stories = main.getStories();
    backlogs = main.getBacklogs();
    estimates = main.getEstimates();
  }

  /**
   * Takes file destination and creates xml save file at specified location
   *
   * @param file File destination
   */
  public void saveData(File file) {
    // Turns the file into a string
    String filename = file.toString();
    if (!filename.endsWith(".xml")) {
      filename = filename + ".xml";
//      System.out.println(filename);
    }

    // Creates the writer and begins the writing process
    try (Writer saveFile = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(filename), "utf-8"))) {
      //saveFile.write("<root>\n");
      //saveProjects(saveFile);
      saveHeader(saveFile);
      saveProjects(saveFile);
      savePeople(saveFile);
      saveSkills(saveFile);
      saveTeams(saveFile);
      saveReleases(saveFile);
      saveRoles(saveFile);
      if (Settings.progVersion >= 0.2) {
        saveStories(saveFile);
      }
      if (Settings.progVersion >= 0.3) {
        saveBacklogs(saveFile);
        saveEstimates(saveFile);
      }
      saveEnd(saveFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Write organization and app version to save file
   *
   * @param saveFile Save file being written to
   * @throws Exception Required field is missing/ error with writer
   */
  private void saveHeader(Writer saveFile) throws Exception{
    String orgName;
    if (!Settings.organizationName.isEmpty()) {
      orgName = Settings.organizationName;
    } else {
      orgName = "__undefined__";
    }
    saveFile.write("<scrumfinity version=\"" + Settings.progVersion + "\" organization=\"" + orgName + "\">\n");
  }

  /**
   * Function to simply write the </scrumfinity> tag. As everything else has it's own function
   * to write to save file, this does too.
   * @param saveFile Save file being written to
   * @throws Exception Error with writer
   */
  private void saveEnd(Writer saveFile) throws Exception {
    saveFile.write("</scrumfinity>");
  }

  /**
   * Write main app project data to xml file
   *
   * @param saveFile Save file being written to
   * @throws Exception Required field is missing/ error with writer
   */
  private void saveProjects(Writer saveFile) throws Exception {
    Team agileTeam;
    saveFile.write("<Projects>\n");
    for (Project project : this.projects) {
      saveFile.write("\t<Project>\n");
      saveFile.write("\t\t<projectLabel>" + project.getLabel() + "</projectLabel>\n");
      saveFile.write("\t\t<projectName>" + project.getProjectName() + "</projectName>\n");
      if (project.getProjectDescription() != null && !project.getProjectDescription().isEmpty()) {
        saveFile.write("\t\t<projectDescription>" + project.getProjectDescription() + "</projectDescription>\n");
      }
      if (!project.getAllocatedTeams().isEmpty()) {
        saveFile.write("\t\t<AllocatedTeams>\n");
        for (AgileHistory allocatedTeam : project.getAllocatedTeams()) {
          saveFile.write("\t\t\t<allocatedTeam>\n");
          agileTeam = (Team) allocatedTeam.getAgileItem();
          saveFile.write("\t\t\t\t<agileTeam>" + agileTeam.getLabel() + "</agileTeam>\n");
          saveFile.write("\t\t\t\t<startDate>" + allocatedTeam.getStartDate() + "</startDate>\n");
          saveFile.write("\t\t\t\t<endDate>" + allocatedTeam.getEndDate() + "</endDate>\n");
          saveFile.write("\t\t\t</allocatedTeam>\n");
        }
        saveFile.write("\t\t</AllocatedTeams>\n");
      }
      saveFile.write("\t</Project>\n");
    }
    saveFile.write("</Projects>\n");
  }

  /**
   * Writes main app people data to xml file
   *
   * @param saveFile Save file being written to
   * @throws Exception Required field is missing/ error with writer
   */
  private void savePeople(Writer saveFile) throws Exception {
    saveFile.write("<People>\n");
    for (Person person : this.people) {
      saveFile.write("\t<Person>\n");
      saveFile.write("\t\t<personLabel>" + person.getLabel() + "</personLabel>\n");
      if (person.getFirstName() != null && !person.getFirstName().isEmpty()) {
        saveFile.write("\t\t<firstName>" + person.getFirstName() + "</firstName>\n");
      }
      if (person.getLastName() != null && !person.getLastName().isEmpty()) {
        saveFile.write("\t\t<lastName>" + person.getLastName() + "</lastName>\n");
      }
      if (person.getTeam() != null) {
        saveFile.write("\t\t<team>" + person.getTeamLabel() + "</team>\n");
      }
      if (!person.getSkillSet().isEmpty()) {
        saveFile.write("\t\t<PersonSkills>\n");
        for (Skill skill : person.getSkillSet()) {
          saveFile.write("\t\t\t<PersonSkill>" + skill.getLabel() + "</PersonSkill>\n");
        }
        saveFile.write("\t\t</PersonSkills>\n");
      }
      saveFile.write("\t</Person>\n");
    }
    saveFile.write("</People>\n");
  }

  /**
   * Writes the main app Skill data to xml
   *
   * @param saveFile Save file being written to
   * @throws Exception Required field is missing/ error with writer
   */
  private void saveSkills(Writer saveFile) throws Exception {
    saveFile.write("<Skills>\n");
    for (Skill skill : this.skills) {
      saveFile.write("\t<Skill>\n");
      saveFile.write("\t\t<skillLabel>" + skill.getLabel() + "</skillLabel>\n");
      if (skill.getSkillDescription() != null && !skill.getSkillDescription().isEmpty()) {
        saveFile.write("\t\t<skillDescription>" + skill.getSkillDescription() + "</skillDescription>\n");
      }
      saveFile.write("\t</Skill>\n");
    }
    saveFile.write("</Skills>\n");
  }

  /**
   * Writes the main app Team data to xml
   *
   * @param saveFile Save file being written to
   * @throws Exception Required field is missing/ error with writer
   */
  private void saveTeams(Writer saveFile) throws Exception {
    Role personRole;

    saveFile.write("<Teams>\n");
    for (Team team : this.teams) {
      saveFile.write("\t<Team>\n");
      saveFile.write("\t\t<teamLabel>" + team.getLabel() + "</teamLabel>\n");
      if (team.getTeamDescription() != null && !team.getTeamDescription().isEmpty()) {
        saveFile.write("\t\t<teamDescription>" + team.getTeamDescription() + "</teamDescription>\n");
      }
      if (!team.getTeamMembers().isEmpty()) {
        saveFile.write("\t\t<TeamPeople>\n");
        for (Person person : team.getTeamMembers()) {
          saveFile.write("\t\t\t<TeamMember>\n");
          saveFile.write("\t\t\t\t<teamPersonLabel>" + person.getLabel() + "</teamPersonLabel>\n");
          personRole = team.getMembersRole().get(person);
          if (personRole != null) {
            saveFile.write("\t\t\t\t<personRole>" + personRole.getLabel() + "</personRole>\n");
          }
          saveFile.write("\t\t\t</TeamMember>\n");
        }
        saveFile.write("\t\t</TeamPeople>\n");
      }
      saveFile.write("\t</Team>\n");
    }
    saveFile.write("</Teams>\n");
  }

  /**
   * Appends the main app release data to the save file
   *
   * @param saveFile Save file being written to
   * @throws Exception Required field is missing/ error with writer
   */
  private void saveReleases(Writer saveFile) throws Exception {
    saveFile.write("<Releases>\n");
    for (Release release : this.releases) {
      saveFile.write("\t<Release>\n");
      saveFile.write("\t\t<releaseLabel>" + release.getLabel() + "</releaseLabel>\n");
      saveFile.write("\t\t<releaseDescription>" + release.getReleaseDescription() + "</releaseDescription>\n");
      saveFile.write("\t\t<releaseNotes>" + release.getReleaseNotes() + "</releaseNotes>\n");
      saveFile.write("\t\t<releaseProject>" + release.getProjectRelease().getLabel() + "</releaseProject>\n");
      saveFile.write("\t\t<releaseDate>" + release.getReleaseDate() + "</releaseDate>\n");
      saveFile.write("\t</Release>\n");
    }
    saveFile.write("</Releases>\n");
  }

  /**
   * Appends the main app roles data to the save file
   *
   * @param saveFile Save file being written to
   * @throws Exception Required field is missing/ error with writer
   */
  private void saveRoles(Writer saveFile) throws Exception {
    saveFile.write("<Roles>\n");
    for (Role role : this.roles) {
      saveFile.write("\t<Role>\n");
      saveFile.write("\t\t<roleLabel>" + role.getLabel() + "</roleLabel>\n");
      saveFile.write("\t\t<roleName>" + role.getRoleName() + "</roleName>\n");
      if (role.getRequiredSkill() != null) {
        saveFile.write("\t\t<roleSkill>" + role.getRequiredSkill().getLabel() + "</roleSkill>\n");
      }
      if (role.getMemberLimit() != Integer.MAX_VALUE) {
        saveFile.write("\t\t<memberLimit>" + role.getMemberLimit() + "</memberLimit>\n");
      }
      saveFile.write("\t</Role>\n");
    }
    saveFile.write("</Roles>\n");
  }

  /**
   * Appends the main stories data to the save file
   *
   * @param saveFile Save file being written to
   * @throws Exception Required field is missing/ error with writer
   */
  private void saveStories(Writer saveFile) throws Exception {
    saveFile.write("<Stories>\n");
    for (Story story : this.stories) {
      saveFile.write("\t<Story>\n");
      saveFile.write("\t\t<storyLabel>" + story.getLabel() + "</storyLabel>\n");
      saveFile.write("\t\t<creator>" + story.getCreator().getLabel() + "</creator>\n");
      if (story.getStoryName() != null && !story.getStoryName().equals("")) {
        saveFile.write("\t\t<longName>" + story.getStoryName() + "</longName>\n");
      }
      if (story.getDescription() != null && !story.getDescription().equals("")) {
        saveFile.write("\t\t<description>" + story.getDescription() + "</description>\n");
      }
      if (!story.getAcceptanceCriteria().isEmpty()) {
        saveFile.write("\t\t<AcceptanceCriteria>\n");
        for (String acceptanceC : story.getAcceptanceCriteria()) {
          saveFile.write("\t\t\t<criteria>" + acceptanceC + "</criteria>\n");
        }
        saveFile.write("\t\t</AcceptanceCriteria>\n");
      }
      saveFile.write("\t</Story>\n");
    }
    saveFile.write("</Stories>\n");
  }

  /**
   * Appends the main backlogs data to the save file
   *
   * @param saveFile Save file being written to
   * @throws Exception Required field is missing or error with writer
   */
  private void saveBacklogs(Writer saveFile) throws Exception {
    saveFile.write("<Backlogs>\n");
    for (Backlog backlog : this.backlogs) {
      saveFile.write("\t<Backlog>\n");
      saveFile.write("\t\t<backlogLabel>" + backlog.getLabel() + "</backlogLabel>\n");
      saveFile.write("\t\t<productOwner>" + backlog.getProductOwner().getLabel() + "</productOwner>\n");

      if (backlog.getBacklogName() != null && !backlog.getBacklogName().equals("")) {
        saveFile.write("\t\t<backlogName>" + backlog.getBacklogName() + "</backlogName>\n");
      }
      if (backlog.getBacklogDescription() != null && !backlog.getBacklogDescription().equals("")) {
        saveFile.write("\t\t<backlogDescription>" + backlog.getBacklogDescription() + "</backlogDescription>\n");
      }

      if (!backlog.getStories().isEmpty()) {
        saveFile.write("\t\t<BacklogStories>\n");
        for (Story story : backlog.getStories()) {
          saveFile.write("\t\t\t<backlogStory>" + story.getLabel() + "</backlogStory>\n");
        }
        saveFile.write("\t\t</BacklogStories>\n");
      }

      saveFile.write("\t</Backlog>\n");
    }
    saveFile.write("</Backlogs>\n");

  }

  private void saveEstimates(Writer saveFile) throws Exception {
    saveFile.write("<Estimates>\n");
    for (Estimate estimate : this.estimates) {
      saveFile.write("\t<Estimate>\n");
      saveFile.write("\t\t<estimateLabel>" + estimate.getLabel() + "</estimateLabel>\n");
      saveFile.write("\t\t<EstimateNames>\n");
      List<String> estimateNames = estimate.getEstimateNames();
      for (int i = 0; i < estimateNames.size(); i++) {
        saveFile.write("\t\t\t<size-" + String.valueOf(i) + ">" + estimateNames.get(i) +
                       "</size-" + String.valueOf(i) + ">\n");
      }
      saveFile.write("\t\t</EstimateNames>\n");
      saveFile.write("\t</Estimate>\n");
    }
    saveFile.write("</Estimates>\n");
  }
}
