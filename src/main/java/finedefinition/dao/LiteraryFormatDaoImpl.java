package finedefinition.dao;

import finedefinition.models.LiteraryFormat;
import finedefinition.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LiteraryFormatDaoImpl implements LiteraryFormatDao {
    @Override
    public List<LiteraryFormat> getAll() {
        List<LiteraryFormat> allFormats = new ArrayList<>();

        try (Connection connection = ConnectionUtil.getConnection();
                Statement getAllFormatsStatement = connection.createStatement()) {
            ResultSet resultSet = getAllFormatsStatement
                       .executeQuery("SELECT * FROM literary_formats;");
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
        try (Connection connection = ConnectionUtil.getConnection();
                Statement createFormatsStatement = connection.createStatement()) {
            String insertFormatRequest = "INSERT INTO literary_formats(format) values('"
                       + format.getTitle() + "')";
            createFormatsStatement.executeUpdate(insertFormatRequest);
        } catch (SQLException e) {
            throw new RuntimeException("Can`t insert format to DB", e);
        }
        return null;
    }
}
