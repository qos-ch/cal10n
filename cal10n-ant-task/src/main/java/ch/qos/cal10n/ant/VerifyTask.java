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
package ch.qos.cal10n.ant;

import ch.qos.cal10n.Cal10nConstants;
import ch.qos.cal10n.verifier.IMessageKeyVerifier;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.LogLevel;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.ClasspathUtils;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class VerifyTask extends Task {
    private List<StringElement> enumTypes;
    private Path classpath;

    @Override
    public void init() throws BuildException {
        this.enumTypes = new LinkedList<StringElement>();
    }

    @Override
    public void execute() throws BuildException {
      if (this.enumTypes.isEmpty()) {
        throw new BuildException(Cal10nConstants.MISSING_ENUM_TYPES_MSG);
      }
      for (StringElement enumType : this.enumTypes) {
        IMessageKeyVerifier imcv = getMessageKeyVerifierInstance(enumType.getText());
        log("Checking all resource bundles for enum type [" + enumType + "]", LogLevel.INFO.getLevel());
        checkAllLocales(imcv);
      }
    }

    public void checkAllLocales(IMessageKeyVerifier mcv) {
        String enumClassAsStr = mcv.getEnumTypeAsStr();
        String[] localeNameArray = mcv.getLocaleNames();

        if (localeNameArray == null || localeNameArray.length == 0) {
            String errMsg = MessageFormat.format(Cal10nConstants.MISSING_LD_ANNOTATION_MESSAGE, enumClassAsStr);
            log(errMsg, LogLevel.ERR.getLevel());
            throw new BuildException(errMsg);
        }

        boolean failure = false;
        for (String localeName : localeNameArray) {
            Locale locale = new Locale(localeName);
            List<String> errorList = mcv.typeIsolatedVerify(locale);
            if (errorList.size() == 0) {
                String resourceBundleName = mcv.getBaseName();
                log("SUCCESSFUL verification for resource bundle [" + resourceBundleName + "] for locale [" + locale + "]", LogLevel.INFO.getLevel());
            } else {
                failure = true;
                log("FAILURE during verification of resource bundle for locale ["
                        + locale + "] enum class [" + enumClassAsStr + "]", LogLevel.ERR.getLevel());
                for (String error : errorList) {
                    log(error, LogLevel.ERR.getLevel());
                }
            }
        }
        if (failure) {
            throw new BuildException("FAIL Verification of [" + enumClassAsStr + "] keys.");
        }
    }

    IMessageKeyVerifier getMessageKeyVerifierInstance(String enumClassAsStr) {
        String errMsg = "Failed to instantiate MessageKeyVerifier class";
        try {
            ClassLoader classLoader = ClasspathUtils.getClassLoaderForPath(this.getProject(), this.classpath, "cal10n.VerifyTask");
            Class<?> mkvClass = Class.forName(
                    Cal10nConstants.MessageKeyVerifier_FQCN, true, classLoader);
            Constructor<?> mkvCons = mkvClass.getConstructor(String.class);
            return (IMessageKeyVerifier) mkvCons.newInstance(enumClassAsStr);
        } catch (ClassNotFoundException e) {
            throw new BuildException(errMsg, e);
        } catch (NoClassDefFoundError e) {
            throw new BuildException(errMsg, e);
        } catch (Exception e) {
            throw new BuildException(errMsg, e);
        }
    }

    public void addClasspath(Path classpath) {
        this.classpath = classpath;
    }

    public void addConfiguredEnumTypes(EnumTypesElement enumTypes) {
        this.enumTypes.addAll(enumTypes.getEnumTypes());
    }

    public void setClasspath(Path classpath) {
        this.classpath = classpath;
    }

    public void setClasspathRef(Reference refId) {
        Path cp = new Path(this.getProject());
        cp.setRefid(refId);
        this.setClasspath(cp);
    }

    public void setEnumType(String enumType) {
        this.enumTypes.add(new StringElement(enumType));
    }
}
