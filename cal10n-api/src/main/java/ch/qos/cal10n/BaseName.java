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
 * This annotation serves to designate the name of the resource bundle
 * corresponding to an enum type.
 * 
 * <p>
 * Typical usage is:
 * 
 * <pre>
 * &#064;BaseName(&quot;colors&quot;);
 * &#064;LocaleData( { @Locale(&quot;en&quot;), @Locale(&quot;jp&quot;) } )
 * public class enum Colors {
 *   RED, WHITE, BLUE; 
 * }
 * </pre>
 * 
 * <p>
 * In the above example, @BaseName("colors") means that there exists a family of
 * resource bundle files with the base name "colors". In conjunction with the
 * information provided in the &#64;{@link LocaleData} annotation, we can assume
 * that the files <em>colors_en.properties</em> and
 * <em>colors_jp.properties</em> exist.
 * 
 * <p>
 * Verification tools such as {@link MessageKeyVerifier} can then proceed to
 * check that the keys defined in the enum type match those in the corresponding
 * resource bundles.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BaseName {
  String value();
}
