package finedefinition.service;

import finedefinition.dao.BookDao;
import finedefinition.lib.Inject;
import finedefinition.lib.Service;
import finedefinition.models.Author;
import finedefinition.models.Book;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {
    @Inject
    private BookDao bookDao;

    @Override
    public Book create(Book book) {
        return bookDao.create(book);
    }

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

    @Override
    public boolean delete(Long id) {
        return bookDao.delete(id);
    }

    @Override
    public Book get(Long id) {
        return bookDao.get(id)
                .orElseThrow(() -> new NoSuchElementException("Could not get LiteraryFormat "
                + "by id = " + id));
    }

    @Override
    public Book update(Book book) {
        return bookDao.update(book);
    }

    @Override
    public void addAuthorToBook(Author author, Book book) {

    }

    @Override
    public void removeAuthorFromBook(Author author, Book book) {

    }

    @Override
    public List<Book> getAllByAuthor(Long authorId) {
        return null;
    }
}
