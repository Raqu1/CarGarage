package com.CarGarage.project.repositories;

import com.CarGarage.project.models.Car;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CarRepository {

    private final List<Car> cars = new ArrayList<>();
    private int nextId = 1;

    public String generateId() {
        return String.valueOf(nextId++);
    }

    public List<Car> findAll() {
        return new ArrayList<>(cars);
    }

    public Optional<Car> findById(String id) {
        return cars.stream().filter(car -> car.getId().equals(id)).findFirst();
    }

    public Car save(Car car) {
        deleteById(car.getId());
        cars.add(car);
        return car;
    }

    public void deleteById(String id) {
        cars.removeIf(car -> car.getId().equals(id));
    }

    public boolean existsByPlate(String plate) {
        return cars.stream()
                .anyMatch(car -> car.getPlate().equals(plate));
    }
}
