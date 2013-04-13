package ch.qos.cal10n.verifier;

import ch.qos.cal10n.util.AnnotationExtractor;
import ch.qos.cal10n.util.CAL10NBundleFinder;
import ch.qos.cal10n.util.MiscUtil;

import static ch.qos.cal10n.verifier.Cal10nError.ErrorType.MISSING_LOCALE_DATA_ANNOTATION;
import static ch.qos.cal10n.verifier.Cal10nError.ErrorType.MISSING_BN_ANNOTATION;


import java.util.*;

/**
 * Abstract class for verifying that for a given an enum type, the keys match those
 * found in the corresponding resource bundles.
 * <p/>
 * <p>This class contains the bundle verification logic. Logic for extracting locate and key information
 * should be provided by derived classes.</p>
 *
 * @author: Ceki Gulcu
 * @since 0.8
 */
abstract public class AbstractMessageKeyVerifier implements IMessageKeyVerifier {

  final String enumTypeAsStr;
  final AnnotationExtractor annotationExtractor;

  protected AbstractMessageKeyVerifier(String enumTypeAsStr, AnnotationExtractor annotationExtractor) {
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
      errorList.add(new Cal10nError(MISSING_BN_ANNOTATION, "",
              enumTypeAsStr, locale, ""));
      return errorList;
    }

    String charset = extractCharsetForLocale(locale);

    CAL10NBundleFinder cal10NResourceCAL10NBundleFinder = getResourceBundleFinder();

    ResourceBundle rb = cal10NResourceCAL10NBundleFinder.getBundle(baseName, locale, charset);

    ErrorFactory errorFactory = new ErrorFactory(enumTypeAsStr, locale, baseName);

    if (rb == null) {
      errorList.add(errorFactory.buildError(Cal10nError.ErrorType.FAILED_TO_FIND_RB, ""));
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

  protected abstract CAL10NBundleFinder getResourceBundleFinder();

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

    ErrorFactory errorFactory = new ErrorFactory(enumTypeAsStr, null, getBaseName());


    if (localeNameArray == null || localeNameArray.length == 0) {
      errorList.add(errorFactory.buildError(MISSING_LOCALE_DATA_ANNOTATION, "*"));
      return errorList;
    }
    for (String localeName : localeNameArray) {
      Locale locale = MiscUtil.toLocale(localeName);
      List<Cal10nError> tmpList = verify(locale);
      errorList.addAll(tmpList);
    }

    return errorList;
  }


}
