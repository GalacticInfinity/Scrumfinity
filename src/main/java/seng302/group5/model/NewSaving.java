package seng302.group5.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
      savePeople(saveFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Write main app project data to xml file
   * @param saveFile
   */
  private void saveProjects(Writer saveFile) throws Exception{
    saveFile.write("<Projects>\n");
    saveFile.write("</Projects>\n");
    //for (Project )
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
        saveFile.write("\t\t<Skills>\n");
        for (Skill skill : person.getSkillSet()) {
          saveFile.write("\t\t\t<skill>" + skill.getSkillName() + "</skill>\n");
        }
        saveFile.write("\t\t</Skills>\n");
      }
      saveFile.write("\t</Person>\n");
    }
    saveFile.write("</People>\n");
  }
}
