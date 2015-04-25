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
    this.releaseName = "";
    this.releaseDescription = "";
    this.releaseDate = null;
    this.releaseNotes = "";
  }

  /**
   * Constructor for Release object.
   *
   * @param releaseName        name of release, None-Null, Can't be greater than 8 characters.
   * @param releaseDescription description of release
   * @param releaseDate        date of release
   * @param releaseNotes       release notes
   * @param projectRelease     project allocated to
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

  public String getReleaseName() {
    return this.releaseName;
  }

  public String getReleaseDescription() {
    return this.releaseDescription;
  }

  public LocalDate getReleaseDate() {
    return this.releaseDate;
  }

  public String getReleaseNotes() {
    return this.releaseNotes;
  }

  public Project getProjectRelease() {
    return this.projectRelease;
  }

  public void setReleaseName(String releaseName) {
    this.releaseName = releaseName;
  }

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

  /**
   * Copies the release input fields into current object.
   *
   * @param agileItem Person who's fields are to be copied
   */
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

  /**
   * toString override
   *
   * @return Release's name
   */
  @Override
  public String toString() {
    return this.releaseName;
  }

  /**
   * Check if two releases' ids are equal
   *
   * @param obj Object to compare to.
   * @return Whether releases' ids are equal
   */
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
