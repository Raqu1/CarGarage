package com.CarGarage.project.repositories;

import com.CarGarage.project.models.Car;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CarRepository {

    private final List<Car> cars = new ArrayList<>();
    private final Map<String, Car> carIndex = new HashMap<>();
    private int nextId = 1;

    public String generateId() {
        return String.valueOf(nextId++);
    }

    public List<Car> findAll() {
        return new ArrayList<>(cars);
    }

    public Optional<Car> findById(String id) {
        return Optional.ofNullable(carIndex.get(id));
    }

    public Car save(Car car) {
        deleteById(car.getId());

        cars.add(car);
        carIndex.put(car.getId(), car);
        return car;
    }

    public void deleteById(String id) {
        Car existing = carIndex.remove(id);

        if (existing != null) {
            cars.remove(existing);
        }
    }

    public boolean existsByPlate(String plate) {
        return cars.stream()
                .anyMatch(car -> car.getPlate().equals(plate));
    }
}
