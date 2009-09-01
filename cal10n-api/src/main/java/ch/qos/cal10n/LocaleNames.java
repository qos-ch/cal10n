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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.qos.cal10n.verifier.MessageKeyVerifier;

/**
 * This annotation serves to designate a list of locale names for which resource
 * bundles exist.
 * 
 * <p>
 * Typical usage is:
 * 
 * <pre>
 * &#064;LocaleNames({&quot;en&quot;, &quot;jp&quot;})
 * &#064;ResourceBundleName(&quot;colors&quot;);
 * public class enum Colors {
 *   RED, WHITE, BLUE; 
 * }
 * </pre>
 * 
 * <p>
 * In the above example, @LocaleNames({"en", "jp"}) means that there are English
 * (en) and Japanese translations for the message keys found in the Colors enum.
 * The name of the corresponding resource bundle is named "colors".
 * 
 * <p>
 * Verification tools such {@link MessageKeyVerifier} can thus conclude that the
 * resource bundle files <em>colors_en.properties</em> and
 * <em>colors_jp.properties</em> should exist and checked against the keys
 * defined in the Colors enum.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocaleNames {
  String[] value();
}
