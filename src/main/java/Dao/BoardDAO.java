package Dao;

import com.mysql.cj.jdbc.StatementImpl;
import entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {

    private final Connection connection;


    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS (name) values (?);";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.executeUpdate();
            if(statement instanceof StatementImpl impl){
                entity.setId(impl.getLastInsertID());
            }
        }
        return entity;
    }


    public void delete (final Long id) throws SQLException {
        var sql = "DELETE FROM boards WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.execute();
        }
    }

    public Optional<BoardEntity> FindbyId(final Long id) throws SQLException {

        //Busca  de fato o dado
        var sql = "SELECT id, name FROM boards WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            //Caso exista um registro ele vai ser retornado pelo metodo
            if (resultSet.next()) {
                var entity = new BoardEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                return Optional.of(entity);
            }
            //Caso não exista, ele vai retorna um optional vazio.
            return Optional.empty();
        }
    }

    //Serve para verificar se o registro existe na tabela
    public boolean existence(final Long id) throws SQLException {
        var sql = "SELECT 1 FROM boards WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            return statement.getResultSet().next();
        }

    }
}
