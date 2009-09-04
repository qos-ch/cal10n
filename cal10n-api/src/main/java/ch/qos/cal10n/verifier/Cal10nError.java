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

import java.util.Locale;

/**
 * 
 * Aggregates various parameters of a verification error in a single place
 * (class).
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class Cal10nError {

  enum ErrorType {
    // MISSING_LOCALE_NAMES_ANNOTATION

    MISSING_BN_ANNOTATION, FAILED_TO_FIND_RB, EMPTY_RB, EMPTY_ENUM, ABSENT_IN_RB, ABSENT_IN_ENUM;
  }

  final ErrorType errorType;
  final String key;
  final Locale locale;
  final Class<?> enumClass;
  final String enumClassName;
  final String baseName;

  Cal10nError(ErrorType errorType, String key, Class<?> enumClass,
      Locale locale, String baseName) {
    this.errorType = errorType;
    this.key = key;
    this.enumClass = enumClass;
    this.enumClassName = enumClass.getName();
    this.locale = locale;
    this.baseName = baseName;
  }

  public ErrorType getErrorType() {
    return errorType;
  }

  public String getKey() {
    return key;
  }

  public Locale getLocale() {
    return locale;
  }

  public Class<?> getEnumClass() {
    return enumClass;
  }

  @Override
  public String toString() {
    switch (errorType) {
    case MISSING_BN_ANNOTATION:
      return "Missing @BaseName annotation in enum type ["
          + enumClassName + "]";
    case FAILED_TO_FIND_RB:
      return "Failed to locate resource bundle [" + baseName
          + "] for locale [" + locale + "] for enum type [" + enumClassName
          + "]";
    case EMPTY_RB:
      return "Empty resource bundle named [" + baseName
          + "] for locale [" + locale + "]";
    case EMPTY_ENUM:
      return "Empty enum type [" + enumClassName + "]";
    case ABSENT_IN_ENUM:
      return "Key [" + key + "] present in resource bundle named ["
          + baseName + "] for locale [" + locale
          + "] but absent in enum type [" + enumClassName + "]";
    case ABSENT_IN_RB:
      return "Key [" + key + "] present in enum type [" + enumClassName
          + "] but absent in resource bundle named [" + baseName
          + "] for locale [" + locale + "]";
    default:
      throw new IllegalStateException("Impossible to reach here");
    }
  }
}
