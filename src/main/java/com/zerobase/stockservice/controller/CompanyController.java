package com.zerobase.stockservice.controller;

import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return null;
    }

    @GetMapping
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            //TODO: 예외 처리
            throw new RuntimeException("ticker is empty");
        }
        return ResponseEntity.ok(companyService.save(ticker));
    }
    
    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
