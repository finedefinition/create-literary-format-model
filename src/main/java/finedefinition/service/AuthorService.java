package finedefinition.service;

import finedefinition.models.Author;
import java.util.List;

public interface AuthorService {
    Author create(Author author);

    Author get(Long id);

    List<Author> getAll();

    Author update(Author author);

    boolean delete(Long id);
}
