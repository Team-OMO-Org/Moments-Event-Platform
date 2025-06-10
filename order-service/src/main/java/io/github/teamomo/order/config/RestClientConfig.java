package io.github.teamomo.order.config;

import io.github.teamomo.order.client.CustomerClient;
import io.github.teamomo.order.client.MomentClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {
    @Value("${backend-service.url}")
    private String momentClientUrl;
    @Value("http://localhost:8083")
    private String customerClientUrl;

    /**
     * Creates a RestClient bean for the Inventory service.
     * Binding the client to the InventoryClient interface.
     *
     * @return a RestClient instance configured with the inventory service URL.
     */
    @Bean
    public MomentClient momentClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(momentClientUrl)
                .requestFactory(getClientRequestFactory())  // to define timeouts
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(MomentClient.class);
    }

    @Bean
    public CustomerClient customerClient() {
        RestClient restClient = RestClient.builder()
            .baseUrl(customerClientUrl)
            .requestFactory(getClientRequestFactory())  // to define timeouts
            .requestInterceptor((request, body, execution) -> { // to add JWT token to request
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                    String token = jwtAuth.getToken().getTokenValue();
                    request.getHeaders().setBearerAuth(token);
                }
                return execution.execute(request, body);
            })
            .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(CustomerClient.class);
    }

    // define timeouts for RestClient connection
    private ClientHttpRequestFactory getClientRequestFactory() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(3))
                .withReadTimeout(Duration.ofSeconds(3));
        return ClientHttpRequestFactories.get(settings);
    }
}
