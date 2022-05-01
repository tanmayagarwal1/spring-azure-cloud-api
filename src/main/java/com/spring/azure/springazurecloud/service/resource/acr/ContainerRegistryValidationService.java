package com.spring.azure.springazurecloud.service.resource.acr;

import com.spring.azure.springazurecloud.models.registry.ContainerRegistry;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

@RequiredArgsConstructor
public class ContainerRegistryValidationService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;

    @SuppressWarnings("all")
    public boolean validateExistsContainerRegistry(String acrId, String username) {
        return transactionTemplate.execute(status -> {
            ContainerRegistry containerRegistry = sessionFactory
                    .getCurrentSession()
                    .createQuery("select cr from ContainerRegistry cr where cr.resourceId = :id", ContainerRegistry.class)
                    .setParameter("id", acrId).uniqueResult();
            if (containerRegistry == null) return false;
            ResourceGroup resourceGroup = containerRegistry.getResourceGroup();
            return Objects.equals(username, resourceGroup.getSubscription().getActiveDirectory().getClient().getUsername());
        });
    }

    @SuppressWarnings("all")
    public<T extends Resource> boolean validateExistsResource(String resourceId, Class<T> clazz){
        String query = String.format("select 1 from %s r where r.resourceId='%s'",
                clazz.getSimpleName(),
                resourceId);
        return transactionTemplate.execute(status -> {
            Integer isPresent = sessionFactory
                    .getCurrentSession()
                    .createQuery(query, Integer.class)
                    .uniqueResult();
            return isPresent == 1;
        });
    }
}
