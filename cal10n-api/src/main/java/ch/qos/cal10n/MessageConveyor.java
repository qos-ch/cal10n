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

package ch.qos.cal10n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import ch.qos.cal10n.util.AnnotationExtractor;

/**
 * The default implementation for {@link IMessageConveyor} based on resource
 * bundles.
 * 
 * <p>
 * See also {@link #getMessage(Enum, Object...)} for details.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class MessageConveyor implements IMessageConveyor {

  Locale locale;

  /**
   * The {@link Locale} associated with this instance.
   * 
   * @param locale
   */
  public MessageConveyor(Locale locale) {
    this.locale = locale;
  }

  /**
   * Given an enum e, find the corresponding resource bundle and return the
   * internationalized message defined by the message code 'e'.
   * 
   * <p>
   * The name of the resource bundle is defined via the
   * {@link ResourceBundleName} annotation whereas the locale was specified in
   * this MessageConveyor instance's constructor.
   * 
   * @param e an enum instance used as message code
   * 
   */
  public <E extends Enum<?>> String getMessage(E e, Object... args) {
    String code = e.toString();

    String resouceBundleName = AnnotationExtractor.getResourceBundleName(e
        .getClass());
    if (resouceBundleName == null) {
      throw new IllegalArgumentException(
          "Missing ResourceBundleAnnotation in [" + e.getClass().getName()
              + "]. See also " + Cal10nConstants.MISSING_RB_ANNOTATION_URL);
    }
    ResourceBundle rb = ResourceBundle.getBundle(resouceBundleName, locale);

    String value = rb.getString(code);
    if (value == null) {
      return "No key found for " + code;
    } else {
      if (args == null || args.length == 0) {
        return value;
      } else {
        return MessageFormat.format(value, args);
      }
    }
  }

}
