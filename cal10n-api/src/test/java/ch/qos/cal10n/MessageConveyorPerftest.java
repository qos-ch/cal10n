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

import java.util.Locale;

import org.junit.Ignore;
import org.junit.Test;

import ch.qos.cal10n.sample.Colors;

// with caching ~300 nanos per translation
// without caching 149'963 nanos or 149 micros per translation

public class MessageConveyorPerftest {

  static int RUN_LENGTH = 100 *1000;
  public String s;
  
  double loop() {
    long start = System.nanoTime();
 
    IMessageConveyor mc = new MessageConveyor(Locale.ENGLISH);
    for(int i = 0; i < RUN_LENGTH; i++) {
      s = mc.getMessage(Colors.BLUE);
    }
    long end = System.nanoTime();
    return (end - start) * 1.0 / RUN_LENGTH;
  }
  
  @Ignore
  @Test
  public void perfTest() {
    loop();
    loop();
    
    System.out.println("avg = "+loop());
  }
}
