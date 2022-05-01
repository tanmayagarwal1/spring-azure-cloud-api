package com.spring.azure.springazurecloud.controllers;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.dto.ResponseDto;
import com.spring.azure.springazurecloud.exception.SubscriptionCreationException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.models.directory.Subscription;
import com.spring.azure.springazurecloud.service.subscription.SubscriptionService;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequestMapping(RestRoutes.ROOT_CONTEXT+RestRoutes.SUBSCRIPTION.ROOT_CONTEXT)
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping()
    public void createSubscription(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        if(body.isEmpty()) throw new SubscriptionCreationException("Body is empty");
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        Subscription subscription = JsonUtil.fromJson(body, Subscription.class).orElse(null);
        if(subscription == null) throw new SubscriptionCreationException("Subscription Object is null");
        String id = subscriptionService.createSubscription(username,subscription);
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", "New Subscription Created with id : "+id);
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);
    }
}
