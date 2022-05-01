package com.spring.azure.springazurecloud.controllers.aks;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.dto.AksRequestDto;
import com.spring.azure.springazurecloud.dto.ResponseDto;
import com.spring.azure.springazurecloud.exception.AksException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.models.aks.*;
import com.spring.azure.springazurecloud.service.resource.aks.AksService;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.jar.asm.Handle;
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
@RequestMapping(RestRoutes.ROOT_CONTEXT+RestRoutes.AKS.ROOT_CONTEXT)
public class AksController {

    private final AksService aksService;

    @GetMapping(RestRoutes.AKS.AKS_CLUSTER)
    public void getAksCluster(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        AksCluster aksCluster = aksService.getAksCluster(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(aksCluster).orElse(null);
        if(json == null) throw new AksException("Error retrieving Cluster");
        ResponseHandler.handleResponse(response,HttpStatus.OK,json);
    }

    @PostMapping(RestRoutes.AKS.AKS_CLUSTER)
    public void createAksCluster(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        AksCluster aksCluster = JsonUtil.fromJson(body, AksCluster.class).orElse(null);
        String id = aksService.createAksCluster(aksCluster);
        AksControllerHelper.handleAksResponse("Aks Cluster with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_CLUSTER)
    public void deleteAksCluster(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), AksCluster.class);
        AksControllerHelper.handleAksResponse("Aks Cluster Successfully deleted", response);
    }

    @GetMapping(RestRoutes.AKS.AKS_NODE_POOL)
    public void getAksNodePool(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        NodePool nodePool = aksService.getNodePool(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(nodePool).orElse(null);
        if(json == null) throw new AksException("Error retrieving Cluster");
        ResponseHandler.handleResponse(response,HttpStatus.OK,json);
    }

    @PostMapping(RestRoutes.AKS.AKS_NODE_POOL)
    public void createAksNodePool(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        NodePool nodePool = JsonUtil.fromJson(body, NodePool.class).orElse(null);
        String id = aksService.createNodePool(nodePool, request.getUserPrincipal().getName());
        AksControllerHelper.handleAksResponse("NodePool with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_NODE_POOL)
    public void deleteNodePool(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), NodePool.class);
        AksControllerHelper.handleAksResponse("NodePool Successfully deleted", response);
    }

    @GetMapping(RestRoutes.AKS.AKS_NODE)
    public void getAksNode(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        Node node = aksService.getNode(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(node).orElse(null);
        if(json == null) throw new AksException("Error retrieving Cluster");
        ResponseHandler.handleResponse(response,HttpStatus.OK,json);
    }

    @PostMapping(RestRoutes.AKS.AKS_NODE)
    public void createAksNode(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Node node = JsonUtil.fromJson(body, Node.class).orElse(null);
        String id = aksService.createNode(node, request.getUserPrincipal().getName());
        AksControllerHelper.handleAksResponse("Node with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_NODE)
    public void deleteNode(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), Node.class);
        AksControllerHelper.handleAksResponse("Node Successfully deleted", response);
    }

    @GetMapping(RestRoutes.AKS.AKS_POD)
    public void getAksPod(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        Pod pod = aksService.getPod(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(pod).orElse(null);
        if(json == null) throw new AksException("Error retrieving Cluster");
        ResponseHandler.handleResponse(response,HttpStatus.OK,json);
    }

    @PostMapping(RestRoutes.AKS.AKS_POD)
    public void createAksPod(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Pod pod = JsonUtil.fromJson(body, Pod.class).orElse(null);
        String id = aksService.createPod(pod, request.getUserPrincipal().getName());
        AksControllerHelper.handleAksResponse("Pod with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_POD)
    public void deletePod(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), Pod.class);
        AksControllerHelper.handleAksResponse("Pod Successfully deleted", response);
    }

    @GetMapping(RestRoutes.AKS.AKS_CONTAINER)
    public void getAksContainer(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        Container container = aksService.getContainer(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(container).orElse(null);
        if(json == null) throw new AksException("Error retrieving Cluster");
        ResponseHandler.handleResponse(response,HttpStatus.OK,json);
    }

    @PostMapping(RestRoutes.AKS.AKS_CONTAINER)
    public void createAksContainer(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Container container = JsonUtil.fromJson(body, Container.class).orElse(null);
        String id = aksService.createContainer(container, request.getUserPrincipal().getName());
        AksControllerHelper.handleAksResponse("StorageContainer with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.AKS.AKS_CONTAINER)
    public void deleteContainer(HttpServletRequest request, HttpServletResponse response){
        AksRequestDto dto = AksControllerHelper.handleAksRequest(request);
        aksService.deleteAksResource(dto, request.getUserPrincipal().getName(), Container.class);
        AksControllerHelper.handleAksResponse("StorageContainer Successfully deleted", response);
    }
}
