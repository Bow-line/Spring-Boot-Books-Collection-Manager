package app.lab3books.repository;

import app.lab3books.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LibraryRepository extends JpaRepository<Library, Long> {}
