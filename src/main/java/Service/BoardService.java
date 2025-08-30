package Service;

import Dao.BoardColumnDAO;
import Dao.BoardDAO;
import entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {
    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        boolean originalAutoCommitState = true;
        try {
            // ---> PASSO CRUCIAL: Desliga o autocommit para iniciar a transação
            originalAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);

            var dao = new BoardDAO(connection);
            var boardColumnDAO = new BoardColumnDAO(connection);

            dao.insert(entity);
            var columns = entity.getBoardColumns().stream().map(c -> {
                c.setBoard(entity);
                return c;
            }).toList();
            for (var column : columns) {
                boardColumnDAO.insert(column);
            }

            // Salva todas as alterações juntas
            connection.commit();

        } catch (SQLException e) {
            // Desfaz todas as alterações em caso de erro
            connection.rollback();
            throw e; // Lança o erro para a camada de UI saber o que aconteceu
        } finally {
            // Garante que a conexão volte ao seu estado original
            if (originalAutoCommitState) {
                connection.setAutoCommit(true);
            }
        }
        return entity;
    }

    //Metodo para deletar
    public boolean delete(final Long id) throws SQLException {
        boolean originalAutoCommitState = true;
        try {
            // ---> PASSO CRUCIAL: Desliga o autocommit para iniciar a transação
            originalAutoCommitState = connection.getAutoCommit();
            connection.setAutoCommit(false);

            var dao = new BoardDAO(connection);
            //Caso não exista o servico com id informado, ele retorna falso
            if (!dao.existence(id)) {
                return false;
            }
            //Caso exista ele returna verdaeiro e apaga
            dao.delete(id);

            // Salva a exclusão
            connection.commit();
            return true;

        } catch (SQLException e) {
            // Desfaz a exclusão em caso de erro
            connection.rollback();
            throw e;
        } finally {
            // Garante que a conexão volte ao seu estado original
            if (originalAutoCommitState) {
                connection.setAutoCommit(true);
            }
        }
    }
}

