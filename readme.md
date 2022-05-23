# Spring-Azure-Cloud-Api
This project aims to replicate the functunality of Azure Cloud by implementing their provided services. This project serves as a proof of concept for working on various intricate backend technologies while building loosely coupled and highly cohersive code.
This project focuses on replicating the below services provided on Azure Cloud : 
1. Azure Kubernetes Services
2. Storage Accounts
3. Container Registries
4. Resource Groups
5. Active Directory
6. Azure APIM (API Manager)
7. Subscriptions

The API Also has spring security implementation via JWT Tokens

## Table of contents
- [Technologies](#Technologies)
- [Getting Started](#Getting-Started)
    - [Local Setup](#Local-Setup)
    - [Generating Access Token](#Generating-Access-Token)
    - [Creating Resources](#Creating-Resources)
      - [Active Directory](#Active-Directory)
      - [Subscription](#Subscription)
      - [Resource Group](#Resource-Group)
      - [AKS Cluster](#AKS-Cluster)
      - [Storage Account](#Storage-Account)
      - [APIM](#APIM)
      - [Container Registry](#Container-Registry)
    - [Making Payment](#Making-Payment)
    - [Docker](#Docker)
- [Kubernetes and Helm](#Kubernetes-and-Helm)

## Technologies
1. Java
2. Spring Boot
3. MySQL
4. Hibernate
5. Spring Security
6. JWT - Json Web Tokens
7. Stripe Payment API
8. Docker
9. Helm
10. Redis Cache

## Getting Started

To Start using the API To provision the mock resources, we first need to generate the Access Token for using the API.
Before We move to generating the access token we need locally setup the project. 

## Local Setup
First clone the git repositoty on the local machine : 

  ```git clone https://github.com/tanmayagarwal1/spring-azure-cloud-api.git ```

Navigate to the project directory and run the Maven Clean install command :

  ``` mvn clean install```

Navigate to the target directory and run the below command :

  ```java -jar spring-azure-cloud-0.0.1-SNAPSHOT.jar```

The API will now be accessible on localhost:8080

## Generating Access Token

The access token is genertaed by using the ```/register``` endpoint and passing User details. The User Details Object contains the following fields : Client Details (username, password), Client Address and Client Card Details which will be used to process payments for the created resources. Sample Postman Request below : 

<img width="463" alt="Screenshot 2022-05-08 at 10 17 44 AM" src="https://user-images.githubusercontent.com/81710149/167282266-3be06e84-34b9-4401-9c6b-a9ccbe4c9326.png">

After the Client is registered, they need to login to get the access token. The endpoint is ```/login?username={username}&password={password}```
The login response will contain the access token, refresh token and access token validity. Sample Login Response below : 

<img width="981" alt="Screenshot 2022-05-08 at 10 20 59 AM" src="https://user-images.githubusercontent.com/81710149/167282340-d80b5e9e-2f96-4a23-b13d-c27088bc4796.png">

The Client can now use this access token in future requests to create resources and billing. 

## Creating Resources

### Active Directory

The first thing that one needs to do is create an Active Directory instance and register the client with that Active Directory. This is handled at the time of client creation itself where a new Instance of Active Directory is automatically assigned to the client. To view the Active Directory the end point is ```GET /client/profile```
Sample image below : 

<img width="392" alt="Screenshot 2022-05-08 at 10 28 57 AM" src="https://user-images.githubusercontent.com/81710149/167282491-cbd96d80-aa16-4883-885d-19eeceff52b4.png">

### Subscription
Next The Client needs to create a subscription. They can do that by passing the required details (All mentioned in post collection) on the POST endpoint : ```/subscription``` 
We get the subscrition ID with the response after creation of subscription. Sample below : 

<img width="497" alt="Screenshot 2022-05-08 at 10 31 38 AM" src="https://user-images.githubusercontent.com/81710149/167282544-e229edf0-dd99-44e8-9833-f5111f887bf5.png">

### Resource Group
To create any other resource, the client will first have to create the resource Group. The resource group is bound to a subscription and is created using the POST endpoint ```/resource-group```
The response will contain the resource-group Id. Sample below : 

<img width="488" alt="Screenshot 2022-05-08 at 10 34 04 AM" src="https://user-images.githubusercontent.com/81710149/167282597-7da03adc-20c1-4779-b1b5-a5500a59b951.png">

Then the client can create individual resources. 

### AKS Cluster
To create an AKS, the client needs to create the following resoures : AKS Cluster, Node Pools, Nodes, Pods and Containers. All these together form the blueprint of creating the Kubernetes Service. They can be created using their respective POST endpoints and can be viewd by using the GET Endpoint on the AKS-Cluster. The costs will be updated on the Resource Group. 
Sample cluster with all the child resources created : 

<img width="600" alt="Screenshot 2022-05-08 at 10 42 14 AM" src="https://user-images.githubusercontent.com/81710149/167282810-587287b1-032e-4d9a-979e-7e4633b1a2a7.png">

### Storage Account
To create a storage account, the client needs to create the following resources : Storage Account, Container, Blob and File. All these together form the blueprint of creating a Storage Account. They can be created using their respective POST endpoints and can be viewd by using the GET Endpoint on the Storage Account. The costs will be updated on the Resource Group. 
Sample Storage Account with all child resources created : 

<img width="586" alt="Screenshot 2022-05-08 at 10 53 35 AM" src="https://user-images.githubusercontent.com/81710149/167283057-f441313c-52f8-438f-8e8c-7be79c42f152.png">

### APIM  
To create an APIM, the client needs to create the following resources : APIM and an API for the APIM. All these together form the blueprint of creating an APIM. They can be created using their respective POST endpoints and can be viewd by using the GET Endpoint on the APIM. The costs will be updated on the Resource Group. 
Sample APIM with all child resources created : 

<img width="497" alt="Screenshot 2022-05-08 at 10 56 20 AM" src="https://user-images.githubusercontent.com/81710149/167283146-00209ba7-6ab5-4a4c-95f9-a2b09c6acdec.png">

### Container Registry
To create an Container Registry, the client needs to create the following resources : Container Registry, Repository and Image. All these together form the blueprint of creating a Container Registry. They can be created using their respective POST endpoints and can be viewd by using the GET Endpoint on the Container Registry. The costs will be updated on the Resource Group. 
Sample Container Registry with all child resources created :

<img width="710" alt="Screenshot 2022-05-08 at 10 59 35 AM" src="https://user-images.githubusercontent.com/81710149/167283232-7cdabb68-e3ff-4e07-8d10-581ff777acf2.png">

## Making Payment

After creating all the resources, the client can initiate a payment for the resources by using the ```\checkout``` endpoint on the Resource Group. Untill the payment is made the subscription is not charged any amount. Only after a successful checkout of the resource group, the subscription and the resource group are activated. 
The payment is initiated using the Stripe API. Below are the sample payment screenshots. 

Payment Checkout of the resource group : 

<img width="512" alt="Screenshot 2022-05-08 at 11 05 28 AM" src="https://user-images.githubusercontent.com/81710149/167283350-5597a472-0955-405d-9d24-87e4a9283603.png">

Stripe Dashboard for the Client showing Successful transaction :

<img width="1432" alt="Screenshot 2022-05-08 at 11 06 38 AM" src="https://user-images.githubusercontent.com/81710149/167283393-b81ed86a-f70e-473d-bee0-4c636238e1bb.png">

Resource Group being marked as `ACTIVE` : 

<img width="338" alt="Screenshot 2022-05-08 at 11 07 09 AM" src="https://user-images.githubusercontent.com/81710149/167283419-576c2d9f-69f6-43e5-9b20-806d2e0ec733.png">

## Docker

The Dockerfile and the Docker Compose yml files can be used to containerize the application. before running the docker commands, run the docker-build.sh file to create the docker image. Then run the docker compose command. The sql scripts are available in ./schema/init.sql which is mounted as a volume to the docker's MySQL instance. The commands to run are as follows : 

```bash
./docker-build.sh
docker compose up -d

docker compose down
```

## Kubernetes and Helm

The Helm Chart for the application has been developed in the ```./Helm``` directory of the Project. For Emulating a Kubernetes Cluster, we can use KIND which allows to create disposable Kubernetes clusters as docker containers. 

The Helm Chart covers : 
1. Application Deployment
2. Application Secrets Deployment
3. Application Ingress Deployment 
4. MySQL Deployment
5. MySQL ConfigMap Deployment
6. Horizontal Pod Autoscaler Deployment

As a pre-requist ensure that Docker is installed on the System and then proceed to install Helm and Kind

Run the following commands for deploying the application

```sh
kind create cluster --name=development-cluster --image=kindest/node:v1.22.7
cd Helm
helm install spring-azure-cloud-api-helm spring-azure-cloud-api-helm
kubectl get all
```

