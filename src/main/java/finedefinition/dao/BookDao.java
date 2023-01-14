package finedefinition.dao;

import finedefinition.models.Book;
import java.util.List;
import java.util.Optional;

//Car
public interface BookDao {
    Book create(Book book);

    List<Book> getAll();

    boolean delete(Long id);

    Optional<Book> get(Long id);

    Book update(Book book);

    List<Book> getAllByAuthor(Long authorId);
}
