package com.spring.azure.springazurecloud.models.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.Region;
import com.spring.azure.springazurecloud.enums.ResourceGroupStatus;
import com.spring.azure.springazurecloud.models.aks.AksCluster;
import com.spring.azure.springazurecloud.models.apim.Apim;
import com.spring.azure.springazurecloud.models.directory.Subscription;
import com.spring.azure.springazurecloud.models.registry.ContainerRegistry;
import com.spring.azure.springazurecloud.models.storage.StorageAccount;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = Constants.SCHEMA.RESOURCE_GROUP)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResourceGroup {
    @Id
    private String resourceId;
    @Enumerated(EnumType.STRING)
    private Region region;
    private String name;
    private long cost;
    @Enumerated(EnumType.STRING)
    private ResourceGroupStatus status;
    @JsonIgnore
    private long lastUpdatedCost;

    @ManyToOne
    @JoinColumn(name = "subscriptionId")
    @JsonIgnore
    private Subscription subscription;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "resourceGroup")
    private Set<AksCluster> aksClusters = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "resourceGroup")
    private Set<StorageAccount> storageAccounts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "resourceGroup")
    private Set<Apim> apim = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "resourceGroup")
    private Set<ContainerRegistry> containerRegistries = new HashSet<>();

    public void addAksCluster(AksCluster aksCluster){this.aksClusters.add(aksCluster);}

    public void addStorageAccount(StorageAccount storageAccount){this.storageAccounts.add(storageAccount);}

    public void addApim(Apim apim){this.apim.add(apim);}

    public void addContainerRegistry(ContainerRegistry cr){this.containerRegistries.add(cr);}

    public void updateResourceGroupCost(long costForResourceGroup, long deletionCost){
        this.cost = costForResourceGroup;
        this.subscription.setCost(this.subscription.getCost() - deletionCost);
    }

    @Override
    public int hashCode() {
        int result = resourceId != null ? resourceId.hashCode() : 0;
        result = 31 * result;
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ResourceGroup myObject = (ResourceGroup) o;
        return Objects.equals(resourceId, myObject.getResourceId());
    }


}
