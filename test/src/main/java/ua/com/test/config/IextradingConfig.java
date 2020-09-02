package ua.com.test.config;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Value;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXTradingClient;

@Service
public class IextradingConfig {
    @Value("${iextrading.secretToken}")
    private String secretToken;

    @Value("${iextrading.token}")
    private String publishableToken;

    public IEXCloudClient getConnection() {
        return IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_BETA_SANDBOX,
                new IEXCloudTokenBuilder()
                        .withPublishableToken(publishableToken)
                        .withSecretToken(secretToken)
                        .build());
    }
}
