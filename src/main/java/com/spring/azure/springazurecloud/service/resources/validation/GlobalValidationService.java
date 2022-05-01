package com.spring.azure.springazurecloud.service.resources.validation;

import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class GlobalValidationService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;

    @SuppressWarnings("all")
    public<T extends Resource> boolean validateExistsMainResource(Class<T> clazz, String id, String username){
        String resourceGroupQuery = String.format("select r.resourceGroup from %s r where r.resourceId = '%s'", clazz.getSimpleName(), id);
        return transactionTemplate.execute(status -> {
            Optional<ResourceGroup> object  = sessionFactory.getCurrentSession()
                    .createQuery(resourceGroupQuery, ResourceGroup.class)
                    .stream().findAny();

            ResourceGroup resourceGroup = object.orElse(null);
            if(resourceGroup == null || !Objects.equals(username, resourceGroup.getSubscription().getActiveDirectory().getClient().getUsername()))
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

            Optional<Integer> aksClusterOption  = sessionFactory.getCurrentSession()
                    .createQuery(query, Integer.class)
                    .stream().findAny();

            return aksClusterOption.isPresent();
        });
    }
}
