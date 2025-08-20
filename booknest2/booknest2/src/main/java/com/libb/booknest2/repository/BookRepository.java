package com.libb.booknest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.libb.booknest2.entities.Book;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByTitleContaining(String title);
}