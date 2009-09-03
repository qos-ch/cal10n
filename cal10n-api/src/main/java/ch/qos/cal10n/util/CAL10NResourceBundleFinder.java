/*
 * Copyright (c) 2009 QOS.ch All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS  IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.qos.cal10n.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

public class CAL10NResourceBundleFinder {

  public static CAL10NResourceBundle getBundle(ClassLoader classLoader,
      String baseName, Locale locale, String charset) {

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

    CAL10NResourceBundle cprbLanguageOnly = makePropertyResourceBundle(
        classLoader, languageOnlyCandidate, charset);
    CAL10NResourceBundle cprbLanguageAndCountry = null;

    if (languageAndCountryCandidate != null) {
      cprbLanguageAndCountry = makePropertyResourceBundle(classLoader,
          languageAndCountryCandidate, charset);
    }

    if (cprbLanguageAndCountry != null) {
      cprbLanguageAndCountry.setParent(cprbLanguageOnly);
      return cprbLanguageAndCountry;
    }
    return cprbLanguageOnly;
  }

  private static CAL10NResourceBundle makePropertyResourceBundle(
      ClassLoader classLoader, String resourceCandiate, String charset) {

    CAL10NResourceBundle prb = null;

    URL url = classLoader.getResource(resourceCandiate);
    if (url != null) {
      try {
        InputStream in = openConnectionForUrl(url);

        Reader reader = toReader(in, charset);
        if (charset == null || charset.length() == 0)
          reader = new InputStreamReader(in);
        else
          reader = new InputStreamReader(in, charset);

        prb = new CAL10NResourceBundle(reader, MiscUtil.urlToFile(url));
        in.close();
      } catch (IOException e) {
      }
    }
    return prb;
  }

  static Reader toReader(InputStream in, String charset) {
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

  private static String computeLanguageAndCountryCandidate(String baseName,
      Locale locale) {
    String language = locale.getLanguage();
    String country = locale.getCountry();
    if (country != null && country.length() > 0) {
      return baseName + "_" + language + "_" + country + ".properties";
    } else {
      return null;
    }
  }

  private static String computeLanguageOnlyCandidate(String baseName,
      Locale locale) {
    String language = locale.getLanguage();
    return baseName + "_" + language + ".properties";
  }

  private static InputStream openConnectionForUrl(URL url) throws IOException {
    URLConnection urlConnection = url.openConnection();
    urlConnection.setDefaultUseCaches(false);
    InputStream in = urlConnection.getInputStream();
    return in;
  }
}
