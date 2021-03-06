CREATE DATABASE `spring_azure_microservice`;
USE `spring_azure_microservice`;

CREATE TABLE `ACTIVE_DIRECTORY` (
  `objectId` varchar(250) DEFAULT NULL,
  `tenantId` varchar(250) DEFAULT NULL,
  `username` varchar(250) DEFAULT NULL
);

CREATE TABLE `AKS_CLUSTER` (
  `resourceId` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `region` varchar(250) DEFAULT NULL,
  `version` varchar(250) DEFAULT NULL,
  `cpu` bigint DEFAULT NULL,
  `os` varchar(250) DEFAULT NULL,
  `memory` varchar(250) DEFAULT NULL,
  `resourceGroup` varchar(250) DEFAULT NULL,
  `cost` mediumtext
);

CREATE TABLE `AKS_CONTAINER` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `image` varchar(250) DEFAULT NULL,
  `pod` varchar(250) DEFAULT NULL
);

CREATE TABLE `AKS_NODE` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `podCount` bigint DEFAULT NULL,
  `labelSelectors` varchar(250) DEFAULT NULL,
  `nodePool` varchar(250) DEFAULT NULL
);

CREATE TABLE `AKS_NODE_POOL` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `sku` varchar(250) DEFAULT NULL,
  `nodeCount` bigint DEFAULT NULL,
  `aksCluster` varchar(250) DEFAULT NULL
);

CREATE TABLE `AKS_POD` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `containerCount` bigint DEFAULT NULL,
  `podStatus` varchar(250) DEFAULT NULL,
  `node` varchar(250) DEFAULT NULL
);

CREATE TABLE `APIM` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `region` varchar(250) DEFAULT NULL,
  `apimSku` varchar(250) DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `apiCount` bigint DEFAULT NULL,
  `resourceGroup` varchar(250) DEFAULT NULL
);

CREATE TABLE `APIM_API` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` mediumtext,
  `name` varchar(250) DEFAULT NULL,
  `openApiSpecFile` varchar(250) DEFAULT NULL,
  `operations` varchar(250) DEFAULT NULL,
  `httpMethod` varchar(250) DEFAULT NULL,
  `deployInDevMode` tinyint(1) DEFAULT NULL,
  `apim` varchar(250) DEFAULT NULL
);

CREATE TABLE `CARD_DETAILS` (
  `cardNumber` varchar(250) DEFAULT NULL,
  `username` varchar(250) DEFAULT NULL,
  `validTill` varchar(250) DEFAULT NULL,
  `cvv` bigint DEFAULT NULL
);

CREATE TABLE `CLIENT` (
  `username` varchar(250) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  `role` varchar(250) DEFAULT NULL,
  `email` varchar(250) DEFAULT NULL,
  `phone` bigint DEFAULT NULL,
  `age` int DEFAULT NULL,
  `organization` varchar(250) DEFAULT NULL,
  `city` varchar(250) DEFAULT NULL,
  `country` varchar(250) DEFAULT NULL,
  `state` varchar(250) DEFAULT NULL,
  `postalCode` bigint DEFAULT NULL,
  `paymentId` varchar(250) DEFAULT NULL
);

CREATE TABLE `CONTAINER_IMAGE` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `digest` varchar(250) DEFAULT NULL,
  `repository` varchar(250) DEFAULT NULL
);

CREATE TABLE `CONTAINER_REGISTRY` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `publicAccess` tinyint(1) DEFAULT NULL,
  `resourceGroup` varchar(250) DEFAULT NULL
);

CREATE TABLE `CONTAINER_REPOSITORY` (
  `resourceId` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `imageCount` bigint DEFAULT NULL,
  `repositorySku` varchar(250) DEFAULT NULL,
  `perImageCost` bigint DEFAULT NULL,
  `containerRegistry` varchar(250) DEFAULT NULL
);

CREATE TABLE `NODE_POOL` (
  `resourceId` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `version` varchar(250) DEFAULT NULL,
  `cpu` bigint DEFAULT NULL,
  `os` varchar(250) DEFAULT NULL,
  `memory` varchar(250) DEFAULT NULL,
  `aksCluster` varchar(250) DEFAULT NULL,
  `cost` mediumtext
);

CREATE TABLE `RESOURCE_GROUP` (
  `resourceId` varchar(250) DEFAULT NULL,
  `region` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `subscriptionId` varchar(250) DEFAULT NULL,
  `cost` mediumtext,
  `status` varchar(250) DEFAULT NULL,
  `lastUpdatedCost` bigint DEFAULT NULL
);

CREATE TABLE `STORAGE_ACCOUNT` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `resourceGroup` varchar(250) DEFAULT NULL
);

CREATE TABLE `STORAGE_BLOB` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `storageAccessTier` varchar(250) DEFAULT NULL,
  `fileCount` bigint DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `accessTierCost` bigint DEFAULT NULL,
  `container` varchar(250) DEFAULT NULL
);

CREATE TABLE `STORAGE_CONTAINER` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `blobCount` bigint DEFAULT NULL,
  `storageAccount` varchar(250) DEFAULT NULL
);

CREATE TABLE `STORAGE_FILE` (
  `resourceId` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `fileType` varchar(250) DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `blobId` varchar(250) DEFAULT NULL
);

CREATE TABLE `SUBSCRIPTION` (
  `subscriptionId` varchar(250) DEFAULT NULL,
  `type` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `tenantId` varchar(250) DEFAULT NULL
);