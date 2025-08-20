package com.libb.booknest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.libb.booknest2.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
