package com.spring.azure.springazurecloud.configuration.constants;

public class RestRoutes {
    public static final String ROOT_CONTEXT="/api/v1";
    public static class CLIENT {
        public static final String ROOT_CONTEXT= "/client";
        public static final String REGISTER= "/register";
        public static final String LOGIN ="/login";
        public static final String PROFILE = "/profile";
        public static final String REFRESH_TOKEN = "/token/refresh";
        public static final String CACHE_INVALIDATE = "/cache/invalidate";
    }

    public static class SUBSCRIPTION {
        public static final String ROOT_CONTEXT = "/subscription";
    }

    public static class RESOURCE_GROUP {
        public static final String ROOT_CONTEXT = "/resource-group";
        public static final String RG_CHECKOUT_PATH = "/checkout";
    }

    public static class AKS {
        public static final String ROOT_CONTEXT = "/aks";
        public static final String AKS_CLUSTER = "/cluster";
        public static final String AKS_NODE_POOL = "/nodePool";
        public static final String AKS_NODE = "/node";
        public static final String AKS_POD = "/pod";
        public static final String AKS_CONTAINER = "/container";
    }

    public static class STORAGE_ACCOUNT {
        public static final String ROOT_CONTEXT = "/storage-account";
        public static final String CONTAINER = "/container";
        public static final String BLOB = "/blob";
        public static final String FILE = "/file";
    }

    public static class APIM {
        public static final String ROOT_CONTEXT = "/apim";
        public static final String APIM_API = "/apim-api";
    }

    public static class CONTAINER_REGISTRY {
        public static final String ROOT_CONTEXT = "/container-registry";
        public static final String REPOSITORY = "/repository";
        public static final String IMAGE = "/image";
    }
}
