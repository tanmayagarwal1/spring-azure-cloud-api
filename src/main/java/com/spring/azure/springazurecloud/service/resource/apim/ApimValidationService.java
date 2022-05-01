package com.spring.azure.springazurecloud.service.resource.apim;

import com.spring.azure.springazurecloud.models.apim.Apim;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

@RequiredArgsConstructor
public class ApimValidationService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;

    @SuppressWarnings("all")
    public boolean validateExistsApim(String apimId, String username) {
        return transactionTemplate.execute(status -> {
            Apim apim = sessionFactory
                    .getCurrentSession()
                    .createQuery("select apim from Apim apim where apim.resourceId = :id", Apim.class)
                    .setParameter("id", apimId).uniqueResult();
            if (apim == null) return false;
            ResourceGroup resourceGroup = apim.getResourceGroup();
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
