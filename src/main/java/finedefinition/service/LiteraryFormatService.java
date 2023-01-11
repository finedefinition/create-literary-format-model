package finedefinition.service;

import finedefinition.models.LiteraryFormat;
import java.util.List;
import java.util.Optional;

public interface LiteraryFormatService {
    List<LiteraryFormat> getAll();

    LiteraryFormat create(LiteraryFormat format);

    Optional<LiteraryFormat> get(Long id);

    LiteraryFormat update(LiteraryFormat format);

    boolean delete(Long id);
}
