package seng302.group5.model.undoredo;

/**
 * Defines actions to be stored into the undo/redo handler
 * @author Su-Shing Chen
 */
public enum Action {
  PROJECT_CREATE, // Project creation - 1 AgileItem
  PROJECT_EDIT,   // Project edit - 2 AgileItems
  PROJECT_DELETE, // Project deletion - 1 AgileItem
  PERSON_CREATE,  // Person creation - 1 AgileItem
  PERSON_EDIT,    // Person edit - 2 AgileItems
  PERSON_DELETE,  // Person deletion - 1 AgileItem
  SKILL_CREATE,   // Skill creation - 1 AgileItem
  SKILL_EDIT,     // Skill edit - 2 AgileItems
  SKILL_DELETE,   // Skill deletion - 1 AgileItem
  RELEASE_CREATE, // Release creation - 1 AgileItem
  RELEASE_EDIT,   // Release edit - 2 AgileItem
  RELEASE_DELETE, // Release delete - 1 AgileItem
  UNDEFINED       // Undefined action - no AgileItems
}
