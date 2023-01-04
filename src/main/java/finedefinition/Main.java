package finedefinition;

import finedefinition.dao.LiteraryFormatDao;
import finedefinition.dao.LiteraryFormatDaoImpl;
import finedefinition.models.LiteraryFormat;

public class Main {
    public static void main(String[] args) {
        LiteraryFormat format = new LiteraryFormat();
        format.setTitle("Proza");
        LiteraryFormatDao literaryFormatDao = new LiteraryFormatDaoImpl();
        literaryFormatDao.create(format);
        literaryFormatDao.getAll().forEach(System.out::println);
    }
}
















