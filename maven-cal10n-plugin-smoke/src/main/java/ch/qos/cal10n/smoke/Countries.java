package ch.qos.cal10n.smoke;

import ch.qos.cal10n.LocaleNames;
import ch.qos.cal10n.BaseName;

@BaseName("countries")
@LocaleNames({"en", "fr"})
public enum Countries {
   CH, 
   CN,
   FR,
   UK;
}
