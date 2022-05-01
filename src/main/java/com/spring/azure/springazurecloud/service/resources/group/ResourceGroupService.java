package com.spring.azure.springazurecloud.service.resources.group;

import com.spring.azure.springazurecloud.enums.payment.PaymentStatus;
import com.spring.azure.springazurecloud.enums.general.ResourceGroupStatus;
import com.spring.azure.springazurecloud.exception.resource.ResourceGroupException;
import com.spring.azure.springazurecloud.models.client.Client;
import com.spring.azure.springazurecloud.models.directory.Subscription;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import com.spring.azure.springazurecloud.service.payment.PaymentGatewayService;
import com.spring.azure.springazurecloud.utils.IdentityGenerator;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class ResourceGroupService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;
    private final PaymentGatewayService paymentService;

    public String createResourceGroup(String username, ResourceGroup resourceGroup){
        AtomicReference<String> id = new AtomicReference<>();
        if(resourceGroup == null) throw new ResourceGroupException("Resource Group object is null");
        transactionTemplate.execute(status -> {
            Query<Client> query = sessionFactory.getCurrentSession().createQuery(
                    "select c from Client c " +
                            "where c.username = :username "
                    ,Client.class
            );
            query.setParameter("username", username);
            Client client = query.uniqueResult();

            List<Subscription> List = client.getActiveDirectory()
                    .getSubscriptions()
                    .stream()
                    .filter(subscription -> {
                        List<ResourceGroup> list = subscription.getResourceGroups()
                                .stream()
                                .filter(rg -> Objects.equals(rg.getName(), resourceGroup.getName()))
                                .collect(Collectors.toList());
                        return list.size() != 0;
                    })
                    .collect(Collectors.toList());
            if(!List.isEmpty()) throw new ResourceGroupException("Resource Group Already Exists");

            id.set(IdentityGenerator.generateId());
            resourceGroup.setResourceId(id.get());
            String subscriptionName = resourceGroup.getSubscription().getName();
            Subscription subscription = client
                    .getActiveDirectory()
                    .getSubscriptions()
                    .stream()
                    .filter(sub -> Objects.equals(sub.getName(),subscriptionName))
                    .findAny()
                    .orElse(null);

            if(subscription == null) throw new ResourceGroupException("Subscription not found");
            subscription.addResourceGroup(resourceGroup);
            resourceGroup.setSubscription(subscription);

            sessionFactory.getCurrentSession().persist(resourceGroup);

            return 1;

        });
        return id.get();
    }

    public PaymentStatus checkoutResourceGroup(String resourceGroupId, String username){
        if(resourceGroupId == null) throw new ResourceGroupException("Resource Group Id is null during checkout");
        return transactionTemplate.execute(status -> {
            boolean doPayment = true;
            Client client = sessionFactory.getCurrentSession().load(Client.class, username);
            ResourceGroup rg = sessionFactory.getCurrentSession().load(ResourceGroup.class, resourceGroupId);
            if(rg == null || client == null ||
                    !Objects.equals(rg.getSubscription().getActiveDirectory().getClient().getUsername(), username)){
                throw new ResourceGroupException("Client details and Resource Group Details do not match or not found");
            }

            if(rg.getCost() - rg.getLastUpdatedCost() <= 0) doPayment = false;

            PaymentStatus paymentStatus = PaymentStatus.SYNCHRONIZED;
            if(doPayment) {
                try {
                    paymentStatus = paymentService.checkoutResourceGroup(client.getPaymentId(), client.getEmail(), rg.getCost() - rg.getLastUpdatedCost());
                } catch (StripeException e) {
                    throw new ResourceGroupException("Exception while initiating payment. Stack Trace : " + e.getMessage());
                }
                rg.setStatus(ResourceGroupStatus.ACTIVE);
            }

            rg.getSubscription().setCost(
                    rg.getSubscription().getCost() + (rg.getCost() - rg.getLastUpdatedCost())
            );
            rg.setLastUpdatedCost(rg.getCost());
            return paymentStatus;
        });
    }

    public void deleteResourceGroup(String resourceGroupId){
        transactionTemplate.execute(status -> {
           ResourceGroup resourceGroup = sessionFactory.getCurrentSession().load(ResourceGroup.class, resourceGroupId);
           resourceGroup.getSubscription().setCost(
                   resourceGroup.getSubscription().getCost() - resourceGroup.getCost()
           );
           sessionFactory.getCurrentSession().delete(resourceGroup);
           return 1;
        });
    }

}
