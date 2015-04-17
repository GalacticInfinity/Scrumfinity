package seng302.group5.model;

import java.time.LocalDate;

/**
 * Release class that sets the model for a release.
 * Created by Craig Barnard on 07/04/2015
 */
public class Release implements AgileItem {

  private String releaseName;
  private String releaseDescription;
  private LocalDate releaseDate;
  private String releaseNotes;
  private Project projectRelease = null;

  public Release() {
    releaseName = "";
    releaseDescription = "";
    releaseNotes = "";
  }

  /**
   * Constructor for Release object.
   * @param releaseName
   * @param releaseDescription
   * @param //releaseDate
   * @param releaseNotes
   * @param //projectRelease
   */
  public Release(String releaseName, String releaseDescription, String releaseNotes,
                 LocalDate releaseDate, Project projectRelease) {
    this.releaseName = releaseName;
    this.releaseDescription = releaseDescription;
    this.releaseDate = releaseDate;
    this.releaseNotes = releaseNotes;
    this.projectRelease = projectRelease;
  }

  public Release(Release clone) {
    this.releaseName = clone.getReleaseName();
    this.releaseDescription = clone.getReleaseDescription();
    this.releaseDate = clone.getReleaseDate();
    this.releaseNotes = clone.getReleaseNotes();
    this.projectRelease = clone.getProjectRelease();
  }

  public String getReleaseName() {return this.releaseName;}

  public String getReleaseDescription() {return this.releaseDescription;}

  public LocalDate getReleaseDate() {return  this.releaseDate;}

  public String getReleaseNotes() {return  this.releaseNotes;}

  public Project getProjectRelease() {return  this.projectRelease;}

  public void setReleaseName(String releaseName) {this.releaseName = releaseName;}

  public void setReleaseDescription(String releaseDescription) {
    this.releaseDescription = releaseDescription;
  }

  public void setReleaseDate(LocalDate releaseDate) {
    this.releaseDate = releaseDate;
  }

  public void setProjectRelease(Project projectRelease) {
    this.projectRelease = projectRelease;
  }

  public void setReleaseNotes(String releaseNotes) {
    this.releaseNotes = releaseNotes;
  }

  @Override
  public void copyValues(AgileItem agileItem) {
    if (agileItem instanceof Release) {
      Release clone = (Release) agileItem;
      this.releaseName = clone.getReleaseName();
      this.releaseDescription = clone.getReleaseDescription();
      this.releaseDate = clone.getReleaseDate();
      this.releaseNotes = clone.getReleaseNotes();
      this.projectRelease = clone.getProjectRelease();
    }
  }

  @Override
  public String toString() {
    return this.releaseName;
  }

  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj instanceof Release) {
      Release release = (Release) obj;
      result = this.releaseName.equals(release.getReleaseName());
    }
    return result;
  }
}
