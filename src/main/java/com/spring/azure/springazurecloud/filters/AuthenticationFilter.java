package com.spring.azure.springazurecloud.filters;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.dto.token.AccessTokenDto;
import com.spring.azure.springazurecloud.dto.response.ResponseDto;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter(Constants.SECURITY.USERNAME_PARAM);
        String password = request.getParameter(Constants.SECURITY.PASSWORD_PARAM);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AccessTokenDto accessTokenDto =
                jwtService.generateAccessAndRefreshTokens(
                        request.getRequestURL().toString(),
                        authResult,
                        Constants.SECURITY.ACCESS_TOKEN_EXPIRY_MIN
                );
        ResponseHandler.handleResponse(response, HttpStatus.ACCEPTED, accessTokenDto);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseDto responseDto = new ResponseDto(false);
        responseDto.addMessage("Error", "Invalid Credentials");
        ResponseHandler.handleResponse(response, HttpStatus.FORBIDDEN, responseDto);
    }
}
