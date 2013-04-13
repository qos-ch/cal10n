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
@SupportedSourceVersion(SourceVersion.RELEASE_5)
public class CAL10NAnnotationProcessor extends AbstractProcessor {

  TypeElement baseNameTypeElement;
  Filer filer;

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
