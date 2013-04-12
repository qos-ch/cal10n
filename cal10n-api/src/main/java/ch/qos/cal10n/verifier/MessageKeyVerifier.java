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

import ch.qos.cal10n.util.AnnotationExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Given an enum class, verify that the resource bundles corresponding to a
 * given locale contains the correct keys.
 *
 * @author Ceki Gulcu
 */
public class MessageKeyVerifier extends MessageKeyVerifierBase {

  final Class<? extends Enum<?>> enumClass;

  public MessageKeyVerifier(Class<? extends Enum<?>> enumClass) {
    super(enumClass.getName(),  new AnnotationExtractor(enumClass));
    this.enumClass = enumClass;
  }

  public MessageKeyVerifier(String enumTypeAsStr) {
    this(buildEnumClass(enumTypeAsStr));
  }

  static Class<? extends Enum<?>> buildEnumClass(String enumClassAsStr) {
    String errMsg = "Failed to find enum class [" + enumClassAsStr + "]";
       try {
         return (Class<? extends Enum<?>>) Class.forName(enumClassAsStr);
       } catch (ClassNotFoundException e) {
         throw new IllegalStateException(errMsg, e);
       } catch (NoClassDefFoundError e) {
         throw new IllegalStateException(errMsg, e);
       }
  }

  protected List<String> extractKeysInEnum() {
    List<String> enumKeyList = new ArrayList<String>();
    Enum<?>[] enumArray = enumClass.getEnumConstants();
    for (Enum<?> e : enumArray) {
      enumKeyList.add(e.toString());
    }
    return enumKeyList;
  }



}
