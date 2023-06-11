package app.lab3libraries.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import app.lab3libraries.entity.Library;
import app.lab3libraries.service.LibraryService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class Initializer {
    private final LibraryService libraryService;

    @Autowired
    public Initializer(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostConstruct
    @Transactional
    private synchronized void init() {
        Library library1 = Library.builder()
                .name("Achilles Library")
                .address("London")
                .placeInRanking(8)
                .build();
        Library library2 = Library.builder()
                .name("Dream Library")
                .address("New York")
                .placeInRanking(10)
                .build();
        Library library3 = Library.builder()
                .name("Amour Library")
                .address("Paris")
                .placeInRanking(4)
                .build();

        libraryService.create(library1);
        libraryService.create(library2);
        libraryService.create(library3);
    }
}
