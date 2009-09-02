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

// note that the getMessage method does not take a Locale.
// It is supposed that the MessageConveyor instance knows
// which locale to use.

/**
 * Retrieve a localized message by its key as specified by an enum.
 * 
 * <p>
 * The strategy in retrieving messages may vary from implementation to
 * implementation.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public interface IMessageConveyor {

  /**
   * Retrieve a localized message by its key as specified by an enum.
   * 
   * <p>
   * Note that any further arguments passed in 'args' will be interpolated using
   * the translated message. The interpolation will be done by and according to
   * conventions of {@link MessageFormat}.
   * 
   * @param <E>
   *          an enum type
   * @param key
   *          an enum instance
   * @param args
   *          optional arguments
   * @return The translated/localized message
   */
  <E extends Enum<?>> String getMessage(E key, Object... args) throws MessageConveyorException;

  /**
   * Syntactic sugar for the case where the massage is contained in a
   * {@link MessageParameterObj}.
   * 
   * <p>
   * Equivalent to calling
   * 
   * <pre>
   * getMessage(mpo.getKey(), mpo.getArgs());
   * </pre>
   * 
   * @see #getMessage(Enum, Object...)
   * @param mpo
   *          The MessageParameterObj to translate
   * @return translated message
   */
  String getMessage(MessageParameterObj mpo) throws MessageConveyorException;
}
