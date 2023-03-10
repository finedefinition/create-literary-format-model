package finedefinition.service;

import finedefinition.dao.LiteraryFormatDao;
import finedefinition.lib.Inject;
import finedefinition.lib.Service;
import finedefinition.models.LiteraryFormat;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LiteraryFormatServiceImpl implements LiteraryFormatService {
    @Inject
    private LiteraryFormatDao literaryFormatDao;

    @Override
    public List<LiteraryFormat> getAll() {
        return literaryFormatDao.getAll();
    }

    @Override
    public LiteraryFormat create(LiteraryFormat format) {
        return literaryFormatDao.create(format);
    }

    @Override
    public LiteraryFormat get(Long id) {
        return literaryFormatDao.get(id)
                .orElseThrow(() -> new NoSuchElementException("Could not get LiteraryFormat "
                + "by id = " + id));
    }

    @Override
    public LiteraryFormat update(LiteraryFormat format) {
        return literaryFormatDao.update(format);
    }

    @Override
    public boolean delete(Long id) {
        return literaryFormatDao.delete(id);
    }
}
