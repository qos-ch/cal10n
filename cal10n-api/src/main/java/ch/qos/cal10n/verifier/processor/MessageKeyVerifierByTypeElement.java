package ch.qos.cal10n.verifier.processor;

import ch.qos.cal10n.util.CAL10NBundleFinder;
import ch.qos.cal10n.verifier.AbstractMessageKeyVerifier;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Given an TypeElement representing an enum class implements IMessageKeyVerifier.
 *
 * @author Ceki Gulcu
 * @since 0.8
 */
public class MessageKeyVerifierByTypeElement extends AbstractMessageKeyVerifier {

  final TypeElement typeElementForEnum;
  final CAL10NBundleFinderByProcessingFiler compileTimeResourceBundleFinder;

  public MessageKeyVerifierByTypeElement(TypeElement typeElement, Filer filer) {
    super(typeElement.getQualifiedName().toString(), new AnnotationExtractorViaTypeElement(typeElement));
    typeElementForEnum = typeElement;
    this.compileTimeResourceBundleFinder = new CAL10NBundleFinderByProcessingFiler(filer);
  }

  @Override
  public List<String> extractKeysInEnum() {
    List<String> keyList = new ArrayList<String>();
    for (VariableElement ve : ElementFilter.fieldsIn(typeElementForEnum.getEnclosedElements())) {
      if ( ve.getKind() == ElementKind.ENUM_CONSTANT) {
        keyList.add(ve.getSimpleName().toString());
      }
    }
    return keyList;
  }

  @Override
  protected CAL10NBundleFinder getResourceBundleFinder() {
    return compileTimeResourceBundleFinder;
  }
}
