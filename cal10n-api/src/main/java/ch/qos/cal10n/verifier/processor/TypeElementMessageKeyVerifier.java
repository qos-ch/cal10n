package ch.qos.cal10n.verifier.processor;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.verifier.MessageKeyVerifierBase;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Given an TypeElement representing an enum class implements IMessageKeyVerifier.
 *
 * @author Ceki Gulcu
 * @since 0.8
 */
public class TypeElementMessageKeyVerifier extends MessageKeyVerifierBase {

  final TypeElement typeElementForEnum;

  public TypeElementMessageKeyVerifier(TypeElement typeElement) {
    super(typeElement.getQualifiedName().toString(), new TypeElementAnnotationExtractor(typeElement));
    typeElementForEnum = typeElement;
  }

  @Override
  public List<String> extractKeysInEnum() {
    List<String> keyList = new ArrayList<String>();
    for (VariableElement ve : ElementFilter.fieldsIn(typeElementForEnum.getEnclosedElements())) {
      keyList.add(ve.getSimpleName().toString());
    }
    return keyList;
  }
}
