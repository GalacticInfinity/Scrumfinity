package seng302.group5.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import seng302.group5.Main;

/**
 * Created by Michael on 4/21/2015.
 */
public class NewLoading {
  private Main main;
  private BufferedReader loadedFile;

  public NewLoading(Main main) {
    this.main = main;
  }

  public void loadFile(File file) {
    // Turns the file into a string
    String filename = file.toString();
    if (!filename.endsWith(".xml")) {
      filename = filename + ".xml";
      System.out.println(filename);
    }
    try {
      String currentLine;
      loadedFile = new BufferedReader(new FileReader(filename));
      loadProjects();
      loadPeople();
      loadSkills();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (loadedFile != null)loadedFile.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private void loadProjects() throws Exception {
    String projectLine;
    String projectData;

    // Untill Project end tag
    while ((!(projectLine = loadedFile.readLine()).equals("</Projects>"))) {
      //System.out.println(projectLine);
      // On new Person tag
      if (projectLine.matches(".*<Project>")) {
        Project newProject = new Project();

        // Mandatory fields
        projectLine = loadedFile.readLine();
        projectData = projectLine.replaceAll("(?i)(.*<projectID.*?>)(.+?)(</projectID>)", "$2");
        //System.out.println(projectData);
        newProject.setProjectID(projectData);
        projectLine = loadedFile.readLine();
        projectData = projectLine.replaceAll("(?i)(.*<projectName.*?>)(.+?)(</projectName>)", "$2");
        //System.out.println(projectData);
        newProject.setProjectName(projectData);

        // Non mandatory fields.
        while ((!(projectLine = loadedFile.readLine()).equals("\t</Project>"))) {
          if (projectLine.startsWith("\t\t<projectDescription>")) {
            projectData = projectLine.replaceAll("(?i)(.*<projectDescription.*?>)(.+?)(</projectDescription>)", "$2");
            //System.out.println(projectData);
            newProject.setProjectDescription(projectData);
          }
        }
        // Add the loaded project into main
        main.addProject(newProject);
      }
    }
    //System.out.println(projectLine);
  }

  /**
   * Loads all people from the loaded xml file into main app
   * @throws Exception
   */
  private void loadPeople() throws Exception{
    String personLine;
    String personData;

    // Untill People end tag
    while ((!(personLine = loadedFile.readLine()).equals("</People>"))) {
      // On new Person tag
      if (personLine.matches(".*<Person>")) {
        // Required initializers
        Person newPerson = new Person();
        ObservableList<Skill> skills = FXCollections.observableArrayList();
        Skill tempSkill;

        // Mandatory data
        personLine = loadedFile.readLine();
        personData = personLine.replaceAll("(?i)(.*<personID.*?>)(.+?)(</personID>)", "$2");
        //System.out.println(personData);
        newPerson.setPersonID(personData);

        // Optional data
        while ((!(personLine = loadedFile.readLine()).equals("\t</Person>"))) {
          if (personLine.startsWith("\t\t<firstName>")) {
            personData = personLine.replaceAll("(?i)(.*<firstName.*?>)(.+?)(</firstName>)", "$2");
            //System.out.println(personData);
            newPerson.setFirstName(personData);
          }
          if (personLine.startsWith("\t\t<lastName>")) {
            personData = personLine.replaceAll("(?i)(.*<lastName.*?>)(.+?)(</lastName>)", "$2");
            //System.out.println(personData);
            newPerson.setLastName(personData);
          }
          if (personLine.startsWith("\t\t<PersonSkills>")) {
            while ((!(personLine = loadedFile.readLine()).equals("\t\t</PersonSkills>"))) {
              if (personLine.startsWith("\t\t\t<PersonSkill>")) {
                tempSkill = new Skill();
                personData = personLine.replaceAll("(?i)(.*<PersonSkill.*?>)(.+?)(</PersonSkill>)", "$2");
                //System.out.println(personData);
                tempSkill.setSkillName(personData);
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
   * Loads all Skills into main app
   * @throws Exception
   */
  private void loadSkills() throws Exception{
    String skillLine;
    String skillData;

    // Untill skill end tag
    while ((!(skillLine = loadedFile.readLine()).equals("</Skills>"))) {
      // For each new skill
      if (skillLine.matches(".*<Skill>")) {
        Skill newSkill = new Skill();

        // Mandatory data
        skillLine = loadedFile.readLine();
        skillData = skillLine.replaceAll("(?i)(.*<skillID.*?>)(.+?)(</skillID>)", "$2");
        newSkill.setSkillName(skillData);

        // Non mandatory data
        while ((!(skillLine = loadedFile.readLine()).equals("\t</Skill>"))) {
          if (skillLine.startsWith("\t\t<skillDescription>")) {
            skillData = skillLine.replaceAll("(?i)(.*<skillDescription.*?>)(.+?)(</skillDescription>)", "$2");
            newSkill.setSkillDescription(skillData);
          }
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
          if (mainSkill.getSkillName().equals(personSkill.getSkillName())) {
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
}
