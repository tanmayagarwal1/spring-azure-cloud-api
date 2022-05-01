package com.spring.azure.springazurecloud.controllers.aks;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.utils.GlobalControllerHelper;
import com.spring.azure.springazurecloud.dto.aks.AksRequestDto;
import com.spring.azure.springazurecloud.exception.aks.AksException;
import com.spring.azure.springazurecloud.models.aks.*;
import com.spring.azure.springazurecloud.service.resources.aks.AksService;
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
@RequestMapping(RestRoutes.ROOT_CONTEXT+RestRoutes.AKS.ROOT_CONTEXT)
public class AksController {

    private final AksService aksService;

    @GetMapping(RestRoutes.AKS.AKS_CLUSTER)
    public void getAksCluster(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        AksCluster aksCluster = aksService.getAksCluster(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(aksCluster, response);
    }

    @PostMapping(RestRoutes.AKS.AKS_CLUSTER)
    public void createAksCluster(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        AksCluster aksCluster = JsonUtil.fromJson(body, AksCluster.class).orElse(null);
        if(aksCluster == null) throw new AksException("Cluster is null specified");
        String id = aksService.createAksCluster(aksCluster);
        GlobalControllerHelper.handleControllerResponse("Aks Cluster with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_CLUSTER)
    public void deleteAksCluster(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), AksCluster.class);
        GlobalControllerHelper.handleControllerResponse("Aks Cluster Successfully deleted", response);
    }

    @GetMapping(RestRoutes.AKS.AKS_NODE_POOL)
    public void getAksNodePool(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        NodePool nodePool = aksService.getNodePool(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(nodePool, response);
    }

    @PostMapping(RestRoutes.AKS.AKS_NODE_POOL)
    public void createAksNodePool(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        NodePool nodePool = JsonUtil.fromJson(body, NodePool.class).orElse(null);
        String id = aksService.createNodePool(nodePool, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleControllerResponse("NodePool with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_NODE_POOL)
    public void deleteNodePool(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), NodePool.class);
        GlobalControllerHelper.handleControllerResponse("NodePool Successfully deleted", response);
    }

    @GetMapping(RestRoutes.AKS.AKS_NODE)
    public void getAksNode(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        Node node = aksService.getNode(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(node, response);
    }

    @PostMapping(RestRoutes.AKS.AKS_NODE)
    public void createAksNode(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Node node = JsonUtil.fromJson(body, Node.class).orElse(null);
        String id = aksService.createNode(node, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleControllerResponse("Node with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_NODE)
    public void deleteNode(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), Node.class);
        GlobalControllerHelper.handleControllerResponse("Node Successfully deleted", response);
    }

    @GetMapping(RestRoutes.AKS.AKS_POD)
    public void getAksPod(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        Pod pod = aksService.getPod(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(pod, response);
    }

    @PostMapping(RestRoutes.AKS.AKS_POD)
    public void createAksPod(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Pod pod = JsonUtil.fromJson(body, Pod.class).orElse(null);
        String id = aksService.createPod(pod, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleControllerResponse("Pod with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_POD)
    public void deletePod(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), Pod.class);
        GlobalControllerHelper.handleControllerResponse("Pod Successfully deleted", response);
    }

    @GetMapping(RestRoutes.AKS.AKS_CONTAINER)
    public void getAksContainer(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        Container container = aksService.getContainer(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(container, response);
    }

    @PostMapping(RestRoutes.AKS.AKS_CONTAINER)
    public void createAksContainer(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Container container = JsonUtil.fromJson(body, Container.class).orElse(null);
        String id = aksService.createContainer(container, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleControllerResponse("StorageContainer with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_CONTAINER)
    public void deleteContainer(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, AksRequestDto.class);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), Container.class);
        GlobalControllerHelper.handleControllerResponse("StorageContainer Successfully deleted", response);
    }
}
