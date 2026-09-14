package co.edu.unicauca.lisw2_t02_g03.access;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseInitializer {

    private final DataBaseManager databaseManager;

    public DataBaseInitializer(DataBaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void initialize() {

        String sql = """
                CREATE TABLE IF NOT EXISTS Usuario (
                    Login TEXT PRIMARY KEY,
                    NombreCompleto TEXT NOT NULL,
                    Rol TEXT NOT NULL,
                    Estado TEXT NOT NULL,
                    Contrasena TEXT NOT NULL
                );
                """;

        try {
        
            databaseManager.connect();

            Connection conn = databaseManager.getConnection();

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }

        } catch (SQLException ex) {

            Logger.getLogger(DataBaseInitializer.class.getName())
                    .log(Level.SEVERE, null, ex);

        } finally {

            databaseManager.disconnect();
        }
    }
}