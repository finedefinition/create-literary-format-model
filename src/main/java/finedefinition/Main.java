package finedefinition;

import finedefinition.dao.LiteraryFormatDao;
import finedefinition.dao.LiteraryFormatDaoImpl;
import finedefinition.models.LiteraryFormat;

public class Main {
    public static void main(String[] args) {
        LiteraryFormat format = new LiteraryFormat();
        format.setTitle("Sci-Fi");
        LiteraryFormatDao literaryFormatDao = new LiteraryFormatDaoImpl();
        literaryFormatDao.create(format);
        //  System.out.println(savedFormat);
        //literaryFormatDao.delete(15L);
        literaryFormatDao.getAll().forEach(System.out::println);
        System.out.println(literaryFormatDao.get(19L));
        System.out.println(literaryFormatDao.update(format));

    }
}
















