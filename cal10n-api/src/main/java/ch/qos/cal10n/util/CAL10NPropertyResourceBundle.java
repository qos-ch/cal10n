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
import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class CAL10NPropertyResourceBundle extends PropertyResourceBundle {

  static long CHECK_DELAY = 10 * 60 * 1000; // 10 minutes delay

  File hostFile;
  volatile long nextCheck;
  long lastModified;

  public CAL10NPropertyResourceBundle(InputStream is, File file)
      throws IOException {
    super(is);
    this.hostFile = file;
    nextCheck = System.currentTimeMillis() + CHECK_DELAY;
  }

  public void setParent(ResourceBundle parent) {
    super.setParent(parent);
  }

  public boolean hasChanged() {
    // if the host file is unknown, no point in a check
    if (hostFile == null) {
      return false;
    }

    long now = System.currentTimeMillis();
    if (now < nextCheck) {
      return false;
    } else {
      nextCheck = now + CHECK_DELAY;
      if (lastModified != hostFile.lastModified()) {
        lastModified = hostFile.lastModified();
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * WARNING: Used for testing purposes. Do not invoke directly in user code.
   */
  public void resetCheckTimes() {
    nextCheck = 0;
    lastModified = 0;
  }
}
