package com.example.demo.security;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.tools.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        var login = request.getParameter("login");
        var password = request.getParameter("password");
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        response.setStatus(200);

        // Send user info after login
        var mapper = new ObjectMapper();
        var user = (User) authResult.getPrincipal();
        var userDTO = Converter.map(user, UserDto.class);
        response.getWriter().write(mapper.writeValueAsString(userDTO));
    }
}
