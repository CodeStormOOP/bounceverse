package com.github.codestorm.bounceverse.data.meta.entities;

import com.google.auto.service.AutoService;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.*;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 *
 *
 * <h1>{@link SuitableEntityProcessor}</h1>
 *
 * {@link Processor} kiểm tra cho annotation {@link SuitableEntity}.
 */
@AutoService(Processor.class)
public class SuitableEntityProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(SuitableEntity.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SuitableEntity.class)) {
            if (element.getKind() != ElementKind.PARAMETER
                    && element.getKind() != ElementKind.TYPE_PARAMETER) {
                continue;
            }

            // Phía yêu cầu (@SuitableEntity)
            final var requireParameter = (VariableElement) element;
            final var requireAnnotation = requireParameter.getAnnotation(SuitableEntity.class);
            final var requiredTypes = Set.of(requireAnnotation.value());

            // Phía thực tế (parameter)
            final var actualClassElement =
                    processingEnv.getTypeUtils().asElement(requireParameter.asType());
            if (actualClassElement == null) {
                continue;
            }
            final var actualAnnotation = actualClassElement.getAnnotation(ForEntity.class);

            // Kiểm tra
            if (actualAnnotation != null) {
                final var actualTypes = Set.of(actualAnnotation.value());
                final var missingTypes = new HashSet<>(requiredTypes);
                missingTypes.removeAll(actualTypes);

                for (var missingType : missingTypes) {
                    final var message =
                            String.format(
                                    "Parameter '%s' must suitable for '%s', but '%s' does not.",
                                    requireParameter.getSimpleName(),
                                    missingType.name(),
                                    actualClassElement.getSimpleName());
                    processingEnv
                            .getMessager()
                            .printMessage(Diagnostic.Kind.ERROR, message, requireParameter);
                }
            }
        }
        return true;
    }
}
