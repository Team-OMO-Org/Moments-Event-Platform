package io.github.teamomo.momentswebapp.config;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import io.github.teamomo.momentswebapp.client.MomentClient;
import io.github.teamomo.momentswebapp.client.MomentClientPublic;
import io.github.teamomo.momentswebapp.client.OrderClient;
import io.github.teamomo.momentswebapp.client.CustomerClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {
    @Value("${backend-service.url}")
    private String backendUrl;
    @Value("${spring.security.oauth2.client.registration.moments-web-app.client-id}")
    private String clientId;

    /**
     * Creates a RestClient bean for the Inventory service.
     * Binding the client to the InventoryClient interface.
     *
     * @return a RestClient instance configured with the inventory service URL.
     */
    @Bean
    public MomentClientPublic momentClientPublic() {

        RestClient restClient = RestClient.builder()
                .baseUrl(backendUrl)
                .requestFactory(getClientRequestFactory())  // to define timeouts
                .requestInterceptor(new LoggingInterceptor()) // Add the logging interceptor
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(MomentClientPublic.class);
    }

    @Bean
    public MomentClient momentClient(HttpServiceProxyFactory factory) {
        return factory.createClient(MomentClient.class);
    }

    @Bean
    public OrderClient orderClient(HttpServiceProxyFactory factory) {
        return factory.createClient(OrderClient.class);
    }

    @Bean
    public CustomerClient customerClient(HttpServiceProxyFactory factory) {
        return factory.createClient(CustomerClient.class);
    }

    @Bean
    public HttpServiceProxyFactory getHttpServiceProxyFactory(
        @Qualifier("authorizedClientManager") OAuth2AuthorizedClientManager manager) {
        OAuth2ClientHttpRequestInterceptor oauth2Interceptor =
            new OAuth2ClientHttpRequestInterceptor(manager);

        RestClient restClient = RestClient.builder()
                .baseUrl(backendUrl)
                .requestFactory(getClientRequestFactory())  // to define timeouts
                .requestInterceptor(oauth2Interceptor)  // adds JWT token to the request
                .requestInterceptor(new LoggingInterceptor()) // Add the logging interceptor
                .defaultRequest(r ->
                    r.attributes(clientRegistrationId(clientId))
                )
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory;
    }

    // define timeouts for RestClient connection
    private ClientHttpRequestFactory getClientRequestFactory() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(3))
                .withReadTimeout(Duration.ofSeconds(3));
        return ClientHttpRequestFactories.get(settings);
    }
}
