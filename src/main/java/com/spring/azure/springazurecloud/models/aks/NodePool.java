package com.spring.azure.springazurecloud.models.aks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.AksSku;
import com.spring.azure.springazurecloud.models.resources.Resource;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = Constants.SCHEMA.AKS_NODE_POOL)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NodePool extends Resource {

    private String name;
    @Enumerated(EnumType.STRING)
    private AksSku sku;
    private long nodeCount = 0;



    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "nodePool")
    private Set<Node> nodes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "aksCluster")
    @JsonIgnore
    private AksCluster aksCluster;

    public void addNode(Node node){
        this.nodes.add(node);
        this.nodeCount ++;
    }

    public void setAksCostOnCreation(){
        this.aksCluster.setCost(this.aksCluster.getCost() + this.getCost(), this.getCost(), true);
    }

    public void setAksCostOnDelete(){
        this.aksCluster.setCost(this.aksCluster.getCost() - this.getCost(), this.getCost(), false);
    }


    public void setCost(){
        super.setCost(sku.getCost());
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
