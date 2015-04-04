package seng302.group5.model;

import java.io.File;
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

      if (xmlPersons != null) {
        main.getPeople().addAll(xmlPersons);
      }
      if (xmlProjects != null) {
        main.getProjects().addAll(xmlProjects);
      }
      if (xmlSkills != null) {
        main.getSkills().addAll(xmlSkills);
      }

      // Save the file path to Settings class
      Settings.defaultFilepath = file.getParentFile();
    } catch (Exception e) {
      System.out.println("Could not load file properly");
      e.printStackTrace();
    }
  }
}


