package finedefinition;

import finedefinition.service.LiteraryFormatService;
import finedefinition.util.Injector;

public class Main {
    private static Injector injector = Injector.getInstance("finedefinition");

    public static void main(String[] args) {

        LiteraryFormatService literaryFormatService = (LiteraryFormatService)
                injector.getInstance(LiteraryFormatService.class);
        literaryFormatService.getAll().forEach(System.out::println);
    }
}
















