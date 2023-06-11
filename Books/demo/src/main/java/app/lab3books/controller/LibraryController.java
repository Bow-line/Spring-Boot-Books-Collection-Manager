package app.lab3books.controller;

import app.lab3books.dto.*;
import app.lab3books.entity.Library;
import app.lab3books.entity.Book;
import app.lab3books.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/libraries")
public class LibraryController {
    private LibraryService libraryService;
    
    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody PostLibraryRequest postLibraryRequest, UriComponentsBuilder builder) {
        Library library = Library.builder().build();
        library = libraryService.create(library);
        return ResponseEntity.created(builder.pathSegment("api", "library", "{id}")
                .buildAndExpand(library.getId()).toUri()).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        Optional<Library> library = libraryService.find(id);
        if (library.isPresent()) {
            libraryService.delete(library.get().getId());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{libraryId}/songs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetBooksResponse> readAllByLibraryId(@PathVariable(name="libraryId") Long libraryId) {
        List<Book> books = libraryService.findAllByLibraryId(libraryId);
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                GetBooksResponse.builder()
                        .books(
                                books.stream().map(
                                        (song) -> GetBookResponse.builder()
                                                .id(song.getId())
                                                .name(song.getName())
                                                .author(song.getAuthor())
                                                .build()
                                ).collect(Collectors.toList())
                        )
                        .build()
        );
    }
}

