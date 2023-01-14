package finedefinition.service;

import finedefinition.lib.Service;
import finedefinition.models.Author;
import finedefinition.models.Book;
import java.util.List;

@Service
public interface BookService {

    Book create(Book book);

    List<Book> getAll();

    boolean delete(Long id);

    Book get(Long id);

    Book update(Book book);

    void addAuthorToBook(Author author, Book book);

    void removeAuthorFromBook(Author author, Book book);

    List<Book> getAllByAuthor(Long authorId);
}
