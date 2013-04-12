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

import ch.qos.cal10n.verifier.CAL10NError.ErrorType;

/**
 * Simplifies the creation of {@link CAL10NError} instances.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
public class ErrorFactory {

  final Locale locale;
  final String enumClassName;
  final String resourceBundleName;

  public ErrorFactory(String enumClassName, Locale locale,
      String resourceBundleName) {
    this.locale = locale;
    this.enumClassName = enumClassName;
    this.resourceBundleName = resourceBundleName;
  }

  CAL10NError buildError(ErrorType errorType, String key) {
    return new CAL10NError(errorType, key, enumClassName, locale,
        resourceBundleName);
  }
}
