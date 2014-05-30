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
package ch.qos.cal10n.plugins;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import ch.qos.cal10n.CAL10NConstants;
import ch.qos.cal10n.util.MiscUtil;
import ch.qos.cal10n.verifier.IMessageKeyVerifier;


// Adding @requiresDependencyResolution  fixes  http://jira.qos.ch/browse/CAL-18

/**
 * Verifies resources bundles in various locales against an enumType
 *
 * @goal verify
 * @phase verify
 * @requiresProject true
 * @requiresDependencyResolution
 */
public class VerifyMojo extends AbstractMojo {

  /**
   * @parameter
   * @required
   */
  private String[] enumTypes;

  /**
   * The directory for compiled classes.
   * 
   * @parameter expression="${project.build.outputDirectory}"
   * @required
   * @readonly
   */
  private File outputDirectory;

  // direct dependencies of this project
  /**
   * 
   * @parameter expression="${project.artifacts}"
   * @required
   * @readonly
   */
  private Set<Artifact> projectArtifacts;

  /**
   * @parameter expression="${localRepository}"
   * @required
   * @readonly 
   * @since 1.0
   */
  private ArtifactRepository localRepository;

  public void execute() throws MojoExecutionException, MojoFailureException {

    if (enumTypes == null) {
      throw new MojoFailureException(CAL10NConstants.MISSING_ENUM_TYPES_MSG);
    }
    for (String enumTypeAsStr : enumTypes) {
      IMessageKeyVerifier imcv = getMessageKeyVerifierInstance(enumTypeAsStr);
      getLog()
          .info(
              "Checking all resource bundles for enum type [" + enumTypeAsStr
                  + "]");
      checkAllLocales(imcv);
    }
  }

  public void checkAllLocales(IMessageKeyVerifier mcv)
      throws MojoFailureException, MojoExecutionException {

    String enumClassAsStr = mcv.getEnumTypeAsStr();

    String[] localeNameArray = mcv.getLocaleNames();

    if (localeNameArray == null || localeNameArray.length == 0) {
      String errMsg = MessageFormat.format(
          CAL10NConstants.MISSING_LOCALE_DATA_ANNOTATION_MESSAGE, enumClassAsStr);
      getLog().error(errMsg);
      throw new MojoFailureException(errMsg);
    }

    boolean failure = false;
    for (String localeName : localeNameArray) {
      Locale locale = MiscUtil.toLocale(localeName);
      List<String> errorList = mcv.typeIsolatedVerify(locale);
      if (errorList.size() == 0) {
        String resourceBundleName = mcv.getBaseName();
        getLog().info(
            "SUCCESSFUL verification for resource bundle [" + resourceBundleName
                + "] for locale [" + locale + "]");
      } else {
        failure = true;
        getLog().error(
            "FAILURE during verification of resource bundle for locale ["
                + locale + "] enum class [" + enumClassAsStr + "]");
        for (String s : errorList) {
          getLog().error(s);
        }
      }
    }
    if (failure) {
      throw new MojoFailureException("FAIL Verification of [" + enumClassAsStr
          + "] keys.");
    }
  }

  IMessageKeyVerifier getMessageKeyVerifierInstance(String enumClassAsStr)
      throws MojoExecutionException {
    String errMsg = "Failed to instantiate MessageKeyVerifier class";
    try {
      ThisFirstClassLoader thisFirstClassLoader = (ThisFirstClassLoader) buildClassLoader();
      Class<?> mkvClass = Class.forName(
          CAL10NConstants.MessageKeyVerifier_FQCN, true, thisFirstClassLoader);
      Constructor<?> mkvCons = mkvClass.getConstructor(String.class);
      IMessageKeyVerifier imcv = (IMessageKeyVerifier) mkvCons
          .newInstance(enumClassAsStr);
      return imcv;
    } catch (ClassNotFoundException e) {
      throw new MojoExecutionException(errMsg, e);
    } catch (NoClassDefFoundError e) {
      throw new MojoExecutionException(errMsg, e);
    } catch (Exception e) {
      throw new MojoExecutionException(errMsg, e);
    }
  }

  ClassLoader buildClassLoader() {
    ArrayList<URL> classpathURLList = new ArrayList<URL>();
    classpathURLList.add(toURL(outputDirectory));
    classpathURLList.addAll(getDirectDependencies());
    ClassLoader parentCL = this.getClass().getClassLoader();
    URL[] classpathURLArray = classpathURLList.toArray(new URL[] {});
    return new ThisFirstClassLoader(classpathURLArray, parentCL);
  }

  List<URL> getDirectDependencies() {
    ArrayList<URL> urlList = new ArrayList<URL>();
    for (Artifact a : projectArtifacts) {
      String pathOfArtifact = localRepository.getBasedir() + "/"
          + localRepository.pathOf(a);
      File artifactAsFile = new File(pathOfArtifact);
      if (!artifactAsFile.exists()) {
        getLog()
            .error("Artifact [" + artifactAsFile + "] could not be located");
      }
      try {
        URL url = artifactAsFile.toURI().toURL();
        urlList.add(url);
      } catch (MalformedURLException e) {
        getLog().info("Failed to build URL", e);
      }
    }
    return urlList;
  }

  URL toURL(File file) {
    try {
      return file.toURI().toURL();
    } catch (MalformedURLException e) {
      // this should never happen
      getLog().error("Failed to convert file [" + file + "] to a URL", e);
      return null;
    }
  }

}