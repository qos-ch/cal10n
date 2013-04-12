package ch.qos.cal10n;

/**
 * 
 * This class defines the shared constants in CAL10N.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
public class CAL10NConstants {

  final public static String MessageKeyVerifier_FQCN = "ch.qos.cal10n.verifier.MessageKeyVerifier";

  final public static String CODE_URL_PREFIX = "http://cal10n.qos.ch/codes.html";
  final public static String MISSING_BN_ANNOTATION_URL = CAL10NConstants.CODE_URL_PREFIX
      + "#missingBaseNameAnnotation";

  final public static String MISSING_LOCALE_DATA_ANNOTATION_URL = CAL10NConstants.CODE_URL_PREFIX
      + "#missingLDAnnotation";
  final public static String MISSING_LOCALE_DATA_ANNOTATION_MESSAGE = "Missing @LocaleData annotation in enum type [{0}]. Please see "
      + MISSING_LOCALE_DATA_ANNOTATION_URL;

  final public static String MISSING_ENUM_TYPES_URL = CAL10NConstants.CODE_URL_PREFIX
      + "#missingEnumType";

  final public static String MISSING_ENUM_TYPES_MSG = "Missing <enumTypes> element. Please see "
      + MISSING_ENUM_TYPES_URL;
}
