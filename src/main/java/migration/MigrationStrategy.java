package migration;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MigrationStrategy {

    private final Connection connection;

    public void executeMigration() {
        String resourcePath = "sql/init.sql";
        try (InputStream inputStream = MigrationStrategy.class.getClassLoader().getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IOException("Erro: Arquivo " + resourcePath + " não encontrado no classpath. Verifique se ele existe em 'src/main/resources/sql/'.");
            }

            // Lê o script SQL do arquivo
            String sqlScriptWithComments = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

            // Remove todos os comentários (linhas que começam com --) do script
            String sqlScript = sqlScriptWithComments.replaceAll("--.*", "");

            try (Statement statement = connection.createStatement()) {
                // Divide o script em comandos individuais usando o ponto e vírgula como delimitador
                String[] individualStatements = sqlScript.split(";");

                for (String singleStatement : individualStatements) {
                    // Executa cada comando, desde que não esteja vazio
                    if (!singleStatement.trim().isEmpty()) {
                        statement.execute(singleStatement);
                    }
                }

                System.out.println("Script SQL 'init.sql' executado com sucesso.");
            } catch (SQLException e) {
                System.err.println("Erro ao executar o script SQL.");
                e.printStackTrace();
            }

        } catch (IOException e) {
            throw new RuntimeException("Falha ao ler o arquivo " + resourcePath, e);
        }
    }
}
