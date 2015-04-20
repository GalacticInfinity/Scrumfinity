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
      while ((currentLine = loadedFile.readLine()) != null) {
        loadListType(currentLine);
        loadedFile.readLine();
      }
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

  private void loadListType(String typeLine) throws Exception{
    switch (typeLine) {
      case "<People>":
        loadPeople();
        break;
    }
  }

  /**
   * Loads all people from the loaded xml file into main app
   * @throws Exception
   */
  private void loadPeople() throws Exception{
    String personLine;

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
        String data = personLine.replaceAll("(?i)(.*<PersonID.*?>)(.+?)(</PersonID>)", "$2");
        System.out.println(data);
        newPerson.setPersonID(data);

        // Optional data
        while ((!(personLine = loadedFile.readLine()).equals("\t</Person>"))) {
          if (personLine.startsWith("\t\t<firstName>")) {
            data = personLine.replaceAll("(?i)(.*<firstName.*?>)(.+?)(</firstName>)", "$2");
            System.out.println(data);
            newPerson.setFirstName(data);
          }
          if (personLine.startsWith("\t\t<lastName>")) {
            data = personLine.replaceAll("(?i)(.*<lastName.*?>)(.+?)(</lastName>)", "$2");
            System.out.println(data);
            newPerson.setLastName(data);
          }
          if (personLine.startsWith("\t\t<Skills>")) {
            while ((!(personLine = loadedFile.readLine()).equals("\t\t</Skills>"))) {
              if (personLine.startsWith("\t\t\t<skill>")) {
                tempSkill = new Skill();
                data = personLine.replaceAll("(?i)(.*<skill.*?>)(.+?)(</skill>)", "$2");
                System.out.println(data);
                tempSkill.setSkillName(data);
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
}
