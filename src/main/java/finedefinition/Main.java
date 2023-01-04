package finedefinition;

import finedefinition.dao.LiteraryFormatDao;
import finedefinition.dao.LiteraryFormatDaoImpl;
import finedefinition.models.LiteraryFormat;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LiteraryFormatDao literaryFormatDao = new LiteraryFormatDaoImpl();
        List<LiteraryFormat> allFormats = literaryFormatDao.getAll();
        for (LiteraryFormat format : allFormats) {
            System.out.println(format);
        }
    }
}















