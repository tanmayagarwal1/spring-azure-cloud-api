package com.spring.azure.springazurecloud.models.storage.account;

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
@Data
@Table(name = Constants.SCHEMA.STORAGE_FILE)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class File extends Resource {

    private String name;
    private String fileType;
    private String url;

    @ManyToOne()
    @JoinColumn(name = "blobId")
    @JsonIgnore
    private Blob blob;

    public void setUrl(){
        this.url = this.name+Constants.STORAGE_ACCOUNT.FILE_URL_SUFFIX;
    }


    public void setBlobCostOnDelete(){
        this.blob.setCostOnFileDeleteCascade(1);
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
