package pl.com.januszex.paka.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtService jwtService;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() {
        return authentication -> {
            String token = (String) authentication.getCredentials();
            if (jwtService.isTokenValid(token)) {
                var auth = new CurrentUser(token, jwtService.getAuthFromJWT(token), authentication.getDetails());
                auth.setAuthenticated(true);
                return auth;
            }
            return null;
        };
    }

    @Profile("dev")
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/pl.com.januszex.paka.security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var filter = new CookieAuthFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        http.cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui.html",
                        "/h2-console/**",
                        "/news/get/**",
                        "/user/login",
                        "/user/logout",
                        "/user/register/**",
                        "GET", "POST", "PUT", "PATCH")
                .permitAll()
                .antMatchers("/**")
                .permitAll()
                //.fullyAuthenticated()
                .and()
                .addFilter(filter);
    }
}