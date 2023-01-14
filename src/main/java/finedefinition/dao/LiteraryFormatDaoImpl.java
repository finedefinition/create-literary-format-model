package finedefinition.dao;

import finedefinition.DataProcessingException;
import finedefinition.lib.Dao;
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

@Dao
public class LiteraryFormatDaoImpl implements LiteraryFormatDao {
    @Override
    public List<LiteraryFormat> getAll() {
        String query = "SELECT * FROM literary_formats WHERE is_deleted = false;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement
                        = connection.prepareStatement(query)) {
            List<LiteraryFormat> literaryFormats = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                literaryFormats.add(getLiteratureFormat(resultSet));
            }
            return literaryFormats;
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't get a list of literaryFormats "
                    + "from literaryFormats table. ", e);
        }
    }

    @Override
    public LiteraryFormat create(LiteraryFormat format) {
        String insertFormatRequest = "INSERT INTO literary_formats(format) values(?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement createFormatsStatement = connection
                        .prepareStatement(insertFormatRequest, Statement
                                .RETURN_GENERATED_KEYS)) {
            createFormatsStatement.setString(1, format.getFormat());
            createFormatsStatement.executeUpdate();
            ResultSet generatedKeys = createFormatsStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getObject(1, Long.class);
                format.setId(id);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create format. " + format, e);
        }
        return format;
    }

    @Override
    public boolean delete(Long id) {
        String deleteRequest = "UPDATE literary_formats SET is_deleted = true where id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement
                         = connection.prepareStatement(deleteRequest)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;

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
            LiteraryFormat literaryFormat = null;
            if (resultSet.next()) {
                literaryFormat = getLiteratureFormat(resultSet);
            }
            return Optional.ofNullable(literaryFormat);
        } catch (SQLException e) {
            throw new DataProcessingException(
                    String.format("Can't get manufacturer with id = %d from database", id), e);
        }
    }

    @Override
    public LiteraryFormat update(LiteraryFormat format) {
        String query = "UPDATE literary_formats SET format = ?"
                + " WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement
                        = connection.prepareStatement(query)) {
            statement.setString(1, format.getFormat());
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
            literaryFormat.setFormat(resultSet.getString("format"));
            return literaryFormat;
        } catch (SQLException e) {
            throw new RuntimeException("Can`t insert format to DB", e);
        }
    }
}
