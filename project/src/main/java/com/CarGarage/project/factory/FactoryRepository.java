package com.CarGarage.project.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Component
public class FactoryRepository {

    @Value("${car.repository.class}")
    private String repositoryClassName;

    private final Map<String, Object> cache = new HashMap<>();

    public <T> T create(Class<T> type) {
        if (cache.containsKey(repositoryClassName)) {
            return type.cast(cache.get(repositoryClassName));
        }

        try {
            Class<?> clazz = Class.forName(repositoryClassName);
            Object instance = clazz.getDeclaredConstructor().newInstance();

            cache.put(repositoryClassName, instance);

            return type.cast(instance);
        } catch (Exception e) {
            throw new RuntimeException("Could not create repository: " + repositoryClassName, e);
        }
    }
}
