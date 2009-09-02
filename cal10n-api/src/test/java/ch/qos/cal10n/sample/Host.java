package ch.qos.cal10n.sample;

import ch.qos.cal10n.BaseName;

public class Host {

  @BaseName("colors")
  enum OtherColors {
    RED {
      
    },
    BLUE, 
    GREEN;
  }
}
