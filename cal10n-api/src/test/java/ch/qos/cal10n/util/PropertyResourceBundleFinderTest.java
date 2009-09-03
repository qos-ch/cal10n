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

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

import ch.qos.cal10n.sample.Colors;

public class PropertyResourceBundleFinderTest {
  ResourceBundle rb;
  String encoding;
  
  Class<?> enumTyoe = Colors.BLUE.getDeclaringClass();
  
  @Test
  public void smoke() throws IOException {
    encoding = AnnotationExtractor.getCharset(enumTyoe, Locale.FRENCH);
    rb = CAL10NResourceBundleFinder.getBundle(this.getClass().getClassLoader(),
         "colors", Locale.FRENCH, encoding);
    assertEquals("les roses sont rouges", rb.getString("RED"));
  }

  @Test
  public void withCountry() throws IOException {
    encoding = AnnotationExtractor.getCharset(enumTyoe, Locale.FRENCH);
    rb = CAL10NResourceBundleFinder.getBundle(this.getClass().getClassLoader(),
        "colors", Locale.FRENCH, encoding);
    assertEquals("les roses sont rouges", rb.getString("RED"));

    rb = CAL10NResourceBundleFinder.getBundle(this.getClass().getClassLoader(),
        "colors", Locale.FRANCE, encoding);
    assertEquals("les roses sont rouges, et alors?", rb.getString("RED"));
  }

  @Test
  public void inDirectory() throws IOException {
    encoding = AnnotationExtractor.getCharset(enumTyoe, Locale.ENGLISH);
    rb = CAL10NResourceBundleFinder.getBundle(this.getClass().getClassLoader(),
        "foobar/sample", Locale.ENGLISH, encoding);
    assertEquals("A is the first letter of the alphabet", rb.getString("A"));

    rb = CAL10NResourceBundleFinder.getBundle(this.getClass().getClassLoader(),
        "foobar.sample", Locale.ENGLISH, encoding);
    assertEquals("A is the first letter of the alphabet", rb.getString("A"));
  }
  

}
