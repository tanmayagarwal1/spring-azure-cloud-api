package com.spring.azure.springazurecloud.configuration.constants;

import com.spring.azure.springazurecloud.utils.PropertyUtils;

public class Constants {
    public static final String APPLICATION_NAME =
            PropertyUtils.getInstance().properties.getProperty("spring.application.name");

    public static final String PROPERTIES_ENVIRONMENT_VAR="ACTIVE_PROFILES";

    public static final String APPLICATION_TIME_ZONE =
            PropertyUtils.getInstance().properties.getProperty("spring.application.timezone");
    public static final int IDENTITY_LENGTH = 7;

    public static final String PLACEHOLDER = "";

    public static class DATABASE{
        public static final String HIBERNATE_URL =
                PropertyUtils.getInstance().properties.getProperty("hibernate.datasource.url");
        public static final String HIBERNATE_USERNAME =
                PropertyUtils.getInstance().properties.getProperty("hibernate.datasource.username");
        public static final String HIBERNATE_PASSWORD =
                PropertyUtils.getInstance().properties.getProperty("hibernate.datasource.password");
        public static final String HIBERNATE_DRIVER =
                PropertyUtils.getInstance().properties.getProperty("hibernate.datasource.jdbcDriver");
        public static final String HIBERNATE_DIALECT =
                PropertyUtils.getInstance().properties.getProperty("hibernate.jpa.properties.hibernate.dialect");
        public static final String HIBERNATE_COMPONENT_CLASS =
                PropertyUtils.getInstance().properties.getProperty("hibernate.jpa.entities.class");
        public static final String HIBERNATE_SHOW_SQL =
                PropertyUtils.getInstance().properties.getProperty("hibernate.jpa.show.sql");
        public static final String HIBERNATE_DIALECT_PROPERTY_SPECIFIER = "hibernate.dialect";
        public static final String HIBERNATE_SHOW_SQL_SPECIFIER = "hibernate.show_sql";



        public static final String JDBC_URL =
                PropertyUtils.getInstance().properties.getProperty("jdbc.datasource.jdbcUrl");
        public static final String JDBC_USERNAME =
                PropertyUtils.getInstance().properties.getProperty("jdbc.datasource.username");
        public static final String JDBC_PASSWORD =
                PropertyUtils.getInstance().properties.getProperty("jdbc.datasource.password");
        public static final String JDBC_DRIVER =
                PropertyUtils.getInstance().properties.getProperty("jdbc.datasource.jdbcDriver");

    }

    public static class CACHE{
        public static final String CACHE_HOST =
                PropertyUtils.getInstance().properties.getProperty("redis.cache.datasource.host");
        public static final int CACHE_PORT = Integer.parseInt(
                PropertyUtils.getInstance().properties.getProperty("redis.cache.datasource.port")
        );
        public static final boolean CACHE_POOl = Boolean.parseBoolean(
                PropertyUtils.getInstance().properties.getProperty("redis.cache.use.pool")
        );

        public static final String CLIENT_CACHE_NAME = "clientCache";
    }

    public static class LOGGER{
        public static final String LOGGER_TYPE =
                PropertyUtils.getInstance().properties.getProperty("logger.log4j.type");
        public static final String LOGGER_NAME =
                PropertyUtils.getInstance().properties.getProperty("logger.log4j.name");
        public static final String LOGGER_LAYOUT_TYPE =
                PropertyUtils.getInstance().properties.getProperty("logger.log4j.layout.type");
        public static final String LAYOUT_NAME =
                PropertyUtils.getInstance().properties.getProperty("logger.log4j.layout.name");
        public static final String LAYOUT_PATTERN =
                PropertyUtils.getInstance().properties.getProperty("logger.log4j.layout.pattern");
        public static final String LOGGER_ROOT_LEVEL =
                PropertyUtils.getInstance().properties.getProperty("logger.log4j.root.level");
        public static final String LOGGER_ROOT_REFERENCE =
                PropertyUtils.getInstance().properties.getProperty("logger.log4j.root.ref");
    }

