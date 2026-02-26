package com.CarGarage.project.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseConnection implements ConnectionProvider{
    private static final Logger log = Logger.getLogger(DatabaseConnection.class.getName());

    private final String url;
    private final String user;
    private final String password;

    private Connection connection;
    private final String dialect;


    /*  CONSTRUCTOR  */
    public DatabaseConnection() {
        // Load DB properties
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar application.properties", e);
        }

        this.url = props.getProperty("spring.datasource.url");
        this.user = props.getProperty("spring.datasource.username");
        this.password = props.getProperty("spring.datasource.password", "");

        // detects the dialect basen on the url
        this.dialect = detectDialect();
        log.info("Dialecto detectado: " + dialect);

        // registers the hook to close the connection when the JMV is turned off
        registerShutdownHook();
        log.info("DatabaseConnection inicializado (URL: " + url + ")");
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
                log.info("Nueva conexion creada a: " + url);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }


    /*  reconnect()  */
    public void reconnect() {
        log.info("Reconectando a la base de datos...");
        close();
        getConnection(); // lazy init creara una nueva
        log.info("Reconexion completada");
    }


    /*  close()  */
    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                log.info("Conexion cerrada correctamente");
            }
        } catch (SQLException e) {
            log.warning("Error al cerrar la conexion: " + e.getMessage());
        }
    }


    /*  registerShutdownHook()  */
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook activado — cerrando conexion...");
            close();
        }, "db-shutdown-hook"));
    }


    /*  detectDialect()  */
    private String detectDialect() {
        if (url == null) return "UNKNOWN";

        String urlLower = url.toLowerCase();

        if (urlLower.contains(":h2:")) return "H2";
        if (urlLower.contains(":mysql:")) return "MYSQL";
        if (urlLower.contains(":postgresql:")) return "POSTGRESQL";

        return "UNKNOWN";
    }

    // returns the dialect detected
    public String getDialect() {
        return dialect;
    }
}
