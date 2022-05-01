package com.spring.azure.springazurecloud.models.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.StorageAccessTier;
import com.spring.azure.springazurecloud.models.resources.Resource;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = Constants.SCHEMA.STORAGE_BLOB)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Blob extends Resource {
    private String name;
    @Enumerated(EnumType.STRING)
    private StorageAccessTier storageAccessTier;
    private long fileCount;
    private String url;

    @JsonIgnore
    private long accessTierCost;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "blob")
    private Set<File> files = new HashSet<>();

    @ManyToOne()
    @JoinColumn(name = "container")
    @JsonIgnore
    private StorageContainer storageContainer;

    public void setAccessTierCost(){
        this.setAccessTierCost(storageAccessTier.getCost());
    }

    public void setCost(){
        this.setCost(this.fileCount * this.getAccessTierCost());
    }

    public void setCountOnDelete(){
        this.storageContainer.setBlobCount(this.storageContainer.getBlobCount() - 1);
    }

    public void setCostOnFileDeleteCascade(long fileCount){
        this.fileCount -= fileCount;
        setCost();
        setPriceInContainer(fileCount, this.accessTierCost, false);
    }


    public void addFile(File file){
        this.fileCount += 1;
        this.files.add(file);
        setCost();
        setPriceInContainer(1, this.accessTierCost, true);
    }

    public void setUrl(){
        this.url = this.name+Constants.STORAGE_ACCOUNT.BLOB_URL_SUFFIX;
    }


    public void setPriceInContainer(long fileCount, long accessTierCost, boolean isAdded){
            this.storageContainer.setCost(fileCount, accessTierCost, isAdded);
    }

    public void setPriceInContainerOnDelete(){
        this.storageContainer.setCost(this.storageContainer.getCost() - this.getCost());
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
