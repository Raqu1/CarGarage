package com.CarGarage.project.repositories;

import com.CarGarage.project.models.Car;

import java.util.*;

public class DatabaseCarRepository implements CarRepository{
    private final Map<String, Car> table = new HashMap<>();
    private int nextId = 1;

    @Override
    public String generateId() {
        return String.valueOf(nextId++);
    }

    @Override
    public List<Car> findAll() {
        System.out.println("[DatabaseRepository]: SELECT * FROM cars");
        return new ArrayList<>(table.values());
    }

    @Override
    public Optional<Car> findById(String id) {
        System.out.println("[DatabaseRepository]: SELECT * FROM cars WHERE id = " +  id);
        return Optional.ofNullable(table.get(id));
    }

    @Override
    public Car save(Car car) {
        System.out.println("[DatabaseRepository]: INSERT/UPDATE * FROM cars WHERE id = " + car.getId());
        deleteById(car.getId());

        table.put(car.getId(), car);
        return car;
    }

    @Override
    public void deleteById(String id) {
        System.out.println("[DatabaseRepository]: DELETE * FROM cars WHERE id = " + id);
        table.remove(id);
    }

    @Override
    public boolean existsByPlate(String plate) {
        return table.values().stream()
                .anyMatch(car -> car.getPlate().equals(plate));
    }
}
