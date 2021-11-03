package pl.com.januszex.paka.flow.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("services.rest.urls")
@Data
public class RestServiceUrls {
    private String pakaWarehouseApiUrl;
    private String pakaUsersApiUrl;
}
