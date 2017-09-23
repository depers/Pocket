package cn.bravedawn.latte.compiler;

import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import cn.bravedawn.latte.annotations.AppRegisterGenerator;
import cn.bravedawn.latte.annotations.EntryGenerator;
import cn.bravedawn.latte.annotations.PayEntryGenerator;

/**
 * Created by 冯晓 on 2017/9/23.
 */

@SuppressWarnings("unused")
@AutoService(Processor.class)
public class LatteProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        final Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(EntryGenerator.class);
        annotations.add(PayEntryGenerator.class);
        annotations.add(AppRegisterGenerator.class);
        return annotations;
    }

    private void scan(RoundEnvironment env, Class<? extends Annotation> annotation
            , AnnotationValueVisitor annotationValueVisitor) {
        for (Element element : env.getElementsAnnotatedWith(annotation)) {
            final List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();

            for (AnnotationMirror annotationMirror : annotationMirrors) {
                final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
                        annotationMirror.getElementValues();

                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                        : elementValues.entrySet()) {
                    entry.getValue().accept(annotationValueVisitor, null);
                }
            }
        }

    }


    private void generateEntryCode(RoundEnvironment env) {
        final EntryVisitor visitor = new EntryVisitor();
        visitor.setFiler(processingEnv.getFiler());
        scan(env, EntryGenerator.class, visitor);
    }

    private void generatePayEntryCode(RoundEnvironment env) {
        final PayEntryVisitor visitor = new PayEntryVisitor();
        visitor.setFiler(processingEnv.getFiler());
        scan(env, PayEntryGenerator.class, visitor);
    }

    private void generateAppRegisterCode(RoundEnvironment env) {
        final AppRegisterVisitor visitor = new AppRegisterVisitor();
        visitor.setFiler(processingEnv.getFiler());
        scan(env, AppRegisterGenerator.class, visitor);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        generateEntryCode(roundEnv);
        generatePayEntryCode(roundEnv);
        generateAppRegisterCode(roundEnv);
        return true;
    }

}
