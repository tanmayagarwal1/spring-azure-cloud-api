package com.spring.azure.springazurecloud.models.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.azure.springazurecloud.configuration.constants.Constants;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = Constants.SCHEMA.CARD_DETAILS)
public class CardDetails {
    @Id
    private String cardNumber;
    private String validTill;
    private long cvv;

    @OneToOne()
    @JoinColumn(name = "username")
    @JsonIgnore
    private Client client;

    @Override
    public int hashCode() {
        int result = cardNumber != null ? cardNumber.hashCode() : 0;
        result = 31 * result;
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CardDetails myObject = (CardDetails) o;
        return Objects.equals(cardNumber, myObject.getCardNumber());
    }
}
