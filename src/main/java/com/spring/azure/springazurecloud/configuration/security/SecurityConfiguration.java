package com.spring.azure.springazurecloud.configuration.security;

import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.filters.AuthenticationFilter;
import com.spring.azure.springazurecloud.filters.AuthorizationFilter;
import com.spring.azure.springazurecloud.handlers.AccessDenied;
import com.spring.azure.springazurecloud.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter customAuthFilter = new AuthenticationFilter(authenticationManagerBean(),jwtService);
        customAuthFilter.setFilterProcessesUrl(RestRoutes.CLIENT.LOGIN);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(RestRoutes.CLIENT.LOGIN+"/?^^").permitAll()
                .antMatchers(RestRoutes.CLIENT.LOGIN+"/**").permitAll()
                .antMatchers(RestRoutes.ROOT_CONTEXT + RestRoutes.CLIENT.ROOT_CONTEXT+RestRoutes.CLIENT.REGISTER).permitAll()
                .antMatchers(RestRoutes.ROOT_CONTEXT + RestRoutes.CLIENT.ROOT_CONTEXT+RestRoutes.CLIENT.REFRESH_TOKEN).permitAll()
                .antMatchers(RestRoutes.ROOT_CONTEXT + RestRoutes.CLIENT.ROOT_CONTEXT+RestRoutes.CLIENT.CACHE_INVALIDATE).hasAnyAuthority("ADMIN")
                .and().authorizeRequests().anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(new AccessDenied());
        http.addFilter(new AuthenticationFilter(authenticationManagerBean(),jwtService));
        http.addFilterBefore(new AuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
    }
}
