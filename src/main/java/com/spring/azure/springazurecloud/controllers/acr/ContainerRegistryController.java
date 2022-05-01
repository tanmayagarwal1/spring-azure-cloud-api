package com.spring.azure.springazurecloud.controllers.acr;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.dto.ContainerRegistryDto;
import com.spring.azure.springazurecloud.exception.ContainerRegistryException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.models.registry.ContainerRegistry;
import com.spring.azure.springazurecloud.models.registry.Image;
import com.spring.azure.springazurecloud.models.registry.Repository;
import com.spring.azure.springazurecloud.service.resource.acr.ContainerRegistryService;
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
@RequestMapping(RestRoutes.ROOT_CONTEXT+RestRoutes.CONTAINER_REGISTRY.ROOT_CONTEXT)
public class ContainerRegistryController {
    private final ContainerRegistryService service;

    @GetMapping()
    public void getContainerRegistry(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = ContainerRegistryControllerHelper.handleContainerRegistryRequest(request);
        ContainerRegistry containerRegistry = service.getContainerRegistry(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(containerRegistry).orElse(null);
        if(json == null) throw new ContainerRegistryException("Error retrieving ACR");
        ResponseHandler.handleResponse(response, HttpStatus.OK,json);
    }

    @PostMapping()
    public void createContainerRegistry(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        ContainerRegistry containerRegistry = JsonUtil.fromJson(body, ContainerRegistry.class).orElse(null);
        String id = service.createContainerRegistry(containerRegistry);
        ContainerRegistryControllerHelper.handleContainerRegistryResponse("ACR with id "+id+" Successfully created", response);
    }

    @DeleteMapping()
    public void deleteContainerRegistry(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = ContainerRegistryControllerHelper.handleContainerRegistryRequest(request);
        service.deleteResource(dto, request.getUserPrincipal().getName(), ContainerRegistry.class);
        ContainerRegistryControllerHelper.handleContainerRegistryResponse("ACR Successfully deleted", response);
    }

    @GetMapping(RestRoutes.CONTAINER_REGISTRY.REPOSITORY)
    public void getRepository(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = ContainerRegistryControllerHelper.handleContainerRegistryRequest(request);
        Repository repository = service.getContainerRepository(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(repository).orElse(null);
        if(json == null) throw new ContainerRegistryException("Error retrieving Repository");
        ResponseHandler.handleResponse(response, HttpStatus.OK,json);
    }

    @PostMapping(RestRoutes.CONTAINER_REGISTRY.REPOSITORY)
    public void createContainerRepository(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Repository repository = JsonUtil.fromJson(body, Repository.class).orElse(null);
        if(repository == null) throw new ContainerRegistryException("Repository is null");
        String id = service.createContainerRepository(repository);
        ContainerRegistryControllerHelper.handleContainerRegistryResponse("Repository with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.CONTAINER_REGISTRY.REPOSITORY)
    public void deleteContainerRepository(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = ContainerRegistryControllerHelper.handleContainerRegistryRequest(request);
        service.deleteResource(dto, request.getUserPrincipal().getName(), Repository.class);
        ContainerRegistryControllerHelper.handleContainerRegistryResponse("Repository Successfully deleted", response);
    }

    @GetMapping(RestRoutes.CONTAINER_REGISTRY.IMAGE)
    public void getImage(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = ContainerRegistryControllerHelper.handleContainerRegistryRequest(request);
        Image image = service.getImage(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(image).orElse(null);
        if(json == null) throw new ContainerRegistryException("Error retrieving image from Repository");
        ResponseHandler.handleResponse(response, HttpStatus.OK,json);
    }

    @PostMapping(RestRoutes.CONTAINER_REGISTRY.IMAGE)
    public void createImage(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Image image = JsonUtil.fromJson(body, Image.class).orElse(null);
        if(image == null) throw new ContainerRegistryException("image is null");
        String id = service.createImageInRepository(image);
        ContainerRegistryControllerHelper.handleContainerRegistryResponse("Image with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.CONTAINER_REGISTRY.IMAGE)
    public void deleteImage(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = ContainerRegistryControllerHelper.handleContainerRegistryRequest(request);
        service.deleteResource(dto, request.getUserPrincipal().getName(), Image.class);
        ContainerRegistryControllerHelper.handleContainerRegistryResponse("Image Successfully deleted", response);
    }
}
