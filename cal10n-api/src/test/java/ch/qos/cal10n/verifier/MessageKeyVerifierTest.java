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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

import ch.qos.cal10n.sample.Colors;
import ch.qos.cal10n.sample.Countries;
import ch.qos.cal10n.sample.Minimal;

/**
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class MessageKeyVerifierTest {

  @Test
  public void smoke() {
    IMessageKeyVerifier miv = new MessageKeyVerifier(Colors.class);
    List<Cal10nError> errorList = miv.verify(Locale.UK);
    assertEquals(0, errorList.size());
  }

  @Test
  public void withErrors_UK() {
    IMessageKeyVerifier miv = new MessageKeyVerifier(Countries.class);
    List<Cal10nError> errorList = miv.verify(Locale.UK);
    assertEquals(2, errorList.size());
    assertEquals("CH", errorList.get(0).getKey());
    assertEquals("BR", errorList.get(1).getKey());
  }

  @Test
  public void withErrors_FR() {
    IMessageKeyVerifier miv = new MessageKeyVerifier(Countries.class);
    List<Cal10nError> errorList = miv.verify(Locale.FRANCE);
    assertEquals(3, errorList.size());
    assertEquals("CH", errorList.get(0).getKey());
    assertEquals("CN", errorList.get(1).getKey());
    assertEquals("BR", errorList.get(2).getKey());
  }

  @Test
  public void all() {
    IMessageKeyVerifier mcv = new MessageKeyVerifier(Minimal.class);
    List<Cal10nError> errorList = mcv.verifyAllLocales();
    assertEquals(0, errorList.size());
  }
}
