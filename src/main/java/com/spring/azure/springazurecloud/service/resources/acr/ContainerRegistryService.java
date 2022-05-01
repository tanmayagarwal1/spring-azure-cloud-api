package com.spring.azure.springazurecloud.service.resources.acr;


import com.spring.azure.springazurecloud.dto.acr.ContainerRegistryDto;
import com.spring.azure.springazurecloud.exception.acr.ContainerRegistryException;
import com.spring.azure.springazurecloud.models.registry.ContainerRegistry;
import com.spring.azure.springazurecloud.models.registry.Image;
import com.spring.azure.springazurecloud.models.registry.Repository;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import com.spring.azure.springazurecloud.service.resources.validation.GlobalValidationService;
import com.spring.azure.springazurecloud.utils.IdentityGenerator;
import com.spring.azure.springazurecloud.utils.Sha256;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class ContainerRegistryService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;
    private final GlobalValidationService validationService;

    public ContainerRegistry getContainerRegistry(ContainerRegistryDto dto, String username){
        if(!validationService.validateExistsMainResource(ContainerRegistry.class, dto.getContainerRegistryId(), username))
            throw new ContainerRegistryException("ACR NotFound");
        return transactionTemplate.execute(status -> {
            return sessionFactory
                    .getCurrentSession()
                    .createQuery("select cr from ContainerRegistry cr " +
                                    "left join fetch cr.repositories registry " +
                                    "left join fetch registry.images image " +
                                    "where cr.resourceId = :id"
                            , ContainerRegistry.class)
                    .setParameter("id", dto.getContainerRegistryId())
                    .uniqueResult();
        });
    }

    public String createContainerRegistry(ContainerRegistry containerRegistry){
        String resourceGroupId = containerRegistry.getResourceGroup() != null ?
                ofNullable(containerRegistry.getResourceGroup().getResourceId()).orElse(null)
                : null;
        if(resourceGroupId == null) throw new ContainerRegistryException("No Resource Group Specified");
        AtomicReference<String> containerRegistryId = new AtomicReference<>();
        return transactionTemplate.execute(status -> {
            ResourceGroup resourceGroup = sessionFactory.getCurrentSession()
                    .createQuery("select rg from ResourceGroup rg where rg.resourceId = :id", ResourceGroup.class)
                    .setParameter("id", resourceGroupId)
                    .uniqueResult();

            if(resourceGroup == null) throw new ContainerRegistryException("Resource Group Not Found");
            containerRegistryId.set(IdentityGenerator.generateId());
            containerRegistry.setResourceId(containerRegistryId.get());
            containerRegistry.setUrl();
            resourceGroup.addContainerRegistry(containerRegistry);
            containerRegistry.setResourceGroup(resourceGroup);
            sessionFactory.getCurrentSession().persist(containerRegistry);
            return containerRegistryId.get();
        });
    }

    public Repository getContainerRepository(ContainerRegistryDto dto, String username){
        if(!validationService.validateExistsMainResource(ContainerRegistry.class, dto.getContainerRegistryId(), username))
            throw new ContainerRegistryException("ACR NotFound");
        if(!validationService.validateExistsResource(dto.getResourceId(), Repository.class))
            throw new ContainerRegistryException("ACR NotFound");
        return transactionTemplate.execute(status -> {
            return sessionFactory
                    .getCurrentSession()
                    .createQuery("select registry from Repository registry " +
                                    "left join fetch registry.images image " +
                                    "where registry.resourceId = :id"
                            , Repository.class)
                    .setParameter("id", dto.getResourceId())
                    .uniqueResult();
        });
    }

    public String createContainerRepository(Repository repository){
        String containerRegistryId = repository.getContainerRegistry() != null ?
                ofNullable(repository.getContainerRegistry().getResourceId()).orElse(null)
                : null;
        if(containerRegistryId == null) throw new ContainerRegistryException("No ACR Specified");
        if(!validationService.validateExistsResource(containerRegistryId, ContainerRegistry.class))
            throw new ContainerRegistryException("No ACR Found");
        AtomicReference<String> repositoryId = new AtomicReference<>();
        return transactionTemplate.execute(status -> {
            ContainerRegistry containerRegistry = sessionFactory.getCurrentSession()
                    .getReference(ContainerRegistry.class, containerRegistryId);

            repositoryId.set(IdentityGenerator.generateId());
            repository.setResourceId(repositoryId.get());
            repository.setPerImageCost();
            repository.setContainerRegistry(containerRegistry);
            containerRegistry.addRepository(repository);
            sessionFactory.getCurrentSession().persist(repository);
            return repositoryId.get();
        });
    }

    public Image getImage(ContainerRegistryDto dto, String username){
        if(!validationService.validateExistsMainResource(ContainerRegistry.class, dto.getContainerRegistryId(), username))
            throw new ContainerRegistryException("ACR NotFound");
        if(!validationService.validateExistsResource(dto.getResourceId(), Image.class))
            throw new ContainerRegistryException("ACR NotFound");
        return transactionTemplate.execute(status -> {
            return sessionFactory
                    .getCurrentSession()
                    .createQuery("select image from Image image " +
                                    "where image.resourceId = :id"
                            , Image.class)
                    .setParameter("id", dto.getResourceId())
                    .uniqueResult();
        });
    }

    public String createImageInRepository(Image image){
        String repositoryId = image.getRepository() != null ?
                ofNullable(image.getRepository().getResourceId()).orElse(null)
                : null;
        if(repositoryId == null) throw new ContainerRegistryException("No Repository Specified");
        if(!validationService.validateExistsResource(repositoryId, Repository.class))
            throw new ContainerRegistryException("No Repository Found");
        AtomicReference<String> imageId = new AtomicReference<>();
        return transactionTemplate.execute(status -> {
            Repository repository = sessionFactory.getCurrentSession()
                    .getReference(Repository.class, repositoryId);

            imageId.set(IdentityGenerator.generateId());
            image.setResourceId(imageId.get());
            image.setDigest(Sha256.getHash(image.getName()));
            image.setRepository(repository);
            repository.addImage(image);
            sessionFactory.getCurrentSession().persist(image);
            return imageId.get();
        });
    }

    public<T extends Resource> void deleteResource(ContainerRegistryDto dto, String username, Class<T> clazz){
        if(!validationService.validateExistsMainResource(ContainerRegistry.class, dto.getContainerRegistryId(), username))
            throw new ContainerRegistryException("No ACR Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), clazz))
            throw new ContainerRegistryException("No Resource Found");
        if(clazz == Image.class){
            transactionTemplate.execute(status -> {
                Image image = sessionFactory.getCurrentSession().getReference(Image.class, dto.getResourceId());
                image.setCountAndCostOnDelete();
                sessionFactory.getCurrentSession().delete(image);
                return 1;
            });
        }
        else if(clazz == Repository.class){
            transactionTemplate.execute(status -> {
                Repository repository = sessionFactory.getCurrentSession().getReference(Repository.class, dto.getResourceId());
                repository.setCostOnDelete();
                sessionFactory.getCurrentSession().delete(repository);
                return 1;
            });
        }
        else if(clazz == ContainerRegistry.class){
            transactionTemplate.execute(status -> {
                ContainerRegistry containerRegistry = sessionFactory.getCurrentSession().getReference(ContainerRegistry.class, dto.getResourceId());
                containerRegistry.setCostOnRegistryDelete();
                sessionFactory.getCurrentSession().delete(containerRegistry);
                return 1;
            });
        }
        else{
            throw new ContainerRegistryException("Invalid Resource !");
        }
    }

}
