package com.spring.azure.springazurecloud.controllers.client;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.dto.response.ResponseDto;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.models.client.Client;
import com.spring.azure.springazurecloud.service.security.ClientService;
import com.spring.azure.springazurecloud.service.security.JwtService;
import com.spring.azure.springazurecloud.utils.GlobalControllerHelper;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import com.spring.azure.springazurecloud.utils.LogHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping(RestRoutes.ROOT_CONTEXT + RestRoutes.CLIENT.ROOT_CONTEXT)
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final JwtService jwtService;

    @PostMapping(RestRoutes.CLIENT.REGISTER)
    public void registerClient(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Client client = JsonUtil.fromJson(body, Client.class).orElse(null);
        clientService.postClient(client);
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", "client created");
        ResponseHandler.handleResponse(response, HttpStatus.ACCEPTED, responseDto);
    }

    @GetMapping(RestRoutes.CLIENT.PROFILE)
    public void getClientProfile(HttpServletRequest request, HttpServletResponse response){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        Client client = clientService.getClient(username);
        GlobalControllerHelper.handleModelControllerResponse(client, response);
    }

    @GetMapping(RestRoutes.CLIENT.REFRESH_TOKEN)
    public void refreshClientAccessToken(HttpServletRequest request, HttpServletResponse response){
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Optional<ResponseDto> responseDto = Optional.empty();
        if(authHeader != null && authHeader.startsWith(Constants.SECURITY.AUTH_HEADER)){
            responseDto = jwtService.handleRefreshTokenRequest(authHeader,
                    request.getRequestURL().toString(),
                    Constants.SECURITY.ACCESS_TOKEN_EXPIRY_MIN);
        }

        ResponseHandler.handleResponse(response,HttpStatus.OK, responseDto.get());
    }

    @DeleteMapping(RestRoutes.CLIENT.CACHE_INVALIDATE)
    public void invalidateClientCache(HttpServletRequest request, HttpServletResponse response){
        clientService.clearCache();
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", "Client cache is cleared");
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);
    }

    @DeleteMapping()
    public void deleteClient(HttpServletRequest request, HttpServletResponse response){
        clientService.deleteClient(request.getUserPrincipal().getName());
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", "Client Successfully deleted");
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);
    }
}
