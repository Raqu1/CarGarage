package com.CarGarage.project.repositories;

import com.CarGarage.project.models.Car;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CarRepository {

    String generateId();

    public List<Car> findAll();

    public Optional<Car> findById(String id);

    public Car save(Car car);

    public void deleteById(String id);

    public boolean existsByPlate(String plate);
}
