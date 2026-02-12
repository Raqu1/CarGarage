package com.CarGarage.project.services;

import com.CarGarage.project.factory.FactoryRepository;
import com.CarGarage.project.repositories.CarRepository;
import com.CarGarage.project.models.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(FactoryRepository factory) {
        this.carRepository = factory.create(CarRepository.class);
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public Car findCar(String id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car with id " + id + " not found"));
    }

    public Car createCar(Car car) {
        validatePlate(car.getPlate());

        if (carRepository.existsByPlate(car.getPlate())) {
            throw new IllegalArgumentException("Plate already registered");
        }

        car.setId(carRepository.generateId());
        return carRepository.save(car);
    }

    public Car modifyCar(String id, Car car) {
        Car storedCar = findCar(id);

        validatePlate(car.getPlate());

        boolean plateChanged = !storedCar.getPlate().equals(car.getPlate());

        if (plateChanged && carRepository.existsByPlate(car.getPlate())) {
            throw new IllegalArgumentException("Plate already registered");
        }

        car.setId(id);

        return carRepository.save(car);
    }

    public void removeCar(String id) {
        findCar(id);
        carRepository.deleteById(id);
    }

    private void validatePlate(String plate) {
        if (plate == null || plate.isBlank()) {
            throw new IllegalArgumentException("Plate cannot be empty");
        }
    }

}
