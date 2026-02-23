package com.CarGarage.project.repositories;

import com.CarGarage.project.dao.CarJdbcDao;
import com.CarGarage.project.models.Car;

import java.sql.*;
import java.util.*;

public class DatabaseCarRepository implements CarRepository{
    private final CarJdbcDao dao;
    private int nextId;

    public DatabaseCarRepository() {
        this.dao = new CarJdbcDao("jdbc:h2:file:./data/garage", "sa", "");
        this.nextId = dao.getMaxId() + 1;
    }

    @Override
    public String generateId() {
        return String.valueOf(nextId++);
    }

    @Override
    public List<Car> findAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Car> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Car save(Car car) {
        return dao.save(car);
    }

    @Override
    public void deleteById(String id) {
        dao.deleteById(id);
    }

    @Override
    public boolean existsByPlate(String plate) {
        return dao.existsByPlate(plate);
    }
}
