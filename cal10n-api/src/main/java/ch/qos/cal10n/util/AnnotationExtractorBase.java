package ch.qos.cal10n.util;

import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * A base implementation which can extract cal10n annotation data from a yet unspecified
 * "enumType" type, e.g. an enum class or an enum Element.
 *
 * @author  Ceki Gulcu
 * @since 0.8
 */
abstract public class AnnotationExtractorBase implements IAnnotationExtractor {

  abstract protected LocaleData extractLocaleData();

  public Locale[] extractLocales() {
    LocaleData localeData = extractLocaleData();
    if (localeData == null) {
      return null;
    }
    return localeData.value();
  }

  public String[] extractLocaleNames() {
    Locale[] localeDataArray = extractLocales();

    if (localeDataArray == null) {
      return null;
    }

    String[] names = new String[localeDataArray.length];
    for (int i = 0; i < localeDataArray.length; i++) {
      names[i] = localeDataArray[i].value();
    }
    return names;
  }


  public String extractCharset(java.util.Locale juLocale) {
    LocaleData localeData = extractLocaleData();
    if (localeData == null) {
      return "";
    }

    String defaultCharset = localeData.defaultCharset();

    Locale la = findLocaleAnnotation(juLocale, localeData);
    String localeCharset = null;
    if (la != null) {
      localeCharset = la.charset();
    }
    if (!isEmptyString(localeCharset)) {
      return localeCharset;
    }

    return defaultCharset;
  }

  static Locale findLocaleAnnotation(java.util.Locale juLocale, LocaleData localeData) {
    Locale[] localeAnnotationArray = localeData.value();
    if (localeAnnotationArray == null) {
      return null;
    }
    for (Locale la : localeAnnotationArray) {
      if (la.value().equals(juLocale.toString())) {
        return la;
      }
    }
    return null;
  }

  static boolean isEmptyString(String s) {
    return s == null || s.length() == 0;
  }
}
