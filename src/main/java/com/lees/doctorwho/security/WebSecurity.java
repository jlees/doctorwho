package com.lees.doctorwho.security;

import com.lees.doctorwho.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.context.annotation.Bean;


@EnableWebSecurity
@AllArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    CorsFilter corsFilter() {
        return new CorsFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager();
        http.cors().disable().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, "/api/companions").hasAuthority("upsert_companion")
                .antMatchers(HttpMethod.PUT, "/api/companions/**").hasAuthority("upsert_companion")
                .antMatchers(HttpMethod.DELETE, "/api/companions/**").hasAuthority("upsert_companion")
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .anyRequest().authenticated()
//                .and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
                .and().headers().frameOptions().sameOrigin()//allow use of frame to same origin urls
                .and()
                .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
                .addFilter(new JWTAuthenticationFilter(authenticationManager))
                .addFilter(new JWTAuthorizationFilter(authenticationManager, userDetailsService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }
}