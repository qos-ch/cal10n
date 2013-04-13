package ch.qos.cal10n.util;


import java.util.Locale;

/**
 *  @since 0.8.1
 */
public interface CAL10NBundleFinder {
  public CAL10NBundle getBundle(String baseName, Locale locale, String charset);

}
