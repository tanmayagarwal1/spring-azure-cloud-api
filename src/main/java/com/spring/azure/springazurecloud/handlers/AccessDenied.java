package com.spring.azure.springazurecloud.handlers;

import com.spring.azure.springazurecloud.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        ResponseDto responseDto = new ResponseDto(false);
        responseDto.addMessage("Error", "Unauthorized");
        ResponseHandler.handleResponse(httpServletResponse,
                HttpStatus.FORBIDDEN,
                responseDto);
    }
}
