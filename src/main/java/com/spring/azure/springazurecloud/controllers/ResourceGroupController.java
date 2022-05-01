package com.spring.azure.springazurecloud.controllers;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.dto.ResponseDto;
import com.spring.azure.springazurecloud.enums.PaymentStatus;
import com.spring.azure.springazurecloud.exception.ResourceGroupException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import com.spring.azure.springazurecloud.service.resource.group.ResourceGroupService;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static java.util.Optional.ofNullable;

@Controller
@RequiredArgsConstructor
@RequestMapping(RestRoutes.ROOT_CONTEXT+RestRoutes.RESOURCE_GROUP.ROOT_CONTEXT)
public class ResourceGroupController {

    private final ResourceGroupService resourceGroupService;

    @PostMapping()
    public void createResourceGroup(HttpServletRequest request, HttpServletResponse response){
        String username = request.getUserPrincipal().getName();
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        if(body.isEmpty()) throw new ResourceGroupException("No Body found with request");
        ResourceGroup resourceGroup = JsonUtil.fromJson(body, ResourceGroup.class).orElse(null);
        if(resourceGroup == null) throw new ResourceGroupException("Resource Group Request is null");
        String id = resourceGroupService.createResourceGroup(username, resourceGroup);
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", "Created Resource Group with id : "+id);
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);
    }

    @PostMapping(RestRoutes.RESOURCE_GROUP.RG_CHECKOUT_PATH)
    public void initiatePayment(HttpServletRequest request, HttpServletResponse response){
        String resourceId = ofNullable(request.getParameter("id")).orElse(null);
        if(resourceId == null) throw new ResourceGroupException("No Resource Group Id sent with request");
        PaymentStatus status = resourceGroupService.checkoutResourceGroup(resourceId, request.getUserPrincipal().getName());
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", "Payment status is : "+status.toString());
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);
    }

    @DeleteMapping()
    public void deleteResourceGroup(HttpServletRequest request, HttpServletResponse response){
        String id = ofNullable(request.getParameter("id")).orElse(null);
        if(id == null) throw new ResourceGroupException("No Resource Id Provided");
        resourceGroupService.deleteResourceGroup(id);
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", "Successfully deleted Resource Group with id : "+id);
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);

    }
}
