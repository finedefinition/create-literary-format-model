package finedefinition.service;

import finedefinition.dao.LiteraryFormatDao;
import finedefinition.models.LiteraryFormat;

import java.util.List;
import java.util.Optional;

public class LiteraryFormatServiceImpl implements LiteraryFormatService {
    private LiteraryFormatDao literaryFormatDao;

    public LiteraryFormatServiceImpl(LiteraryFormatDao literaryFormatDao) {
        this.literaryFormatDao = literaryFormatDao;
    }
    @Override
    public List<LiteraryFormat> getAll() {
        return literaryFormatDao.getAll();
    }

    @Override
    public LiteraryFormat create(LiteraryFormat format) {
        return literaryFormatDao.create(format);
    }

    @Override
    public Optional<LiteraryFormat>  get(Long id) {
        return literaryFormatDao.get(id);
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
