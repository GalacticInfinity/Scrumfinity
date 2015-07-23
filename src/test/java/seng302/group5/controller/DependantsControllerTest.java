package seng302.group5.controller;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import seng302.group5.controller.dialogControllers.DependantsDialogController;
import seng302.group5.model.Story;

/**
 * Created by @author Alex Woo
 */
public class DependantsControllerTest {

  Story storya;
  Story storyb;
  Story storyc;
  Story storyd;
  Story storye;
  Story storyf;
  Story storyg;
  Story storyh;

  @Before
  public void setUp() throws Exception {
    DependantsDialogController ddc = new DependantsDialogController();

    storya = new Story("a", "a", "a", null);
    storyb = new Story("b", "b", "b", null);
    storyc = new Story("c", "c", "c", null);
    storyd = new Story("d", "d", "d", null);
    storye = new Story("e", "e", "e", null);
    storyf = new Story("f", "f", "f", null);
    storyg = new Story("g", "g", "g", null);
    storyh = new Story("h", "h", "h", null);

    List<Story> depsa = new ArrayList<Story>();
    depsa.add(storyb);
    depsa.add(storyc);
    storya.addAllDependencies(depsa);

    List<Story> depsb = new ArrayList<Story>();
    depsb.add(storyd);
    depsb.add(storye);
    storyb.addAllDependencies(depsb);

  }


}
