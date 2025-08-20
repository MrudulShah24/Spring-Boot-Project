package com.libb.booknest2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.libb.booknest2.entities.Loan;
import com.libb.booknest2.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        if (loan != null) {
            return ResponseEntity.ok(loan);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/member/{memberId}")
    @PreAuthorize("#memberId == principal.id or hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public List<Loan> getLoansByMember(@PathVariable Long memberId) {
        return loanService.getLoansByMember(memberId);
    }

    @GetMapping("/active")
    public List<Loan> getActiveLoans() {
        return loanService.getActiveLoans();
    }

    @PreAuthorize("hasRole('ROLE_MEMBER') or hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan, @RequestParam Long bookId, @RequestParam Long memberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user: " + authentication.getName());  // Log the authenticated user
        return ResponseEntity.ok(loanService.createLoan(loan, bookId, memberId));
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnLoan(id);
        if (returnedLoan != null) {
            return ResponseEntity.ok(returnedLoan);
        }
        return ResponseEntity.notFound().build();
    }
}
