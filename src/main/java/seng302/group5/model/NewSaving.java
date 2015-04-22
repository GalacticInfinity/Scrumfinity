package seng302.group5.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

import seng302.group5.Main;

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

  public NewSaving(Main main) {
    projects = main.getProjects();
    people = main.getPeople();
    skills = main.getSkills();
    teams = main.getTeams();
    releases = main.getReleases();
  }

  /**
   * Takes file destination and creates xml save file at specified location
   * @param file File destination
   */
  public void saveData(File file) {
    // Turns the file into a string
    String filename = file.toString();
    if (!filename.endsWith(".xml")) {
      filename = filename + ".xml";
      System.out.println(filename);
    }

    // Creates the writer and begins the writing process
    try (Writer saveFile = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(filename), "utf-8"))) {
      //saveFile.write("<root>\n");
      //saveProjects(saveFile);
      saveProjects(saveFile);
      savePeople(saveFile);
      saveSkills(saveFile);
      saveTeams(saveFile);
      saveReleases(saveFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Write main app project data to xml file
   * @param saveFile
   * @throws Exception
   */
  private void saveProjects(Writer saveFile) throws Exception{
    Team agileTeam;
    saveFile.write("<Projects>\n");
    for (Project project : this.projects) {
      saveFile.write("\t<Project>\n");
      saveFile.write("\t\t<projectID>" + project.getProjectID() + "</projectID>\n");
      saveFile.write("\t\t<projectName>" + project.getProjectName() + "</projectName>\n");
      if (project.getProjectDescription() != null && !project.getProjectDescription().isEmpty()) {
        saveFile.write("\t\t<projectDescription>" + project.getProjectDescription() + "</projectDescription>\n");
      }
      if (!project.getTeam().isEmpty()) {
        saveFile.write("\t\t<AllocatedTeams>\n");
        for (AgileHistory allocatedTeam : project.getTeam()) {
          saveFile.write("\t\t\t<allocatedTeam>\n");
          agileTeam = (Team) allocatedTeam.getAgileItem();
          saveFile.write("\t\t\t\t<agileTeam>" + agileTeam.getTeamID() + "</agileTeam>\n");
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
   * @param saveFile File to write
   * @throws Exception
   */
  private void savePeople(Writer saveFile) throws Exception {
    saveFile.write("<People>\n");
    for (Person person : this.people) {
      saveFile.write("\t<Person>\n");
      saveFile.write("\t\t<personID>" + person.getPersonID() + "</personID>\n");
      if (person.getFirstName() != null && !person.getFirstName().isEmpty()) {
        saveFile.write("\t\t<firstName>" + person.getFirstName() + "</firstName>\n");
      }
      if (person.getLastName() != null && !person.getLastName().isEmpty()) {
        saveFile.write("\t\t<lastName>" + person.getLastName() + "</lastName>\n");
      }
      if (person.getTeam() != null) {
        saveFile.write("\t\t<team>" + person.getTeamID() + "</team>\n");
      }
      if (!person.getSkillSet().isEmpty()) {
        saveFile.write("\t\t<PersonSkills>\n");
        for (Skill skill : person.getSkillSet()) {
          saveFile.write("\t\t\t<PersonSkill>" + skill.getSkillName() + "</PersonSkill>\n");
        }
        saveFile.write("\t\t</PersonSkills>\n");
      }
      saveFile.write("\t</Person>\n");
    }
    saveFile.write("</People>\n");
  }

  /**
   * Writes the main app Skill data to xml
   * @param saveFile
   * @throws Exception
   */
  private void saveSkills(Writer saveFile) throws Exception {
    saveFile.write("<Skills>\n");
    for (Skill skill : this.skills) {
      saveFile.write("\t<Skill>\n");
      saveFile.write("\t\t<skillID>" + skill.getSkillName() + "</skillID>\n");
      if (skill.getSkillDescription() != null && !skill.getSkillDescription().isEmpty()) {
        saveFile.write("\t\t<skillDescription>" + skill.getSkillDescription() + "</skillDescription>\n");
      }
      saveFile.write("\t</Skill>\n");
    }
    saveFile.write("</Skills>\n");
  }

  /**
   * Writes the main app Team data to xml
   * @param saveFile
   * @throws Exception
   */
  private void saveTeams(Writer saveFile) throws Exception {
    saveFile.write("<Teams>\n");
    for (Team team : this.teams) {
      saveFile.write("\t<Team>\n");
      saveFile.write("\t\t<teamID>" + team.getTeamID() + "</teamID>\n");
      if (team.getTeamDescription() != null && !team.getTeamDescription().isEmpty()) {
        saveFile.write("\t\t<teamDescription>" + team.getTeamDescription() + "</teamDescription>\n");
      }
      if (!team.getTeamMembers().isEmpty()) {
        saveFile.write("\t\t<TeamPeople>\n");
        for (Person person : team.getTeamMembers()) {
          saveFile.write("\t\t\t<teamPersonID>" + person.getPersonID() + "</teamPersonID>\n");
        }
        saveFile.write("\t\t</TeamPeople>\n");
      }
      if (team.getCurrentProject() != null) {
        saveFile.write("\t\t<teamProject>" + team.getCurrentProject().getProjectID() + "</teamProject>\n");
      }
      saveFile.write("\t</Team>\n");
    }
    saveFile.write("</Teams>\n");
  }

  /**
   * Appends the main app release data to the save file
   * @param saveFile
   * @throws Exception
   */
  private void saveReleases(Writer saveFile) throws Exception {
    saveFile.write("<Releases>\n");
    for (Release release : this.releases) {
      saveFile.write("\t<Release>\n");
      saveFile.write("\t\t<releaseID>" + release.getReleaseName() + "</releaseID>\n");
      saveFile.write("\t\t<releaseDescription>" + release.getReleaseDescription() + "</releaseDescription>\n");
      saveFile.write("\t\t<releaseNotes>" + release.getReleaseNotes() + "</releaseNotes>\n");
      saveFile.write("\t\t<releaseProject>" + release.getProjectRelease().getProjectID() + "</releaseProject>\n");
      saveFile.write("\t\t<releaseDate>" + release.getReleaseDate() + "</releaseDate>\n");
      saveFile.write("\t</Release>\n");
    }
    saveFile.write("</Releases>\n");
  }
}
