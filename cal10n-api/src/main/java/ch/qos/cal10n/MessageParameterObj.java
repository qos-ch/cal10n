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

import java.util.Arrays;

/**
 * Holds data relevant for a deferred message lookup. This is useful when the
 * appropriate locale is <em>unknown</em> at the time or place where the message
 * key and message args are emitted. For example, a low level library might wish
 * to emit a localized message but it is only at the UI (user interface) layer
 * that the locale is known. As another example, imagine that the host where the
 * localized messages are presented to the user is in a different locale, e.g.
 * Japan, than the locale of the source host. e.g. US.
 * 
 * <p>
 * Instances of this class are intended to be immutable, subject to the
 * immutability of the supplied args.
 * 
 * @author Rick Beton
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
public class MessageParameterObj {

  private final Enum<?> key;
  private final Object[] args;

  /**
   * Constructs an instance.
   * 
   * @param key
   *          the key for the localized message.
   * @param args
   *          any message parameters, as required.
   */
  public MessageParameterObj(Enum<?> key, Object... args) {
    if (key == null) {
      new IllegalArgumentException("Enum argument \"key\" can't be null");
    }
    this.key = key;
    this.args = args;
  }

  public Enum<?> getKey() {
    return key;
  }

  public Object[] getArgs() {
    return args;
  }

  @Override
  public String toString() {
    final StringBuilder b = new StringBuilder("MessageParameterObj(");
    b.append(key.name());
    b.append(", ");
    b.append(Arrays.toString(args));
    b.append(")");
    return b.toString();
  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;

    MessageParameterObj other = (MessageParameterObj) obj;
    // key cannot be null by virtue of the check done within the constructor
    if (!key.equals(other.key))
      return false;

    return Arrays.equals(args, other.args);
  }
}
