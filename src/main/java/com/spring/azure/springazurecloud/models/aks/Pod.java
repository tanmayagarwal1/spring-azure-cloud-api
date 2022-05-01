package com.spring.azure.springazurecloud.models.aks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.PodStatus;
import com.spring.azure.springazurecloud.models.resources.Resource;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = Constants.SCHEMA.AKS_POD)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pod extends Resource {

    private String name;
    @Enumerated(EnumType.STRING)
    private PodStatus podStatus;

    private long containerCount = 0;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pod")
    private Set<Container> containers = new HashSet<>();

    public void addContainer(Container container){
        this.containers.add(container);
        containerCount++;
    }

    public void setPodCountOnDelete(){
        this.node.setPodCount(this.node.getPodCount() - 1);
    }

    @ManyToOne
    @JoinColumn(name = "node")
    @JsonIgnore
    private Node node;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
