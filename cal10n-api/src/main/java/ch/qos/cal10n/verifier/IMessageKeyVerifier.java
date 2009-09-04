package ch.qos.cal10n.verifier;

import java.util.List;
import java.util.Locale;

/**
 * An interface for verifying that given an enum type, the keys match those
 * found in the corresponding resource bundles.
 * 
 * <p>
 * See also {@link MessageKeyVerifier} for a concrete implementation.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
public interface IMessageKeyVerifier {

  // WARNING:
  // WARNING: The name of this class is referenced in String form
  // to do class loader tricks. Do not change the name of this class
  // without looking at the maven-cal10n-plugin.

  /**
   * Get the of enum type that this verifier is related to to.
   * 
   * @return
   */
  public Class<? extends Enum<?>> getEnumType();

  /**
   * Get the name of enum type to this verifier is related to to.
   * 
   * @return
   */
  public String getEnumTypeAsStr();

  /**
   * Verify that the keys defined in the enumClass match those found in the
   * resource bundle corresponding to a certain locale
   * 
   * @param locale
   * @return
   */
  public List<Cal10nError> verify(Locale locale);

  /**
   * Verify that the keys defined in the enumClass match those found in the
   * corresponding resource bundle for all locales declared in the enum type
   * via the &#64;{@link LocaleData} annotation.
   * 
   * @param locale
   * @return
   */
  public List<Cal10nError> verifyAllLocales();

  /**
   * Same as {@link #verify(Locale)} except that the return type is
   * List<String>.
   * 
   * @param locale
   * @return
   */
  public List<String> typeIsolatedVerify(Locale locale);

  /**
   * Get the locales specified in the enumType (via annotations)
   * 
   * @return
   */
  public String[] getLocaleNames();

  /**
   * Get the base name for the resource bundle family as specified in the enumType (via
   * annotations)
   * 
   * @return
   */
  public String getBaseName();
}