package seng302.group5.model;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by @author Alex Woo
 */
public enum Status {
  DONE,
  VERIFY,
  IN_PROGRESS,
  NOT_STARTED;


  private static Map<Status, String> statusMapString;
  static {
    statusMapString = new EnumMap<>(Status.class);
    statusMapString.put(DONE, "Done");
    statusMapString.put(VERIFY, "Verify");
    statusMapString.put(IN_PROGRESS, "In Progress");
    statusMapString.put(NOT_STARTED, "Not started");
  }
  public static String getStatusString(Status status) {
    return statusMapString.get(status);
  }
  }

