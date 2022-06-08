package org.example.infrastructure;

import org.springframework.core.GenericTypeResolver;

/**
 * Сервис для обработки объектов заданного типа из одной иерархии.
 * Используется, когда объекты разного типа из одной иерархии должны
 * иметь разные обработчики (для предотвращения исопльзования instanceof).
 * <p>
 * Все сервисы, реализующие данный интерфейс, автоматически регистрируются в TypeSpecificServiceResolver и
 * должны использоваться через него.
 *
 * @param <T> тип обрабатваемых сервисом объектов
 * @see TypeSpecificServiceResolver
 */
public interface TypeSpecificService<T> {

    /**
     * Возвращает класс объектов, обрабатываемых данным сервисом
     *
     * @return класс объектов, обрабатываемых данным сервисом
     * @throws IllegalStateException если не смог определить своей Generic-тип - т.е. если имплементация его не задала
     */
    default Class<T> getProcessedClass() {
        Class genericClass = GenericTypeResolver.resolveTypeArgument(this.getClass(), TypeSpecificService.class);
        if (genericClass == null) {
            throw new IllegalStateException("В системе содержится не-generic-озированная имплементация TypeSpecificService!");
        }
        return genericClass;
    }

    ;
}
