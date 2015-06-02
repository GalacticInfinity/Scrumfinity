package seng302.group5.model;

import java.util.List;

/**
 * Estimate class, will not be displayed in list, only available in backlogs.
 * Created by Michael on 6/2/2015.
 */
public class Estimate {

  private String label;
  private List<String> estimateNames;

  /**
   * Default constructor for Estimate
   */
  public Estimate() {}

  /**
   * Constructor for Estimate with a label and list of estimate names.
   * @param label String name of estimate scale
   * @param estimateNames String list of items in the sacle
   */
  public Estimate(String label, List<String> estimateNames) {
    this.label = label;
    this.estimateNames = estimateNames;
  }

  /**
   * Replaces one of the previous estimate names with a new one.
   * @param changeName New name of estimate
   * @param index Position of estimate
   */
  public void changeEstimate(String changeName, int index) {
    estimateNames.remove(index);
    estimateNames.add(index, changeName);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public List<String> getEstimateNames() {
    return estimateNames;
  }

  public void setEstimateNames(List<String> estimateNames) {
    this.estimateNames = estimateNames;
  }
}
