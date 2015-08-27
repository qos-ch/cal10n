package ch.qos.cal10n.verifier.processor;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.verifier.Cal10nError;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("ch.qos.cal10n.BaseName")
public class CAL10NAnnotationProcessor extends AbstractProcessor {

  TypeElement baseNameTypeElement;
  Filer filer;

  @Override
  public SourceVersion getSupportedSourceVersion() {

    //Replacement of @SupportedSourceVersion(SourceVersion.RELEASE_5) because it generate compilation warning like:
    //[WARNING] Supported source version 'RELEASE_5' from annotation processor 'ch.qos.cal10n.verifier.processor.CAL10NAnnotationProcessor' less than -source '1.7'
    try {
      return SourceVersion.valueOf("RELEASE_8");
    } catch (IllegalArgumentException e) {}

    try {
      return SourceVersion.valueOf("RELEASE_7");
    } catch (IllegalArgumentException e) {}

    try {
      return SourceVersion.valueOf("RELEASE_6");
    } catch (IllegalArgumentException x) {}

    return SourceVersion.RELEASE_5;
  }

  @Override
  public void init(ProcessingEnvironment env) {
    super.init(env);
    note("CAL10NAnnotationProcessor 0.8.1 initialized");
    baseNameTypeElement = getType("ch.qos.cal10n.BaseName");
    filer = env.getFiler();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<? extends Element> entityAnnotated =
            roundEnv.getElementsAnnotatedWith(baseNameTypeElement);
    for (TypeElement typeElement : ElementFilter.typesIn(entityAnnotated)) {
      verify(typeElement);
    }
    return false;
  }

  private void verify(TypeElement typeElementForEnum) {
    MessageKeyVerifierByTypeElement modelMessageKeyVerifier = new MessageKeyVerifierByTypeElement(typeElementForEnum, filer);

    BaseName baseNameAnnotation = typeElementForEnum.getAnnotation(BaseName.class);
    //note("performing verification for basename [" + baseNameAnnotation.value() +"]");
    List<Cal10nError> errorList = modelMessageKeyVerifier.verifyAllLocales();
    for(Cal10nError error: errorList) {
      error(error.toString(), typeElementForEnum);
    }


  }

  private TypeElement getType(String className) {
    return processingEnv.getElementUtils().getTypeElement(className);
  }

  void note(String s) {
    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, s);
  }

  void warn(String s) {
    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, s);
  }

  void error(String s, Element element) {
    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, s, element);
  }

}
