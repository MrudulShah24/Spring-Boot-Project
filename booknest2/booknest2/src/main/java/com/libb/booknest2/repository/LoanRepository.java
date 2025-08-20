package com.libb.booknest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.libb.booknest2.entities.Loan;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByBookId(Long bookId);
    List<Loan> findByStatus(String status);
    List<Loan> findByUserId(Long userId);
}
