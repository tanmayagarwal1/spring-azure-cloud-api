package com.spring.azure.springazurecloud.models.registry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.ContainerRepositorySku;
import com.spring.azure.springazurecloud.models.resources.Resource;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Constants.SCHEMA.CONTAINER_REGISTRY_REPOSITORY)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Repository extends Resource {
    private String name;
    private long imageCount;
    @Enumerated(EnumType.STRING)
    private ContainerRepositorySku repositorySku;
    private long perImageCost;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "repository")
    private Set<Image> images = new HashSet<>();

    public void addImage(Image image){
        this.imageCount ++;
        this.images.add(image);
        setCost(1, true);
    }

    public void setPerImageCost(){
        this.perImageCost = repositorySku.getCost();
    }

    public void setCost(long imageCount, boolean isAdded){
        if(isAdded){
            this.setCost(this.getCost() + imageCount * perImageCost);
            this.containerRegistry.setCost(imageCount, perImageCost, true);
        }
        else {
            this.imageCount --;
            this.setCost(this.getCost() - imageCount * perImageCost);
            this.containerRegistry.setCost(imageCount, perImageCost, false);
        }
    }

    public void setCostOnDelete(){
        this.containerRegistry.setCostOnRepositoryDelete(this.getCost());
    }

    @ManyToOne
    @JoinColumn(name = "containerRegistry")
    @JsonIgnore
    private ContainerRegistry containerRegistry;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
