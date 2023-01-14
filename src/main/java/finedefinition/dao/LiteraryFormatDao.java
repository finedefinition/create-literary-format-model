package finedefinition.dao;

import finedefinition.models.LiteraryFormat;
import java.util.List;
import java.util.Optional;

//Manufacturer
public interface LiteraryFormatDao {
    List<LiteraryFormat> getAll();

    LiteraryFormat create(LiteraryFormat format);

    boolean delete(Long id);

    Optional<LiteraryFormat> get(Long id);

    LiteraryFormat update(LiteraryFormat format);
}
