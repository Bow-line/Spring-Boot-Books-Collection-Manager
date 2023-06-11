package app.lab3books.service;

import app.lab3books.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.lab3books.entity.Library;
import app.lab3books.repository.LibraryRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final BookService bookService;

    @Autowired
    public LibraryService(LibraryRepository libraryRepository, BookService bookService) {
        this.libraryRepository = libraryRepository;
        this.bookService = bookService;
    }

    public Optional<Library> find(Long id) {
        return libraryRepository.findById(id);
    }

    @Transactional
    public Library create(Library entity) {
        return libraryRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        libraryRepository.deleteById(id);
    }

    public List<Book> findAllByLibraryId(Long libraryId) {
        return bookService.findAllByLibraryId(libraryId);
    }
}
