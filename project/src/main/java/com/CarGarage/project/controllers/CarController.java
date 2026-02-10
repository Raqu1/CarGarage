package com.CarGarage.project.controllers;

import com.CarGarage.project.models.Car;
import com.CarGarage.project.services.CarService;
import com.CarGarage.project.view.CarView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CarController {
    @Autowired
    private CarService carService;

    @Autowired
    private CarView carView;

    private boolean active = true;

    public void run() {
        while (active) {
            carView.printMenu();
            handleOption(carView.getInput("Select option"));
        }
        carView.shutdown();
    }

    private void handleOption(String option) {
        try {
            switch (option) {
                case "1":
                    showAll();
                    break;
                case "2":
                    showById();
                    break;
                case "3":
                    create();
                    break;
                case "4":
                    edit();
                    break;
                case "5":
                    remove();
                    break;
                case "6":
                    exit();
                    break;
                default:
                    carView.notify("Unknown option");
            }
        } catch (Exception ex) {
            carView.notify("Operation failed: " + ex.getMessage());
        }
    }

    private void showAll() {
        carView.renderCars(carService.listCars());
    }

    private void showById() {
        String id = carView.getInput("Car id");
        Car car = carService.findCar(id);
        carView.renderCar(car);
    }

    private void create() {
        Car car = readCarData(null);
        carService.createCar(car);
        carView.notify("Car successfully created");
    }

    private void edit() {
        String id = carView.getInput("Car id to edit");
        Car updated = readCarData(id);
        carService.modifyCar(id, updated);
        carView.notify("Car successfully updated");
    }

    private void remove() {
        String id = carView.getInput("Car id to delete");
        carService.removeCar(id);
        carView.notify("Car removed");
    }

    private void exit() {
        carView.notify("Bye.");
        active = false;
    }

    private Car readCarData(String id) {
        String brand = carView.getInput("Brand");
        String model = carView.getInput("Model");
        int year = Integer.parseInt(carView.getInput("Year"));
        String plate = carView.getInput("Plate");

        return new Car(id, brand, model, year, plate);
    }
}
