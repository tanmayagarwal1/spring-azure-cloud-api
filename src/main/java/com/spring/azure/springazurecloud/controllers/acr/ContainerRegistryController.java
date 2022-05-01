package com.spring.azure.springazurecloud.controllers.acr;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.utils.GlobalControllerHelper;
import com.spring.azure.springazurecloud.dto.acr.ContainerRegistryDto;
import com.spring.azure.springazurecloud.exception.acr.ContainerRegistryException;
import com.spring.azure.springazurecloud.models.registry.ContainerRegistry;
import com.spring.azure.springazurecloud.models.registry.Image;
import com.spring.azure.springazurecloud.models.registry.Repository;
import com.spring.azure.springazurecloud.service.resources.acr.ContainerRegistryService;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
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
        ContainerRegistryDto dto = GlobalControllerHelper.handleControllerRequest(request, ContainerRegistryDto.class);
        ContainerRegistry containerRegistry = service.getContainerRegistry(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(containerRegistry, response);
    }

    @PostMapping()
    public void createContainerRegistry(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        ContainerRegistry containerRegistry = JsonUtil.fromJson(body, ContainerRegistry.class).orElse(null);
        if(containerRegistry == null) throw new ContainerRegistryException("ACR Pojo is null");
        String id = service.createContainerRegistry(containerRegistry);
        GlobalControllerHelper.handleControllerResponse("ACR with id "+id+" Successfully created", response);
    }

    @DeleteMapping()
    public void deleteContainerRegistry(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = GlobalControllerHelper.handleControllerRequest(request, ContainerRegistryDto.class);
        service.deleteResource(dto, request.getUserPrincipal().getName(), ContainerRegistry.class);
        GlobalControllerHelper.handleControllerResponse("ACR Successfully deleted", response);
    }

    @GetMapping(RestRoutes.CONTAINER_REGISTRY.REPOSITORY)
    public void getRepository(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = GlobalControllerHelper.handleControllerRequest(request, ContainerRegistryDto.class);
        Repository repository = service.getContainerRepository(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(repository, response);
    }

    @PostMapping(RestRoutes.CONTAINER_REGISTRY.REPOSITORY)
    public void createContainerRepository(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Repository repository = JsonUtil.fromJson(body, Repository.class).orElse(null);
        if(repository == null) throw new ContainerRegistryException("Repository is null");
        String id = service.createContainerRepository(repository);
        GlobalControllerHelper.handleControllerResponse("Repository with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.CONTAINER_REGISTRY.REPOSITORY)
    public void deleteContainerRepository(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = GlobalControllerHelper.handleControllerRequest(request, ContainerRegistryDto.class);
        service.deleteResource(dto, request.getUserPrincipal().getName(), Repository.class);
        GlobalControllerHelper.handleControllerResponse("Repository Successfully deleted", response);
    }

    @GetMapping(RestRoutes.CONTAINER_REGISTRY.IMAGE)
    public void getImage(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = GlobalControllerHelper.handleControllerRequest(request, ContainerRegistryDto.class);
        Image image = service.getImage(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(image, response);
    }

    @PostMapping(RestRoutes.CONTAINER_REGISTRY.IMAGE)
    public void createImage(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Image image = JsonUtil.fromJson(body, Image.class).orElse(null);
        if(image == null) throw new ContainerRegistryException("image is null");
        String id = service.createImageInRepository(image);
        GlobalControllerHelper.handleControllerResponse("Image with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.CONTAINER_REGISTRY.IMAGE)
    public void deleteImage(HttpServletRequest request, HttpServletResponse response){
        ContainerRegistryDto dto = GlobalControllerHelper.handleControllerRequest(request, ContainerRegistryDto.class);
        service.deleteResource(dto, request.getUserPrincipal().getName(), Image.class);
        GlobalControllerHelper.handleControllerResponse("Image Successfully deleted", response);
    }
}
