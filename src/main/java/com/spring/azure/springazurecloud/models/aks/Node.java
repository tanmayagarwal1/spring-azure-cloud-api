package com.spring.azure.springazurecloud.models.aks;


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
@Table(name = Constants.SCHEMA.AKS_NODE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Node extends Resource {

    private String name;
    private long podCount = 0;
    private String labelSelectors;

    @ManyToOne
    @JoinColumn(name = "nodePool")
    @JsonIgnore
    private NodePool nodePool;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "node")
    private Set<Pod> pods = new HashSet<>();

    public void addPod(Pod pod){
        this.pods.add(pod);
        this.podCount ++;
    }

    public void setNodeCountOnDelete(){
        this.nodePool.setNodeCount(this.nodePool.getNodeCount()- 1);
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
