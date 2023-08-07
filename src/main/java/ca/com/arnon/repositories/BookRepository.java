package ca.com.arnon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.com.arnon.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
