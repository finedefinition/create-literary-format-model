package finedefinition.dao;

import finedefinition.models.Author;
import java.util.List;
import java.util.Optional;

//Driver
public interface AuthorDao {
    Author create(Author author);

    Optional<Author> get(Long id);

    List<Author> getAll();

    Author update(Author author);

    boolean delete(Long id);

}
