package ch.qos.cal10n.util;

import ch.qos.cal10n.Locale;

/**
 * Interface for extracting cal10-related annotation data from an "enumTtype".
 * The actual <em>type</em> of the enumType is implementation specific. The basic
 * implementation uses an enum Class.
 *
 * @since 0.8
 */
public interface IAnnotationExtractor {

  public String getBaseName();

  public String[] extractLocaleNames();

  public Locale[] extractLocales();

  public String extractCharset(java.util.Locale juLocale);
}
