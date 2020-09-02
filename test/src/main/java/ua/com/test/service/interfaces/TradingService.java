package ua.com.test.service.interfaces;

import ua.com.test.model.CompanyEntity;

import java.util.List;

public interface TradingService {
    List<CompanyEntity> findAll(int limit, int offset);

    CompanyEntity findBySymbol(String symbol);
}
