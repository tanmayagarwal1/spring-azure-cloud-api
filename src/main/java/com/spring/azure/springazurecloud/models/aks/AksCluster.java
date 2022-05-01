package com.spring.azure.springazurecloud.models.aks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.Region;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = Constants.SCHEMA.AKS_CLUSTER)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AksCluster extends Resource {
    private String name;
    private Region region;
    private String version;
    private long cpu;
    private String os;
    private String memory;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "aksCluster")
    private Set<NodePool> nodePools = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "resourceGroup")
    @JsonIgnore
    private ResourceGroup resourceGroup;

    public void addNodePool(NodePool nodePool){this.nodePools.add(nodePool);}


    public void setCost(long costForCluster, long costForResourceGroup, boolean isCreation){
        super.setCost(costForCluster);
        if(isCreation){
            this.resourceGroup.setCost(this.resourceGroup.getCost() + costForResourceGroup);
        }
        else {
            this.resourceGroup.setCost(this.resourceGroup.getCost() - costForResourceGroup);
        }
    }

    public void setResourceGroupCostOnDelete(){
        this.resourceGroup.updateResourceGroupCost(this.resourceGroup.getCost() - this.getCost(), this.getCost());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
