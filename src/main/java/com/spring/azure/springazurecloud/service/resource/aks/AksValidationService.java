package com.spring.azure.springazurecloud.service.resource.aks;

import com.spring.azure.springazurecloud.models.aks.AksCluster;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class AksValidationService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;

    @SuppressWarnings("all")
    public boolean validateExistsAks(String aksId, String username){
        return transactionTemplate.execute(status -> {
            Optional<AksCluster> aksClusterOption  = sessionFactory.getCurrentSession()
                    .createQuery("select aks from AksCluster aks where aks.resourceId = :id", AksCluster.class)
                    .setParameter("id", aksId).stream().findAny();

            AksCluster aksCluster = aksClusterOption.orElse(null);
            if(aksCluster == null) return false;
            ResourceGroup resourceGroup = aksCluster.getResourceGroup();
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

            Optional<Integer> aksClusterOption  = sessionFactory.getCurrentSession()
                    .createQuery(query, Integer.class)
                    .stream().findAny();

            return aksClusterOption.isPresent();
        });
    }
}
