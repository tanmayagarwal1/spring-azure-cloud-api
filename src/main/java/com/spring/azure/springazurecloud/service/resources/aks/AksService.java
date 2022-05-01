package com.spring.azure.springazurecloud.service.resources.aks;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.dto.aks.AksRequestDto;
import com.spring.azure.springazurecloud.enums.aks.PodStatus;
import com.spring.azure.springazurecloud.exception.aks.AksException;
import com.spring.azure.springazurecloud.models.aks.*;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import com.spring.azure.springazurecloud.service.resources.validation.GlobalValidationService;
import com.spring.azure.springazurecloud.utils.IdentityGenerator;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AksService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;
    private final GlobalValidationService validationService;

    /* Aks Cluster Methods */

    public AksCluster getAksCluster(AksRequestDto dto, String username){
        if (!validationService.validateExistsMainResource(AksCluster.class, dto.getAksId(), username))throw new AksException("Cluster not Found");
        return transactionTemplate.execute(status -> sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select aks from AksCluster aks " +
                                "left join fetch aks.nodePools nodePool " +
                                "left join fetch nodePool.nodes node " +
                                "left join fetch node.pods pod " +
                                "left join fetch pod.containers container " +
                                "where aks.resourceId = :id"
                        , AksCluster.class
                ).setParameter("id", dto.getResourceId()).uniqueResult());
    }

    public String createAksCluster(AksCluster aksCluster){
        String resourceGroupId = aksCluster.getResourceGroup() != null ?
                ofNullable(aksCluster.getResourceGroup().getResourceId()).orElse(null)
                : null;
        if(resourceGroupId == null) throw new AksException("No Resource Group Specified");
        AtomicReference<String> id = new AtomicReference<>(Constants.PLACEHOLDER);
        transactionTemplate.execute(status -> {
            Optional<Integer> aksClusterOption  = sessionFactory.getCurrentSession()
                    .createQuery("select 1 from ResourceGroup rg where rg.resourceId = :id", Integer.class)
                    .setParameter("id", resourceGroupId)
                    .stream().findAny();

            int validationResult = aksClusterOption.orElse(0);
            if (validationResult == 0) throw new AksException("No Resource Group Found");
            ResourceGroup rg = sessionFactory.getCurrentSession().createQuery(
                    "select rg from ResourceGroup rg where rg.resourceId = :id",ResourceGroup.class
            ).setParameter("id", aksCluster.getResourceGroup().getResourceId()).uniqueResult();

            id.set(IdentityGenerator.generateId());
            aksCluster.setResourceId(id.get());
            aksCluster.setResourceGroup(rg);
            rg.addAksCluster(aksCluster);

            sessionFactory.getCurrentSession().persist(aksCluster);
            return 1;

        });
        return id.get();
    }

    /* Node Pool Methods */

    public NodePool getNodePool(AksRequestDto dto,String username){
        if (!validationService.validateExistsMainResource(AksCluster.class, dto.getAksId(), username))throw new AksException("Cluster not Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), NodePool.class)) throw new AksException("NodePool not Found");
        return transactionTemplate.execute(status -> sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select nodePool from NodePool nodePool " +
                                "left join fetch nodePool.nodes node " +
                                "left join fetch node.pods pod " +
                                "left join fetch pod.containers container " +
                                "where nodePool.resourceId = :id"
                        , NodePool.class
                ).setParameter("id", dto.getResourceId()).uniqueResult());
    }

    public String createNodePool(NodePool nodePool, String username){
        String aksId =nodePool.getAksCluster() != null ?
                ofNullable(nodePool.getAksCluster().getResourceId()).orElse(null)
                : null;
        if(aksId == null || !validationService.validateExistsResource(aksId, AksCluster.class))
            throw new AksException("No Aks with id "+aksId+" Found");
        AtomicReference<String> id = new AtomicReference<>(Constants.PLACEHOLDER);
        transactionTemplate.execute(status -> {
           if(!validationService.validateExistsMainResource(AksCluster.class, nodePool.getAksCluster().getResourceId(), username)) throw new AksException("Aks Cluster not found");
           AksCluster aksCluster = sessionFactory.getCurrentSession().getReference(AksCluster.class, nodePool.getAksCluster().getResourceId());
            id.set(IdentityGenerator.generateId());
           nodePool.setResourceId(id.get());
           nodePool.setAksCluster(aksCluster);
           nodePool.setCost();
           aksCluster.addNodePool(nodePool);
           nodePool.setAksCostOnCreation();

           sessionFactory.getCurrentSession().persist(nodePool);
           return 1;
        });
        return id.get();
    }

    /* Node Methods */

    public Node getNode(AksRequestDto dto, String username){
        if (!validationService.validateExistsMainResource(AksCluster.class, dto.getAksId(), username))throw new AksException("Cluster not Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), Node.class)) throw new AksException("Node not Found");
        return transactionTemplate.execute(status -> sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select node from Node node " +
                                "left join fetch node.pods pod " +
                                "left join fetch pod.containers container " +
                                "where node.resourceId = :id"
                        , Node.class
                ).setParameter("id", dto.getResourceId()).uniqueResult());
    }

    public String createNode(Node node, String username){
        String nodePoolId = node.getNodePool() != null ?
                ofNullable(node.getNodePool().getResourceId()).orElse(null)
                : null;
        if(nodePoolId == null || !validationService.validateExistsResource(nodePoolId, NodePool.class))
            throw new AksException("No Node Pool with id "+nodePoolId+" Found");
        AtomicReference<String> id = new AtomicReference<>(Constants.PLACEHOLDER);
        transactionTemplate.execute(status -> {
           NodePool nodePool = sessionFactory.getCurrentSession().getReference(NodePool.class, node.getNodePool().getResourceId());
           id.set(IdentityGenerator.generateId());
           node.setResourceId(id.get());
           node.setNodePool(nodePool);

           nodePool.addNode(node);
           sessionFactory.getCurrentSession().persist(node);
           return 1;
        });
        return id.get();
    }


    /* Pod Methods */

    public Pod getPod(AksRequestDto dto, String username){
        if (!validationService.validateExistsMainResource(AksCluster.class, dto.getAksId(), username))throw new AksException("Cluster not Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), Pod.class)) throw new AksException("Pod not Found");
        return transactionTemplate.execute(status -> sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select pod from Pod pod " +
                                "left join fetch pod.containers container " +
                                "where pod.resourceId = :id"
                        , Pod.class
                ).setParameter("id", dto.getResourceId()).uniqueResult());
    }

    public String createPod(Pod pod, String username){
        String nodeId = pod.getNode() != null ?
                ofNullable(pod.getNode().getResourceId()).orElse(null)
                : null ;
        if(nodeId == null || !validationService.validateExistsResource(nodeId, Node.class))
            throw new AksException("No Node with id "+nodeId+" Found");
        AtomicReference<String> id = new AtomicReference<>(Constants.PLACEHOLDER);
        transactionTemplate.execute(status -> {
            Node node = sessionFactory.getCurrentSession().getReference(Node.class, pod.getNode().getResourceId());
            pod.setName(pod.getName()+IdentityGenerator.generateId());
            id.set(IdentityGenerator.generateId());
            pod.setResourceId(id.get());
            pod.setPodStatus(PodStatus.IMAGE_PULL_BACKOFF);
            pod.setNode(node);
            node.addPod(pod);
            sessionFactory.getCurrentSession().persist(pod);
            return 1;
        });
        return id.get();
    }


    /* Container Methods */

    public Container getContainer(AksRequestDto dto, String username){
        if (!validationService.validateExistsMainResource(AksCluster.class, dto.getAksId(), username))throw new AksException("Cluster not Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), Container.class)) throw new AksException("Container not Found");
        return transactionTemplate.execute(status -> sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select container from Container container " +
                                "where container.resourceId = :id"
                        , Container.class
                ).setParameter("id", dto.getResourceId()).uniqueResult());
    }

    public String createContainer(Container container, String username){
        String podId = container.getPod() != null ?
                ofNullable(container.getPod().getResourceId()).orElse(null)
                : null;
        if(podId == null || !validationService.validateExistsResource(podId, Pod.class))
            throw new AksException("No pod with id "+podId+" Found");
        AtomicReference<String> id = new AtomicReference<>(Constants.PLACEHOLDER);
        transactionTemplate.execute(status -> {
           Pod pod = sessionFactory.getCurrentSession().getReference(Pod.class, container.getPod().getResourceId());
           pod.setPodStatus(PodStatus.RUNNING);
           id.set(IdentityGenerator.generateId());
           container.setResourceId(id.get());
           container.setPod(pod);
           pod.addContainer(container);
           sessionFactory.getCurrentSession().persist(container);
           return 1;
        });
        return id.get();
    }

    /* DELETE METHOD */

    public<T extends Resource> void deleteAksResource(AksRequestDto dto, String username, Class<T> clazz){
        if (!validationService.validateExistsMainResource(AksCluster.class, dto.getAksId(), username))throw new AksException("Cluster not Found");
        if(!validationService.validateExistsResource(dto.getResourceId(), clazz))
            throw new AksException(String.format("%s not Found", clazz.getSimpleName()));
        transactionTemplate.execute(status -> {
            if(clazz == Node.class){
                Node node = sessionFactory.getCurrentSession().load(Node.class, dto.getResourceId());
                node.setNodeCountOnDelete();
                sessionFactory.getCurrentSession().delete(node);
            }
            else if(clazz == Pod.class){
                Pod pod = sessionFactory.getCurrentSession().load(Pod.class, dto.getResourceId());
                pod.setPodCountOnDelete();
                sessionFactory.getCurrentSession().delete(pod);
            }
            else if(clazz == Container.class){
                Container container = sessionFactory.getCurrentSession().load(Container.class, dto.getResourceId());
                container.setContainerCountOnDelete();
                sessionFactory.getCurrentSession().delete(container);
            }
            else if(clazz == NodePool.class){
                NodePool nodePool = sessionFactory.getCurrentSession().load(NodePool.class, dto.getResourceId());
                nodePool.setAksCostOnDelete();
                sessionFactory.getCurrentSession().delete(nodePool);
            }
            else {
                AksCluster aksCluster = sessionFactory.getCurrentSession().getReference(AksCluster.class, dto.getAksId());
                aksCluster.setResourceGroupCostOnDelete();
                sessionFactory.getCurrentSession().delete(aksCluster);
            }
            return 1;
        });
    }
}
