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
import seng302.group5.model.Release;

/**
 * Created by Michael + Craig on 5/5/2015.
 * TODO Make a real javadoc
 */
public class ReportWriter {

  private Document report;
  private Element rootElement;
  Element projElem;

  /**
   * Creates a report based on data currently stored in the main application memory.
   * Uses XML format, no pretty print.
   * @param mainApp The currently opened main application
   * @param saveLocation Where the report is saved to
   */
  public void writeReport(Main mainApp, File saveLocation) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      report = docBuilder.newDocument();
      rootElement = report.createElement("Company");
      report.appendChild(rootElement);

      for (Project project : mainApp.getProjects()) {
        projElem = report.createElement("Project");
        rootElement.appendChild(projElem);
        projElem.setAttribute("label", project.getLabel());

        Element projName = report.createElement("Name");
        projName.appendChild(report.createTextNode(project.getProjectName()));
        projElem.appendChild(projName);

        Element projDesc = report.createElement("Description");
        if (project.getProjectDescription() != null && !project.getProjectDescription().isEmpty()) {
          projDesc.appendChild(report.createTextNode(project.getProjectDescription()));
        }
        projElem.appendChild(projDesc);

        for (Release release : mainApp.getReleases()) {
          if (release.getProjectRelease().getLabel().equals(project.getLabel())) {
            createReleaseChild(release);
          }
        }
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

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(report);
      StreamResult result = new StreamResult(saveLocation);

      transformer.transform(source, result);
      System.out.println("Mrews");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createReleaseChild(Release release) {
    Element releaseElem = report.createElement("Release");
    projElem.appendChild(releaseElem);
    releaseElem.setAttribute("label", release.getLabel());

    Element releaseDesc = report.createElement("Description");
    releaseDesc.appendChild(report.createTextNode(release.getReleaseDescription()));
    releaseElem.appendChild(releaseDesc);

    Element releaseNotes = report.createElement("Notes");
    releaseNotes.appendChild(report.createTextNode(release.getReleaseNotes()));
    releaseElem.appendChild(releaseNotes);

    Element releaseDate = report.createElement("ReleaseDate");
    releaseDate.appendChild(report.createTextNode(release.getReleaseDate().toString()));
    releaseElem.appendChild(releaseDate);
  }
}
