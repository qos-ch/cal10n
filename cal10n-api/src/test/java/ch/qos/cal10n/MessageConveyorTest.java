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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Test;

import ch.qos.cal10n.sample.Colors;
import ch.qos.cal10n.sample.Minimal;
import ch.qos.cal10n.sample.Host.OtherColors;

public class MessageConveyorTest {

  @Test
  public void smoke_EN() {
    MessageConveyor rbbmc = new MessageConveyor(Locale.UK);
    String val;

    val = rbbmc.getMessage(Colors.BLUE);
    assertEquals("violets are blue", val);

    val = rbbmc.getMessage(Colors.GREEN, "apples");
    assertEquals("apples are green", val);
  }

  // see http://jira.qos.ch/browse/CAL-1
  @Test
  public void nestedEnum_EN() {
    MessageConveyor rbbmc = new MessageConveyor(Locale.UK);
    String val;

    val = rbbmc.getMessage(Colors.RED);
    assertEquals("roses are red", val);

    val = rbbmc.getMessage(OtherColors.RED);
    assertEquals("roses are red", val);

    val = rbbmc.getMessage(OtherColors.BLUE);
    assertEquals("violets are blue", val);
  }

  @Test
  public void smoke_FR() {
    MessageConveyor rbbmc = new MessageConveyor(Locale.FRANCE);
    String val;

    val = rbbmc.getMessage(Colors.BLUE);
    assertEquals("les violettes sont bleues", val);

    // lemon=citron in french. This illustrates the problem of
    // translating the parameters of a message
    val = rbbmc.getMessage(Colors.GREEN, "pommes");
    assertEquals("les pommes sont verts", val);
  }

  @Test
  public void mpo() {
    MessageConveyor rbbmc = new MessageConveyor(Locale.UK);
    MessageParameterObj mpo;
    String val;

    mpo = new MessageParameterObj(Colors.BLUE);
    val = rbbmc.getMessage(mpo);
    assertEquals("violets are blue", val);

    mpo = new MessageParameterObj(Colors.GREEN, "apples");
    val = rbbmc.getMessage(mpo);
    assertEquals("apples are green", val);
  }

  @Test
  public void failedRBLookup() {

    MessageConveyor mc = new MessageConveyor(Locale.CHINA);
    try {
      mc.getMessage(Colors.BLUE);
      fail("missing exception");
    } catch (MessageConveyorException e) {
      assertEquals(
          "Failed to locate resource bundle [colors] for locale [zh_CN] for enum type [ch.qos.cal10n.sample.Colors]",
          e.getMessage());
    }
  }
  
  @Test
  public void minimal() {
    MessageConveyor mc = new MessageConveyor(Locale.ENGLISH);
    assertEquals("A", mc.getMessage(Minimal.A));
  }
  
}
