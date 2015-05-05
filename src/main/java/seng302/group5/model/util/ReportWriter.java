package seng302.group5.model.util;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import seng302.group5.Main;
import seng302.group5.model.Project;

/**
 * Created by Michael + Craig on 5/5/2015.
 */
public class ReportWriter {

  public void writeReport(Main mainApp) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      Document report = docBuilder.newDocument();
      Element rootElement = report.createElement("Company");
      report.appendChild(rootElement);

      for (Project project : mainApp.getProjects()) {
        Element projElem = report.createElement(project.getProjectID());
        rootElement.appendChild(projElem);
        //  add proj name field
        //  add proj Desc field
        //  For allocated releases
        //    create release child
        //    add description
        //    add release notes
        //    add release date
        //
        //    add to project
        //  For allocated teams
        //    Create child team element with team label
        //    add desc field
        //    for available members
        //      create child persom element with label
        //      add fname field
        //      add lname field
        //      for skill in person
        //        create skill child element
        //        add skill desc
        //
        //    add to project
        //  add to root
      }

    } catch (Exception e) {

    }
  }
}
