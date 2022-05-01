package com.spring.azure.springazurecloud.service;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.enums.PaymentStatus;
import com.spring.azure.springazurecloud.exception.UserCreationException;
import com.spring.azure.springazurecloud.models.client.CardDetails;
import com.spring.azure.springazurecloud.models.client.Client;
import com.spring.azure.springazurecloud.utils.LogHelper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import com.stripe.param.CustomerRetrieveParams;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PaymentGatewayService {

    public PaymentGatewayService(String key) {
        Stripe.apiKey=key;
    }


    public String generateClientPaymentId(Client client) throws StripeException {
        Map<String, Object> customerProps = new HashMap<>();
        customerProps.put(Constants.STRIPE.CLIENT_NAME, client.getUsername());
        customerProps.put(Constants.STRIPE.CLIENT_EMAIL, client.getEmail());
        customerProps.put(Constants.STRIPE.CLIENT_PHONE, client.getPhone());
        Customer customer = Customer.create(customerProps);

        CustomerRetrieveParams params = CustomerRetrieveParams.builder()
                .addExpand(Constants.STRIPE.CUSTOMER_RETRIEVE_EXPAND)
                .build();
        Customer currCustomer= Customer.retrieve(customer.getId(), params, null);

        String tokenId = generateClientClientPaymentMethod(client.getCardDetails());
        Map<String, Object> sourceDetails = new HashMap<>();
        sourceDetails.put(Constants.STRIPE.PAYMENT_SOURCE_PROPERTY, tokenId);
        currCustomer.getSources().create(sourceDetails);
        return customer.getId();
    }

    public String generateClientClientPaymentMethod(CardDetails cardDetails) throws StripeException {
        if (cardDetails == null) throw new UserCreationException("No Card Details Found");
        String[] cardNumber = cardDetails.getCardNumber().split(Constants.STRIPE.CARD_NUMBER_DELIMITER);
        String[] expiry = cardDetails.getValidTill().split(Constants.STRIPE.CARD_EXPIRY_DELIMITER);
        Map<String, Object> properties = new HashMap<>();
        properties.put(Constants.STRIPE.CARD_NUMBER, String.join(Constants.STRIPE.CARD_NUMBER_JOIN_DELIMITER
                ,cardNumber));
        properties.put(Constants.STRIPE.CARD_EXPIRY_MONTH, expiry[0]);
        properties.put(Constants.STRIPE.CARD_EXPIRY_YEAR, expiry[1]);
        properties.put(Constants.STRIPE.CARD_CVV,cardDetails.getCvv());

        Map<String, Object> tokenParam = new HashMap<>();
        tokenParam.put(Constants.STRIPE.PAYMENT_METHOD, properties);

        Token token = Token.create(tokenParam);

        return token.getId();
    }
    public PaymentStatus checkoutResourceGroup(String clientToken, String email, long cost) throws StripeException {
        Map<String, Object> paymentInit = new HashMap<>();
        paymentInit.put(Constants.STRIPE.PAYMENT_AMOUNT_PROPERTY, cost * Constants.STRIPE.PAYMENT_UNIT_MULTIPLIER);
        paymentInit.put(Constants.STRIPE.PAYMENT_CURRENCY_PROPERTY, Constants.STRIPE.PAYMENT_CURRENCY_VALUE);
        paymentInit.put(Constants.STRIPE.PAYMENT_CUSTOMER_PROPERTY, clientToken);
        paymentInit.put(Constants.STRIPE.PAYMENT_CUSTOMER_EMAIL, email);

        Charge charge = Charge.create(paymentInit);
        LogHelper.logInfo(charge.toJson());

        return PaymentStatus.SUCCEEDED;
    }

    public void deleteClient(String clientId) throws StripeException {
        Customer customer = Customer.retrieve(clientId);
        customer.delete();
    }
}
