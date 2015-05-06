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
import seng302.group5.model.AgileHistory;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Team;


/**
 * Created by Michael + Craig on 5/5/2015.
 * TODO Make a real javadoc
 */
public class ReportWriter {

  private Document report;
  private Element rootElement;
  Element projElem;
  Element releasesElement;
  Element teamElement;
  Element membersElement;

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

        releasesElement = report.createElement("Releases");
        projElem.appendChild(releasesElement);

        for (Release release : mainApp.getReleases()) {
          if (release.getProjectRelease().getLabel().equals(project.getLabel())) {
            createReleaseChild(release);
          }
        }
        teamElement = report.createElement("Teams");
        projElem.appendChild(teamElement);
        for (AgileHistory team : project.getAllocatedTeams()) {
          createTeamChild(mainApp, team);
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
    releasesElement.appendChild(releaseElem);
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


  public void createTeamChild(Main mainApp, AgileHistory team) {

    Element teamElem = report.createElement("Team");
    teamElement.appendChild(teamElem);
    teamElem.setAttribute("label", team.getAgileItem().getLabel());

    Element teamStartDate = report.createElement("StartDate");
    teamStartDate.appendChild(report.createTextNode(team.getStartDate().toString()));
    teamElem.appendChild(teamStartDate);

    Element teamEndDate = report.createElement("EndDate");
    teamEndDate.appendChild(report.createTextNode(team.getEndDate().toString()));
    teamElem.appendChild(teamEndDate);

    membersElement = report.createElement("Members");
    teamElement.appendChild(membersElement);
    for (Team listTeam : mainApp.getTeams()) {

      if (team.getAgileItem().getLabel().equals(listTeam.getLabel())) {
        System.out.println(listTeam.getLabel());
        createPersonChild(listTeam, teamElem);
      }
    }
  }

  public void createPersonChild(Team listTeam, Element teamElem) {
    for (Person member : listTeam.getTeamMembers()) {
      Element memberElem = report.createElement("Member");
      membersElement.appendChild(memberElem);
      memberElem.setAttribute("label", member.getLabel());

      Element teamMemberName = report.createElement("FirstName");
      teamMemberName.appendChild(report.createTextNode(member.getFirstName()));
      memberElem.appendChild(teamMemberName);

      Element teamMemberLastName = report.createElement("LastName");
      teamMemberLastName.appendChild(report.createTextNode(member.getLastName()));
      memberElem.appendChild(teamMemberLastName);
    }
  }
}
