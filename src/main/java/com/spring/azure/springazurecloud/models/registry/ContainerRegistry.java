package com.spring.azure.springazurecloud.models.registry;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Constants.SCHEMA.CONTAINER_REGISTRY)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContainerRegistry extends Resource {
    private String name;
    private String url;
    private boolean publicAccess;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "containerRegistry")
    private Set<Repository> repositories = new HashSet<>();

    public void addRepository(Repository repository){this.repositories.add(repository);}

    public void setCost(long imageCount, long perImageCost, boolean isAdded){
        long newCost = imageCount * perImageCost;
        if(isAdded){
            this.setCost(this.getCost() + newCost);
            this.resourceGroup.setCost(this.resourceGroup.getCost() + newCost);
        }
        else{
            this.setCost(this.getCost() - newCost);
            this.resourceGroup.setCost(this.resourceGroup.getCost() - newCost);
        }
    }

    public void setCostOnRepositoryDelete(long toBeUpdateCost){
        this.setCost(this.getCost() - toBeUpdateCost);
        this.resourceGroup.setCost(this.resourceGroup.getCost() - toBeUpdateCost);
    }

    public void setCostOnRegistryDelete(){
        this.resourceGroup.setCost(this.resourceGroup.getCost() - this.getCost());
    }

    public void setUrl(){this.url= this.name+Constants.CONTAINER_REGISTRY.URL;}


    @ManyToOne
    @JoinColumn(name = "resourceGroup")
    @JsonIgnore
    private ResourceGroup resourceGroup;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
