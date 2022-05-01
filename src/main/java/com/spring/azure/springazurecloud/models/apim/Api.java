package com.spring.azure.springazurecloud.models.apim;

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
@Table(name = Constants.SCHEMA.APIM_API)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Api extends Resource {
    private String name;
    private String openApiSpecFile;
    private String operations;
    private String httpMethod;
    private boolean deployInDevMode;

    @ManyToOne
    @JoinColumn(name = "apim")
    @JsonIgnore
    private Apim apim;

    public void setCountInApimOnDelete(){
        this.apim.setApiCount(this.apim.getApiCount() - 1);
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
