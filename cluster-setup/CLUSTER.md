# Basic CockroachDB Cluster with NGINX
Simple 2 node CockroachDB cluster with NGINX acting as load balancer

## Services
* `roach1` - CockroachDB node
* `roach2` - CockroachDB node
* `lb` - NGINX acting as load balancer

## Getting started
1) run `docker-compose up`
2) visit the CockroachDB UI @ http://localhost:8080
3) have fun!

## Helpful Commands

### Execute SQL
Use the following to execute arbitrary SQL on the CockroachDB cluster.  The following creates a database called `test`.
```bash
docker compose exec roach1 /cockroach/cockroach sql --insecure --execute="CREATE DATABASE test;"
```

### Open Interactive Shells
```bash
docker compose exec roach1 /bin/bash
docker compose exec roach2 /bin/bash
docker compose exec lb /bin/bash
```