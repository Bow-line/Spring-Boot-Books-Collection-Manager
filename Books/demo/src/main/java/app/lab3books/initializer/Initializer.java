package app.lab3books.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import app.lab3books.entity.Library;
import app.lab3books.entity.Book;
import app.lab3books.service.LibraryService;
import app.lab3books.service.BookService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class Initializer {
    private final LibraryService libraryService;
    private final BookService bookService;

    @Autowired
    public Initializer(LibraryService libraryService, BookService bookService) {
        this.libraryService = libraryService;
        this.bookService = bookService;
    }

    @PostConstruct
    @Transactional
    private synchronized void init() {
        Library library1 = Library.builder().build();
        Library library2 = Library.builder().build();
        Library library3 = Library.builder().build();

        libraryService.create(library1);
        libraryService.create(library2);
        libraryService.create(library3);

        Book book1 = Book.builder()
                .name("Lord of the rings")
                .author("J.R.R Tolkien")
                .library(library1)
                .build();
        Book book2 = Book.builder()
                .name("Harry Potter and Prisoner of Azkaban")
                .author("J.K.R Rowling")
                .library(library2)
                .build();
        Book book3 = Book.builder()
                .name("A.B.C Murders")
                .author("Agatha Christie")
                .library(library3)
                .build();
        Book book4 = Book.builder()
                .name("Horrid Henry")
                .author("Francesca Simon")
                .library(library1)
                .build();

        bookService.create(book1);
        bookService.create(book2);
        bookService.create(book3);
        bookService.create(book4);
    }
}
