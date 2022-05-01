package com.spring.azure.springazurecloud.models.apim;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.apim.ApimSku;
import com.spring.azure.springazurecloud.enums.general.Region;
import com.spring.azure.springazurecloud.models.resources.Resource;
import com.spring.azure.springazurecloud.models.resources.ResourceGroup;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Constants.SCHEMA.APIM)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Apim extends Resource {
    private String name;
    @Enumerated(EnumType.STRING)
    private Region region;
    private ApimSku apimSku;
    private String url;
    private long apiCount;

    @ManyToOne
    @JoinColumn(name = "resourceGroup")
    @JsonIgnore
    private ResourceGroup resourceGroup;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "apim")
    private Set<Api> apis = new HashSet<>();

    public void addApi(Api api){
        this.apiCount ++;
        this.apis.add(api);
    }

    public void setCost(boolean isAdded){
        if(isAdded) {
            this.setCost(this.apimSku.getCost());
            this.resourceGroup.setCost(this.resourceGroup.getCost() + this.getCost());
        }
        else {
            this.resourceGroup.setCost(this.resourceGroup.getCost() - this.getCost());
        }
    }

    public void setUrl(){
        this.url = this.name+Constants.APIM.APIM_URL;
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
