package org.example.infrastructure;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Возвращает сервисы-обработчики TypeSpecificService для объектов из одной иерархии.
 *
 * @see TypeSpecificService
 */
@Service
public class TypeSpecificServiceResolver {

    @Autowired(required = false)
    private List<TypeSpecificService> allServices;

    ImmutableSetMultimap<Class, TypeSpecificService> categorizedServices;

    @PostConstruct
    void init() {
        initAllServices();
        categorizedServices = buildCategorizedServices();
    }

    /**
     * Возврашает сервис-обработчик с базовым типом serviceType, зарегистрированный для объекта с типом
     * processedObjectType или его предков. Если для processedObjectType и его предков нет зарегистрированных
     * обработчиков, выкидывает IllegalStateException.
     *
     * @param processedObjectType тип объекта, для которого нужно получить сервис-обработчик
     * @param serviceType         базовый класс или интерфейс сервиса, зарегистрированного для обработки объекта с типом
     *                            processedObjectType (или его предка)
     * @param <T>                 тип зарегистрированного в системе сервиса-обработчика
     * @return сервис-обработччик с базовым типом serviceType, зарегистрированный для объекта с типом
     * processedObjectType или его предков или IllegalStateException, если обработчик не найден.
     * @throws IllegalStateException если обработчик не найден
     */
    public <T extends TypeSpecificService> T resolveStrictly(Class processedObjectType, Class<T> serviceType) {
        Optional<T> serviceOptional = resolve(processedObjectType, serviceType);
        serviceOptional.orElseThrow(() -> new IllegalStateException("Не найден обработчик с типом " + serviceType + " для " + processedObjectType));
        return serviceOptional.get();
    }

    /**
     * Возврашает сервис-обработчик с базовым типом serviceType, зарегистрированный для объекта с типом
     * processedObjectType или его предков. Если для processedObjectType и его предков нет зарегистрированных
     * обработчиков, возвращает absent.
     *
     * @param processedObjectType тип объекта, для которого нужно получить сервис-обработчик
     * @param serviceType         базовый класс или интерфейс сервиса, зарегистрированного для обработки объекта с типом
     *                            processedObjectType (или его предка)
     * @param <T>                 тип зарегистрированного в системе сервиса-обработчика
     * @return сервис-обработчик с базовым типом serviceType, зарегистрированный для объекта с типом
     * processedObjectType или его предков или Optional.empty(), если обработчик не найден.
     */
    public <T extends TypeSpecificService> Optional<T> resolve(Class processedObjectType, Class<T> serviceType) {
        return typeSuperclasses(processedObjectType).stream()
            .map(type -> findProcessorForType(serviceType, type))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();
    }

    private ImmutableSetMultimap<Class, TypeSpecificService> buildCategorizedServices() {
        ImmutableSetMultimap.Builder<Class, TypeSpecificService> servicesBuilder = ImmutableSetMultimap.builder();
        allServices.forEach(processor -> servicesBuilder.put(processor.getProcessedClass(), processor));
        return servicesBuilder.build();
    }

    private void initAllServices() {
        if (allServices == null) {
            allServices = new ArrayList<>();
        }
    }

    private List<Class> typeSuperclasses(Class processedType) {
        if (processedType != null) {
            List<Class> result = Lists.newArrayList(processedType);
            result.addAll(typeSuperclasses(processedType.getSuperclass()));
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends TypeSpecificService> Optional<T> findProcessorForType(Class<T> serviceType, Class type) {
        ImmutableSet<TypeSpecificService> services = categorizedServices.get(type);
        return (Optional<T>) services.stream().filter(serviceType::isInstance).findFirst();
    }
}
