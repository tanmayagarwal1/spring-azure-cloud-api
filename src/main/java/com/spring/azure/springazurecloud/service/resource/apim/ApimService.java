package com.spring.azure.springazurecloud.service.resource.apim;

import com.spring.azure.springazurecloud.dto.ApimRequestDto;
import com.spring.azure.springazurecloud.exception.ApimException;
import com.spring.azure.springazurecloud.models.apim.Api;
import com.spring.azure.springazurecloud.models.apim.Apim;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import com.spring.azure.springazurecloud.utils.IdentityGenerator;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class ApimService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;
    private final ApimValidationService validationService;

    public Apim getAllApim(ApimRequestDto dto, String username){
        if(!validationService.validateExistsApim(dto.getApimId(), username))
            throw new ApimException("ApimNotFound");
        return transactionTemplate.execute(status -> {
            return sessionFactory
                    .getCurrentSession()
                    .createQuery("select apim from Apim apim left join fetch apim.apis api where apim.resourceId = :id"
                    , Apim.class)
                    .setParameter("id", dto.getApimId())
                    .uniqueResult();
        });
    }

    public String createApim(Apim apim){
        String resourceGroupId = apim.getResourceGroup() != null ?
                ofNullable(apim.getResourceGroup().getResourceId()).orElse(null)
                : null;
        if(resourceGroupId == null) throw new ApimException("No Resource Group Specified");
        AtomicReference<String> apimId = new AtomicReference<>();
        return transactionTemplate.execute(status -> {
            ResourceGroup resourceGroup = sessionFactory.getCurrentSession()
                    .createQuery("select rg from ResourceGroup rg where rg.resourceId = :id", ResourceGroup.class)
                    .setParameter("id", resourceGroupId)
                    .uniqueResult();

            if(resourceGroup == null) throw new ApimException("Resource Group Not Found");
            apimId.set(IdentityGenerator.generateId());
            apim.setResourceId(apimId.get());
            apim.setUrl();
            resourceGroup.addApim(apim);
            apim.setResourceGroup(resourceGroup);
            apim.setCost(true);

            sessionFactory.getCurrentSession().persist(apim);
            return apimId.get();
        });
    }

    public Api getAllApis(ApimRequestDto dto, String username){
        if(!validationService.validateExistsApim(dto.getApimId(), username))
            throw new ApimException("Apim not found");
        if(!validationService.validateExistsResource(dto.getResourceId(), Api.class))
            throw new ApimException("Api not found");
        return transactionTemplate.execute(status -> {
            return sessionFactory
                    .getCurrentSession()
                    .createQuery("select api from Api api where api.resourceId = :id"
                            , Api.class)
                    .setParameter("id", dto.getResourceId())
                    .uniqueResult();
        });
    }

    public String createApi(Api api){
        String apimId = api.getApim() != null ?
                ofNullable(api.getApim().getResourceId()).orElse(null)
                : null;
        if(apimId == null) throw new ApimException("Apim Not found");
        AtomicReference<String> id = new AtomicReference<>();
        return transactionTemplate.execute(status -> {
            id.set(IdentityGenerator.generateId());
            api.setResourceId(id.get());
            Apim apim = sessionFactory.getCurrentSession().getReference(Apim.class, api.getApim().getResourceId());
            apim.addApi(api);
            api.setApim(apim);
            sessionFactory.getCurrentSession().persist(api);
            return id.get();
        });
    }

    public<T extends Resource> void deleteApimResource(ApimRequestDto dto, String username, Class<T> clazz){
        if(!validationService.validateExistsApim(dto.getApimId(), username)) throw new ApimException("No APIM Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), clazz)) throw new ApimException("Resource Not Found");
        if(clazz == Apim.class){
            transactionTemplate.execute(status -> {
                Apim apim = sessionFactory.getCurrentSession().getReference(Apim.class, dto.getApimId());
                apim.setCost(false);
                sessionFactory.getCurrentSession().delete(apim);
                return 1;
            });
        }
        else if(clazz == Api.class){
            transactionTemplate.execute(status -> {
                Api api = sessionFactory.getCurrentSession().getReference(Api.class, dto.getResourceId());
                api.setCountInApimOnDelete();
                sessionFactory.getCurrentSession().delete(api);
                return 1;
            });
        }
        else {
            throw new ApimException("Wrong Resource Specified to Delete");
        }

    }
}
