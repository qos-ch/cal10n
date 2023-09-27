package ch.qos.cal10n.util;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import ch.qos.cal10n.Settings;

/**
 *  @since 0.8.1
 */
public abstract class AbstractCAL10NBundleFinder implements CAL10NBundleFinder {
   
  public CAL10NBundle getBundle(String baseName, Locale locale, String charset) {

    // same as the JDK convention
    //
    // It generates a path name from the candidate bundle name by replacing all
    // "."
    // characters with "/" and appending the string ".properties".
    // / see also http: // tinyurl.com/ldgej8
    baseName = baseName.replace('.', '/');

    String languageAndCountryCandidate = computeLanguageAndCountryCandidate(
            baseName, locale);
    String languageOnlyCandidate = computeLanguageOnlyCandidate(baseName,
            locale);
    String noLanguageCandidate = computeNoLanguageCandidate(baseName);
    
    final boolean rootLanguageFallbackEnabled = Settings.isRootLanguageFallbackEnabled();
    CAL10NBundle cprbNoLanguage = null;
    if (rootLanguageFallbackEnabled) {
        cprbNoLanguage = makePropertyResourceBundle(noLanguageCandidate, charset);
    }

    CAL10NBundle cprbLanguageOnly = makePropertyResourceBundle(languageOnlyCandidate, charset);
    CAL10NBundle cprbLanguageAndCountry = null;

    if (languageAndCountryCandidate != null) {
      cprbLanguageAndCountry = makePropertyResourceBundle(languageAndCountryCandidate, charset);
    }
    
    if (rootLanguageFallbackEnabled && cprbNoLanguage != null && cprbLanguageOnly != null) {
        cprbLanguageOnly.setParent(cprbNoLanguage);
    }

    if (cprbLanguageAndCountry != null) {
      if (cprbLanguageOnly != null) {
        cprbLanguageAndCountry.setParent(cprbLanguageOnly);
      } else if (rootLanguageFallbackEnabled && cprbNoLanguage != null) {
        cprbLanguageAndCountry.setParent(cprbNoLanguage);
      }
      return cprbLanguageAndCountry;
    } else if (cprbLanguageOnly != null) {
      return cprbLanguageOnly;
    }
    return cprbNoLanguage;
  }

  private String computeLanguageAndCountryCandidate(String baseName,
                                                    Locale locale) {
    String language = locale.getLanguage();
    String country = locale.getCountry();
    if (country != null && country.length() > 0) {
      return baseName + "_" + language + "_" + country + ".properties";
    } else {
      return null;
    }
  }

  abstract protected URL getResource(String resourceCandidate);

  private CAL10NBundle makePropertyResourceBundle(String resourceCandidate, String charset) {

    CAL10NBundle prb = null;
    URL url = getResource(resourceCandidate);
    if (url != null) {
      try {
        InputStream in = openConnectionForUrl(url);

        Reader reader = toReader(in, charset);
        if (charset == null || charset.length() == 0)
          reader = new InputStreamReader(in);
        else
          reader = new InputStreamReader(in, charset);

        prb = new CAL10NBundle(reader, MiscUtil.urlToFile(url));
        in.close();
      } catch (IOException e) {
      }
    }
    return prb;
  }

  private String computeLanguageOnlyCandidate(String baseName,
                                              Locale locale) {
    String language = locale.getLanguage();
    return baseName + "_" + language + ".properties";
  }

  private String computeNoLanguageCandidate(String baseName) {
    return baseName +  ".properties";
  }

  Reader toReader(InputStream in, String charset) {
    if (charset == null || charset.length() == 0)
      return new InputStreamReader(in);
    else {
      try {
        return new InputStreamReader(in, charset);
      } catch (UnsupportedEncodingException e) {
        throw new IllegalArgumentException("Failed to open reader", e);
      }
    }
  }

  private InputStream openConnectionForUrl(URL url) throws IOException {
    URLConnection urlConnection = url.openConnection();
    urlConnection.setDefaultUseCaches(false);
    InputStream in = urlConnection.getInputStream();
    return in;
  }
}
