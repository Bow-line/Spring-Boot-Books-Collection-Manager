package app.lab3libraries.controller;

import app.lab3libraries.dto.*;
import app.lab3libraries.entity.Library;
import app.lab3libraries.service.LibraryService;
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
        Library library = Library.builder()
                .name(postLibraryRequest.getName())
                .address(postLibraryRequest.getAddress())
                .placeInRanking(postLibraryRequest.getPlaceInRanking())
                .build();
        library = libraryService.create(library);
        return ResponseEntity.created(builder.pathSegment("api", "library", "{id}")
                .buildAndExpand(library.getId()).toUri()).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetLibraryResponse> read(@PathVariable(name = "id") Long id) {
        Optional<Library> response = libraryService.find(id);
        if (response.isPresent()) {
            Library library = response.get();
            return ResponseEntity.ok(GetLibraryResponse.builder()
                    .id(library.getId())
                    .name(library.getName())
                    .address(library.getAddress())
                    .placeInRanking(library.getPlaceInRanking())
                    .build());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetLibrariesResponse> readAll() {
        List<Library> libraries = libraryService.findAll();
        return ResponseEntity.ok(
                GetLibrariesResponse.builder()
                        .libraries(
                                libraries.stream().map(
                                        (library) -> GetLibraryResponse.builder()
                                                .id(library.getId())
                                                .name(library.getName())
                                                .address(library.getAddress())
                                                .placeInRanking(library.getPlaceInRanking())
                                                .build()
                                ).collect(Collectors.toList())
                        )
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") Long id,
                                       @RequestBody PutLibraryResponse libraryResponse) {
        try {
            Library library = Library.builder()
                    .name(libraryResponse.getName())
                    .address(libraryResponse.getAddress())
                    .placeInRanking(libraryResponse.getPlaceInRanking())
                    .build();
            libraryService.update(id, libraryResponse);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
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
}

