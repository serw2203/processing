package org.example.validator;

import com.google.common.base.Strings;
import org.example.infrastructure.generator.classfields.EnumClassField;
import org.example.infrastructure.generator.classfields.InstanceClassField;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.example.validator.BaseValidationCode.LEGAL_VALUE;


public class ValidationUtils {

    public static void rejectIfRegexpFailed(EnumClassField classField, String pattern, ValidationResult result) {
        if (result.getContext().getField() != null) {
            rejectIfRegexpFailed(result.getContext().getField().createNestedField(classField), pattern, result);
        } else {
            rejectIfRegexpFailed(InstanceClassField.createInstanceField(classField), pattern, result);
        }
    }

    public static void rejectIfNull(EnumClassField classField, ValidationResult result) {
        if (result.getContext().getField() != null) {
            rejectIfNull(result.createNestedField(classField), result);
        } else {
            rejectIfNull(InstanceClassField.createInstanceField(classField), result);
        }
    }

    public static <N, T> void rejectIfNull(EnumClassField<N> nestedClassField, EnumClassField<T> classField, ValidationResult<T> result) {
        ValidationContext<N> nestedContext = result.createNestedContext(classField);
        if (nestedContext.getValidateObject() == null) {
            return;
        }
        ValidationResult<N> nestedResult = result.getOrCreateEnclosedResult(nestedContext, createEnclosedResult(result, nestedContext));
        rejectIfNull(nestedContext.getField().createNestedField(nestedClassField), nestedResult);
        result.addResult(nestedResult);
    }



    public static void rejectIfNullConditions(EnumClassField notNullField, EnumClassField conditionField,
                                              Object conditionValue, ValidationResult result) {
        if (Objects.equals(conditionValue, result.getValue(conditionField)) && isNullOrEmpty(result.getValue(notNullField))) {
            result.addErrors(BaseValidationCode.NOT_NULL_CONDITION_CHECK.message()
                    .field(result.createNestedField(notNullField))
                    .param(result.createNestedField(notNullField))
                    .param(InstanceClassField.createInstanceField(conditionField))
                    .param(conditionValue)
            );
        }
    }

    public static void rejectIfNotLegalValue(EnumClassField field, ValidationResult result, Object... values) {
        Object value = result.getValue(result.createNestedField(field));
        if (value == null) {
            return;
        }

        if (Arrays.stream(values).noneMatch(v -> v.equals(value))) {
            result.addErrors(
                    LEGAL_VALUE.message()
                            .field(result.createNestedField(field))
                            .param(result.createNestedField(field))
                            .param(Arrays.stream(values).map(Object::toString).collect(Collectors.joining(",")))
            );
        }
    }


    public static <ENUM_TYPE extends Enum<ENUM_TYPE>> void validateEnum(Object enumValue,
                                                                        Class<ENUM_TYPE> enumClass,
                                                                        EnumClassField enumClassField,
                                                                        ValidationResult result) {
        validateEnum(enumValue, enumClass, enumClassField, result, null);
    }

    public static <ENUM_TYPE extends Enum<ENUM_TYPE>> void validateEnum(Object enumValue,
                                                                        Class<ENUM_TYPE> enumClass,
                                                                        EnumClassField enumClassField,
                                                                        ValidationResult result,
                                                                        Integer index) {
        if (enumValue != null && isValueNotBelongToEnumClass(enumValue, enumClass)) {
            InstanceClassField field = result.createNestedField(enumClassField, index);
            result.addErrors(LEGAL_VALUE.message()
                    .field(field)
                    .param(field)
                    .param(Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).collect(Collectors.joining(",")))
            );
        }
    }

    private static <N, C> ValidationResult createEnclosedResult(ValidationResult<C> result, ValidationContext<N> context) {
        ValidationResult<N> resultByValidationContext = new ValidationResult<>(context);
        result.addResult(resultByValidationContext);
        return resultByValidationContext;
    }

    private static void rejectIfNull(InstanceClassField field, ValidationResult result) {
        if (result.getContext().getValidateObject() == null) {
            return;
        }

        Object value = result.getValue(field);
        if (isNullOrEmpty(value)) {
            ValidationMessage.ValidationMessageBuilder builder = BaseValidationCode.NULL_CHECK
                    .message().field(field).param(field);
            result.addErrors(builder);
        }
    }

    private static void rejectIfRegexpFailed(InstanceClassField field, String pattern, ValidationResult result) {
        Object value = result.getValue(field);
        if (!isNullOrEmpty(value) && !value.toString().matches(pattern)) {
            result.addErrors(BaseValidationCode.REG_EXPR_MATCH.message().field(field).param(field).param(pattern));
        }
    }

    public static boolean isNullOrEmpty(Object value) {
        return value == null ||
            value instanceof String && Strings.nullToEmpty((String) value).trim().isEmpty() ||
            value instanceof Collection && ((Collection) value).isEmpty();
    }

    public static <ENUM_TYPE extends Enum<ENUM_TYPE>> boolean isValueBelongToEnumClass(Object enumValue, Class<ENUM_TYPE> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(currentValue -> currentValue. name().equals(enumValue)).count() > 0;

    }

    public static <ENUM_TYPE extends Enum<ENUM_TYPE>> boolean isValueNotBelongToEnumClass(Object enumValue, Class<ENUM_TYPE> enumClass) {
        return !isValueBelongToEnumClass(enumValue, enumClass);
    }
}
