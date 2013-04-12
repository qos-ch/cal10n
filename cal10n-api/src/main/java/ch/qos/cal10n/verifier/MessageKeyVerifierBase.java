package ch.qos.cal10n.verifier;

import ch.qos.cal10n.Cal10nConstants;
import ch.qos.cal10n.util.AnnotationExtractor;
import ch.qos.cal10n.util.Cal10nResourceBundleFinder;
import ch.qos.cal10n.util.IAnnotationExtractor;
import ch.qos.cal10n.util.MiscUtil;

import java.text.MessageFormat;
import java.util.*;

/**
 * Abstract class for verifying that for a given an enum type, the keys match those
 * found in the corresponding resource bundles.
 *
 * <p>This class contains the bundle verification logic. Logic for extracting locate and key information
 * should be provided by derived classes.</p>
 *
 * @author: Ceki Gulcu
 * @since 0.8
 */
abstract public class MessageKeyVerifierBase implements IMessageKeyVerifier {

  final String enumTypeAsStr;
  final IAnnotationExtractor annotationExtractor;

  protected MessageKeyVerifierBase(String enumTypeAsStr, IAnnotationExtractor annotationExtractor) {
    this.enumTypeAsStr = enumTypeAsStr;
    this.annotationExtractor = annotationExtractor;
  }

  public String getEnumTypeAsStr() {
    return enumTypeAsStr;
  }

  protected String extractCharsetForLocale(Locale locale) {
    return annotationExtractor.extractCharset(locale);
  }

  abstract protected List<String> extractKeysInEnum();


  public String[] getLocaleNames() {
    String[] localeNameArray = annotationExtractor.extractLocaleNames();
    return localeNameArray;
  }

  public String getBaseName() {
    String rbName = annotationExtractor.getBaseName();
    return rbName;
  }

  public List<Cal10nError> verify(Locale locale) {
    List<Cal10nError> errorList = new ArrayList<Cal10nError>();

    String baseName = getBaseName();

    if (baseName == null) {
      errorList.add(new Cal10nError(Cal10nError.ErrorType.MISSING_BN_ANNOTATION, "",
              enumTypeAsStr, locale, ""));
      // no point in continuing
      return errorList;
    }

    String charset = extractCharsetForLocale(locale);

    ResourceBundle rb = Cal10nResourceBundleFinder.getBundle(this.getClass()
            .getClassLoader(), baseName, locale, charset);

    ErrorFactory errorFactory = new ErrorFactory(enumTypeAsStr, locale, baseName);

    if (rb == null) {
      errorList.add(errorFactory.buildError(Cal10nError.ErrorType.FAILED_TO_FIND_RB, ""));
      // no point in continuing
      return errorList;
    }

    Set<String> rbKeySet = buildKeySetFromEnumeration(rb.getKeys());

    if (rbKeySet.size() == 0) {
      errorList.add(errorFactory.buildError(Cal10nError.ErrorType.EMPTY_RB, ""));
    }

    if (errorList.size() != 0) {
      return errorList;
    }

    List<String> enumKeys = extractKeysInEnum();
    if (enumKeys.size() == 0) {
      errorList.add(errorFactory.buildError(Cal10nError.ErrorType.EMPTY_ENUM, ""));
    }

    for (String enumKey : enumKeys) {
      if (rbKeySet.contains(enumKey)) {
        rbKeySet.remove(enumKey);
      } else {
        errorList.add(errorFactory.buildError(Cal10nError.ErrorType.ABSENT_IN_RB, enumKey));
      }
    }

    for (String rbKey : rbKeySet) {
      errorList.add(errorFactory.buildError(Cal10nError.ErrorType.ABSENT_IN_ENUM, rbKey));
    }
    return errorList;
  }

  public List<String> typeIsolatedVerify(Locale locale) {
    List<Cal10nError> errorList = verify(locale);
    List<String> strList = new ArrayList<String>();
    for (Cal10nError error : errorList) {
      strList.add(error.toString());
    }
    return strList;
  }

  protected Set<String> buildKeySetFromEnumeration(Enumeration<String> e) {
    Set<String> set = new HashSet<String>();
    while (e.hasMoreElements()) {
      String s = e.nextElement();
      set.add(s);
    }
    return set;
  }
  /**
   * Verify all declared locales in one step.
   */
  public List<Cal10nError> verifyAllLocales() {
    List<Cal10nError> errorList = new ArrayList<Cal10nError>();

    String[] localeNameArray = getLocaleNames();

    if (localeNameArray == null || localeNameArray.length == 0) {
      String errMsg = MessageFormat.format(Cal10nConstants.MISSING_LD_ANNOTATION_MESSAGE, enumTypeAsStr);
      throw new IllegalStateException(errMsg);
    }
    for (String localeName : localeNameArray) {
      Locale locale = MiscUtil.toLocale(localeName);
      List<Cal10nError> tmpList = verify(locale);
      errorList.addAll(tmpList);
    }

    return errorList;
  }



}
