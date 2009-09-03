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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

import ch.qos.cal10n.sample.Colors;
import ch.qos.cal10n.util.CAL10NResourceBundle;
import ch.qos.cal10n.util.MiscUtil;

public class MessageConveyorReloadTest {

  
  @Test
  public void bundleReload() throws IOException, InterruptedException {
    ClassLoader classLoader = this.getClass().getClassLoader();
    String resourceCandidate =  "colors" + "_" + "en" + ".properties";
    URL url = classLoader.getResource(resourceCandidate);
    assertNotNull("the problem is in this test, not the code tested", url);

    MessageConveyor mc = new MessageConveyor(new Locale("en"));
    mc.getMessage(Colors.BLUE);
    
    CAL10NResourceBundle initalRB = mc.cache.get(Colors.BLUE.getDeclaringClass().getName());
    initalRB.resetCheckTimes();
    File file =  MiscUtil.urlToFile(url);
    file.setLastModified(System.currentTimeMillis()+60*60*1000);
    mc.getMessage(Colors.BLUE);
    ResourceBundle other = mc.cache.get(Colors.BLUE.getDeclaringClass().getName());
    assertTrue(initalRB != other);
  }
}
