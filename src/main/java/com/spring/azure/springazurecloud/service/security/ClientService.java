package com.spring.azure.springazurecloud.service.security;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.exception.UserCreationException;
import com.spring.azure.springazurecloud.models.client.CardDetails;
import com.spring.azure.springazurecloud.models.client.Client;
import com.spring.azure.springazurecloud.models.directory.ActiveDirectory;
import com.spring.azure.springazurecloud.service.PaymentGatewayService;
import com.spring.azure.springazurecloud.service.cache.CacheService;
import com.spring.azure.springazurecloud.utils.IdentityGenerator;
import com.spring.azure.springazurecloud.utils.LogHelper;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class ClientService implements UserDetailsService {
    private final SessionFactory sessionFactory;
    private final TransactionTemplate transactionTemplate;
    private final PasswordEncoder passwordEncoder;
    private final CacheService cacheService;
    private final PaymentGatewayService paymentGatewayService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return transactionTemplate.execute(status -> {
            try {
                Query<Client> query = sessionFactory.getCurrentSession().createQuery(
                        "select c from Client c where c.username = :username",
                        Client.class
                );
                query.setParameter("username", s);
                Client client = query.uniqueResult();
                if(client == null) throw new UsernameNotFoundException("No User Found");
                return new User(client.getUsername(), client.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority(client.getRole().toString())));
            }
            catch (Exception e){
                LogHelper.logError("User Not Found", e);
                status.setRollbackOnly();
                throw new UserCreationException();
            }
        });
    }

    public void postClient(Client client){
        transactionTemplate.execute(status -> {
            String username = client.getUsername();
            if(username == null || username.isEmpty()) throw new UserCreationException("Username is not found");

            Query<Integer> query= sessionFactory.getCurrentSession().createQuery("select 1 from Client c where c.username = :username",Integer.class);
            query.setParameter("username", username);
            if(query.uniqueResult() != null) throw new UserCreationException("This username has already been taken");

            CardDetails cardDetails = client.getCardDetails();
            cardDetails.setClient(client);
            client.setCardDetails(cardDetails);
            client.setPassword(passwordEncoder.encode(client.getPassword()));

            ActiveDirectory activeDirectory = new ActiveDirectory();
            activeDirectory.setTenantId(IdentityGenerator.generateId());
            activeDirectory.setObjectId(IdentityGenerator.generateId());
            activeDirectory.setClient(client);
            client.setActiveDirectory(activeDirectory);

            String paymentId;
            try {
                paymentId = paymentGatewayService.generateClientPaymentId(client);
            } catch (StripeException e) {
                throw new UserCreationException(e.getMessage());
            }

            client.setPaymentId(paymentId);
            sessionFactory.getCurrentSession().persist(client);
            return Optional.of(1);
        });
    }

    public Client getClient(String username){
        return transactionTemplate.execute(status -> {

            Query<Client> query = sessionFactory.getCurrentSession().createQuery(
                    "select c from Client c " +
                            "left join fetch c.activeDirectory directory " +
                            "left join fetch directory.subscriptions subscription " +
                            "left join fetch subscription.resourceGroups rg " +
                            "left join fetch rg.aksClusters aks " +
                            "left join fetch aks.nodePools nodePools " +
                            "left join fetch nodePools.nodes nodes " +
                            "left join fetch nodes.pods pod " +
                            "left join fetch pod.containers container " +
                            "left join fetch rg.storageAccounts sa " +
                            "left join fetch sa.storageContainers sc " +
                            "left join fetch sc.blobs blob " +
                            "left join fetch blob.files file " +
                            "left join fetch rg.apim rgApim " +
                            "left join fetch rgApim.apis api " +
                            "left join fetch rg.containerRegistries cr " +
                            "left join fetch cr.repositories repo " +
                            "left join fetch repo.images image " +
                            "where c.username = :username",
                    Client.class
            );
            query.setParameter("username", username);
            return query.uniqueResult();
        });
    }

    public void deleteClient(String username){
        if(username == null) throw new UserCreationException("Error deleting user");
        transactionTemplate.execute(status -> {
            Client client = sessionFactory.getCurrentSession().load(Client.class, username);

            try {
                paymentGatewayService.deleteClient(client.getPaymentId());
            } catch (StripeException e) {
                throw new UserCreationException(e.getMessage());
            }

            sessionFactory.getCurrentSession().delete(client);
            return 1;
        });
    }


    public void clearCache(){
        cacheService.clear(Constants.CACHE.CLIENT_CACHE_NAME);
    }
}
