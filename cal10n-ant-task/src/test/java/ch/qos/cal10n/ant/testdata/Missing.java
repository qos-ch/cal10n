package ch.qos.cal10n.ant.testdata;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("missing")
@LocaleData({
        @Locale("en"),
        @Locale("fr")
})
public enum Missing {
    TEST,
    ANOTHER
}
