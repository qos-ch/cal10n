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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ch.qos.cal10n.Cal10nTestConstants;

/**
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
public class ResourceBundleEncodingTest {

  // Useful links:

  // http://www.terena.org/activities/multiling/ml-docs/iso-8859.html

  Map<String, String> map = new HashMap<String, String>();
  Map<String, String> witness = new HashMap<String, String>();

  // encoding for Greek "ISO8859_7"
  @Test
  public void greek_ISO8859_7() throws IOException {
    FileInputStream fis = new FileInputStream(Cal10nTestConstants.TEST_CLASSES
        + "/encodingsISO8859/a_el_GR.properties");
    Reader reader = new InputStreamReader(fis, "ISO8859_7");
    Parser parser = new Parser(reader, map);
    parser.parseAndPopulate();

    String alpha = "\u03b1";
    witness.put("A", alpha);

    // char alphaChar = alpha.charAt(0);
    // System.out.println("alphaChar="+((int) alphaChar));
    // String suspected = map.get("A");
    // char suspectedChar = suspected.charAt(0);
    // System.out.println("suspectedChar="+((int) suspectedChar));

    assertEquals(witness, map);
  }

  // encoding for Turkish "ISO8859_3"
  @Test
  public void turkishISO8859_3() throws IOException {
    FileInputStream fis = new FileInputStream(Cal10nTestConstants.TEST_CLASSES
        + "/encodingsISO8859/a_tr_TR.properties");
    Reader reader = new InputStreamReader(fis, "ISO8859_3");
    Parser parser = new Parser(reader, map);
    parser.parseAndPopulate();

    // 0xBA 0x015F # LATIN SMALL LETTER S WITH CEDILLA
    char sCedilla = '\u015F';

    // 0xB9 0x0131 # LATIN SMALL LETTER DOTLESS I
    char iDotless = '\u0131';

    // nisan pronounced (nIshan)
    String witnessValue = "n" + iDotless + sCedilla + "an";
    witness.put("A", witnessValue);
    assertEquals(witness, map);
  }
  
  @Test
  public void greek_UTF8() throws IOException {
    FileInputStream fis = new FileInputStream(Cal10nTestConstants.TEST_CLASSES
        + "/encodingsUTF8/a_el_GR.properties");
    Reader reader = new InputStreamReader(fis, "UTF8");
    Parser parser = new Parser(reader, map);
    parser.parseAndPopulate();

    String alpha = "\u03b1";
    witness.put("A", alpha);

    assertEquals(witness, map);
  }
  
  @Test
  public void turkishUTF8() throws IOException {
    FileInputStream fis = new FileInputStream(Cal10nTestConstants.TEST_CLASSES
        + "/encodingsUTF8/a_tr_TR.properties");
    Reader reader = new InputStreamReader(fis, "UTF8");
    Parser parser = new Parser(reader, map);
    parser.parseAndPopulate();

    // 0xBA 0x015F # LATIN SMALL LETTER S WITH CEDILLA
    char sCedilla = '\u015F';

    // 0xB9 0x0131 # LATIN SMALL LETTER DOTLESS I
    char iDotless = '\u0131';

    // nisan pronounced (nIshan)
    String witnessValue = "n" + iDotless + sCedilla + "an";
    witness.put("A", witnessValue);
    assertEquals(witness, map);
  }
}
