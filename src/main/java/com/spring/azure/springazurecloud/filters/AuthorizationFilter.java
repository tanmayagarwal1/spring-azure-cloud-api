package com.spring.azure.springazurecloud.filters;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.dto.ResponseDto;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.service.security.JwtService;
import com.spring.azure.springazurecloud.utils.LogHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals(RestRoutes.CLIENT.LOGIN) ||
                request.getServletPath().equals(RestRoutes.ROOT_CONTEXT + RestRoutes.CLIENT.ROOT_CONTEXT+RestRoutes.CLIENT.REGISTER) ||
                request.getServletPath().equals(RestRoutes.ROOT_CONTEXT + RestRoutes.CLIENT.ROOT_CONTEXT+RestRoutes.CLIENT.REFRESH_TOKEN)){
            filterChain.doFilter(request, response);
        }
        else{
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(authHeader != null && authHeader.startsWith(Constants.SECURITY.AUTH_HEADER)){
                try{
                    jwtService.authorizeJwt(authHeader);
                    filterChain.doFilter(request, response);
                }catch (Exception e){
                    LogHelper.logError("Exception", e);
                    ResponseDto responseDto = new ResponseDto(false);
                    responseDto.addMessage("Exception", e.getMessage());
                    ResponseHandler.handleResponse(response, HttpStatus.FORBIDDEN, responseDto);
                }
            }
            else {
                ResponseDto responseDto = new ResponseDto(false);
                responseDto.addMessage("Error", "Unauthorized ! Try Login");
                ResponseHandler.handleResponse(response,
                        HttpStatus.FORBIDDEN,
                        responseDto);
            }
        }
    }
}
