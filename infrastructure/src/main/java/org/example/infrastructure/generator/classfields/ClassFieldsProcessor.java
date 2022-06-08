package org.example.infrastructure.generator.classfields;

import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Sets;
import com.squareup.javapoet.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
@Slf4j
public class ClassFieldsProcessor extends AbstractProcessor {

    private static final Set<String> collections = Sets.newHashSet();

    static {
        collections.add("java.util.List");
        collections.add("java.util.ArrayList");
        collections.add("java.util.Map");
        collections.add("java.util.HashMap");
        collections.add("java.util.Queue");
        collections.add("java.util.Set");
    }

    private static final String FIELD_NAME_FIELD_NAME = "fieldName";
    private static final String VALUE_EXTRACTOR_FIELD_NAME = "valueExtractor";
    private ProcessingEnvironment processingEnvironment;

    @Override
    public final synchronized void init(final ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(a -> handleAnnotation(a, roundEnv));
        return true;
    }

    public void handleAnnotation(TypeElement annotation, RoundEnvironment roundEnv) {
        Set<? extends Element> types = roundEnv.getElementsAnnotatedWith(annotation);
        types.stream()
            .filter(e -> e.getKind().isClass())
            .map(e -> (TypeElement) e)
            .forEach(this::generateClassFieldsEnumFor);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Sets.newHashSet(XmlType.class.getName(), GenerateClassField.class.getName());
    }

    private void generateClassFieldsEnumFor(TypeElement type) {
        Set<Element> fields = extractNonStaticAndNonFinalFieldsHavingGetters(type);

        if (fields.isEmpty()) {
            return;
        }
        String packageName = processingEnv.getElementUtils().getPackageOf(type).getQualifiedName().toString();
        safelyCreateClassFieldEnumSourceFile(type, fields, packageName);
    }

    private void safelyCreateClassFieldEnumSourceFile(TypeElement type, Set<Element> fields, String packageName) {
        try {
            createClassFieldEnumSourceFile(type, fields, packageName);
        } catch (IOException e) {
            reportError(type, e);
        }
    }

    private void createClassFieldEnumSourceFile(TypeElement type, Set<Element> fields, String packageName)
        throws IOException {
        TypeSpec classFieldsEnum = generateClassFieldsEnum(type, fields);
        JavaFile javaFile = JavaFile.builder(packageName, classFieldsEnum).build();
        javaFile.writeTo(processingEnv.getFiler());
    }

    private void reportError(TypeElement type, IOException e) {
        try {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Не удалось создать файл: " + e.getMessage(), type);
        } catch (Exception e1) {
            log.error("Невозможно передать ошибку вызывающей программе при генерации файла для типа " + type, e);
        }
        log.error("Ошибка создания файла для типа " + type, e);
    }

    private Set<Element> extractNonStaticAndNonFinalFieldsHavingGetters(TypeElement type) {

        return type.getEnclosedElements().stream()
            .filter(this::isNonStaticAndNonFinalField)
            .filter(e -> findValueExtractorMethodName(e, type).isPresent())
            .collect(toSet());
    }

    private boolean isNonStaticAndNonFinalField(Element element) {
        return element.getKind().isField() && (!(isStaticField(element) || isFinalField(element)));
    }

    private boolean isStaticField(Element element) {
        return element.getModifiers().contains(Modifier.STATIC);
    }

    private boolean isFinalField(Element element) {
        return element.getModifiers().contains(Modifier.FINAL);
    }

    private TypeSpec generateClassFieldsEnum(TypeElement originalType, Set<Element> fields) throws IOException {
        String enumName = createEnumClassName(originalType);

        FieldSpec fieldNameField =
            FieldSpec.builder(String.class, FIELD_NAME_FIELD_NAME).addModifiers(Modifier.PRIVATE).build();
        TypeName extractorTypeName = ParameterizedTypeName.get(ClassName.get(Function.class),
            WildcardTypeName.supertypeOf(ClassName.get(originalType)),
            WildcardTypeName.subtypeOf(Object.class)
        );
        FieldSpec valueExtractorField =
            FieldSpec.builder(extractorTypeName, VALUE_EXTRACTOR_FIELD_NAME)
                .addModifiers(Modifier.PRIVATE).build();

        MethodSpec toStringMethod = getToStringMethodSpec();
        MethodSpec getFieldNameMethod = getGetFieldNameMethodSpec();
        MethodSpec getValueExtractorMethod = getGetValueExtractorMethodSpec();
        MethodSpec constructorMethod = getConstructorMethodSpec(originalType);

        TypeSpec.Builder fieldsEnumBuilder =
            TypeSpec.enumBuilder(enumName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(EnumClassField.class), ClassName.get(originalType)))

                .addField(fieldNameField)
                .addField(valueExtractorField)

                .addMethod(constructorMethod)
                .addMethod(getFieldNameMethod)
                .addMethod(getValueExtractorMethod)
                .addMethod(toStringMethod)
                .addMethod(getObjectTypeSpec(originalType));

        fields.stream()
            .forEach(field -> {
                    addEnumConstant(originalType, fieldsEnumBuilder, field);
                    if (isCollection(field)) {
                        addEnumConstantForElementOfCollection(originalType, fieldsEnumBuilder, field);
                    }
                }
            );

