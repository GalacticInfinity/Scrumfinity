package seng302.group5.sample;

import java.util.Calendar;

/**
 * Created by @author Alex Woo
 */
abstract public class Actor {

  private String name;
  private Calendar DOB;

  public Calendar getDOB() {
    return DOB;
  }

  public void setDOB(Calendar DOB) {
    this.DOB = DOB;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName(){
    return name;
  }



}
