package config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectionConfig {

    private static final Properties properties = new Properties();

    // Bloco static para carregar as propriedades uma única vez quando a classe for iniciada
    static {
        loadProperties();
    }

    private static void loadProperties() {
        // Tenta carregar o arquivo db.properties do classpath (src/main/resources)
        try (InputStream input = ConnectionConfig.class.getClassLoader().getResourceAsStream("sql/db.properties")) {
            if (input == null) {
                System.out.println("Desculpe, não foi possível encontrar o arquivo db.properties");
                // Termina a execução se o arquivo de configuração não for encontrado
                System.exit(1);
            }
            // Carrega as propriedades do arquivo
            properties.load(input);
        } catch (IOException ex) {
            // Imprime o erro e encerra se houver uma falha de leitura
            ex.printStackTrace();
            throw new RuntimeException("Falha ao carregar o arquivo de propriedades.", ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        // Usa as propriedades carregadas do arquivo para criar a conexão
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}

