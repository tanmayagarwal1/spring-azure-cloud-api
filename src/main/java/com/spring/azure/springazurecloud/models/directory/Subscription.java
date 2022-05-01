package com.spring.azure.springazurecloud.models.directory;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.subscripton.SubscriptionType;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "subscriptionId")
@Table(name = Constants.SCHEMA.SUBSCRIPTION)
public class Subscription {
    @Id
    private String subscriptionId;
    @Enumerated(EnumType.STRING)
    private SubscriptionType type;
    private String name;
    private long cost;

    @ManyToOne
    @JoinColumn(name = "tenantId")
    @JsonIgnore
    private ActiveDirectory activeDirectory;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "subscription")
    private Set<ResourceGroup> resourceGroups = new HashSet<>();

    public void addResourceGroup(ResourceGroup resourceGroup){this.resourceGroups.add(resourceGroup);}

    @Override
    public int hashCode() {
        int result = subscriptionId != null ? subscriptionId.hashCode() : 0;
        result = 31 * result;
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Subscription myObject = (Subscription) o;
        return Objects.equals(subscriptionId, myObject.getSubscriptionId());
    }


}
