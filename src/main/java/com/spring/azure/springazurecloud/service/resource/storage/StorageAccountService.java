package com.spring.azure.springazurecloud.service.resource.storage;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.dto.StorageAccountRequestDto;
import com.spring.azure.springazurecloud.exception.AksException;
import com.spring.azure.springazurecloud.exception.StorageAccountException;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import com.spring.azure.springazurecloud.models.storage.account.Blob;
import com.spring.azure.springazurecloud.models.storage.account.StorageContainer;
import com.spring.azure.springazurecloud.models.storage.account.File;
import com.spring.azure.springazurecloud.models.storage.account.StorageAccount;
import com.spring.azure.springazurecloud.utils.IdentityGenerator;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class StorageAccountService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;
    private final StorageAccountValidationService validationService;

    public StorageAccount getStorageAccount(StorageAccountRequestDto dto, String username){
        if (!validationService.validateExistsStorageAccount(dto.getStorageAccountId(), username))
            throw new StorageAccountException("Storage Account not Found");
        return transactionTemplate.execute(status -> sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select sa from StorageAccount sa " +
                                "left join fetch sa.storageContainers container " +
                                "left join fetch container.blobs blob " +
                                "left join fetch blob.files file " +
                                "where sa.resourceId = :id"
                        , StorageAccount.class
                ).setParameter("id", dto.getStorageAccountId()).uniqueResult());
    }

    public String createStorageAccount(StorageAccount storageAccount){
        String resourceGroupId = storageAccount.getResourceGroup() != null ?
                ofNullable(storageAccount.getResourceGroup().getResourceId()).orElse(null)
                : null;
        if(resourceGroupId == null) throw new StorageAccountException("No Resource Group Specified");
        AtomicReference<String> id = new AtomicReference<>(Constants.PLACEHOLDER);
        transactionTemplate.execute(status -> {
            Optional<Integer> storageAccountOptional  = sessionFactory.getCurrentSession()
                    .createQuery("select 1 from ResourceGroup rg where rg.resourceId = :id", Integer.class)
                    .setParameter("id", resourceGroupId)
                    .stream().findAny();

            int validationResult = storageAccountOptional.orElse(0);
            if (validationResult == 0) throw new AksException("No Resource Group Found");
            ResourceGroup rg = sessionFactory.getCurrentSession().createQuery(
                    "select rg from ResourceGroup rg where rg.resourceId = :id",ResourceGroup.class
            ).setParameter("id", storageAccount.getResourceGroup().getResourceId()).uniqueResult();
            id.set(IdentityGenerator.generateId());

            storageAccount.setResourceId(id.get());
            storageAccount.setUrl();
            rg.addStorageAccount(storageAccount);

            sessionFactory.getCurrentSession().persist(storageAccount);
            return 1;
        });
        return id.get();
    }

    public StorageContainer getContainer(StorageAccountRequestDto dto, String username){
        if (!validationService.validateExistsStorageAccount(dto.getStorageAccountId(), username))
            throw new StorageAccountException("Storage Account not Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), StorageContainer.class))
            throw new StorageAccountException("StorageContainer not Found");
        return transactionTemplate.execute(status -> sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select container from StorageContainer container " +
                                "left join fetch container.blobs blob " +
                                "left join fetch blob.files file " +
                                "where container.resourceId = :id"
                        , StorageContainer.class
                ).setParameter("id", dto.getResourceId()).uniqueResult());
    }

    public String createContainer(StorageContainer storageContainer, String username){
        String storageAccountId = storageContainer.getStorageAccount() != null ?
                ofNullable(storageContainer.getStorageAccount().getResourceId()).orElse(null)
                : null;
        if(storageAccountId == null || !validationService.validateExistsResource(storageAccountId, StorageAccount.class))
            throw new StorageAccountException("No Aks with id "+storageAccountId+" Found");
        AtomicReference<String> id = new AtomicReference<>(Constants.PLACEHOLDER);
        transactionTemplate.execute(status -> {
            if(!validationService.validateExistsStorageAccount(storageContainer.getStorageAccount().getResourceId(), username))
                throw new StorageAccountException("Storage Account for the storageContainer not found");
            StorageAccount storageAccount = sessionFactory.getCurrentSession().getReference(StorageAccount.class, storageContainer.getStorageAccount().getResourceId());
            id.set(IdentityGenerator.generateId());

            storageContainer.setResourceId(id.get());
            storageContainer.setStorageAccount(storageAccount);
            storageAccount.addContainer(storageContainer);

            sessionFactory.getCurrentSession().persist(storageContainer);
            return 1;
        });
        return id.get();
    }

    public Blob getBlob(StorageAccountRequestDto dto, String username){
        if (!validationService.validateExistsStorageAccount(dto.getStorageAccountId(), username))
            throw new StorageAccountException("Storage Account not Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), Blob.class))
            throw new StorageAccountException("Blob not Found");
        return transactionTemplate.execute(status -> sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select blob from Blob blob " +
                                "left join fetch blob.files file " +
                                "where blob.resourceId = :id"
                        , Blob.class
                ).setParameter("id", dto.getResourceId()).uniqueResult());
    }

    public String createBlob(Blob blob){
        String containerId = blob.getStorageContainer() != null ?
                ofNullable(blob.getStorageContainer().getResourceId()).orElse(null)
                : null;
        if(containerId == null || !validationService.validateExistsResource(containerId, StorageContainer.class))
            throw new StorageAccountException("No StorageContainer Found");
        AtomicReference<String> id = new AtomicReference<>(Constants.PLACEHOLDER);
        transactionTemplate.execute(status -> {
           StorageContainer storageContainer = sessionFactory.getCurrentSession().getReference(StorageContainer.class, containerId);
           storageContainer.addBlob(blob);
           id.set(IdentityGenerator.generateId());
           blob.setUrl();
           blob.setResourceId(id.get());
           blob.setAccessTierCost();
           blob.setStorageContainer(storageContainer);
           sessionFactory.getCurrentSession().persist(blob);
           return 1;
        });
        return id.get();
    }

    public File getFile(StorageAccountRequestDto dto, String username){
        if (!validationService.validateExistsStorageAccount(dto.getStorageAccountId(), username))
            throw new StorageAccountException("Storage Account not Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), File.class))
            throw new StorageAccountException("File not Found");
        return transactionTemplate.execute(status -> sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select file from File file " +
                                "where file.resourceId = :id"
                        , File.class
                ).setParameter("id", dto.getResourceId()).uniqueResult());
    }

    public String createFile(File file){
        String blobId = file.getBlob() != null ?
                ofNullable(file.getBlob().getResourceId()).orElse(null)
                : null;
        if(blobId == null || !validationService.validateExistsResource(blobId, Blob.class))
            throw new StorageAccountException("No StorageContainer Found");
        AtomicReference<String> id = new AtomicReference<>(Constants.PLACEHOLDER);
        transactionTemplate.execute(status -> {
           Blob blob = sessionFactory.getCurrentSession().getReference(Blob.class, blobId);
           id.set(IdentityGenerator.generateId());
           file.setResourceId(id.get());
           file.setUrl();
           file.setBlob(blob);
           blob.addFile(file);

           sessionFactory.getCurrentSession().persist(file);
           return 1;
        });
        return id.get();
    }


    public<T extends Resource> void deleteStorageAccountResource(StorageAccountRequestDto dto, String username, Class<T> clazz){
        if (!validationService.validateExistsStorageAccount(dto.getStorageAccountId(), username))throw new StorageAccountException("Storage Account not Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), clazz))
            throw new StorageAccountException(String.format("%s not Found", clazz.getSimpleName()));
        transactionTemplate.execute(status -> {
            if(clazz == File.class){
                File file = sessionFactory.getCurrentSession().load(File.class, dto.getResourceId());
                file.setBlobCostOnDelete();
                sessionFactory.getCurrentSession().delete(file);
            }
            else if(clazz == Blob.class){
                Blob blob = sessionFactory.getCurrentSession().load(Blob.class, dto.getResourceId());
                blob.setCountOnDelete();
                blob.setPriceInContainerOnDelete();
                sessionFactory.getCurrentSession().delete(blob);
            }
            else if(clazz == StorageContainer.class){
                StorageContainer storageContainer = sessionFactory.getCurrentSession().load(StorageContainer.class, dto.getResourceId());
                storageContainer.setStorageAccountCostOnDelete();
                sessionFactory.getCurrentSession().delete(storageContainer);
            }
            else {
                StorageAccount storageAccount = sessionFactory.getCurrentSession().getReference(StorageAccount.class, dto.getStorageAccountId());
                storageAccount.setResourceGroupCostOnDelete();
                sessionFactory.getCurrentSession().delete(storageAccount);
            }
            return 1;
        });
    }
}
