package com.spring.azure.springazurecloud.models.aks;

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
@Table(name = Constants.SCHEMA.AKS_CONTAINER)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Container extends Resource {

    private String image;


    @ManyToOne
    @JoinColumn(name = "pod")
    @JsonIgnore
    private Pod pod;

    public void setContainerCountOnDelete(){
        this.pod.setContainerCount(this.pod.getContainerCount() - 1);
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
