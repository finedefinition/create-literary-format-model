package finedefinition.dao;

import finedefinition.DataProcessingException;
import finedefinition.models.LiteraryFormat;
import finedefinition.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LiteraryFormatDaoImpl implements LiteraryFormatDao {
    @Override
    public List<LiteraryFormat> getAll() {
        List<LiteraryFormat> allFormats = new ArrayList<>();

        try (Connection connection = ConnectionUtil.getConnection();
                Statement getAllFormatsStatement = connection.createStatement()) {
            ResultSet resultSet = getAllFormatsStatement
                       .executeQuery("SELECT * FROM literary_formats WHERE is_deleted = false;");
            while (resultSet.next()) {
                String format = resultSet.getString("format");
                Long id = resultSet.getObject("id", Long.class);
                LiteraryFormat literaryFormat = new LiteraryFormat();
                literaryFormat.setId(id);
                literaryFormat.setTitle(format);
                allFormats.add(literaryFormat);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can`t get all formats from DB", e);
        }
        return allFormats;
    }

    @Override
    public LiteraryFormat create(LiteraryFormat format) {
        String insertFormatRequest = "INSERT INTO literary_formats(format) values(?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement createFormatsStatement = connection
                        .prepareStatement(insertFormatRequest, Statement
                                .RETURN_GENERATED_KEYS)) {
            createFormatsStatement.setString(1, format.getTitle());
            createFormatsStatement.executeUpdate();
            ResultSet generatedKeys = createFormatsStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getObject(1, Long.class);
                format.setId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can`t insert format to DB", e);
        }
        return format;
    }

    @Override
    public boolean delete(Long id) {
        String deleteRequest = "UPDATE literary_formats SET is_deleted = true where id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement createFormatsStatement = connection
                        .prepareStatement(deleteRequest, Statement
                             .RETURN_GENERATED_KEYS)) {
            createFormatsStatement.setLong(1, id);
            return createFormatsStatement.executeUpdate() >= 1;

        } catch (SQLException e) {
            throw new RuntimeException("Can`t insert format to DB", e);
        }
    }

    @Override
    public Optional<LiteraryFormat> get(Long id) {
        String selectQuery = "SELECT * FROM literary_formats WHERE id = ? AND is_deleted = false;";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getLiteratureFormat(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException(
                    String.format("Can't get manufacturer with id = %d from database", id), e);
        }
        return Optional.empty();
    }

    @Override
    public LiteraryFormat update(LiteraryFormat format) {
        String query = "UPDATE literary_formats SET format = ?"
                + " WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(query)) {
            statement.setString(1, format.getTitle());
            statement.setLong(2, format.getId());
            statement.executeUpdate();
            return format;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update a manufacturer "
                    + format, e);
        }
    }

    private LiteraryFormat getLiteratureFormat(ResultSet resultSet) {
        try {
            LiteraryFormat literaryFormat = new LiteraryFormat();
            literaryFormat.setId(resultSet.getObject("id", Long.class));
            literaryFormat.setTitle(resultSet.getString("format"));
            return literaryFormat;
        } catch (SQLException e) {
            throw new RuntimeException("Can`t insert format to DB", e);
        }
    }
}
