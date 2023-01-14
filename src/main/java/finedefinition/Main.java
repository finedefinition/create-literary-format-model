package finedefinition;

import finedefinition.models.Author;
import finedefinition.models.Book;
import finedefinition.models.LiteraryFormat;
import finedefinition.service.AuthorService;
import finedefinition.service.BookService;
import finedefinition.service.LiteraryFormatService;
import finedefinition.util.Injector;
import java.math.BigDecimal;
import java.util.List;

public class Main {
    private static final Injector injector = Injector.getInstance("finedefinition");

    public static void main(String[] args) {

        LiteraryFormat literaryFormat1 = new LiteraryFormat(null, "Detective");
        LiteraryFormat literaryFormat2 = new LiteraryFormat(null, "Dystopia");
        LiteraryFormat literaryFormat3 = new LiteraryFormat(null, "Sci-Fi");
        LiteraryFormatService literaryFormatService
                = (LiteraryFormatService) injector.getInstance(LiteraryFormatService.class);
        literaryFormatService.create(literaryFormat1);
        literaryFormatService.create(literaryFormat2);
        literaryFormatService.create(literaryFormat3);
        Author author1 = new Author(null, "Arthur Conan ","Doyle ");
        Author author2 = new Author(null, "George ","Orwell ");
        Author author3 = new Author(null, "Clifford D. ","Simak ");
        Author author4 = new Author(null, "James ","Blish ");
        Author author5 = new Author(null, "Judith A. ","Lawrence ");
        AuthorService authorService
                = (AuthorService) injector.getInstance(AuthorService.class);
        authorService.create(author1);
        authorService.create(author2);
        authorService.create(author3);
        authorService.create(author4);
        authorService.create(author5);
        Book book1 = new Book(null, "Sherlock Holmes",
                new BigDecimal(100),literaryFormat1, List.of(author1));
        Book book2 = new Book(null, "1984",
                new BigDecimal(120),literaryFormat2, List.of(author2));
        Book book3 = new Book(null, "Way Station",
                new BigDecimal(90),literaryFormat3, List.of(author3));
        Book book4 = new Book(null, "Star Trek",
                new BigDecimal(130),literaryFormat3, List.of(author4, author5));
        BookService bookService
                = (BookService) injector.getInstance(BookService.class);
        bookService.create(book1);
        bookService.create(book2);
        bookService.create(book3);
        bookService.create(book4);

    }
}
















