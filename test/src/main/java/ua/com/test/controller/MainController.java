package ua.com.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.test.controller.dto.CompanyDto;
import ua.com.test.model.CompanyEntity;
import ua.com.test.service.interfaces.TradingService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class MainController {

    private final TradingService tradingService;

    @GetMapping
    public ResponseEntity<List<CompanyDto>> list(@RequestParam(defaultValue = "0") int offset,
                                                 @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(tradingService.findAll(limit, offset).stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CompanyDto> getTaskById(@PathVariable String symbol) {
        return ResponseEntity.ok(convertToDto(tradingService.findBySymbol(symbol)));
    }

    private CompanyDto convertToDto(CompanyEntity companyEntity) {
        return CompanyDto.builder()
                .name(companyEntity.getCompanyName())
                .symbol(companyEntity.getCompanySymbol())
                .url(companyEntity.getLogoUrl())
                .build();
    }


}
