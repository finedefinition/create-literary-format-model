package finedefinition.dao;

import finedefinition.DataProcessingException;
import finedefinition.lib.Dao;
import finedefinition.models.Author;
import finedefinition.models.Book;
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
public class BookDaoImpl implements BookDao {
    @Override
    public Book create(Book book) {
        String insertRequest = "INSERT INTO library_db.books (title,"
                + " price, literary_format_id) values (?, ?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement createBookStatement = connection
                        .prepareStatement(insertRequest, Statement.RETURN_GENERATED_KEYS)) {
            createBookStatement.setString(1, book.getTitle());
            createBookStatement.setBigDecimal(2, book.getPrice());
            createBookStatement.setLong(3, book.getFormat().getId());
            createBookStatement.executeUpdate();

            ResultSet generatedKeys = createBookStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getObject(1, Long.class);
                book.setId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can`t insert book to DB", e);
        }
        insertAuthors(book);
        return book;
    }

    private void insertAuthors(Book book) {
        String insertAuthorsQuery = "INSERT INTO books_authors "
                + "(book_id, author_id) VALUES (?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(insertAuthorsQuery)) {
            statement.setLong(1, book.getId());
            for (Author author : book.getAuthors()) {
                statement.setLong(2, author.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can`t insert authors to book: " + book, e);
        }
    }

    @Override
    public List<Book> getAll() {
        return null;
    }

    @Override
    public boolean delete(Long bookId) {
        String deleteBookQuery = "UPDATE books SET is_deleted = TRUE" + "WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement softDeleteBookStatement = connection
                        .prepareStatement(deleteBookQuery)) {
            softDeleteBookStatement.setLong(1, bookId);
            int numberOfDeletedRows = softDeleteBookStatement.executeUpdate();
            return numberOfDeletedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException("Can`t delete book with id: " + bookId, e);
        }
    }

    @Override
    public Optional<Book> get(Long id) {
        String selectRequest = "SELECT b.id AS book_id, title, price,"
                + " lf.id AS literary_format_id, lf.format\n"
                + "FROM library_db.books b\n" + "JOIN literary_formats lf\n"
                + "ON b.literary_format_id = lf.id\n"
                + "WHERE b.id = ? AND b.is_deleted = false;";
        Book book = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getBookStatement = connection
                        .prepareStatement(selectRequest)) {
            getBookStatement.setLong(1, id);
            ResultSet resultSet = getBookStatement.executeQuery();
            if (resultSet.next()) {
                book = parsedBookWithLiteraryFormatFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            throw new DataProcessingException(String
                    .format("Can't find book with id = %d from database", id), e);
        }
        if (book != null) {
            book.setAuthors(getAuthorsForBook(id));
        }
        return Optional.ofNullable(book);
    }

    @Override
    public Book update(Book book) {
        //TODO 1. update books fields
        // 2. delete all relations in books_authors table
        // where bookId = book.getId() IN CASE OF DELETE AUTHOR
        // 3.add new relations to the books_authors table
        return book;
    }

    @Override
    public List<Book> getAllByAuthor(Long authorId) {
        return null;
    }

    private Book parsedBookWithLiteraryFormatFromResultSet(ResultSet resultSet)
            throws SQLException {
        Book book = new Book();
        book.setTitle(resultSet.getString("title"));
        book.setPrice(resultSet.getBigDecimal("price"));
        LiteraryFormat literaryFormat = new LiteraryFormat();
        literaryFormat.setId(resultSet
                .getObject("literary_format_id", Long.class));
        literaryFormat.setFormat(resultSet.getString("format"));
        book.setFormat(literaryFormat);
        book.setId(resultSet.getObject("book_id", Long.class));
        return book;
    }

    private List<Author> getAuthorsForBook(Long bookId) {
        String getAllAuthorsForBookRequest = "SELECT id, name, lastname FROM authors a JOIN "
                + "books_authors ba ON a.id = ba.author_id WHERE ba.book_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllAuthorsStatement = connection
                        .prepareStatement(getAllAuthorsForBookRequest)) {
            getAllAuthorsStatement.setLong(1, bookId);
            ResultSet resultSet = getAllAuthorsStatement.executeQuery();
            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                authors.add(parsedAuthorsFromResultSet(resultSet));
            }
            return authors;
        } catch (SQLException e) {
            throw new DataProcessingException(String
                    .format("Can't find book with id = %d from database", bookId), e);
        }
    }

    private Author parsedAuthorsFromResultSet(ResultSet resultSet)
            throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getObject("id", Long.class));
        author.setName(resultSet.getString("name"));
        author.setLastname(resultSet.getString("lastname"));
        return author;
    }
}

