package com.libb.booknest2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libb.booknest2.entities.Book;
import com.libb.booknest2.entities.Loan;
import com.libb.booknest2.entities.User;
import com.libb.booknest2.repository.BookRepository;
import com.libb.booknest2.repository.LoanRepository;
import com.libb.booknest2.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).get();
    }

    @Transactional
    public Loan createLoan(Loan loan, Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId).get();
        User member = userRepository.findById(memberId).get();

        loan.setBook(book);
        loan.setUser(member);
        loan.setLoanDate(new Date());
        loan.setStatus("ACTIVE");
        
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).get();
        loan.setReturnDate(new Date());
        loan.setStatus("RETURNED");
        
        Book book = loan.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);
        
        return loanRepository.save(loan);
    }

    public List<Loan> getLoansByMember(Long memberId) {
        return loanRepository.findByUserId(memberId);
    }

    public List<Loan> getActiveLoans() {
        return loanRepository.findByStatus("ACTIVE");
    }
}