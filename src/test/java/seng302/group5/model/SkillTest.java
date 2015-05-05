package seng302.group5.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shingy on 3/04/2015.
 */
public class SkillTest {

  private String skillName;
  private String skillDescription;

  private Skill skill;

  @Before
  public void setUp() throws Exception {
    skillName = "C++";
    skillDescription = "Can program in C++";
    skill = new Skill(skillName, skillDescription);
  }

  @Test
  public void testToString() throws Exception {
    String result = skill.getLabel();
    assertEquals(skillName, result);
  }
}
