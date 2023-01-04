package finedefinition.dao;

import finedefinition.models.LiteraryFormat;
import java.util.List;

public interface LiteraryFormatDao {
    List<LiteraryFormat> getAll();

    LiteraryFormat create(LiteraryFormat format);
}
