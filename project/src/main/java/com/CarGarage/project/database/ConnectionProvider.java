package com.CarGarage.project.database;

import java.sql.Connection;

public interface ConnectionProvider {

    Connection getConnection();
    void close();
}
