package app.lab3books.controller;

import app.lab3books.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import app.lab3books.entity.Book;
import app.lab3books.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody PostBookRequest postBookRequest, UriComponentsBuilder builder) {
        Book book = Book.builder()
                .name(postBookRequest.getName())
                .author(postBookRequest.getAuthor())
                .build();
        book = bookService.create(book);
        return ResponseEntity.created(builder.pathSegment("api", "book", "{id}")
                .buildAndExpand(book.getId()).toUri()).build();
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetBooksResponse> readAll() {
        List<Book> books = bookService.findAll();
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetBookResponse> read(@PathVariable(name = "id") Long id) {
        Optional<Book> response = bookService.find(id);
        if (response.isPresent()) {
            Book book = response.get();
            return ResponseEntity.ok(GetBookResponse.builder()
                    .id(book.getId())
                    .name(book.getName())
                    .author(book.getAuthor())
                    .build());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") Long id,
                                       @RequestBody PutBookResponse bookResponse) {
        try {
            Book book = Book.builder()
                    .name(bookResponse.getName())
                    .author(bookResponse.getAuthor())
                    .build();
            bookService.update(id, book);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        Optional<Book> book = bookService.find(id);
        if (book.isPresent()) {
            bookService.delete(book.get().getId());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> addAlbumToSong(
            @RequestBody PatchBookLibraryAddResponse response,
            @PathVariable(name="id" ) Long id) {
        Optional<Book> book = bookService.find(id);
        if (book.isPresent()) {
            bookService.addLibraryToBook(response.getLibraryId(), book.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
