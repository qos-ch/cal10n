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

import java.io.File;
import java.net.URL;
import java.util.Locale;

public class MiscUtil {

  public static Locale toLocale(String localeName) {
    if (localeName == null) {

    }
    if (localeName.contains("_")) {
      String[] array = localeName.split("_");
      return new Locale(array[0], array[1]);
    } else {
      return new Locale(localeName);
    }
  }
  
  public static File urlToFile(URL url) {
    if(url.getProtocol() != "file") {
      return null;
    }
    String path = url.getPath();
    if(path == null)
      return null;
    File candidate = new File(path);
    if(candidate.exists()) {
      return candidate;
    } else {
     return null;
    }
  }
  
}
