package ch.qos.cal10n.verifier.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("ch.qos.cal10n.BaseName")
@SupportedSourceVersion(SourceVersion.RELEASE_5)
public class MessageKeyAnnotationProcessor extends AbstractProcessor {

  ElementTypePair baseNameType;
  ElementTypePair localeDataType;

  @Override
  public void init(ProcessingEnvironment env) {
    super.init(env);
    print("in init");

    baseNameType = getType("ch.qos.cal10n.BaseName");
    localeDataType = getType("ch.qos.cal10n.LocaleData");
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    print("in process");
    for (TypeElement typeElement : annotations) {
      print("seeing typeElement.getQualifiedName():" + typeElement.getQualifiedName());
    }

    Set<? extends Element> entityAnnotated =
            roundEnv.getElementsAnnotatedWith(baseNameType.element);
    for (TypeElement typeElement : ElementFilter.typesIn(entityAnnotated)) {
      print("["+typeElement.getQualifiedName()+"]" + " is annotated with @BaseName");
      verify(typeElement);
    }
    return false;
  }

  private void verify(TypeElement typeElement) {
     for(VariableElement ve: ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {

       print("enclosed: "+ve.getSimpleName());

     }
  }

  private ElementTypePair getType(String className) {
    TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(className);
    DeclaredType declaredType = typeUtils().getDeclaredType(typeElement);
    return new ElementTypePair(typeElement, declaredType);
  }

  private Types typeUtils() {
    return processingEnv.getTypeUtils();
  }

  void print(String s) {
    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, s);
  }
}
