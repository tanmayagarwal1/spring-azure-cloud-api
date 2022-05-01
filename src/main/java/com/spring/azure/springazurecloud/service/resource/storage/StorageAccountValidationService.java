package com.spring.azure.springazurecloud.service.resource.storage;

import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import com.spring.azure.springazurecloud.models.storage.StorageAccount;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor
public class StorageAccountValidationService {

    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;

    @SuppressWarnings("all")
    public boolean validateExistsStorageAccount(String saId, String username){
        return transactionTemplate.execute(status -> {
            Optional<StorageAccount> storageAccountOptional  = sessionFactory.getCurrentSession()
                    .createQuery("select sa from StorageAccount sa where sa.resourceId = :id", StorageAccount.class)
                    .setParameter("id", saId).stream().findAny();

            StorageAccount storageAccount = storageAccountOptional.orElse(null);
            if(storageAccount == null) return false;
            ResourceGroup resourceGroup = storageAccount.getResourceGroup();
            if(!Objects.equals(username, resourceGroup.getSubscription().getActiveDirectory().getClient().getUsername()))
                return false;
            return true;
        });
    }

    @SuppressWarnings("all")
    public<T extends Resource> boolean validateExistsResource(String resourceId, Class<T> clazz){
        return transactionTemplate.execute(status -> {
            String query = String.format("select 1 from %s r where r.resourceId = '%s'"
                    ,clazz.getSimpleName()
                    ,resourceId);

            Optional<Integer> storageAccountOptional  = sessionFactory.getCurrentSession()
                    .createQuery(query, Integer.class)
                    .stream().findAny();

            return storageAccountOptional.isPresent();
        });
    }
}
