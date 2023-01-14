package finedefinition.service;

import finedefinition.dao.AuthorDao;
import finedefinition.lib.Inject;
import finedefinition.lib.Service;
import finedefinition.models.Author;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Inject
    private AuthorDao authorDao;

    @Override
    public Author create(Author author) {
        return authorDao.create(author);
    }

    @Override
    public Author get(Long id) {
        return authorDao.get(id)
                .orElseThrow(() -> new NoSuchElementException("Could not get LiteraryFormat "
                + "by id = " + id));
    }

    @Override
    public List<Author> getAll() {
        return authorDao.getAll();
    }

    @Override
    public Author update(Author author) {
        return authorDao.update(author);
    }

    @Override
    public boolean delete(Long id) {
        return authorDao.delete(id);
    }
}
