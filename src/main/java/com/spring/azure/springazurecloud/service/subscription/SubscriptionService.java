package com.spring.azure.springazurecloud.service.subscription;

import com.spring.azure.springazurecloud.exception.SubscriptionCreationException;
import com.spring.azure.springazurecloud.models.client.Client;
import com.spring.azure.springazurecloud.models.directory.Subscription;
import com.spring.azure.springazurecloud.utils.IdentityGenerator;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SubscriptionService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;

    public String createSubscription(String username, Subscription subscription){
        AtomicReference<String> id = new AtomicReference<>();
        transactionTemplate.execute(status -> {
            Query<Client> query = sessionFactory.getCurrentSession().createQuery("Select c from Client c where c.username = :username",Client.class);
            query.setParameter("username", username);
            Client client = query.uniqueResult();

            List<Subscription> subscriptions = client
                    .getActiveDirectory()
                    .getSubscriptions()
                    .stream()
                    .filter(sub -> Objects.equals(sub.getName(), subscription.getName()))
                    .collect(Collectors.toList());

            if(!subscriptions.isEmpty()) throw new SubscriptionCreationException("Subscription with same name already exists");

            id.set(IdentityGenerator.generateId());
            client.getActiveDirectory().addSubscription(subscription);
            subscription.setSubscriptionId(id.get());
            subscription.setActiveDirectory(client.getActiveDirectory());
            return 1;
        });
        return id.get();
    }
}
