package com.CarGarage.project.view;

import com.CarGarage.project.models.Car;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CarView {

    private final Scanner input = new Scanner(System.in);

    public void printMenu() {
        System.out.println("\n=== GARAGE MENU ===");
        System.out.println("1) Show cars");
        System.out.println("2) Show car by ID");
        System.out.println("3) Create car");
        System.out.println("4) Edit car");
        System.out.println("5) Remove car");
        System.out.println("6) Exit");
    }

    public String getInput(String label) {
        if (label != null && !label.isBlank()) {
            System.out.print(label + ": ");
        }
        return input.nextLine();
    }

    public void renderCars(List<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            System.out.println("Garage is empty.");
            return;
        }

        System.out.println("\n--- CAR LIST ---");
        cars.forEach(car ->
                System.out.println(
                        "[" + car.getId() + "] " +
                                car.getBrand() + " " +
                                car.getModel() + " - " +
                                car.getYear() + " | " +
                                car.getPlate()
                )
        );
    }

    public void renderCar(Car car) {
        if (car == null) {
            System.out.println("No car found with that ID.");
            return;
        }

        System.out.println("\n--- CAR INFO ---");
        System.out.println("Id:    " + car.getId());
        System.out.println("Brand: " + car.getBrand());
        System.out.println("Model: " + car.getModel());
        System.out.println("Year:  " + car.getYear());
        System.out.println("Plate: " + car.getPlate());
    }

    public void notify(String text) {
        System.out.println(text);
    }

    public void shutdown() {
        input.close();
    }
}
