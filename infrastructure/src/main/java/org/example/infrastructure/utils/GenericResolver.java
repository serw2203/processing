package org.example.infrastructure.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class GenericResolver {

    public static Class getFirstGenericTypeFromInterface(Class<?> concreteClass) {
        Class genericType = null;
        Type type = ((ParameterizedType) concreteClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return fromType(type);
    }

    public static Class getFirstGenericTypeFromSuperclass(Class<?> concreteClass) {
        Class genericType = null;
        Type type = ((ParameterizedType) concreteClass.getGenericSuperclass()).getActualTypeArguments()[0];
        return fromType(type);
    }

    private static Class fromType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof TypeVariable) {
            Type typeVariable;
            typeVariable = ((TypeVariable) type).getBounds()[0];
            if (typeVariable instanceof Class) {
                return (Class) typeVariable;
            }
        }
        return null;
    }
}
