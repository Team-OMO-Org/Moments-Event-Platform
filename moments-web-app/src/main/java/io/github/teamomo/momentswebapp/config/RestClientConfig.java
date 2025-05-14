package io.github.teamomo.momentswebapp.config;

import io.github.teamomo.momentswebapp.client.BackendClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {
    @Value("${backend-service.url}")
    private String backendUrl;

    /**
     * Creates a RestClient bean for the Inventory service.
     * Binding the client to the InventoryClient interface.
     *
     * @return a RestClient instance configured with the inventory service URL.
     */
    @Bean
    public BackendClient backendClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(backendUrl)
                .requestFactory(getClientRequestFactory())  // to define timeouts
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(BackendClient.class);
    }

    // define timeouts for RestClient connection
    private ClientHttpRequestFactory getClientRequestFactory() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(3))
                .withReadTimeout(Duration.ofSeconds(3));
        return ClientHttpRequestFactories.get(settings);
    }
}
