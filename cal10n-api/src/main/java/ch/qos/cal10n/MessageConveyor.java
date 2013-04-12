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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ch.qos.cal10n.util.AnnotationExtractor;
import ch.qos.cal10n.util.Cal10nResourceBundle;
import ch.qos.cal10n.util.Cal10nResourceBundleFinder;

/**
 * The default implementation for {@link IMessageConveyor} based on resource
 * bundles.
 * 
 * <p>
 * See also {@link #getMessage(Enum, Object...)} for details.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class MessageConveyor implements IMessageConveyor {

	final Locale locale;

	final Map<String, Cal10nResourceBundle> cache = new ConcurrentHashMap<String, Cal10nResourceBundle>();

	/**
	 * The {@link Locale} associated with this instance.
	 * 
	 * @param locale the Locale which this conveyor targets
	 */
	public MessageConveyor(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Given an enum as key, find the resource bundle corresponding to this locale and return
	 * the message corresponding to the key passed as parameter (internationalized per this locale).
	 * 
	 * <p>
	 * The name of the resource bundle is defined via the {@link BaseName}
	 * annotation whereas the locale is specified in this MessageConveyor
	 * instance's constructor.
	 * 
	 * @param key
	 *            an enum instance used as message key
	 * 
	 */
	public <E extends Enum<?>> String getMessage(E key, Object... args)
			throws MessageConveyorException {

    Class<? extends Enum> declaringClass = key.getDeclaringClass();

		String declaringClassName = declaringClass.getName();
		Cal10nResourceBundle rb = cache.get(declaringClassName);
		if (rb == null || rb.hasChanged()) {
			rb = lookupResourceBundleByEnumClassAndLocale(declaringClass);
			cache.put(declaringClassName, rb);
		}

		String keyAsStr = key.toString();
		String value = rb.getString(keyAsStr);
		if (value == null) {
			return "No key found for " + keyAsStr;
		} else {
			if (args == null || args.length == 0) {
				return value;
			} else {
				return MessageFormat.format(value, args);
			}
		}
	}

	private <E extends Enum<?>> Cal10nResourceBundle lookupResourceBundleByEnumClassAndLocale(Class<E> declaringClass)
			throws MessageConveyorException {

    AnnotationExtractor annotationExtractor = new AnnotationExtractor(declaringClass);
    // basename is declared via an annotation on the declaringClass
		String baseName = annotationExtractor.getBaseName();
		if (baseName == null) {
			throw new MessageConveyorException(
					"Missing @BaseName annotation in enum type ["
							+ declaringClass.getName() + "]. See also "
							+ Cal10nConstants.MISSING_BN_ANNOTATION_URL);
		}

		String charset = annotationExtractor.extractCharset(locale);
		// use the declaring class' loader instead of
		// this.getClass().getClassLoader()
		// see also http://jira.qos.ch/browse/CAL-8
		Cal10nResourceBundle rb = Cal10nResourceBundleFinder.getBundle(
            declaringClass.getClassLoader(), baseName, locale, charset);

		if (rb == null) {
			throw new MessageConveyorException(
					"Failed to locate resource bundle [" + baseName
							+ "] for locale [" + locale + "] for enum type ["
							+ declaringClass.getName() + "]");
		}
		return rb;
	}

	public String getMessage(MessageParameterObj mpo)
			throws MessageConveyorException {
		if (mpo == null) {
			throw new IllegalArgumentException(
					"MessageParameterObj argumument cannot be null");
		}
		return getMessage(mpo.getKey(), mpo.getArgs());
	}
}
