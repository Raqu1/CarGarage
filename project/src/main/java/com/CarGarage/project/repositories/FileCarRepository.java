package com.CarGarage.project.repositories;

import com.CarGarage.project.models.Car;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileCarRepository implements CarRepository{
    private static final String FILE_PATH = "cars.yaml";
    private int nextId = 1;

    @Override
    public String generateId() {
        return String.valueOf(nextId++);
    }

    @Override
    public List<Car> findAll() {
        return loadFromFile();
    }

    @Override
    public Optional<Car> findById(String id) {
        return loadFromFile().stream().filter(car -> car.getId().equals(id)).findFirst();
    }

    @Override
    public Car save(Car car) {
        List<Car> cars = loadFromFile();

        cars.removeIf(c -> c.getId().equals(car.getId()));

        cars.add(car);
        saveToFile(cars);
        return car;
    }

    @Override
    public void deleteById(String id) {
        List<Car> cars = loadFromFile();
        cars.removeIf(car -> car.getId().equals(id));

        saveToFile(cars);
    }

    @Override
    public boolean existsByPlate(String plate) {
        return loadFromFile().stream()
                .anyMatch(car -> car.getPlate().equals(plate));
    }

    private List<Car> loadFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Yaml yaml = new Yaml();
            List<Map<String, Object>> data = yaml.load(reader);

            if (data == null) {
                return new ArrayList<>();
            }

            List<Car> cars = new ArrayList<>();
            for (Map<String, Object> map : data) {
                Car car = new Car();
                car.setId(String.valueOf(map.get("id")));
                car.setBrand((String) map.get("brand"));
                car.setModel((String) map.get("model"));
                car.setYear((Integer) map.get("year"));
                car.setPlate((String) map.get("plate"));
                cars.add(car);
            }
            return cars;
        } catch (IOException e) {
            throw new RuntimeException("Error reading " + FILE_PATH, e);
        }
    }

    private void saveToFile(List<Car> cars) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        List<Map<String, Object>> data = new ArrayList<>();
        for (Car car : cars) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", car.getId());
            map.put("brand", car.getBrand());
            map.put("model", car.getModel());
            map.put("year", car.getYear());
            map.put("plate", car.getPlate());
            data.add(map);
        }

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error writing " + FILE_PATH, e);
        }
    }
}
