package com.spring.azure.springazurecloud.configuration.advice;

import com.spring.azure.springazurecloud.dto.response.ResponseDto;
import com.spring.azure.springazurecloud.exception.acr.ContainerRegistryException;
import com.spring.azure.springazurecloud.exception.aks.AksException;
import com.spring.azure.springazurecloud.exception.apim.ApimException;
import com.spring.azure.springazurecloud.exception.client.UserCreationException;
import com.spring.azure.springazurecloud.exception.resource.ResourceGroupException;
import com.spring.azure.springazurecloud.exception.storage.StorageAccountException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(UserCreationException.class)
    public void handleUserCreationException(HttpServletResponse response,
                                            UserCreationException e){
        ResponseDto responseDto = new ResponseDto(false);
        responseDto.addMessage("Error",e.getMessage());
        ResponseHandler.handleResponse(response, HttpStatus.BAD_REQUEST, responseDto);
    }

    @ExceptionHandler(AksException.class)
    public void handleAksException(HttpServletResponse response,
                                            AksException e){
        ResponseDto responseDto = new ResponseDto(false);
        responseDto.addMessage("Error",e.getMessage());
        ResponseHandler.handleResponse(response, HttpStatus.BAD_REQUEST, responseDto);
    }

    @ExceptionHandler(ResourceGroupException.class)
    public void handleResourceGroupException(HttpServletResponse response,
                                   ResourceGroupException e){
        ResponseDto responseDto = new ResponseDto(false);
        responseDto.addMessage("Error",e.getMessage());
        ResponseHandler.handleResponse(response, HttpStatus.BAD_REQUEST, responseDto);
    }

    @ExceptionHandler(StorageAccountException.class)
    public void handleStorageAccountException(HttpServletResponse response,
                                             StorageAccountException e){
        ResponseDto responseDto = new ResponseDto(false);
        responseDto.addMessage("Error",e.getMessage());
        ResponseHandler.handleResponse(response, HttpStatus.BAD_REQUEST, responseDto);
    }

    @ExceptionHandler(ApimException.class)
    public void handleApimException(HttpServletResponse response,
                                              ApimException e){
        ResponseDto responseDto = new ResponseDto(false);
        responseDto.addMessage("Error",e.getMessage());
        ResponseHandler.handleResponse(response, HttpStatus.BAD_REQUEST, responseDto);
    }

    @ExceptionHandler(ContainerRegistryException.class)
    public void handleContainerRegistryException(HttpServletResponse response,
                                    ContainerRegistryException e){
        ResponseDto responseDto = new ResponseDto(false);
        responseDto.addMessage("Error",e.getMessage());
        ResponseHandler.handleResponse(response, HttpStatus.BAD_REQUEST, responseDto);
    }
}
