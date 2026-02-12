package com.CarGarage.project.repositories;

import com.CarGarage.project.models.Car;

import java.util.*;

public class InMemoryCarRepository implements CarRepository{
    private final List<Car> cars = new ArrayList<>();
    private final Map<String, Car> carIndex = new HashMap<>();
    private int nextId = 1;

    @Override
    public String generateId() {
        return String.valueOf(nextId++);
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars);
    }

    @Override
    public Optional<Car> findById(String id) {
        return Optional.ofNullable(carIndex.get(id));
    }

    @Override
    public Car save(Car car) {
        deleteById(car.getId());

        cars.add(car);
        carIndex.put(car.getId(), car);
        return car;
    }

    @Override
    public void deleteById(String id) {
        Car existing = carIndex.remove(id);

        if (existing != null) {
            cars.remove(existing);
        }
    }

    @Override
    public boolean existsByPlate(String plate) {
        return cars.stream()
                .anyMatch(car -> car.getPlate().equals(plate));
    }
}
