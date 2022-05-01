package com.spring.azure.springazurecloud.controllers.subscription;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.utils.GlobalControllerHelper;
import com.spring.azure.springazurecloud.exception.client.SubscriptionCreationException;
import com.spring.azure.springazurecloud.models.directory.Subscription;
import com.spring.azure.springazurecloud.service.subscription.SubscriptionService;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(RestRoutes.ROOT_CONTEXT+RestRoutes.SUBSCRIPTION.ROOT_CONTEXT)
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping()
    public void createSubscription(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        if(body.isEmpty()) throw new SubscriptionCreationException("Body is empty");
        String username = request.getUserPrincipal().getName();
        Subscription subscription = JsonUtil.fromJson(body, Subscription.class).orElse(null);
        if(subscription == null) throw new SubscriptionCreationException("Subscription Object is null");
        String id = subscriptionService.createSubscription(username,subscription);
        GlobalControllerHelper.handleControllerResponse("New Subscription Created with id : "+id, response);
    }
}
