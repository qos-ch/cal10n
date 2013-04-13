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
import ch.qos.cal10n.LocaleData;

/**
 * Given an enum class, retrieve its cal10n-related values from its cal10-specific annotations.
 * Note that much of the work in this class is performed by the base class.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class AnnotationExtractorViaEnumClass extends AbstractAnnotationExtractor {

  final Class<?> enumClass;

  public AnnotationExtractorViaEnumClass(Class<?> enumClass) {
    this.enumClass = enumClass;
  }

  public String getBaseName() {
    BaseName rbnAnnotation = enumClass.getAnnotation(BaseName.class);
    if (rbnAnnotation == null) {
      return null;
    }
    return rbnAnnotation.value();
  }

  @Override
  protected LocaleData extractLocaleData() {
    return enumClass.getAnnotation(LocaleData.class);
  }

}
