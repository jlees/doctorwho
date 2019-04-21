package com.lees.doctorwho.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.lees.doctorwho.security.SecurityConstants.HEADER_STRING;
import static com.lees.doctorwho.security.SecurityConstants.SECRET;
import static com.lees.doctorwho.security.SecurityConstants.TOKEN_PREFIX;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authManager,
                                  UserDetailsService userDetailsService) {
        super(authManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().
                verify(token.replace(TOKEN_PREFIX, "")).getSubject();
            if (user != null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user);
                    if (userDetails != null) {
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        if (userDetails.getAuthorities() != null) {
                            for (GrantedAuthority authority : userDetails.getAuthorities()) {
                                authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
                            }
                        }
                        return new UsernamePasswordAuthenticationToken(user, null, authorities);
                    }
                } catch (UsernameNotFoundException ex) {
                }
            }
            return null;
        }
        return null;
    }
}
