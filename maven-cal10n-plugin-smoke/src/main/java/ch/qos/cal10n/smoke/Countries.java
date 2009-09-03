package ch.qos.cal10n.smoke;

import ch.qos.cal10n.Locale;
import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.LocaleData;

@BaseName("countries")
@LocaleData({
  @Locale("en"),
  @Locale("fr")
  })
public enum Countries {
   CH, 
   CN,
   FR,
   UK;
}
