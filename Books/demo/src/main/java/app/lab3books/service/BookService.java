package app.lab3books.service;

import app.lab3books.entity.Library;
import app.lab3books.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.lab3books.entity.Book;
import app.lab3books.repository.BookRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    @Autowired
    public BookService(BookRepository bookRepository, LibraryRepository libraryRepository) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
    }

    public Optional<Book> find(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public List<Book> findAllByLibraryId(Long libraryId) {
        return bookRepository.findAllByLibraryId(libraryId);
    }

    @Transactional
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public void update(Long id, Book book) {
        find(id).ifPresentOrElse(
                (original) -> {
                    original.setName(book.getName());
                    original.setAuthor(book.getAuthor());
                },
                () -> {
                    throw new IllegalArgumentException("Cannot update book");
                }
        );
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void addLibraryToBook(Long libraryId, Book book) {
        Optional<Library> library = libraryRepository.findById(libraryId);
        library.ifPresentOrElse(
                book::setLibrary,
                () -> {
                    throw new IllegalArgumentException("Cannot add book to library");
                });
    }
}
