/*
 * Copyright (c) 2010 QOS.ch All rights reserved.
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ceki G&uuml;c&uuml;
 */
public class LexicalUtilTest {


  void verify(String in, String expected) {
    StringBuilder inBuf = new StringBuilder(in);
    StringBuilder outBuf = LexicalUtil.convertSpecialCharacters(inBuf);
    assertEquals(expected, outBuf.toString());
  }
  @Test
  public void identity() {
    verify("abc", "abc");
  }

  @Test
  public void empty() {
    verify("", "");
  }

  @Test
  public void withLF() {
    verify("a\\nbc", "a\nbc");
  }

  @Test
  public void withMultipleLF() {
    verify("a\\nb\\rc", "a\nb\rc");
    verify("a\\nb\\r", "a\nb\r");
    verify("\\nb\\r", "\nb\r");
  }

  @Test
  public void withUnicode() {
    verify("a\\u00FCbc", "a\u00FCbc");
    verify("a\\u00fcbc", "a\u00fcbc");
    verify("a\\u2297bc", "a\u2297bc");

    verify("a\\u2169bc", "a\u2169bc");
    verify("\\u2169", "\u2169");
    verify("a\\u2169", "a\u2169");

  }

    @Test
    public void testCal29() {
      verify("\\\\n", "\\\\n");
      verify("beans\\\\n\\\\nProducer", "beans\\\\n\\\\nProducer");
    }
}
