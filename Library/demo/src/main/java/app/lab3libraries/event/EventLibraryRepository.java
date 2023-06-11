package app.lab3libraries.event;

import app.lab3libraries.entity.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class EventLibraryRepository {
    private RestTemplate restTemplate;

    static private final String BASE_URL = "http://localhost:8082/api/libraries/";

    @Autowired
    public EventLibraryRepository() {
        restTemplate = new RestTemplateBuilder().rootUri(BASE_URL).build();
    }

    public boolean deleteById(Long id) {
        try {
            restTemplate.delete("/{id}", id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean save(Library library) {
        try {
            restTemplate.postForLocation("/", EventPostLibraryRequest.builder().name(library.getName()).build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
