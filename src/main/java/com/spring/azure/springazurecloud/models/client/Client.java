package com.spring.azure.springazurecloud.models.client;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.general.ClientRole;
import com.spring.azure.springazurecloud.models.directory.ActiveDirectory;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = Constants.SCHEMA.CLIENT)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
public class Client {
    @Id
    private String username;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private ClientRole role;

    private String email;
    private long phone;
    private int age;
    private String organization;
    @Embedded
    private Address address;

    @JsonIgnore
    private String paymentId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "client", optional = false)
    private CardDetails cardDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "client")
    private ActiveDirectory activeDirectory;

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result;
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Client myObject = (Client) o;
        return Objects.equals(username, myObject.getUsername());
    }

}
