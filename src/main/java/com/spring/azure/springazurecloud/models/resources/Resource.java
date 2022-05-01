package com.spring.azure.springazurecloud.models.resources;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "resourceId")
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Resource {
    @Id
    private String resourceId;
    private Long cost = 0L;

    @Override
    public int hashCode() {
        int result = resourceId != null ? resourceId.hashCode() : 0;
        result = 31 * result;
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Resource myObject = (Resource) o;
        return Objects.equals(resourceId, myObject.getResourceId());
    }

}
