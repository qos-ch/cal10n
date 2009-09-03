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

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ch.qos.cal10n.Cal10nTestConstants;

public class ParserTest {

  Map<String, String> map = new HashMap<String, String>();
  Map<String, String> witness = new HashMap<String, String>();

  
  @Test
  public void smoke() throws IOException {
    FileReader fr = new FileReader(Cal10nTestConstants.TEST_CLASSES+"/parser/smoke.properties");
    Parser parser  = new Parser(fr, map);
    parser.parseAndPopulate();
    
    witness.put("K0", "V0");
    witness.put("K1", "V1");
    assertEquals(witness, map);
  }
  
  
  @Test
  public void medium() throws IOException {
    FileReader fr = new FileReader(Cal10nTestConstants.TEST_CLASSES+"/parser/medium.properties");
    Parser parser  = new Parser(fr, map);
    parser.parseAndPopulate();
    
    witness.put("K0", "V0 X");
    witness.put("K1", "V1");
    assertEquals(witness, map);
  }
  
  @Test
  public void full() throws IOException {
    FileReader fr = new FileReader(Cal10nTestConstants.TEST_CLASSES+"/parser/full.properties");
    Parser parser  = new Parser(fr, map);
    parser.parseAndPopulate();
    
    witness.put("K0", "V0 X");
    witness.put("K1", "V1");
    witness.put("K2", "V2 l1l2  l3");
    witness.put("K3", "V3 \\t a");
    assertEquals(witness, map);
  }
  

}
