package com.spring.azure.springazurecloud.controllers.apim;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.dto.ApimRequestDto;
import com.spring.azure.springazurecloud.exception.ApimException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.models.apim.Api;
import com.spring.azure.springazurecloud.models.apim.Apim;
import com.spring.azure.springazurecloud.models.storage.account.StorageAccount;
import com.spring.azure.springazurecloud.service.resource.apim.ApimService;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping(RestRoutes.ROOT_CONTEXT+RestRoutes.APIM.ROOT_CONTEXT)
public class ApimController {
    private final ApimService apimService;

    @GetMapping()
    public void getApim(HttpServletRequest request, HttpServletResponse response){
        ApimRequestDto dto = ApimControllerHelper.handleApimRequest(request);
        Apim apim = apimService.getAllApim(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(apim).orElse(null);
        if(json == null) throw new ApimException("Error retrieving APIM");
        ResponseHandler.handleResponse(response, HttpStatus.OK, json);
    }

    @PostMapping()
    public void createApim(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Apim apim = JsonUtil.fromJson(body, Apim.class).orElse(null);
        if(apim == null) throw new ApimException("Error Converting to POJO class");
        String id = apimService.createApim(apim);
        ApimControllerHelper.handleApimResponse("Apim with id "+id+" Successfully created", response);
    }

    @DeleteMapping()
    public void deleteApim(HttpServletRequest request, HttpServletResponse response){
        ApimRequestDto dto = ApimControllerHelper.handleApimRequest(request);
        apimService.deleteApimResource(dto, request.getUserPrincipal().getName(), Apim.class);
        ApimControllerHelper.handleApimResponse("APIM Successfully deleted", response);
    }

    @GetMapping(RestRoutes.APIM.APIM_API)
    public void getApi(HttpServletRequest request, HttpServletResponse response){
        ApimRequestDto dto = ApimControllerHelper.handleApimRequest(request);
        Api api = apimService.getAllApis(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(api).orElse(null);
        if(json == null) throw new ApimException("Error retrieving Api Info");
        ResponseHandler.handleResponse(response, HttpStatus.OK, json);
    }

    @PostMapping(RestRoutes.APIM.APIM_API)
    public void createApi(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Api api = JsonUtil.fromJson(body, Api.class).orElse(null);
        if(api == null) throw new ApimException("Error Converting to POJO class");
        String id = apimService.createApi(api);
        ApimControllerHelper.handleApimResponse("Api with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.APIM.APIM_API)
    public void deleteApi(HttpServletRequest request, HttpServletResponse response){
        ApimRequestDto dto = ApimControllerHelper.handleApimRequest(request);
        apimService.deleteApimResource(dto, request.getUserPrincipal().getName(), Api.class);
        ApimControllerHelper.handleApimResponse("Api Successfully deleted", response);
    }
}
