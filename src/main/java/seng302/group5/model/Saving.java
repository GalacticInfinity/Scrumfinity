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

  /**
   * Saves all data currently stored in Main in CSV format to specified file.
   * Overwrites any file chosen without checks currently
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

      // Marshalling and saving XML to the file.
      m.marshal(wrapper, file);

      // Save the file path to the registry, not doing yet
      Settings.defaultFilepath = file.getParentFile();
    } catch (Exception e) { // catches ANY exception
      e.printStackTrace();
    }
  }

  /**
   * Loads data from specified csv and saves into main.
   * @param file Filepath for csv
   * @param main Main class to store data in
   */
  public static void loadDataFromFile(File file, Main main) {
    try {
      JAXBContext context = JAXBContext
          .newInstance(Saving.class);
      Unmarshaller um = context.createUnmarshaller();

      // Reading XML from the file and unmarshalling.
      Saving wrapper = (Saving) um.unmarshal(file);
      main.getPeople().clear();
      main.getProjects().clear();
      main.getPeople().addAll(wrapper.getPersons());
      main.getProjects().addAll(wrapper.getProjects());

      // Save the file path to Settings class
      Settings.defaultFilepath = file.getParentFile();
    } catch (Exception e) { // catches ANY exception
      e.printStackTrace();
    }
  }
}


