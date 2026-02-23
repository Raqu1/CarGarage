package com.CarGarage.project.dao;

import com.CarGarage.project.models.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarJdbcDao {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private int nextId = 1;

    public CarJdbcDao(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public int getMaxId() {
        String sql = "SELECT MAX(CAST(car_id AS INT)) FROM cars";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Table might not exist yet on first run
        }
        return 0;
    }

    public String generateId() {
        return String.valueOf(nextId++);
    }

    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        String findAllSql = "SELECT car_id, car_brand, car_model, car_year, car_plate FROM cars";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(findAllSql)) {

            while (rs.next()) {
                cars.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all cars", e);
        }
        return cars;
    }

    public Optional<Car> findById(String id) {
        String byIdSql = "SELECT car_id, car_brand, car_model, car_year, car_plate FROM cars WHERE car_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(byIdSql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding car by id: " + id, e);
        }
        return Optional.empty();
    }

    public Car save(Car car) {
        // Try update first, if no rows affected then insert
        String updateSql = "UPDATE cars SET car_brand = ?, car_model = ?, car_year = ?, car_plate = ? WHERE car_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setString(4, car.getPlate());
            stmt.setString(5, car.getId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 0) {
                // No existing row, insert a new one
                insert(conn, car);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving car: " + car.getId(), e);
        }
        return car;
    }

    public void deleteById(String id) {
        String deleteSql = "DELETE FROM cars WHERE car_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting car: " + id, e);
        }
    }

    public boolean existsByPlate(String plate) {
        String byPlateSql = "SELECT COUNT(*) FROM cars WHERE car_plate = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(byPlateSql)) {

            stmt.setString(1, plate);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking plate: " + plate, e);
        }
    }

    // --- Private helpers ---

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private void insert(Connection conn, Car car) throws SQLException {
        String insertSql = "INSERT INTO cars (car_id, car_brand, car_model, car_year, car_plate) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setString(1, car.getId());
            stmt.setString(2, car.getBrand());
            stmt.setString(3, car.getModel());
            stmt.setInt(4, car.getYear());
            stmt.setString(5, car.getPlate());
            stmt.executeUpdate();
        }
    }

    private Car mapRow(ResultSet rs) throws SQLException {
        return new Car(
                rs.getString("car_id"),
                rs.getString("car_brand"),
                rs.getString("car_model"),
                rs.getInt("car_year"),
                rs.getString("car_plate")
        );
    }
}
