package pl.com.januszex.paka.flow.configuration;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Server;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.com.januszex.paka.security.RestTemplateAuthInterceptor;
import pl.com.januszex.paka.users.service.CurrentUserServicePort;

import java.security.SecureRandom;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final CurrentUserServicePort currentUserService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                        .exposedHeaders("Location")
                        .allowedOrigins("*");
            }
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplateBuilder
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .interceptors(new RestTemplateAuthInterceptor(currentUserService))
                .build();
    }

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }


    @Profile("dev")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseServer() throws SQLException {
        return Server.createTcpServer(
                "-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
    }
}
