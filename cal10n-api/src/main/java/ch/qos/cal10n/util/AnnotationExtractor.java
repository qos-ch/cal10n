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

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
public class AnnotationExtractor {

  static public <E extends Enum<?>> String getBaseName(Class<E> enumClass) {
    BaseName rbnAnnotation = (BaseName) enumClass.getAnnotation(BaseName.class);
    if (rbnAnnotation == null) {
      return null;
    }
    return rbnAnnotation.value();
  }

  static public <E extends Enum<?>> String[] getLocaleNames(Class<E> enumClass) {
    Locale[] localeDataArray = getLocaleData(enumClass);

    if (localeDataArray == null) {
      return null;
    }

    String[] names = new String[localeDataArray.length];
    for (int i = 0; i < localeDataArray.length; i++) {
      names[i] = localeDataArray[i].value();
    }
    return names;
  }

  static public <E extends Enum<?>> Locale[] getLocaleData(Class<E> enumClass) {
    LocaleData localeDataArrayAnnotation = (LocaleData) enumClass
        .getAnnotation(LocaleData.class);
    if (localeDataArrayAnnotation == null) {
      return null;
    }
    return localeDataArrayAnnotation.value();
  }

  public static String getCharset(Class<?> enumClass,
      java.util.Locale juLocale) {
    LocaleData localeDataArrayAnnotation = (LocaleData) enumClass
        .getAnnotation(LocaleData.class);
    if (localeDataArrayAnnotation == null) {
      return "";
    }
    
    String defaultCharset = localeDataArrayAnnotation.defaultCharset();
    
    Locale  la = findLocaleAnnotation(juLocale, localeDataArrayAnnotation);
    String localeCharset = null;
    if(la != null) {
      localeCharset = la.charset();
    }
    if(!isEmttyString(localeCharset)) {
      return localeCharset;
    }
    
    return defaultCharset;
  }

  static Locale findLocaleAnnotation(java.util.Locale julocale, LocaleData localeDataArrayAnnotation) {
    Locale[] localeAnnotationArray = localeDataArrayAnnotation.value();
    if(localeAnnotationArray == null) {
      return null;
    }
    for(Locale la: localeAnnotationArray) {
      if(la.value().equals(julocale.toString())) {
        return la;
      }
    }
    
    return null;
  }
  
  static boolean isEmttyString(String s) {
    return s == null || s.length() == 0;
  }
}
