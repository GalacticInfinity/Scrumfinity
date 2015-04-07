package seng302.group5.model;

import java.util.Date;

/**
 * Release class that sets the model for a release.
 * Created by Craig Barnard on 07/04/2015
 */
public class Release {

  private String releaseName;
  private String releaseDescription;
  private Date releaseDate;
  private String releaseNotes;
  private Project projectRelease = null;

  public void Release() {
    releaseName = "";
    releaseDescription = "";
    releaseNotes = "";
  }

  /**
   * Constructor for Release object.
   * @param releaseName
   * @param releaseDescription
   * @param releaseDate
   * @param releaseNotes
   * @param projectRelease
   */
  public void Release(String releaseName, String releaseDescription, Date releaseDate,
                      String releaseNotes, Project projectRelease) {
    this.releaseName = releaseName;
    this.releaseDescription = releaseDescription;
    this.releaseDate = releaseDate;
    this.releaseNotes = releaseNotes;
    this.projectRelease = projectRelease;
  }

  public String getReleaseName() {return this.releaseName;}

  public String getReleaseDescription() {return this.releaseDescription;}

  public Date getReleaseDate() {return  this.releaseDate;}

  public String getReleaseNotes() {return  this.releaseNotes;}

  public Project getProjectRelease() {return  this.projectRelease;}

  public void setReleaseName(String releaseName) {this.releaseName = releaseName;}

  public void setReleaseDescription(String releaseDescription) {
    this.releaseDescription = releaseDescription;
  }

  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  public void setProjectRelease(Project projectRelease) {
    this.projectRelease = projectRelease;
  }

  public void setReleaseNotes(String releaseNotes) {
    this.releaseNotes = releaseNotes;
  }

  @Override
  public String toString() {return this.releaseName;}
}
