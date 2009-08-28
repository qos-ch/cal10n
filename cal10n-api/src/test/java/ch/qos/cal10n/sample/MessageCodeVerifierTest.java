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

package ch.qos.cal10n.sample;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

import ch.qos.cal10n.verifier.Cal10nError;
import ch.qos.cal10n.verifier.IMessageCodeVerifier;
import ch.qos.cal10n.verifier.MessageCodeVerifier;

/**
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class MessageCodeVerifierTest {

  
  @Test
  public void smoke() {
    IMessageCodeVerifier miv = new MessageCodeVerifier(Colors.class);
    List<Cal10nError> errorList = miv.verify(Locale.UK);
    assertEquals(0, errorList.size());
  }
  
  @Test
  public void withErrors_UK() {
    IMessageCodeVerifier miv = new MessageCodeVerifier(Countries.class);
    List<Cal10nError> errorList = miv.verify(Locale.UK);
    assertEquals(2, errorList.size());
    assertEquals("CH", errorList.get(0).getCode());
    assertEquals("BR", errorList.get(1).getCode());
  }

  
  @Test
  public void withErrors_FR() {
    IMessageCodeVerifier miv = new MessageCodeVerifier(Countries.class);
    List<Cal10nError> errorList = miv.verify(Locale.FRANCE);
    assertEquals(3, errorList.size());
    assertEquals("CH", errorList.get(0).getCode());
    assertEquals("CN", errorList.get(1).getCode());
    assertEquals("BR", errorList.get(2).getCode());
  }
}
