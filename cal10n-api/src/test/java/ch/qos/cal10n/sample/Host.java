package ch.qos.cal10n.sample;

import ch.qos.cal10n.BaseName;

public class Host {

  @BaseName("colors")
  public enum OtherColors {
    RED {
      
    },
    BLUE, 
    GREEN;
  }
}
