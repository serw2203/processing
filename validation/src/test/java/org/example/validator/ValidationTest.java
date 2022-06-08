package org.example.validator;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.example.infrastructure.generator.classfields.GenerateClassField;
import org.junit.Test;

import static org.example.infrastructure.generator.classfields.InstanceClassField.createInstanceField;

public class ValidationTest {

    @Test
    public void testAllPassedValidate() {
        //given
        TestInnerClass testInnerClass = new TestInnerClass("fieldInnerClass");
        TestClass testClass = new TestClass("fieldValue", testInnerClass);
        TestClassValidator validator = new TestClassValidator();
        ValidationResult vr = new ValidationResult(testClass);
        //when
        validator.validate(testClass, vr);
        //then
        Assertions.assertThat(vr.isPassed()).isTrue();
    }

    @Test
    public void testValidateWithErrorInTopClassField() {
        //given
        TestClass testClass = new TestClass(null, null);
        TestClassValidator validator = new TestClassValidator();
        ValidationResult vr = new ValidationResult(testClass);
        //when
        validator.validate(testClass, vr);
        //then
        Assertions.assertThat(vr.isPassed()).isFalse();
        Assertions.assertThat(vr.getAllErrors())
            .contains(new ValidationMessage(BaseValidationCode.NULL_CHECK,
                Lists.newArrayList(createInstanceField(ValidationTest$TestClassFields.FIELD1)),
                createInstanceField(ValidationTest$TestClassFields.FIELD1)))
            .contains(new ValidationMessage(BaseValidationCode.NULL_CHECK,
                Lists.newArrayList(createInstanceField(ValidationTest$TestClassFields.FIELD2)),
                createInstanceField(ValidationTest$TestClassFields.FIELD2)));
    }

    @Test
    public void testValidateWithErrorInInnerClassField() {
        //given
        TestInnerClass testInnerClass = new TestInnerClass(null);
        TestClass testClass = new TestClass("fieldVal", testInnerClass);
        TestClassValidator validator = new TestClassValidator();
        ValidationResult vr = new ValidationResult(testClass);
        //when
        validator.validate(testClass, vr);
        //then
        Assertions.assertThat(vr.isPassed()).isFalse();
        Assertions.assertThat(vr.getAllErrors())
            .contains(new ValidationMessage(BaseValidationCode.NULL_CHECK,
                Lists.newArrayList(createInstanceField(ValidationTest$TestClassFields.FIELD2).createNestedField(ValidationTest$TestInnerClassFields.FIELD1)),
                createInstanceField(ValidationTest$TestClassFields.FIELD2).createNestedField(ValidationTest$TestInnerClassFields.FIELD1)));
    }

    @GenerateClassField
    @Getter @AllArgsConstructor
    public static class TestClass{
        String field1;
        TestInnerClass field2;
    }

    @GenerateClassField
    @Getter @AllArgsConstructor
    public static class TestInnerClass{
        String field1;
    }

    public static class TestClassValidator extends Validator<TestClass> {
        @Override
        protected ValidationResult doValidate(TestClass o, ValidationResult vr) {
            ValidationUtils.rejectIfNull(ValidationTest$TestClassFields.FIELD1, vr);
            ValidationUtils.rejectIfNull(ValidationTest$TestClassFields.FIELD2, vr);
            ValidationUtils.rejectIfNull(ValidationTest$TestInnerClassFields.FIELD1, ValidationTest$TestClassFields.FIELD2, vr);
            return vr;
        }
    }
}
