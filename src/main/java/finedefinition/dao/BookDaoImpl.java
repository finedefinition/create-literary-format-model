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
                book = parsedBookWithLiteraryFormat(resultSet);
            }

        } catch (SQLException e) {
            throw new DataProcessingException(String
                    .format("Can't find book with id = %d from database", id), e);
        }
        if (book != null) {
            book.setAuthors(getAuthors(book));
        }
        return Optional.ofNullable(book);
    }

    private Book parsedBookWithLiteraryFormat(ResultSet resultSet)
            throws SQLException {
        Book book = new Book();
        book.setTitle(resultSet.getString("title"));
        book.setPrice(resultSet.getBigDecimal("price"));
        LiteraryFormat literaryFormat = new LiteraryFormat();
        literaryFormat.setId(resultSet
                .getObject("literary_format_id", Long.class));
        literaryFormat.setFormat(resultSet.getString("format"));
        book.setFormat(literaryFormat);
        book.setId(resultSet.getObject("id", Long.class));
        return book;
    }

    private List<Author> getAuthors(Book book) {
        String query = "SELECT * FROM authors INNER JOIN books_authors "
                + "ON books_authors.author_id = authors.id "
                + "WHERE books_authors.book_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(query)) {
            statement.setLong(1, book.getId());
            ResultSet resultSet = statement.executeQuery();
            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                Author author = parsedAuthorsFromResultSet(resultSet);
                authors.add(author);
            }
            return authors;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get authors from book " + book, e);
        }
    }

    @Override
    public List<Book> getAll() {
        String query = "SELECT * FROM books INNER JOIN literary_formats "
                + "ON books.literary_format_id = literary_formats.id "
                + "WHERE books.is_deleted = FALSE";
        List<Book> books = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                books.add(parsedBookWithLiteraryFormat(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all books form DB", e);
        }
        for (Book book : books) {
            book.setAuthors(getAuthors(book));
        }
        return books;
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
    public Book update(Book book) {
        String query = "UPDATE books SET title = ?, price = ?, literary_format_id = ? "
                + "WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());
            statement.setLong(3, book.getFormat().getId());
            statement.setLong(4, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update a car " + book, e);
        }
        deleteAuthorsFromBook(book);
        insertAuthors(book);
        return book;
    }

    private void deleteAuthorsFromBook(Book book) {
        String query = "DELETE FROM books_authors WHERE book_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete authors from book  " + book, e);
        }
    }

    @Override
    public List<Book> getAllByAuthor(Long authorId) {
        String query = "SELECT * FROM books INNER JOIN literary_formats "
                + "ON books.literary_format_id = literary_formats.id "
                + "INNER JOIN books_authors "
                + "ON books.id = books_authors.book_id "
                + "WHERE books_authors.author_id = ?;";
        List<Book> books = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, authorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                books.add(parsedBookWithLiteraryFormat(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get books by author id " + authorId, e);
        }
        for (Book book : books) {
            book.setAuthors(getAuthors(book));
        }
        return books;
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

