package com.spring.azure.springazurecloud.models.storage.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = Constants.SCHEMA.STORAGE_ACCOUNT)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StorageAccount extends Resource {

    private String name;
    private String url;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "storageAccount")
    private Set<StorageContainer> storageContainers = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "resourceGroup")
    @JsonIgnore
    private ResourceGroup resourceGroup;

    public void setUrl(){
        this.url = this.name+Constants.STORAGE_ACCOUNT.STORAGE_ACCOUNT_URL_SUFFIX;
    }

    public void addContainer(StorageContainer storageContainer){this.storageContainers.add(storageContainer);}

    public void setCost(long cost, boolean isAdded){
        if(isAdded) {
            super.setCost(this.getCost() + cost);
            updateResourceGroupCost(cost, true);
        }
        else{
            super.setCost(this.getCost() - cost);
            updateResourceGroupCost(cost, false);
        }
    }

    public void updateResourceGroupCost(long cost, boolean isAdded){
        if(isAdded){
            this.resourceGroup.setCost(this.resourceGroup.getCost() + cost);
        }
        else{
            this.resourceGroup.setCost(this.resourceGroup.getCost() - cost);
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
