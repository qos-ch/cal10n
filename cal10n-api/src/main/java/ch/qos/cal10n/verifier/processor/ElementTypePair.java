package ch.qos.cal10n.verifier.processor;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;


public class ElementTypePair {
  public ElementTypePair(TypeElement element, DeclaredType type) {
    this.element = element;
    this.type = type;
  }

  final TypeElement element;
  final DeclaredType type;
}
