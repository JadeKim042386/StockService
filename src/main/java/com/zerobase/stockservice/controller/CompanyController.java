package com.zerobase.stockservice.controller;

import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.constants.CacheKey;
import com.zerobase.stockservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;
    private final RedisCacheManager redisCacheManager;

    /**
     * keyword로 시작하는 모든 기업 조회
     */
    @GetMapping("/autocomplete")
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<List<String>> autocomplete(@RequestParam String keyword) {
        return ResponseEntity.ok(companyService.getCompanyNamesByKeyword(keyword));
    }

    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<Page<CompanyDto>> searchCompany(final Pageable pageable) {
        return ResponseEntity.ok(companyService.findAllCompany(pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<CompanyDto> addCompany(@RequestBody CompanyDto request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            //TODO: 예외 처리
            throw new RuntimeException("ticker is empty");
        }
        CompanyDto companyDto = companyService.save(ticker);
        return ResponseEntity.ok(companyDto);
    }
    
    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = companyService.deleteCompany(ticker);
        clearFinanceCache(companyName);
        return ResponseEntity.ok(companyName);
    }

    public void clearFinanceCache(String companyName) {
        Cache cache = redisCacheManager.getCache(CacheKey.KEY_FINANCE);
        if (Objects.isNull(cache)) {
            //TODO: 예외 처리
            throw new NullPointerException("캐시가 없습니다.");
        }
        cache.evict(companyName);
    }
}
