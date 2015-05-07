package seng302.group5.model.util;

import java.io.File;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.model.AgileHistory;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Release;
import seng302.group5.model.Role;
import seng302.group5.model.Skill;
import seng302.group5.model.Story;
import seng302.group5.model.Team;
import seng302.group5.model.util.Settings;


/**
 * Created by Michael + Craig on 5/5/2015.
 * TODO Make a real javadoc
 */
public class ReportWriter {

  private Document report;
  private Element rootElement;
  Element projElem;
  Element projElement;
  Element releasesElement;
  Element teamElement;
  Element membersElement;
  Element skillElement;
  Element orphanTeam;
  Element orphanPeople;
  Element allSkills;
  Element allStories;
  Element allReleases;
  String dateFormat = "dd/MM/yyyy";

  ObservableList<Team> orphanTeamsList = FXCollections.observableArrayList();

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
      rootElement = report.createElement("Organization");
      String orgName = Settings.organizationName;
      rootElement.setAttribute("Label", orgName);
      report.appendChild(rootElement);

      projElement = report.createElement("Projects");
      rootElement.appendChild(projElement);
      for (Project project : mainApp.getProjects()) {
        orphanTeamsList.setAll(mainApp.getTeams());

        projElem = report.createElement("Project");
        projElement.appendChild(projElem);
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
            createReleaseChild(release, releasesElement);
          }
        }
        teamElement = report.createElement("Teams");
        projElem.appendChild(teamElement);
        for (AgileHistory team : project.getAllocatedTeams()) {
          if (orphanTeamsList.contains(team.getAgileItem())) {
            orphanTeamsList.remove(team.getAgileItem());
          }
          createTeamChild(mainApp, team);
        }

      }

      orphanTeam = report.createElement("UnassignedTeams");
      rootElement.appendChild(orphanTeam);
      for (Team team : orphanTeamsList) {
          createOrphanTeam(team);
        }

      orphanPeople = report.createElement("UnassignedPeople");
      rootElement.appendChild(orphanPeople);
      for (Person person : mainApp.getPeople()) {
        if (!person.isInTeam()) {
          createOrphanPeople(person);
        }
      }

      allSkills = report.createElement("Skills");
      rootElement.appendChild(allSkills);
      for (Skill skill : mainApp.getSkills()) {
        createSkillChild(skill);
      }

      allStories = report.createElement("Stories");
      rootElement.appendChild(allStories);
      for (Story story : mainApp.getStories()) {
        createStoryChild(story);
      }

      allReleases = report.createElement("Releases");
      rootElement.appendChild(allReleases);
      for (Release release : mainApp.getReleases()) {
        createReleaseChild(release, allReleases);
      }


      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(report);
      StreamResult result = new StreamResult(saveLocation);

      transformer.transform(source, result);
      System.out.println("Report Created");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createReleaseChild(Release release, Element releasesElement) {
    Element releaseElem = report.createElement("Release");
    releasesElement.appendChild(releaseElem);
    releaseElem.setAttribute("label", release.getLabel());

    Element releaseDesc = report.createElement("Description");
    releaseDesc.appendChild(report.createTextNode(release.getReleaseDescription()));
    releaseElem.appendChild(releaseDesc);

    Element releaseNotes = report.createElement("Notes");
    releaseNotes.appendChild(report.createTextNode(release.getReleaseNotes()));
    releaseElem.appendChild(releaseNotes);

    String releaseDateString = release.getReleaseDate().format(
        DateTimeFormatter.ofPattern(dateFormat));
    Element releaseDate = report.createElement("ReleaseDate");
    releaseDate.appendChild(report.createTextNode(releaseDateString));
    releaseElem.appendChild(releaseDate);

    Element projectElement = report.createElement("Project");
    projectElement.appendChild(report.createTextNode(release.getProjectRelease().getLabel()));
    releaseElem.appendChild(projectElement);
  }


  public void createTeamChild(Main mainApp, AgileHistory team) {

    Element teamElem = report.createElement("Team");
    teamElement.appendChild(teamElem);
    teamElem.setAttribute("label", team.getAgileItem().getLabel());


    String theString = team.getStartDate().format(
        DateTimeFormatter.ofPattern(dateFormat));
    Element teamStartDate = report.createElement("StartDate");
    teamStartDate.appendChild(report.createTextNode(theString));
    teamElem.appendChild(teamStartDate);

    String endDate;
    if (team.getEndDate() != null) {
      endDate = team.getEndDate().format(
          DateTimeFormatter.ofPattern(dateFormat));
    } else {
      endDate = "No end Date";
    }

    Element teamEndDate = report.createElement("EndDate");
    teamEndDate.appendChild(report.createTextNode(endDate));
    teamElem.appendChild(teamEndDate);


    membersElement = report.createElement("Members");
    teamElem.appendChild(membersElement);
    for (Team listTeam : mainApp.getTeams()) {
      if (team.getAgileItem().getLabel().equals(listTeam.getLabel())) {
        createPersonChild(listTeam);
      }
    }
  }

  public void createPersonChild(Team listTeam) {
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

      Role role = listTeam.getMembersRole().get(member);
      Element teamMemberRole = report.createElement("Role");
      if (role != null) {
        teamMemberRole.appendChild(report.createTextNode(role.toString()));
      } else {
        teamMemberRole.appendChild(report.createTextNode("No role"));
      }
      memberElem.appendChild(teamMemberRole);

      skillElement = report.createElement("Skills");
      memberElem.appendChild(skillElement);
      for (Skill skill : member.getSkillSet()) {
        Element skillElem = report.createElement("Skill");
        skillElement.appendChild(skillElem);
        skillElem.setAttribute("label", skill.getLabel());

        Element skillDescription = report.createElement("Description");
        skillDescription.appendChild(report.createTextNode(skill.getSkillDescription()));
        skillElem.appendChild(skillDescription);
      }
    }
  }

  public void createOrphanTeam(Team team) {

    Element orphanTeamElem = report.createElement("Team");
    orphanTeamElem.setAttribute("label", team.getLabel());
    orphanTeam.appendChild(orphanTeamElem);

    for (Person person : team.getTeamMembers()) {
      Element memberElem = report.createElement("Member");
      orphanTeamElem.appendChild(memberElem);
      memberElem.setAttribute("label", person.getLabel());

      Element teamMemberName = report.createElement("FirstName");
      teamMemberName.appendChild(report.createTextNode(person.getFirstName()));
      memberElem.appendChild(teamMemberName);

      Element teamMemberLastName = report.createElement("LastName");
      teamMemberLastName.appendChild(report.createTextNode(person.getLastName()));
      memberElem.appendChild(teamMemberLastName);

      Role role = team.getMembersRole().get(person);
      Element teamMemberRole = report.createElement("Role");
      if (role != null) {
        teamMemberRole.appendChild(report.createTextNode(role.toString()));
      } else {
        teamMemberRole.appendChild(report.createTextNode("No role"));
      }
      memberElem.appendChild(teamMemberRole);

      skillElement = report.createElement("Skills");
      memberElem.appendChild(skillElement);
      for (Skill skill : person.getSkillSet()) {
        Element skillElem = report.createElement("Skill");
        skillElement.appendChild(skillElem);
        skillElem.setAttribute("label", skill.getLabel());

        Element skillDescription = report.createElement("Description");
        skillDescription.appendChild(report.createTextNode(skill.getSkillDescription()));
        skillElem.appendChild(skillDescription);
      }
    }
  }

  public void createOrphanPeople(Person person) {

    Element orphanPersonElem = report.createElement("Person");
    orphanPersonElem.setAttribute("label", person.getLabel());
    orphanPeople.appendChild(orphanPersonElem);

    Element teamMemberName = report.createElement("FirstName");
    teamMemberName.appendChild(report.createTextNode(person.getFirstName()));
    orphanPersonElem.appendChild(teamMemberName);

    Element teamMemberLastName = report.createElement("LastName");
    teamMemberLastName.appendChild(report.createTextNode(person.getLastName()));
    orphanPersonElem.appendChild(teamMemberLastName);

    skillElement = report.createElement("Skills");
    orphanPersonElem.appendChild(skillElement);
    for (Skill skill : person.getSkillSet()) {
      Element skillElem = report.createElement("Skill");
      skillElement.appendChild(skillElem);
      skillElem.setAttribute("label", skill.getLabel());

      Element skillDescription = report.createElement("Description");
      skillDescription.appendChild(report.createTextNode(skill.getSkillDescription()));
      skillElem.appendChild(skillDescription);
    }
  }

  public void createSkillChild(Skill skill) {
    Element skillElem = report.createElement("Skill");
    allSkills.appendChild(skillElem);
    skillElem.setAttribute("label", skill.getLabel());

    Element skillDescription = report.createElement("Description");
    skillDescription.appendChild(report.createTextNode(skill.getSkillDescription()));
    skillElem.appendChild(skillDescription);
  }

  public void createStoryChild(Story story) {
    Element storyElem = report.createElement("Story");
    allStories.appendChild(storyElem);
    storyElem.setAttribute("label", story.getLabel());

    Element storyName = report.createElement("Name");
    storyName.appendChild(report.createTextNode(story.getLongName()));
    storyElem.appendChild(storyName);

    Element storyDescription = report.createElement("Description");
    storyDescription.appendChild(report.createTextNode(story.getDescription()));
    storyElem.appendChild(storyDescription);

    Element storyCreator = report.createElement("Creator");
    storyCreator.appendChild(report.createTextNode(story.getCreator().getLabel()));
    storyElem.appendChild(storyCreator);
  }

}