        return fieldsEnumBuilder.build();
    }

    private String createEnumClassName(TypeElement originalType) {
        boolean isStatic = originalType.getModifiers().stream().anyMatch(Modifier.STATIC::equals);
        if (isStatic) {
            String qualifiedName = originalType.getQualifiedName().toString();
            String[] splitString = qualifiedName.split("\\.");
            return splitString[splitString.length - 2] + "$" + splitString[splitString.length - 1] + "Fields";
        } else {
            return originalType.getSimpleName() + "Fields";
        }
    }

    private void addEnumConstantForElementOfCollection(TypeElement originalType, TypeSpec.Builder fieldsEnumBuilder,
                                                       Element field) {
        String fieldName = field.getSimpleName().toString() + "Element";
        String enumName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fieldName);

        TypeSpec typeSpec = TypeSpec.anonymousClassBuilder(
            "$S, $T::" + getValueExtractorMethodName(field, originalType),
            fieldName, ClassName.get(originalType)).build();

        fieldsEnumBuilder.addEnumConstant(enumName, typeSpec);
    }

    private void addEnumConstant(TypeElement originalType, TypeSpec.Builder fieldsEnumBuilder, Element field) {
        fieldsEnumBuilder.addEnumConstant(
            CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getSimpleName().toString()),
            TypeSpec.anonymousClassBuilder(
                "$S, $T::" + getValueExtractorMethodName(field, originalType),
                field.getSimpleName(),
                ClassName.get(originalType)
            ).build()

        );
    }

    private boolean isCollection(Element field) {
        String erasedType = processingEnvironment.getTypeUtils().erasure(field.asType()).toString();
        return collections.contains(erasedType);
    }

    private MethodSpec getConstructorMethodSpec(TypeElement originalType) {
        TypeName typeName = ParameterizedTypeName.get(ClassName.get(Function.class),
            WildcardTypeName.supertypeOf(ClassName.get(originalType)),
            WildcardTypeName.subtypeOf(Object.class)
        );

        return MethodSpec.constructorBuilder()
            .addParameter(
                ParameterSpec.builder(String.class, FIELD_NAME_FIELD_NAME).addAnnotation(NonNull.class).build())
            .addParameter(
                ParameterSpec.builder(typeName, VALUE_EXTRACTOR_FIELD_NAME).addAnnotation(NonNull.class).build())
            .addStatement("this.$N = $N", FIELD_NAME_FIELD_NAME, FIELD_NAME_FIELD_NAME)
            .addStatement("this.$N = $N", VALUE_EXTRACTOR_FIELD_NAME, VALUE_EXTRACTOR_FIELD_NAME)
            .build();
    }

    private MethodSpec getGetValueExtractorMethodSpec() {
        return MethodSpec.methodBuilder(String.format("get%s", StringUtils.capitalize(VALUE_EXTRACTOR_FIELD_NAME)))
            .addModifiers(Modifier.PUBLIC)
            .addCode(CodeBlock.builder().addStatement(String.format("return  %s", VALUE_EXTRACTOR_FIELD_NAME)).build())
            .addAnnotation(Override.class)
            .returns(Function.class)
            .build();
    }

    private MethodSpec getGetFieldNameMethodSpec() {
        return MethodSpec.methodBuilder(String.format("get%s", StringUtils.capitalize(FIELD_NAME_FIELD_NAME)))
            .addModifiers(Modifier.PUBLIC)
            .addCode(CodeBlock.builder().addStatement(String.format("return %s", FIELD_NAME_FIELD_NAME)).build())
            .addAnnotation(Override.class)
            .returns(String.class)
            .build();
    }

    private MethodSpec getObjectTypeSpec(TypeElement originalType) {
        return MethodSpec.methodBuilder("getObjectType")
            .addModifiers(Modifier.PUBLIC)
            .addCode(CodeBlock.builder().addStatement(String.format("return %s.%s", ClassName.get(originalType), "class")).build())
            .addAnnotation(Override.class)
            .returns(ParameterizedTypeName.get(ClassName.get(Class.class), ClassName.get(originalType)))
            .build();
    }

    private MethodSpec getToStringMethodSpec() {
        return MethodSpec.methodBuilder("toString")
            .addModifiers(Modifier.PUBLIC)
            .addCode(CodeBlock.builder().addStatement("return getClass().getName() +\".\" + name()").build())
            .returns(String.class)
            .build();
    }

    private String getValueExtractorMethodName(Element field, TypeElement parentElementType) {
        return findValueExtractorMethodName(field, parentElementType)
            .orElseThrow(() -> new IllegalStateException("Field value extractor is missing for field " + field.getSimpleName()));
    }

    private Optional<String> findValueExtractorMethodName(Element field, TypeElement parentElementType) {
        String isStartedGetterName = "is" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getSimpleName().toString());
        String getStartedGetterName = "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getSimpleName().toString());
        return parentElementType.getEnclosedElements().stream()
            .filter(e -> ElementKind.METHOD.equals(e.getKind()))
            .map(e -> e.getSimpleName().toString())
            .filter(name -> name.equals(isStartedGetterName) || name.equals(getStartedGetterName))
            .findFirst();
    }
}
