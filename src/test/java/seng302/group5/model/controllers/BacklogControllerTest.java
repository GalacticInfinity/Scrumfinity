package seng302.group5.model.controllers;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Person;
import seng302.group5.model.Story;

/**
 * Created by Michael on 5/29/2015.
 */
public class BacklogControllerTest {
  ObservableList<Story> allocatedStories;
  ObservableList<Story> availableStories;
  
  @Test
  public void testAdding() {
    ListView<Story> availableStoriesList = new ListView<>();

    allocatedStories = FXCollections.observableArrayList();
    availableStories = FXCollections.observableArrayList();

    Story story1 = new Story();
    story1.setLabel("Moo");
    story1.setCreator(new Person());
    Story story2 = new Story();
    story2.setLabel("Mooz");
    story2.setCreator(new Person());
    availableStories.addAll(story1, story2);
    availableStoriesList.setItems(availableStories);
    availableStoriesList.getSelectionModel().select(1);

    Story selectedStory = availableStoriesList.getSelectionModel().getSelectedItem();
    if (selectedStory != null) {
      this.allocatedStories.add(selectedStory);
      this.availableStories.remove(selectedStory);

      // TODO: estimates

/*      this.allocatedStoriesList.getSelectionModel().select(selectedStory);
      if (createOrEdit == CreateOrEdit.EDIT) {
        checkButtonDisabled();
      }*/
    }
  }

}
