package org.example.validator;

import lombok.Getter;

@Getter
public enum BaseValidationCode implements ValidationCode {
    STATUS_FLOW_ERROR("Переход из статуса %s в статус %s невозможен"),
    REG_EXPR_MATCH("%s - неверный формат данных"),
    NULL_CHECK("%s - обязательно для заполнения"),
    LEGAL_VALUE("Параметр %s не может иметь значение отличное от допустимых: %s"),
    LEGAL_VALUE_CONDITION("%s - должен иметь значение %s, если %s имеет значение %s"),
    NOT_NULL_CONDITION_CHECK("%s - обязательно для заполнения, если %s имеет значение, равное %s"),
    UNIQUE_CHECK("Операция %s может существовать только в единственном экземпляре");

    private String messageTemplate;

    BaseValidationCode(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }
}
