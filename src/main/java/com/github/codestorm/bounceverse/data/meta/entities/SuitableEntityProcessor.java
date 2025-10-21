package com.github.codestorm.bounceverse.data.meta.entities;

import com.google.auto.service.AutoService;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;

/**
 *
 *
 * <h1>{@link SuitableEntityProcessor}</h1>
 *
 * {@link Processor} kiểm tra cho annotation {@link SuitableEntity}.
 */
@AutoService(Processor.class)
public final class SuitableEntityProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(SuitableEntity.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SuitableEntity.class)) {
            if (element.getKind() == ElementKind.PARAMETER) {
                // Phía yêu cầu (parameter của @SuitableEntity)
                final var requireParameter = (VariableElement) element;
                final var requireAnnotation = requireParameter.getAnnotation(SuitableEntity.class);
                final var requiredTypes = EnumSet.copyOf(Arrays.asList(requireAnnotation.value()));

                // Phía thực tế (parameter truyền vào trong hàm)
                final var actualClassElement =
                        processingEnv.getTypeUtils().asElement(requireParameter.asType());
                if (actualClassElement == null) {
                    continue;
                }
                final var actualAnnotation = actualClassElement.getAnnotation(ForEntity.class);

                // Kiểm tra (bao hàm - loại trừ)
                if (actualAnnotation != null) {
                    final var actualTypes = EnumSet.copyOf(Arrays.asList(actualAnnotation.value()));
                    if (actualTypes.isEmpty()) {
                        continue; // bao phủ cả (quy ước là mảng rỗng)
                    }
                    requiredTypes.removeAll(actualTypes);
                    if (requiredTypes.isEmpty()) {
                        continue; // đáp ứng hết
                    }
                }

                for (var requiredType : requiredTypes) {
                    final var message =
                            String.format(
                                    "Parameter '%s' must suitable for '%s', but '%s' does not.",
                                    requireParameter.getSimpleName(),
                                    requiredType.name(),
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
