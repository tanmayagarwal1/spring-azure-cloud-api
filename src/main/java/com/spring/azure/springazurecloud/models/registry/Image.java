package com.spring.azure.springazurecloud.models.registry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.models.resources.Resource;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = Constants.SCHEMA.CONTAINER_REGISTRY_IMAGE)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Image extends Resource {
    private String name;
    private String digest;

    @ManyToOne
    @JoinColumn(name = "repository")
    @JsonIgnore
    private Repository repository;

    public void setCountAndCostOnDelete(){
        this.repository.setCost(1, false);
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
