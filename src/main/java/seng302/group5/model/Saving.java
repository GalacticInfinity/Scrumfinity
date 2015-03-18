package seng302.group5.model;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seng302.group5.Main;

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
      //setPersonFilePath(file);
    } catch (Exception e) { // catches ANY exception
      e.printStackTrace();
    }
  }
}


