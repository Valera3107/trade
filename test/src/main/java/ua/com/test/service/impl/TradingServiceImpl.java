package ua.com.test.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import pl.zankowski.iextrading4j.api.exception.IEXTradingException;
import pl.zankowski.iextrading4j.api.stocks.Logo;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.LogoRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchStocksRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchStocksType;
import ua.com.test.config.IextradingConfig;
import ua.com.test.model.CompanyEntity;
import ua.com.test.model.CompanyJson;
import ua.com.test.service.interfaces.TradingService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradingServiceImpl implements TradingService {
    private static final String GET_ALL_COMPANIES_URL = "https://api.iextrading.com/1.0/ref-data/symbols?filter=symbol,name";

    private final IextradingConfig iextradingConfig;

    @Override
    public List<CompanyEntity> findAll(int limit, int offset) {
        final IEXCloudClient cloudClient = iextradingConfig.getConnection();

        List<CompanyJson> list = getAllCompanySymbols();
        return list.stream().skip(offset).limit(limit).map(c -> convertIntoCompanyEntity(cloudClient, c)).collect(Collectors.toList());
    }

    @Override
    public CompanyEntity findBySymbol(String symbol) {
        IEXCloudClient cloudClient = iextradingConfig.getConnection();
        BatchStocks result = cloudClient.executeRequest(new BatchStocksRequestBuilder()
                .withSymbol(symbol)
                .addType(BatchStocksType.LOGO)
                .addType(BatchStocksType.COMPANY)
                .build());

        return CompanyEntity.builder()
                .companyName(result.getCompany().getCompanyName())
                .companySymbol(result.getCompany().getSymbol())
                .logoUrl(result.getLogo().getUrl()).build();
    }

    private List<CompanyJson> getAllCompanySymbols() {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(GET_ALL_COMPANIES_URL);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    content.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CompanyJson[] companyEntities = new Gson().fromJson(content.toString(), CompanyJson[].class);
        return List.of(companyEntities);
    }

    private CompanyEntity convertIntoCompanyEntity(IEXCloudClient cloudClient, CompanyJson c) {
        try {
            Logo logo = cloudClient.executeRequest(new LogoRequestBuilder()
                    .withSymbol(c.getSymbol())
                    .build());

            return CompanyEntity.builder()
                    .companyName(c.getName())
                    .companySymbol(c.getSymbol())
                    .logoUrl(logo.getUrl())
                    .build();
        } catch (IEXTradingException ex) {
            return null;
        }
    }
}
