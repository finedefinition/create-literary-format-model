package finedefinition;

import finedefinition.dao.LiteraryFormatDao;
import finedefinition.dao.LiteraryFormatDaoImpl;
import finedefinition.models.LiteraryFormat;
import finedefinition.service.LiteraryFormatService;
import finedefinition.util.Injector;

public class Main {
    private static Injector injector = Injector.getInstance("finedefinition");
    public static void main(String[] args) {
        LiteraryFormatService literaryFormatService = (LiteraryFormatService)
                injector.getInstance(LiteraryFormatService.class);
        literaryFormatService.getAll().forEach(System.out::println);

//        LiteraryFormat format = new LiteraryFormat();
//        format.setTitle("Sci-Fi");
//        LiteraryFormatDao literaryFormatDao = new LiteraryFormatDaoImpl();
//        literaryFormatDao.create(format);
//        //  System.out.println(savedFormat);
//        //literaryFormatDao.delete(15L);
//        literaryFormatDao.getAll().forEach(System.out::println);
//        System.out.println(literaryFormatDao.get(19L));
//        System.out.println(literaryFormatDao.update(format));

    }
}
















