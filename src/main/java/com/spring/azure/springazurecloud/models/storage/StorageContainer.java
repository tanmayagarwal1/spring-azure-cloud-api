package com.spring.azure.springazurecloud.models.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.models.resources.Resource;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = Constants.SCHEMA.STORAGE_CONTAINER)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StorageContainer extends Resource {

    private String name;
    private long blobCount = 0L;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "storageContainer")
    private Set<Blob> blobs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name= "storageAccount")
    @JsonIgnore
    private StorageAccount storageAccount;

    public void addBlob(Blob blob){
        this.blobCount += 1;
        this.blobs.add(blob);
    }

    public void setCost(long fileCount, long accessTierCost, boolean isAdded){
        long newCost = fileCount * accessTierCost;
        if(isAdded){
            this.setCost(this.getCost() + newCost);
            setStorageAccountCost(newCost, true);
        }
        else {
            this.setCost(this.getCost() - newCost);
            setStorageAccountCost(newCost, false);
        }
    }

    public void setStorageAccountCost(long cost, boolean isAdded){
        if(isAdded) {
            this.storageAccount.setCost(cost, true);
        }
        else{
            this.storageAccount.setCost(cost, false);
        }
    }

    public void setStorageAccountCostOnDelete(){
        this.storageAccount.setCost(this.storageAccount.getCost() - this.getCost());
    }

    public void setCost(long cost){
        super.setCost(cost);
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
