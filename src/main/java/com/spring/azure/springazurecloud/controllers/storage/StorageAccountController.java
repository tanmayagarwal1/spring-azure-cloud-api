package com.spring.azure.springazurecloud.controllers.storage;


import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.configuration.constants.RestRoutes;
import com.spring.azure.springazurecloud.utils.GlobalControllerHelper;
import com.spring.azure.springazurecloud.dto.storage.StorageAccountRequestDto;
import com.spring.azure.springazurecloud.exception.storage.StorageAccountException;
import com.spring.azure.springazurecloud.models.storage.Blob;
import com.spring.azure.springazurecloud.models.storage.StorageContainer;
import com.spring.azure.springazurecloud.models.storage.File;
import com.spring.azure.springazurecloud.models.storage.StorageAccount;
import com.spring.azure.springazurecloud.service.resources.storage.StorageAccountService;
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
@RequestMapping(RestRoutes.ROOT_CONTEXT+RestRoutes.STORAGE_ACCOUNT.ROOT_CONTEXT)
public class StorageAccountController {
    private final StorageAccountService storageAccountService;

    @GetMapping()
    public void getStorageAccount(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, StorageAccountRequestDto.class);
        StorageAccount storageAccount = storageAccountService.getStorageAccount(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(storageAccount, response);
    }

    @PostMapping()
    public void createStorageAccount(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        StorageAccount storageAccount = JsonUtil.fromJson(body, StorageAccount.class).orElse(null);
        if(storageAccount == null) throw new StorageAccountException("Error Converting to POJO class");
        String id = storageAccountService.createStorageAccount(storageAccount);
        GlobalControllerHelper.handleControllerResponse("Storage Account with id "+id+" Successfully created", response);
    }

    @DeleteMapping()
    public void deleteStorageAccount(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, StorageAccountRequestDto.class);
        storageAccountService.deleteStorageAccountResource(dto, request.getUserPrincipal().getName(), StorageAccount.class);
        GlobalControllerHelper.handleControllerResponse("Storage Account Successfully deleted", response);
    }

    @GetMapping(RestRoutes.STORAGE_ACCOUNT.CONTAINER)
    public void getStorageContainer(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, StorageAccountRequestDto.class);
        StorageContainer storageContainer = storageAccountService.getContainer(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(storageContainer, response);
    }

    @PostMapping(RestRoutes.STORAGE_ACCOUNT.CONTAINER)
    public void createStorageContainer(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        StorageContainer storageContainer = JsonUtil.fromJson(body, StorageContainer.class).orElse(null);
        if(storageContainer == null) throw new StorageAccountException("Error Converting to POJO class");
        String id = storageAccountService.createContainer(storageContainer, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleControllerResponse("Storage StorageContainer with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.STORAGE_ACCOUNT.CONTAINER)
    public void deleteStorageContainer(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, StorageAccountRequestDto.class);
        storageAccountService.deleteStorageAccountResource(dto, request.getUserPrincipal().getName(), StorageContainer.class);
        GlobalControllerHelper.handleControllerResponse("Storage StorageContainer Successfully deleted", response);
    }

    @GetMapping(RestRoutes.STORAGE_ACCOUNT.BLOB)
    public void getStorageBlob(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, StorageAccountRequestDto.class);
        Blob blob = storageAccountService.getBlob(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(blob, response);
    }

    @PostMapping(RestRoutes.STORAGE_ACCOUNT.BLOB)
    public void createStorageBlob(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        Blob blob = JsonUtil.fromJson(body, Blob.class).orElse(null);
        if(blob == null) throw new StorageAccountException("Error Converting to POJO class");
        String id = storageAccountService.createBlob(blob);
        GlobalControllerHelper.handleControllerResponse("Storage Blob with id "+id+" Successfully created", response);
    }

    @DeleteMapping(RestRoutes.STORAGE_ACCOUNT.BLOB)
    public void deleteStorageBlob(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, StorageAccountRequestDto.class);
        storageAccountService.deleteStorageAccountResource(dto, request.getUserPrincipal().getName(), Blob.class);
        GlobalControllerHelper.handleControllerResponse("Storage Blob Successfully deleted", response);
    }

    @GetMapping(RestRoutes.STORAGE_ACCOUNT.FILE)
    public void getStoredFile(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, StorageAccountRequestDto.class);
        File file = storageAccountService.getFile(dto, request.getUserPrincipal().getName());
        GlobalControllerHelper.handleModelControllerResponse(file, response);
    }

    @PostMapping(RestRoutes.STORAGE_ACCOUNT.FILE)
    public void uploadFileInBlob(HttpServletRequest request, HttpServletResponse response){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        File file = JsonUtil.fromJson(body, File.class).orElse(null);
        if(file == null) throw new StorageAccountException("Error Converting to POJO class");
        String id = storageAccountService.createFile(file);
        GlobalControllerHelper.handleControllerResponse("File with id "+id+" Successfully stored", response);
    }

    @DeleteMapping(RestRoutes.STORAGE_ACCOUNT.FILE)
    public void deleteStoredFile(HttpServletRequest request, HttpServletResponse response){
        StorageAccountRequestDto dto = GlobalControllerHelper.handleControllerRequest(request, StorageAccountRequestDto.class);
        storageAccountService.deleteStorageAccountResource(dto, request.getUserPrincipal().getName(), File.class);
        GlobalControllerHelper.handleControllerResponse("Stored File Successfully deleted", response);
    }


}
