package seng302.group5.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seng302.group5.Main;
import seng302.group5.model.util.Settings;

/**
 * Created by Michael on 3/19/2015.
 */

@XmlRootElement(name = "root")
public class Saving {

  private List<Person> persons;
  private List<Project> projects;
  private List<Skill> skills;
  private List<Team> teams;

  @XmlElement(name = "person")
  public List<Person> getPersons() {
    return persons;
  }

  public void setPersons(List<Person> persons) {
    this.persons = persons;
  }

  @XmlElement(name = "project")
  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  @XmlElement(name = "skill")
  public List<Skill> getSkills() {
    return skills;
  }

  public void setSkills(List<Skill> skills) {
    this.skills = skills;
  }

  @XmlElement(name = "team")
  public List<Team> getTeams() {
    return teams;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  /**
   * Saves all data currently stored in Main in CSV format to specified file.
   * Overwrites any file chosen without checks currently.
   * Makes sure file ends with .csv.
   * @param file Directory + filename
   * @param main Main class containing data
   */
  public static void saveDataToFile(File file, Main main) {
    try {
      JAXBContext context = JAXBContext
          .newInstance(Saving.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      // Wrapping our person data.
      Saving wrapper = new Saving();
      wrapper.setPersons(main.getPeople());
      wrapper.setProjects(main.getProjects());
      wrapper.setSkills(main.getSkills());
      wrapper.setTeams(main.getTeams());


      // Checks if file ends with .xml, appends if does not.
      String filename = file.toString();
      if (!filename.endsWith(".xml")) {
        filename = filename + ".xml";
        System.out.println(filename);
        file = new File(filename);
      }

      // Marshalling and saving XML to the file.
      m.marshal(wrapper, file);

      Settings.defaultFilepath = file.getParentFile();
    } catch (Exception e) { // catches ANY exception
      System.out.println("Could not save file properly");
      e.printStackTrace();
    }
  }

  /**
   * Loads data from specified csv and saves into main.
   * Must be in .xml or will thorw error.
   * @param file Filepath for csv
   * @param main Main class to store data in
   */
  public static void loadDataFromFile(File file, Main main) {
    try {
      // Checks if file ends with .csv, throws error if doesn't.
      String filename = file.toString();
      if (!filename.endsWith(".xml")) {
        throw new Exception("Wrong form, cannot load non .xml");
      }

      JAXBContext context = JAXBContext
          .newInstance(Saving.class);
      Unmarshaller um = context.createUnmarshaller();

      // Reading XML from the file and unmarshalling.
      Saving wrapper = (Saving) um.unmarshal(file);

      // Reset main to its original state
      main.resetAll();

      List<Person> xmlPersons = wrapper.getPersons();
      List<Project> xmlProjects = wrapper.getProjects();
      List<Skill> xmlSkills = wrapper.getSkills();
      List<Team> xmlTeams = wrapper.getTeams();

      // TODO comments
      if (xmlProjects != null) {
        main.getProjects().addAll(xmlProjects);
      }
      if (xmlPersons != null) {
        main.getPeople().addAll(xmlPersons);
      }
      if (xmlSkills != null) {
        main.getSkills().addAll(xmlSkills);
      }
      syncSkills(main);
      if (xmlTeams != null) {
        main.getTeams().addAll(xmlTeams);
      }

      // Save the file path to Settings class
      Settings.defaultFilepath = file.getParentFile();
    } catch (Exception e) {
      System.out.println("Could not load file properly");
      e.printStackTrace();
    }
  }

  /**
   * Creates proper object reference between people and skills.
   * @param main Main application
   */
  private static void syncSkills(Main main){
    // For every available person
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