    public static class SECURITY{
        public static final String KEY =
                PropertyUtils.getInstance().properties.getProperty("secure.key");
        public static final String USERNAME_PARAM = "username";
        public static final String PASSWORD_PARAM = "password";
        public static final String AUTH_HEADER = "Bearer ";
        public static final long ACCESS_TOKEN_EXPIRY_MIN = 300;
        public static final long REFRESH_TOKEN_EXPIRY_MIN = 120;
    }

    public static class SCHEMA{
        public static final String CLIENT= "CLIENT";
        public static final String CARD_DETAILS="CARD_DETAILS";
        public static final String ACTIVE_DIRECTORY="ACTIVE_DIRECTORY";
        public static final String SUBSCRIPTION="SUBSCRIPTION";
        public static final String RESOURCE_GROUP="RESOURCE_GROUP";
        public static final String AKS_CLUSTER="AKS_CLUSTER";
        public static final String AKS_NODE_POOL="AKS_NODE_POOL";
        public static final String AKS_NODE="AKS_NODE";
        public static final String AKS_POD="AKS_POD";
        public static final String AKS_CONTAINER="AKS_CONTAINER";
        public static final String STORAGE_ACCOUNT="STORAGE_ACCOUNT";
        public static final String STORAGE_CONTAINER="STORAGE_CONTAINER";
        public static final String STORAGE_BLOB="STORAGE_BLOB";
        public static final String STORAGE_FILE = "STORAGE_FILE";
        public static final String APIM = "APIM";
        public static final String APIM_API = "APIM_API";
        public static final String CONTAINER_REGISTRY = "CONTAINER_REGISTRY";
        public static final String CONTAINER_REGISTRY_REPOSITORY = "CONTAINER_REPOSITORY";
        public static final String CONTAINER_REGISTRY_IMAGE = "CONTAINER_IMAGE";

    }

    public static class STORAGE_ACCOUNT{
        public static final String BLOB_URL_SUFFIX=".blob.core.windows.net";
        public static final String FILE_URL_SUFFIX=".file.core.windows.net";
        public static final String STORAGE_ACCOUNT_URL_SUFFIX=".sa.core.windows.net";
    }

    public static class APIM{
        public static final String APIM_URL=".apim.azure-services.net";
    }

    public static class CONTAINER_REGISTRY{
        public static final String URL=".apim.azure-container-registry-services.net";
    }

    public static class STRIPE{
        public static final String KEY =
                PropertyUtils.getInstance().properties.getProperty("stripe.key");
        public static final String CLIENT_NAME = "name";
        public static final String CLIENT_EMAIL = "email";
        public static final String CLIENT_PHONE = "phone";

        public static final String CARD_NUMBER_DELIMITER = "-";
        public static final String CARD_NUMBER_JOIN_DELIMITER = "";
        public static final String CARD_EXPIRY_DELIMITER = "/";

        public static final String CARD_NUMBER = "number";
        public static final String CARD_EXPIRY_MONTH = "exp_month";
        public static final String CARD_EXPIRY_YEAR = "exp_year";
        public static final String CARD_CVV = "cvc";

        public static final String PAYMENT_METHOD = "card";
        public static final String PAYMENT_SOURCE_PROPERTY = "source";
        public static final long PAYMENT_UNIT_MULTIPLIER = 100;

        public static final String CUSTOMER_RETRIEVE_EXPAND="sources";

        public static final String PAYMENT_AMOUNT_PROPERTY = "amount";
        public static final String PAYMENT_CURRENCY_PROPERTY = "currency";
        public static final String PAYMENT_CURRENCY_VALUE = "inr";
        public static final String PAYMENT_CUSTOMER_PROPERTY = "customer";
        public static final String PAYMENT_CUSTOMER_EMAIL = "receipt_email";

    }

}
