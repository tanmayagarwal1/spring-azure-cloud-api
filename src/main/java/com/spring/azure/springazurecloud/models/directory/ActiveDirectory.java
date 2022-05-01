package com.spring.azure.springazurecloud.models.directory;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.models.client.CardDetails;
import com.spring.azure.springazurecloud.models.client.Client;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tenantId")
@Table(name = Constants.SCHEMA.ACTIVE_DIRECTORY)
public class ActiveDirectory {
    @Id
    private String objectId;
    private String tenantId;

    @OneToOne
    @JoinColumn(name = "username")
    @JsonIgnore
    private Client client;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "activeDirectory")
    private Set<Subscription> subscriptions = new HashSet<>();

    public void addSubscription(Subscription subscription){this.subscriptions.add(subscription);}

    @Override
    public int hashCode() {
        int result = objectId != null ? objectId.hashCode() : 0;
        result = 31 * result;
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ActiveDirectory myObject = (ActiveDirectory) o;
        return Objects.equals(objectId, myObject.getObjectId());
    }

}
