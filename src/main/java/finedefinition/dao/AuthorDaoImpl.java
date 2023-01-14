package finedefinition.dao;

import finedefinition.DataProcessingException;
import finedefinition.lib.Dao;
import finedefinition.models.Author;
import finedefinition.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class AuthorDaoImpl implements AuthorDao {
    @Override
    public Author create(Author author) {
        String query = "INSERT INTO authors (name, lastname) VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getLastname());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                author.setId(resultSet.getObject(1, Long.class));
            }
            return author;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create "
                    + author + ". ", e);
        }
    }

    @Override
    public Optional<Author> get(Long id) {
        String query = "SELECT id, name, lastname "
                + "FROM authors "
                + "WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Author author = null;
            if (resultSet.next()) {
                author = getAuthor(resultSet);
            }
            return Optional.ofNullable(author);
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get author by id " + id, e);
        }
    }

    @Override
    public List<Author> getAll() {
        String query = "SELECT * FROM authors WHERE is_deleted = FALSE";
        List<Author> authors = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                authors.add(getAuthor(resultSet));
            }
            return authors;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of authors from authors DB.", e);
        }
    }

    @Override
    public Author update(Author author) {
        String query = "UPDATE authors "
                + "SET name = ?, lastname = ? "
                + "WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement
                        = connection.prepareStatement(query)) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getLastname());
            statement.setLong(3, author.getId());
            statement.executeUpdate();
            return author;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update "
                    + author + " in driversDB.", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE authors SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete author with id " + id, e);
        }
    }

    private Author getAuthor(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getObject("id", Long.class);
        String name = resultSet.getString("name");
        String lastname = resultSet.getString("lastname");
        return new Author(id, name, lastname);
    }
}
