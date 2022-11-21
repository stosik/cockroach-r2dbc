Demo application for JOOQ, R2DBC and Cocroach integration

### Prerequisites:
- docker installed and running
- java 17


### Technical details:
- two CockroachDB nodes running in cluster (roach1 node as master)
- cluster hidden behind nginx load balancer
- Spring Boot using JOOQ, R2DBC to interact with db layer + simple API

### To run the project we need to follow these steps:

1) Cluster setup and access master node
```shell
cd cluster-setup
docker-compose up -d
```

2) Run Liquibase migration:
```shell
./gradlew update
```

3) Then we can confirm the above setup going to:
http://localhost:8080

4) Then we can start our Spring Boot application.

## Examples of api calls for testing:

### **/POST payment**
```shell
curl --location --request POST 'http://localhost:8081/payments/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "amount": "15",
    "data": {
        "information": "Data information"
    }
}'
```

### **/GET payment**
```shell
GET payment

curl --location --request GET 'http://localhost:8081/payments/1bd6f9ea-7c03-4514-8824-7f399b31b7c4' \
--header 'Content-Type: application/json'
```

