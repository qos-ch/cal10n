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

package ch.qos.cal10n.verifier;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import ch.qos.cal10n.Cal10nConstants;
import ch.qos.cal10n.util.AnnotationExtractor;
import ch.qos.cal10n.util.CAL10NResourceBundleFinder;
import ch.qos.cal10n.util.MiscUtil;
import ch.qos.cal10n.verifier.Cal10nError.ErrorType;

/**
 * Given an enum class, verify that the resource bundles corresponding to a
 * given locale contains the correct keys.
 * 
 * @author Ceki Gulcu
 */
public class MessageKeyVerifier implements IMessageKeyVerifier {

  Class<? extends Enum<?>> enumType;
  String enumTypeAsStr;

  public MessageKeyVerifier(Class<? extends Enum<?>> enumClass) {
    this.enumType = enumClass;
    this.enumTypeAsStr = enumClass.getName();
  }

  @SuppressWarnings("unchecked")
  public MessageKeyVerifier(String enumTypeAsStr) {
    this.enumTypeAsStr = enumTypeAsStr;
    String errMsg = "Failed to find enum class [" + enumTypeAsStr + "]";
    try {
      this.enumType = (Class<? extends Enum<?>>) Class.forName(enumTypeAsStr);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(errMsg, e);
    } catch (NoClassDefFoundError e) {
      throw new IllegalStateException(errMsg, e);
    }
  }

  public Class<? extends Enum<?>> getEnumType() {
    return enumType;
  }

  public String getEnumTypeAsStr() {
    return enumTypeAsStr;
  }

  public List<Cal10nError> verify(Locale locale) {
    List<Cal10nError> errorList = new ArrayList<Cal10nError>();

    String baseName = AnnotationExtractor.getBaseName(enumType);

    if (baseName == null) {
      errorList.add(new Cal10nError(ErrorType.MISSING_BN_ANNOTATION, "",
          enumType, locale, ""));
      // no point in continuing
      return errorList;
    }

    String charset = AnnotationExtractor.getCharset(enumType, Locale.FRENCH);
    ResourceBundle rb = CAL10NResourceBundleFinder.getBundle(this.getClass()
        .getClassLoader(), baseName, locale, charset);

    ErrorFactory errorFactory = new ErrorFactory(enumType, locale, baseName);

    if (rb == null) {
      errorList.add(errorFactory.buildError(ErrorType.FAILED_TO_FIND_RB, ""));
   // no point in continuing
      return errorList;
    }
    
    Set<String> rbKeySet = buildKeySetFromEnumeration(rb.getKeys());

    if (rbKeySet.size() == 0) {
      errorList.add(errorFactory.buildError(ErrorType.EMPTY_RB, ""));
    }

    Enum<?>[] enumArray = enumType.getEnumConstants();
    if (enumArray == null || enumArray.length == 0) {
      errorList.add(errorFactory.buildError(ErrorType.EMPTY_ENUM, ""));
    }

    if (errorList.size() != 0) {
      return errorList;
    }

    for (Enum<?> e : enumArray) {
      String enumKey = e.toString();
      if (rbKeySet.contains(enumKey)) {
        rbKeySet.remove(enumKey);
      } else {
        errorList.add(errorFactory.buildError(ErrorType.ABSENT_IN_RB, enumKey));
      }
    }

    for (String rbKey : rbKeySet) {
      errorList.add(errorFactory.buildError(ErrorType.ABSENT_IN_ENUM, rbKey));
    }
    return errorList;
  }

  private Set<String> buildKeySetFromEnumeration(Enumeration<String> e) {
    Set<String> set = new HashSet<String>();
    while (e.hasMoreElements()) {
      String s = e.nextElement();
      set.add(s);
    }
    return set;
  }

  public List<String> typeIsolatedVerify(Locale locale) {
    List<Cal10nError> errorList = verify(locale);
    List<String> strList = new ArrayList<String>();
    for (Cal10nError error : errorList) {
      strList.add(error.toString());
    }
    return strList;
  }

  /***
   * Verify all declared locales in one step.
   */
  public List<Cal10nError> verifyAllLocales() {
    List<Cal10nError> errorList = new ArrayList<Cal10nError>();

    String[] localeNameArray = getLocaleNames();

    if (localeNameArray == null || localeNameArray.length == 0) {
      String errMsg = MessageFormat.format(Cal10nConstants.MISSING_LD_ANNOTATION_MESSAGE, enumTypeAsStr);
      throw new IllegalStateException(errMsg);
    }
    for (String localeName : localeNameArray) {
      Locale locale = MiscUtil.toLocale(localeName);
      System.out.println(locale);
      List<Cal10nError> tmpList = verify(locale);
      errorList.addAll(tmpList);
    }

    return errorList;
  }

  public String[] getLocaleNames() {
    String[] localeNameArray = AnnotationExtractor.getLocaleNames(enumType);
    return localeNameArray;
  }

  public String getBaseName() {
    String rbName = AnnotationExtractor.getBaseName(enumType);
    return rbName;
  }

}
