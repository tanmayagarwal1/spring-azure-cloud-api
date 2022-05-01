package com.spring.azure.springazurecloud.controllers.storage;


import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.dto.StorageAccountRequestDto;
import com.spring.azure.springazurecloud.exception.StorageAccountException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.models.storage.Blob;
import com.spring.azure.springazurecloud.models.storage.StorageContainer;
import com.spring.azure.springazurecloud.models.storage.File;
import com.spring.azure.springazurecloud.models.storage.StorageAccount;
import com.spring.azure.springazurecloud.service.resource.storage.StorageAccountService;
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
@RequestMapping(RestRoutes.ROOT_CONTEXT+RestRoutes.STORAGE_ACCOUNT.ROOT_CONTEXT)
public class StorageAccountController {
    private final StorageAccountService storageAccountService;

    @GetMapping()
    public void getStorageAccount(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = StorageAccountControllerHelper.handleStorageAccountRequest(request);
        StorageAccount storageAccount = storageAccountService.getStorageAccount(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(storageAccount).orElse(null);
        if(json == null) throw new StorageAccountException("Error retrieving Storage Account");
        ResponseHandler.handleResponse(response, HttpStatus.OK, json);
    }

    @PostMapping()
    public void createStorageAccount(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        StorageAccount storageAccount = JsonUtil.fromJson(body, StorageAccount.class).orElse(null);
        if(storageAccount == null) throw new StorageAccountException("Error Converting to POJO class");
        String id = storageAccountService.createStorageAccount(storageAccount);
        StorageAccountControllerHelper.handleStorageAccountResponse("Storage Account with id "+id+" Successfully created", response);
    }

    @DeleteMapping()
    public void deleteStorageAccount(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = StorageAccountControllerHelper.handleStorageAccountRequest(request);
        storageAccountService.deleteStorageAccountResource(dto, request.getUserPrincipal().getName(), StorageAccount.class);
        StorageAccountControllerHelper.handleStorageAccountResponse("Storage Account Successfully deleted", response);
    }

    @GetMapping(RestRoutes.STORAGE_ACCOUNT.CONTAINER)
    public void getStorageContainer(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = StorageAccountControllerHelper.handleStorageAccountRequest(request);
        StorageContainer storageContainer = storageAccountService.getContainer(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(storageContainer).orElse(null);
        if(json == null) throw new StorageAccountException("Error retrieving Storage StorageContainer");
        ResponseHandler.handleResponse(response, HttpStatus.OK, json);
    }

    @PostMapping(RestRoutes.STORAGE_ACCOUNT.CONTAINER)
    public void createStorageContainer(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        StorageContainer storageContainer = JsonUtil.fromJson(body, StorageContainer.class).orElse(null);
        if(storageContainer == null) throw new StorageAccountException("Error Converting to POJO class");
        String id = storageAccountService.createContainer(storageContainer, request.getUserPrincipal().getName());
        StorageAccountControllerHelper.handleStorageAccountResponse("Storage StorageContainer with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.STORAGE_ACCOUNT.CONTAINER)
    public void deleteStorageContainer(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = StorageAccountControllerHelper.handleStorageAccountRequest(request);
        storageAccountService.deleteStorageAccountResource(dto, request.getUserPrincipal().getName(), StorageContainer.class);
        StorageAccountControllerHelper.handleStorageAccountResponse("Storage StorageContainer Successfully deleted", response);
    }

    @GetMapping(RestRoutes.STORAGE_ACCOUNT.BLOB)
    public void getStorageBlob(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = StorageAccountControllerHelper.handleStorageAccountRequest(request);
        Blob blob = storageAccountService.getBlob(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(blob).orElse(null);
        if(json == null) throw new StorageAccountException("Error retrieving Storage Blob");
        ResponseHandler.handleResponse(response, HttpStatus.OK, json);
    }

    @PostMapping(RestRoutes.STORAGE_ACCOUNT.BLOB)
    public void createStorageBlob(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Blob blob = JsonUtil.fromJson(body, Blob.class).orElse(null);
        if(blob == null) throw new StorageAccountException("Error Converting to POJO class");
        String id = storageAccountService.createBlob(blob);
        StorageAccountControllerHelper.handleStorageAccountResponse("Storage Blob with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.STORAGE_ACCOUNT.BLOB)
    public void deleteStorageBlob(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = StorageAccountControllerHelper.handleStorageAccountRequest(request);
        storageAccountService.deleteStorageAccountResource(dto, request.getUserPrincipal().getName(), Blob.class);
        StorageAccountControllerHelper.handleStorageAccountResponse("Storage Blob Successfully deleted", response);
    }

    @GetMapping(RestRoutes.STORAGE_ACCOUNT.FILE)
    public void getStoredFile(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = StorageAccountControllerHelper.handleStorageAccountRequest(request);
        File file = storageAccountService.getFile(dto, request.getUserPrincipal().getName());
        String json = JsonUtil.toJson(file).orElse(null);
        if(json == null) throw new StorageAccountException("Error retrieving Stored file");
        ResponseHandler.handleResponse(response, HttpStatus.OK, json);
    }

    @PostMapping(RestRoutes.STORAGE_ACCOUNT.FILE)
    public void uploadFileInBlob(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        File file = JsonUtil.fromJson(body, File.class).orElse(null);
        if(file == null) throw new StorageAccountException("Error Converting to POJO class");
        String id = storageAccountService.createFile(file);
        StorageAccountControllerHelper.handleStorageAccountResponse("File with id "+id+" Successfully stored", response);
    }

    @DeleteMapping(RestRoutes.STORAGE_ACCOUNT.FILE)
    public void deleteStoredFile(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = StorageAccountControllerHelper.handleStorageAccountRequest(request);
        storageAccountService.deleteStorageAccountResource(dto, request.getUserPrincipal().getName(), File.class);
        StorageAccountControllerHelper.handleStorageAccountResponse("Stored File Successfully deleted", response);
    }


}
