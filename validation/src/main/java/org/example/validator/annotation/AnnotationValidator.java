package org.example.validator.annotation;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.example.infrastructure.generator.classfields.EnumClassField;
import org.example.infrastructure.utils.GenericResolver;
import org.example.validator.ValidationResult;
import org.example.validator.ValidationUtils;
import org.example.validator.flow.Status;
import org.example.validator.flow.Statusable;
import org.reflections.Reflections;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.reflections.ReflectionUtils.getAllSuperTypes;
import static org.reflections.ReflectionUtils.getFields;


public class AnnotationValidator {
    static Map<Class, Class<? extends EnumClassField>> mapEnums = Maps.newHashMap();

    static {
        Collection<Class<? extends EnumClassField>> enumFields = new Reflections("org.example").getSubTypesOf (EnumClassField.class);
        for (Class<? extends EnumClassField> current : enumFields) {
            Class genType = GenericResolver.getFirstGenericTypeFromInterface(current);
            if (genType != null) {
                mapEnums.put(genType, current);
            }
        }
    }

    public void validateStringMaxLength(ValidationResult result) {
        Class objectType = result.getContext().getValidateObject().getClass();
        List<Field> annotatedFields = findFieldWithAnnotation(objectType, Size.class)
            .filter(f -> f.getType().equals(String.class))
            .collect(Collectors.toList());

        for (Field field : annotatedFields) {
            Integer max = field.getAnnotation(Size.class).max();
            ValidationUtils.rejectIfRegexpFailed(getEnumClassField(field), "^[\\w|\\W]{0," + max + "}$", result);
        }
    }

    public void validateNotNull(ValidationResult result) {
        Class objectType = result.getContext().getValidateObject().getClass();
        List<Field> annotatedFields = findFieldWithAnnotation(objectType, NotNull.class).collect(Collectors.toList());

        for (Field field : annotatedFields) {
            ValidationUtils.rejectIfNull(getEnumClassField(field), result);
        }
    }

    public void validateStatusableNotNull(ValidationResult<? extends Statusable> result) {
        Class objectType = result.getContext().getValidateObject().getClass();
        List<Field> annotatedFields = findFieldWithAnnotation(objectType, StatusableNotNull.class).collect(Collectors.toList());
        Status objectStatus = result.getContext().getValidateObject().getStatus();
        if (objectStatus == null) {
            return;
        }
        for (Field field : annotatedFields) {
            String[] validateStatuses = field.getAnnotation(StatusableNotNull.class).statuses();
            Boolean isNotNullStatus = Stream.of(validateStatuses)
                .anyMatch(validateStatus -> validateStatus.equals(objectStatus.name()));

            if (isNotNullStatus) {
                ValidationUtils.rejectIfNull(getEnumClassField(field), result);
            }
        }
    }

    private Stream<Field> findFieldWithAnnotation(Class objectType, Class annotation) {
        return getAllFields(objectType).stream()
            .filter(f -> f.isAnnotationPresent(annotation));
    }

    private EnumClassField getEnumClassField(Field field) {
        Class<? extends EnumClassField> enumClassFields = getEnumClassFieldsByEntity(field.getDeclaringClass());
        return EnumClassField.valueOfFieldName(field.getName(), enumClassFields);
    }

    private Class<? extends EnumClassField> getEnumClassFieldsByEntity(Class<?> declaringClass) {
        return mapEnums.get(declaringClass);
    }

    private   Set<Field> getAllFields(final Class<?> type, Predicate<? super Field>... predicates) {
        Set<Field> result = Sets.newHashSet();
        for (Class<?> t : getAllSuperTypes(type)) {
            result.addAll(getFields(t, predicates));
        }
        return result;
    }
}
