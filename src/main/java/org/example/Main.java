package org.example;

import migration.MigrationStrategy;
import ui.MainMenu;

import java.sql.SQLException;

import static config.ConnectionConfig.getConnection;


public class Main {

    public static void main(String[] args) throws SQLException {
        try (var connection = getConnection()) {
            new MigrationStrategy(connection).executeMigration();
        }
        new MainMenu().execute();
    }
}
