package app.lab3libraries.service;

import app.lab3libraries.dto.PutLibraryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.lab3libraries.entity.Library;
import app.lab3libraries.repository.LibraryRepository;
import app.lab3libraries.event.EventLibraryRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final EventLibraryRepository eventLibraryRepository;

    @Autowired
    public LibraryService(LibraryRepository libraryRepository, EventLibraryRepository eventLibraryRepository) {
        this.libraryRepository = libraryRepository;
        this.eventLibraryRepository = eventLibraryRepository;
    }

    public Optional<Library> find(Long id) {
        return libraryRepository.findById(id);
    }

    public List<Library> findAll() {
        return libraryRepository.findAll();
    }

    @Transactional
    public Library create(Library entity) {
        eventLibraryRepository.save(entity);
        return libraryRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        eventLibraryRepository.deleteById(id);
        libraryRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, PutLibraryResponse libraryResponse) {
        find(id).ifPresentOrElse(
                (original) -> {
                    original.setName(libraryResponse.getName());
                    original.setAddress(libraryResponse.getAddress());
                    original.setPlaceInRanking(libraryResponse.getPlaceInRanking());
                },
                () -> {
                    throw new IllegalArgumentException("Cannot update library");
                }
        );
    }
}
